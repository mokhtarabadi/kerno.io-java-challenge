/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.di;

import com.mokhtarabadi.messagesworker.repositories.MessageRepository;
import com.mokhtarabadi.messagesworker.repositories.MessageRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class RepositoryModule {

	@Singleton
	@Provides
	public MessageRepository provideMessageRepository() {
		return new MessageRepositoryImpl();
	}
}
