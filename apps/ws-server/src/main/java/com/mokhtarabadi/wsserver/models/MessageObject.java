package com.mokhtarabadi.wsserver.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class MessageObject {
    @NonNull
    private String sender;
    @NonNull
    private String text;

    private long timestamp;
}
