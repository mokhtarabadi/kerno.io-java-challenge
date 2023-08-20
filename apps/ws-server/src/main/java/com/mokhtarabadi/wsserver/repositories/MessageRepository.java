/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.repositories;

import com.datastax.oss.driver.api.core.MappedAsyncPagingIterable;
import com.mokhtarabadi.sharedlib.models.MessageDTO;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MessageRepository {

	CompletableFuture<List<MessageDTO>> loadHistory(String channel);

	CompletableFuture<MappedAsyncPagingIterable<MessageEntity>> loadHistoryFromDatabase(String channel);

}
