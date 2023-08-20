/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.di;

import com.mokhtarabadi.messagesworker.App;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, ConfigModule.class, ServiceModule.class, RepositoryModule.class,
		ControllerModule.class})
public interface AppComponent {
	App getApp();
}
