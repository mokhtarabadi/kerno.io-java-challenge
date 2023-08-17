package com.mokhtarabadi.messagesworker.listeners;

import com.mokhtarabadi.messagesworker.constants.TopicConstants;
import com.mokhtarabadi.kafka.Message;
import com.mokhtarabadi.messagesworker.models.MessageEntity;
import com.mokhtarabadi.messagesworker.repositories.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @KafkaListener(topics = TopicConstants.WS_SERVER_TO_MESSAGES_WORKERS, groupId = "${spring.kafka.consumer.group-id}")
    public void receivedMessage(Message message) {
        // handle message

        log.info("Received message: " + message);
        MessageEntity entity = new MessageEntity(message.getSender(), message.getText());
        entity.setTimestamp(message.getTimestamp());

        messageRepository.insert(entity);

        kafkaTemplate.send(TopicConstants.MESSAGES_WORKERS_TO_WS_SERVER, message);
    }
}
