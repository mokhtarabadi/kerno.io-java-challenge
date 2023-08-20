/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.services;

import com.google.gson.Gson;
import com.mokhtarabadi.messagesworker.controllers.MessageController;
import com.mokhtarabadi.messagesworker.events.BrokerEvent;
import com.mokhtarabadi.sharedlib.constants.KafkaTopics;
import com.mokhtarabadi.sharedlib.helpers.KafkaHelper;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import com.mokhtarabadi.sharedlib.models.MessageEvent;
import dorkbox.messageBus.annotations.Subscribe;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BrokerService {

	@NonNull private Gson gson;

	@NonNull private MessageController messageController;

	public void listenForMessages() {
		KafkaHelper.getInstance().receiveMessages("my_broker", KafkaTopics.WS_SERVER_TO_MESSAGES_WORKER,
				(Consumer<MessageEvent>) event -> messageController.saveMessage(event));
	}

	public void sendMessage(MessageEvent message) {
		KafkaHelper.getInstance().sendMessage("my_broker", KafkaTopics.MESSAGES_WORKER_TO_WS_SERVER,
				message.getChannel(), message);
	}

	@Subscribe
	public void handleBrokerEvent(BrokerEvent event) {
		switch (event.getType()) {
			case PUBLISH_MESSAGE : {
				MessageEntity messageEntity = (MessageEntity) event.getData();
				MessageEvent messageEvent = MessageEvent.newBuilder().setChannel(messageEntity.getChannel())
						.setSender(messageEntity.getSender()).setText(messageEntity.getText())
						.setTimestamp(messageEntity.getTimestamp()).build();
				sendMessage(messageEvent);
				break;
			}
			default :
				log.error("Unknown event type: {}", event.getType());
		}
	}
}
