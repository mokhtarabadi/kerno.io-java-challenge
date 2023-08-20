/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker;

import com.mokhtarabadi.messagesworker.config.AppConfig;
import com.mokhtarabadi.messagesworker.di.AppComponent;
import com.mokhtarabadi.messagesworker.di.DaggerAppComponent;
import com.mokhtarabadi.messagesworker.services.BrokerService;
import com.mokhtarabadi.sharedlib.helpers.CassandraHelper;
import com.mokhtarabadi.sharedlib.helpers.KafkaHelper;
import com.mokhtarabadi.sharedlib.helpers.RedisHelper;
import dorkbox.messageBus.MessageBus;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

	private final AppConfig appConfig;
	private final MessageBus messageBus;
	private final BrokerService brokerService;

	@Inject
	public App(AppConfig appConfig, MessageBus messageBus, BrokerService brokerService) {
		this.appConfig = appConfig;
		this.messageBus = messageBus;
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
		messageBus.subscribe(brokerService);

		log.info("Starting messages-worker...");

		// listen for messages and process them
		brokerService.listenForMessages();
	}

	public static void main(String[] args) {
		AppComponent appComponent = DaggerAppComponent.create();
		appComponent.getApp().run();
	}
}
