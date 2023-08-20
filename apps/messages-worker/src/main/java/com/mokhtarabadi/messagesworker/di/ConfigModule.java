/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.di;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ConfigModule {

	@Singleton
	@Provides
	public Config provideConfig() {
		return ConfigFactory.systemProperties().withFallback(ConfigFactory.load());
	}

}
