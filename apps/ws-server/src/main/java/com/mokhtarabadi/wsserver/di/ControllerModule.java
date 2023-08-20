/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.di;

import com.google.gson.Gson;
import com.mokhtarabadi.wsserver.controllers.AuthController;
import com.mokhtarabadi.wsserver.controllers.AuthControllerImpl;
import com.mokhtarabadi.wsserver.controllers.ChannelController;
import com.mokhtarabadi.wsserver.controllers.ChannelControllerImpl;
import com.mokhtarabadi.wsserver.repositories.ChannelRepository;
import com.mokhtarabadi.wsserver.repositories.MessageRepository;
import com.mokhtarabadi.wsserver.repositories.UserRepository;
import dagger.Module;
import dagger.Provides;
import dorkbox.messageBus.MessageBus;
import javax.inject.Singleton;

@Module
public class ControllerModule {

	@Singleton
	@Provides
	public ChannelController provideChannelController(Gson gson, MessageBus messageBus,
			ChannelRepository channelRepository, MessageRepository messageRepository) {
		return new ChannelControllerImpl(gson, messageBus, channelRepository, messageRepository);
	}

	@Singleton
	@Provides
	public AuthController provideAuthController(MessageBus messageBus, UserRepository userRepository) {
		return new AuthControllerImpl(messageBus, userRepository);
	}
}
