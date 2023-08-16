package com.mokhtarabadi.wsserver.controllers;

import com.mokhtarabadi.wsserver.constants.TopicConstants;
import com.mokhtarabadi.wsserver.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;


@Controller
@Slf4j
public class WebSocketController {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    // receive message from client
    @MessageMapping("/message/")
    public CompletableFuture<SendResult<String, Message>> receiveMessage(@Payload Message message) {
        message.setTimestamp(System.currentTimeMillis());

        log.info("Received message: " + message);
        return kafkaTemplate.send(TopicConstants.WS_SERVER_TO_MESSAGES_WORKERS, message);
    }

}
