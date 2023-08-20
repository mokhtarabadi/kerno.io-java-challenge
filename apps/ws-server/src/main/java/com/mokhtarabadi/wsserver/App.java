/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver;

import com.google.gson.Gson;
import com.mokhtarabadi.sharedlib.helpers.CassandraHelper;
import com.mokhtarabadi.sharedlib.helpers.KafkaHelper;
import com.mokhtarabadi.sharedlib.helpers.RedisHelper;
import com.mokhtarabadi.wsserver.config.AppConfig;
import com.mokhtarabadi.wsserver.di.AppComponent;
import com.mokhtarabadi.wsserver.di.DaggerAppComponent;
import com.mokhtarabadi.wsserver.services.BrokerService;
import com.mokhtarabadi.wsserver.services.WebSocketService;
import dorkbox.messageBus.MessageBus;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

	private final Gson gson;
	private final AppConfig appConfig;
	private final MessageBus messageBus;
	private final WebSocketService webSocketService;
	private final BrokerService brokerService;

	@Inject
	public App(Gson gson, AppConfig appConfig, MessageBus messageBus, WebSocketService webSocketService,
			BrokerService brokerService) {
		this.gson = gson;
		this.appConfig = appConfig;
		this.messageBus = messageBus;
		this.webSocketService = webSocketService;
		this.brokerService = brokerService;
	}

	public void run() {
		// initialize redis
		RedisHelper.getInstance().initializeConnection(appConfig.getRedisAddress(), appConfig.getRedisPort(),
				appConfig.getRedisPassword());

		// initialize kafka
		KafkaHelper.getInstance().initializeConnection(
				String.format("%s:%s", appConfig.getKafkaAddress(), appConfig.getKafkaPort()),
				appConfig.getKafkaSchemaRegistryUrl(), appConfig.getKafkaGroupId());

		// initialize cassandra
		CassandraHelper.getInstance().initConnection(appConfig.getCassandraAddress(), appConfig.getCassandraPort(),
				appConfig.getCassandraUsername(), appConfig.getCassandraPassword(), appConfig.getCassandraDatacenter(),
				appConfig.getCassandraKeyspace());

		// initialize message bus
		messageBus.subscribe(webSocketService);

		log.info("Starting ws-server...");
		webSocketService.setReuseAddr(true);
		webSocketService.setTcpNoDelay(true);
		webSocketService.start();

		// listen for messages from worker
		brokerService.listenForMessages();

		log.info("ws-server started");
	}

	public static void main(String[] args) {
		AppComponent appComponent = DaggerAppComponent.create();
		appComponent.getApp().run();
	}
}
