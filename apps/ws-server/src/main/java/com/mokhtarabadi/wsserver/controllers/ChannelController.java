/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.controllers;

import com.mokhtarabadi.wsserver.models.ChannelListRequestDTO;
import com.mokhtarabadi.wsserver.models.SubscribeRequestDTO;
import com.mokhtarabadi.wsserver.models.UnsubscribeRequestDTO;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ChannelController {

	void addUserToChannel(SubscribeRequestDTO dto);

	void removeUserFromChannel(UnsubscribeRequestDTO dto);

	CompletableFuture<Set<String>> listSubscribers(String channel);

	void listChannels(ChannelListRequestDTO dto);
}
