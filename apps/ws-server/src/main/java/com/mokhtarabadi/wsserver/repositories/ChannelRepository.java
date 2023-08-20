/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.repositories;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ChannelRepository {

	List<String> listChannels();

	CompletableFuture<Void> subscribe(String user, String channel);

	CompletableFuture<Void> unsubscribe(String user, String channel);

	CompletableFuture<Set<String>> listSubscribers(String channel);
}
