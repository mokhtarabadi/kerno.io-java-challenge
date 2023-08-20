/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.dao;

import com.datastax.oss.driver.api.core.MappedAsyncPagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import java.util.concurrent.CompletionStage;

@Dao
public interface MessageDAO {

	@Query("SELECT * FROM ${keyspaceId}.${tableId} WHERE channel = :channel")
	@StatementAttributes(consistencyLevel = "QUORUM")
	CompletionStage<MappedAsyncPagingIterable<MessageEntity>> getByChannelAsync(String channel);

	// get by sender async
	@Query("SELECT * FROM ${keyspaceId}.${tableId} WHERE sender = :sender")
	@StatementAttributes(consistencyLevel = "QUORUM")
	CompletionStage<MappedAsyncPagingIterable<MessageEntity>> getBySenderAsync(String sender);

	@Insert
	CompletionStage<Void> upsertAsync(MessageEntity message);
}
