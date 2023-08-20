/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.di;

import com.google.gson.Gson;
import com.mokhtarabadi.messagesworker.controllers.MessageController;
import com.mokhtarabadi.messagesworker.services.BrokerService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Module
public class ServiceModule {

	@Singleton
	@Provides
	public BrokerService provideBrokerService(@NotNull Gson gson, MessageController messageController) {
		return new BrokerService(gson, messageController);
	}

}
