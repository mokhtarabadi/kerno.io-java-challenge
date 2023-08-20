/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.repositories;

import com.mokhtarabadi.sharedlib.helpers.CassandraHelper;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import java.util.concurrent.CompletableFuture;

public class MessageRepositoryImpl implements MessageRepository {

	@Override
	public CompletableFuture<Void> saveMessage(MessageEntity message) {
		return CassandraHelper.getInstance().getMessageDAO().upsertAsync(message).toCompletableFuture();
	}
}
