/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.services;

import com.google.gson.Gson;
import com.mokhtarabadi.sharedlib.constants.KafkaTopics;
import com.mokhtarabadi.sharedlib.helpers.KafkaHelper;
import com.mokhtarabadi.sharedlib.models.MessageDTO;
import com.mokhtarabadi.sharedlib.models.MessageEvent;
import com.mokhtarabadi.wsserver.events.WebSocketBroadcastEvent;
import com.mokhtarabadi.wsserver.models.WebSocketMessage;
import dorkbox.messageBus.MessageBus;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BrokerService {

	@NonNull private Gson gson;
	@NonNull private MessageBus messageBus;

	public void sendMessageToWorker(MessageDTO messageDTO) {
		MessageEvent messageEvent = MessageEvent.newBuilder().setChannel(messageDTO.getChannel())
				.setSender(messageDTO.getSender()).setText(messageDTO.getText()).setTimestamp(messageDTO.getTimestamp())
				.build();

		KafkaHelper.getInstance().sendMessage("my_broker", KafkaTopics.WS_SERVER_TO_MESSAGES_WORKER,
				messageDTO.getChannel(), messageEvent);
	}
	public void listenForMessages() {
		KafkaHelper.getInstance().receiveMessages("my_broker", KafkaTopics.MESSAGES_WORKER_TO_WS_SERVER,
				(Consumer<MessageEvent>) messageEvent -> {
					log.debug("Received message from worker: {}", messageEvent);

					MessageDTO dto = new MessageDTO(messageEvent.getSender(), messageEvent.getChannel(),
							messageEvent.getText());
					dto.setTimestamp(messageEvent.getTimestamp());

					WebSocketBroadcastEvent event = WebSocketBroadcastEvent
							.builder().channel(messageEvent.getChannel()).message(WebSocketMessage.builder()
									.type(WebSocketMessage.MessageType.BROADCAST).data(gson.toJsonTree(dto)).build())
							.build();

					messageBus.publish(event);
				});
	}

}
