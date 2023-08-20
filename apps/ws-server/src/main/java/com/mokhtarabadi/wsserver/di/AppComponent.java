/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.di;

import com.mokhtarabadi.wsserver.App;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, ConfigModule.class, ServiceModule.class, ControllerModule.class,
		RepositoryModule.class})
public interface AppComponent {
	App getApp();
}
