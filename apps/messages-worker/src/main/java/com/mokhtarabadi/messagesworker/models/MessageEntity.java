package com.mokhtarabadi.messagesworker.models;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "messages")
public class MessageEntity {
    @Id
    private String id;

    @NonNull
    private String sender;
    @NonNull
    private String text;
    private long timestamp;
}
