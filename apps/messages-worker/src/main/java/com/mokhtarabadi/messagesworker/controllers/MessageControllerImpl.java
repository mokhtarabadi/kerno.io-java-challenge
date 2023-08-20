/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.controllers;

import com.google.gson.Gson;
import com.mokhtarabadi.messagesworker.events.BrokerEvent;
import com.mokhtarabadi.messagesworker.repositories.MessageRepository;
import com.mokhtarabadi.sharedlib.helpers.RedisHelper;
import com.mokhtarabadi.sharedlib.models.MessageDTO;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import com.mokhtarabadi.sharedlib.models.MessageEvent;
import dorkbox.messageBus.MessageBus;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageControllerImpl implements MessageController {

	@NonNull private Gson gson;
	@NonNull private MessageBus messageBus;

	@NonNull private MessageRepository messageRepository;

	@Override
	public void saveMessage(MessageEvent message) {
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setId(UUID.randomUUID());
		messageEntity.setText(message.getText());
		messageEntity.setChannel(message.getChannel());
		messageEntity.setSender(message.getSender());
		messageEntity.setTimestamp(message.getTimestamp());
		messageEntity.setCreatedAt(System.currentTimeMillis());

		messageRepository.saveMessage(messageEntity).whenComplete((completable, throwable) -> {
			if (throwable != null) {
				log.error("Error saving message", throwable);
			} else {
				log.trace("Message saved");

				// add it to cache
				MessageDTO messageDTO = new MessageDTO(messageEntity.getSender(), messageEntity.getChannel(),
						messageEntity.getText());
				messageDTO.setTimestamp(messageEntity.getTimestamp());
				RedisHelper.getInstance().addToList(String.format("%s:history", messageEntity.getChannel()),
						gson.toJson(messageDTO));

				messageBus.publishAsync(
						BrokerEvent.builder().type(BrokerEvent.EventType.PUBLISH_MESSAGE).data(messageEntity).build());
			}
		});
	}
}
