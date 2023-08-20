/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.controllers;

import com.google.gson.Gson;
import com.mokhtarabadi.sharedlib.helpers.RedisHelper;
import com.mokhtarabadi.sharedlib.models.MessageDTO;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import com.mokhtarabadi.wsserver.events.WebSocketEvent;
import com.mokhtarabadi.wsserver.models.*;
import com.mokhtarabadi.wsserver.repositories.ChannelRepository;
import com.mokhtarabadi.wsserver.repositories.MessageRepository;
import dorkbox.messageBus.MessageBus;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChannelControllerImpl implements ChannelController {

	@NonNull private Gson gson;
	@NonNull private MessageBus messageBus;

	@NonNull private ChannelRepository channelRepository;
	@NonNull private MessageRepository messageRepository;

	@Override
	public void addUserToChannel(SubscribeRequestDTO dto) {
		channelRepository.subscribe(dto.getUser(), dto.getChannel()).whenComplete((v, e) -> {
			WebSocketMessage.WebSocketMessageBuilder builder = WebSocketMessage.builder()
					.type(WebSocketMessage.MessageType.SUBSCRIBE);
			SubscribeResponseDTO subscribeResponseDTO = new SubscribeResponseDTO();
			if (e != null) {
				subscribeResponseDTO.setErrorMessage(e.getMessage());
				subscribeResponseDTO.setSuccess(false);
			} else {
				subscribeResponseDTO.setSuccess(true);

				// load history for this channel
				sendChannelSnapshot(dto.getUser(), dto.getChannel());
			}
			messageBus.publishAsync(
					WebSocketEvent.builder().message(builder.data(gson.toJsonTree(subscribeResponseDTO)).build())
							.recipient(dto.getUser()).build());
		});
	}

	@Override
	public void removeUserFromChannel(UnsubscribeRequestDTO dto) {
		channelRepository.unsubscribe(dto.getUser(), dto.getChannel()).whenComplete((v, e) -> {
			WebSocketMessage.WebSocketMessageBuilder builder = WebSocketMessage.builder()
					.type(WebSocketMessage.MessageType.UNSUBSCRIBE);
			UnsubscribeResponseDTO unsubscribeResponseDTO = new UnsubscribeResponseDTO();
			if (e != null) {
				unsubscribeResponseDTO.setErrorMessage(e.getMessage());
				unsubscribeResponseDTO.setSuccess(false);
			} else {
				unsubscribeResponseDTO.setSuccess(true);
			}
			messageBus.publishAsync(
					WebSocketEvent.builder().message(builder.data(gson.toJsonTree(unsubscribeResponseDTO)).build())
							.recipient(dto.getUser()).build());
		});
	}

	@Override
	public CompletableFuture<Set<String>> listSubscribers(String channel) {
		return channelRepository.listSubscribers(channel);
	}

	@Override
	public void listChannels(ChannelListRequestDTO dto) {
		// TODO: 8/20/23 write logic for user groups and roles
		List<String> channels = channelRepository.listChannels();

		ChannelListResponseDTO channelListResponseDTO = new ChannelListResponseDTO();
		channelListResponseDTO.setChannels(channels);

		messageBus
				.publishAsync(WebSocketEvent.builder()
						.message(WebSocketMessage.builder().type(WebSocketMessage.MessageType.CHANNELS)
								.data(gson.toJsonTree(channelListResponseDTO)).build())
						.recipient(dto.getUser()).build());
	}

	private void sendChannelSnapshot(String user, String channel) {
		messageRepository.loadHistory(channel).whenComplete((v, e) -> {
			if (e != null) {
				log.error("Error while loading history", e);
			} else {
				log.trace("Loaded history for channel {} for user {}", channel, user);

				if (v.isEmpty()) {
					sendChannelSnapshotDatabase(user, channel);
				} else {
					sendHistoryToWebSocket(user, v);
				}
			}
		});
	}

	private void sendChannelSnapshotDatabase(String user, String channel) {
		messageRepository.loadHistoryFromDatabase(channel).whenComplete((v, e) -> {
			if (e != null) {
				log.error("Error while loading history from database", e);
			} else {
				log.trace("Loaded history for channel {} for user {} from database", channel, user);

				List<MessageDTO> messageDTOList = new ArrayList<>();
				for (MessageEntity messageEntity : v.currentPage()) {
					MessageDTO messageDTO = new MessageDTO(messageEntity.getSender(), messageEntity.getChannel(),
							messageEntity.getText());
					messageDTO.setTimestamp(messageEntity.getTimestamp());
					messageDTOList.add(messageDTO);

					// add to cache
					RedisHelper.getInstance().addToList(String.format("%s:history", channel), gson.toJson(messageDTO));
				}

				sendHistoryToWebSocket(user, messageDTOList);
			}
		});
	}

	private void sendHistoryToWebSocket(String user, List<MessageDTO> messages) {
		messageBus
				.publishAsync(WebSocketEvent
						.builder().recipient(user).message(WebSocketMessage.builder()
								.type(WebSocketMessage.MessageType.SNAPSHOT).data(gson.toJsonTree(messages)).build())
						.build());
	}
}
