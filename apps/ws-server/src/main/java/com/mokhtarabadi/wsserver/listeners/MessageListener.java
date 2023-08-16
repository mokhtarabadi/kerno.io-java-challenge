package com.mokhtarabadi.wsserver.listeners;

import com.mokhtarabadi.wsserver.constants.TopicConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = TopicConstants.MESSAGES_WORKERS_TO_WS_SERVER, groupId = "${spring.kafka.consumer.group-id}")
    public void sendMessage(String message) {
        simpMessagingTemplate.convertAndSend("/topic/group", message);
    }
}
