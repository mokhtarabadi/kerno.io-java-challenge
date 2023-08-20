/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.di;

import com.google.gson.Gson;
import com.mokhtarabadi.wsserver.config.AppConfig;
import com.mokhtarabadi.wsserver.controllers.AuthController;
import com.mokhtarabadi.wsserver.controllers.ChannelController;
import com.mokhtarabadi.wsserver.services.BrokerService;
import com.mokhtarabadi.wsserver.services.WebSocketService;
import dagger.Module;
import dagger.Provides;
import dorkbox.messageBus.MessageBus;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Module
public class ServiceModule {

	@Singleton
	@Provides
	public WebSocketService provideWebSocketService(@NotNull AppConfig config, BrokerService brokerService,
			@NotNull AuthController authController, @NotNull ChannelController channelController, @NotNull Gson gson) {
		return new WebSocketService(config.getServerAddress(), config.getServerPort(), brokerService, authController,
				channelController, gson);
	}

	@Singleton
	@Provides
	public BrokerService provideBrokerService(@NotNull Gson gson, @NotNull MessageBus messageBus) {
		return new BrokerService(gson, messageBus);
	}
}
