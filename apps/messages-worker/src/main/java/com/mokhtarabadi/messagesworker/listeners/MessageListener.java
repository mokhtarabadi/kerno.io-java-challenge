package com.mokhtarabadi.messagesworker.listeners;

import com.mokhtarabadi.messagesworker.constants.TopicConstants;
import com.mokhtarabadi.messagesworker.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    private KafkaTemplate<String, Message> kafkaTemplate;

    @KafkaListener(topics = TopicConstants.WS_SERVER_TO_MESSAGES_WORKERS, groupId = "${spring.kafka.consumer.group-id}")
    public void receivedMessage(String message) {
        // handle message

        log.info("Received message: " + message);

        //kafkaTemplate.send(TopicConstants.MESSAGES_WORKERS_TO_WS_SERVER, );
    }
}
