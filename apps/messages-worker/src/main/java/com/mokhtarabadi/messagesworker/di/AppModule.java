/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mokhtarabadi.messagesworker.config.AppConfig;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;
import dorkbox.messageBus.MessageBus;
import javax.inject.Singleton;

@Module
public class AppModule {

	@Singleton
	@Provides
	public AppConfig provideAppConfig(Config config) {
		return new AppConfig(config);
	}

	@Provides
	@Singleton
	public Gson provideGson() {
		return new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().create();
	}

	@Provides
	@Singleton
	public MessageBus provideMessageBus() {
		return new MessageBus();
	}
}
