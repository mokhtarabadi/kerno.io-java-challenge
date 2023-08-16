package com.mokhtarabadi.messagesworker.models;

import lombok.Data;

@Data
public class Message {
    private String sender;
    private String content;
    private long timestamp;
}
