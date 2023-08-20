/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.di;

import com.google.gson.Gson;
import com.mokhtarabadi.wsserver.repositories.*;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Module
public class RepositoryModule {

	@Singleton
	@Provides
	public ChannelRepository provideChannelRepository() {
		return new ChannelRepositoryImpl();
	}

	@Singleton
	@Provides
	public UserRepository provideUserRepository() {
		return new UserRepositoryImpl();
	}

	@Singleton
	@Provides
	public MessageRepository provideMessageRepository(@NotNull Gson gson) {
		return new MessageRepositoryImpl(gson);
	}
}
