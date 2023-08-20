/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.di;

import com.google.gson.Gson;
import com.mokhtarabadi.messagesworker.controllers.MessageController;
import com.mokhtarabadi.messagesworker.controllers.MessageControllerImpl;
import com.mokhtarabadi.messagesworker.repositories.MessageRepository;
import dagger.Module;
import dagger.Provides;
import dorkbox.messageBus.MessageBus;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Module
public class ControllerModule {

	@Singleton
	@Provides
	public MessageController provideMessageController(@NotNull Gson gson, MessageBus messageBus,
			MessageRepository messageRepository) {
		return new MessageControllerImpl(gson, messageBus, messageRepository);
	}
}
