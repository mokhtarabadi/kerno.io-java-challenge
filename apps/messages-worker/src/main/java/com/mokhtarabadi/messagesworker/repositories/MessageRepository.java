/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.repositories;

import com.mokhtarabadi.sharedlib.models.MessageEntity;
import java.util.concurrent.CompletableFuture;

public interface MessageRepository {

	CompletableFuture<Void> saveMessage(MessageEntity message);
}
