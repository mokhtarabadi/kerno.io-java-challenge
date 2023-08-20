/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mokhtarabadi.sharedlib.models.MessageDTO;
import com.mokhtarabadi.wsserver.controllers.AuthController;
import com.mokhtarabadi.wsserver.controllers.ChannelController;
import com.mokhtarabadi.wsserver.events.WebSocketBroadcastEvent;
import com.mokhtarabadi.wsserver.events.WebSocketEvent;
import com.mokhtarabadi.wsserver.models.*;
import dorkbox.messageBus.annotations.Subscribe;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

@Slf4j
public class WebSocketService extends WebSocketServer {

	private static final ConcurrentHashMap<String, WebSocket> CLIENTS = new ConcurrentHashMap<>();

	private final Gson gson;

	private final BrokerService brokerService;
	private final AuthController authController;
	private final ChannelController channelController;

	public WebSocketService(String listen, int port, BrokerService brokerService, AuthController authController,
			ChannelController channelController, Gson gson) {
		super(new InetSocketAddress(listen, port));

		this.brokerService = brokerService;

		this.authController = authController;
		this.channelController = channelController;

		this.gson = gson;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		log.debug("WebSocket connected.");

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log.debug("WebSocket disconnected. Code: {}, Reason: {}, Remote: {}", code, reason, remote);

		// remove the client by value
		CLIENTS.entrySet().removeIf(entry -> entry.getValue() == conn);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		log.debug("WebSocket message: " + message);

		// the protocol is JSON
		WebSocketMessage json = gson.fromJson(message, WebSocketMessage.class);

		switch (json.getType()) {
			case AUTH : {
				AuthRequestDTO request = gson.fromJson(json.getData(), AuthRequestDTO.class);
				AuthResponseDTO authResponseDTO = new AuthResponseDTO();
				if (authController.authenticate(request)) {
					CLIENTS.put(request.getUsername(), conn);
					authResponseDTO.setSuccess(true);
				} else {
					authResponseDTO.setSuccess(false);
				}

				conn.send(gson.toJson(WebSocketMessage.builder().type(WebSocketMessage.MessageType.AUTH)
						.data(gson.toJsonTree(authResponseDTO)).build()));
				break;
			}
			case CHANNELS : {
				ChannelListRequestDTO request = gson.fromJson(json.getData(), ChannelListRequestDTO.class);
				channelController.listChannels(request);
				break;
			}
			case SUBSCRIBE : {
				SubscribeRequestDTO request = gson.fromJson(json.getData(), SubscribeRequestDTO.class);
				channelController.addUserToChannel(request);
				break;
			}
			case UNSUBSCRIBE : {
				UnsubscribeRequestDTO request = gson.fromJson(json.getData(), UnsubscribeRequestDTO.class);
				channelController.removeUserFromChannel(request);
				break;
			}
			case MESSAGE : {
				MessageDTO messageDTO = gson.fromJson(json.getData(), MessageDTO.class);
				messageDTO.setTimestamp(System.currentTimeMillis());

				// send message to kafka for processing
				brokerService.sendMessageToWorker(messageDTO);

				// send acknowledgement to client
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", messageDTO.getAcknowledgeId());
				conn.send(gson.toJson(WebSocketMessage.builder().type(WebSocketMessage.MessageType.ACKNOWLEDGE)
						.data(jsonObject).build()));

				break;
			}
			default :
				log.error("Invalid message type: " + json.getType().name());
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		log.error("WebSocket error.", ex);
	}

	@Override
	public void onStart() {
		log.info("WebSocket server started.");
	}

	@Subscribe
	public void handleWebSocketEvent(WebSocketEvent event) {
		log.debug("WebSocket event: " + event);

		if (CLIENTS.containsKey(event.getRecipient())) {
			CLIENTS.get(event.getRecipient()).send(gson.toJson(event.getMessage()));
		} else {
			log.warn("WebSocket recipient not found: " + event.getRecipient());
		}
	}

	@Subscribe
	public void handleWebSocketBroadcastEvent(WebSocketBroadcastEvent event) {
		log.debug("WebSocket broadcast event: {}", event);

		channelController.listSubscribers(event.getChannel()).whenComplete((v, e) -> {
			if (e == null) {
				for (String s : v) {
					if (CLIENTS.containsKey(s)) {
						log.info("Sending message to: {}", s);
						CLIENTS.get(s).send(gson.toJson(event.getMessage()));
					} else {
						log.warn("Seems like user is offline: {}", s);

						// unsubscribe offline users
						channelController.removeUserFromChannel(new UnsubscribeRequestDTO(s, event.getChannel()));
					}
				}
			} else {
				log.error("Error while listing subscribers", e);
			}
		});
	}
}
