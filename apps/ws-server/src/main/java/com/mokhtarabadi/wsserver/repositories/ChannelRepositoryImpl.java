/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.repositories;

import com.mokhtarabadi.sharedlib.helpers.RedisHelper;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ChannelRepositoryImpl implements ChannelRepository {

	private static final String KEY = "%s:subscribers";

	@Override
	public List<String> listChannels() {
		return Arrays.asList("channel1", "channel2");
	}

	@Override
	public CompletableFuture<Void> subscribe(String user, String channel) {
		return RedisHelper.getInstance().addToSet(String.format(KEY, channel), user);
	}

	@Override
	public CompletableFuture<Void> unsubscribe(String user, String channel) {
		return RedisHelper.getInstance().removeFromSet(String.format(KEY, channel), user);
	}

	@Override
	public CompletableFuture<Set<String>> listSubscribers(String channel) {
		return RedisHelper.getInstance().getSetValues(String.format(KEY, channel));
	}

}
