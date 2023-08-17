package com.mokhtarabadi.wsserver.controllers;

import com.mokhtarabadi.wsserver.constants.TopicConstants;
import com.mokhtarabadi.kafka.Message;
import com.mokhtarabadi.wsserver.models.MessageObject;
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

    // receive messageObject from client
    @MessageMapping("/message/")
    public CompletableFuture<SendResult<String, Message>> receiveMessage(@Payload MessageObject messageObject) {

        log.info("Received messageObject: " + messageObject);
        return kafkaTemplate.send(TopicConstants.WS_SERVER_TO_MESSAGES_WORKERS, Message.newBuilder()
                        .setSender(messageObject.getSender())
                        .setText(messageObject.getText())
                        .setTimestamp(System.currentTimeMillis())
                .build());
    }

}
