/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.repositories;

import com.datastax.oss.driver.api.core.MappedAsyncPagingIterable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mokhtarabadi.sharedlib.helpers.CassandraHelper;
import com.mokhtarabadi.sharedlib.helpers.RedisHelper;
import com.mokhtarabadi.sharedlib.models.MessageDTO;
import com.mokhtarabadi.sharedlib.models.MessageEntity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

	private static final String KEY = "%s:history";

	@NonNull private Gson gson;

	@Override
	public CompletableFuture<List<MessageDTO>> loadHistory(String channel) {
		return RedisHelper.getInstance().getListValues(String.format(KEY, channel))
				.thenApply(this::convertToMessageDTOList);
	}

	@Override
	public CompletableFuture<MappedAsyncPagingIterable<MessageEntity>> loadHistoryFromDatabase(String channel) {
		return CassandraHelper.getInstance().getMessageDAO().getByChannelAsync(channel).toCompletableFuture();
	}

	private List<MessageDTO> convertToMessageDTOList(List<String> stringList) {
		Type messageType = new TypeToken<MessageDTO>() {
		}.getType();
		List<MessageDTO> messageDTOList = new ArrayList<>(stringList.size());
		for (String s : stringList) {
			messageDTOList.add(gson.fromJson(s, messageType));
		}
		// sort by timestamp
		messageDTOList.sort(Comparator.comparingLong(MessageDTO::getTimestamp));
		return messageDTOList;
	}
}
