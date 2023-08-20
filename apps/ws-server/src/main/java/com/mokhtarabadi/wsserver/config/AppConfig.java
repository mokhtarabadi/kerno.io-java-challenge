/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.config;

import com.typesafe.config.Config;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class AppConfig {

	@Getter(AccessLevel.NONE)
	private final Config config;

	private final String serverAddress;
	private final int serverPort;

	// redis
	private final String redisAddress;
	private final int redisPort;
	private final String redisPassword;

	// kafka
	private final String kafkaAddress;
	private final int kafkaPort;
	private final String kafkaSchemaRegistryUrl;
	private final String kafkaGroupId;

	// cassandra
	private final String cassandraAddress;
	private final int cassandraPort;
	private final String cassandraUsername;
	private final String cassandraPassword;
	private final String cassandraDatacenter;
	private final String cassandraKeyspace;

	public AppConfig(Config config) {
		this.config = config;

		this.serverAddress = config.getString("server.address");
		this.serverPort = config.getInt("server.port");

		this.redisAddress = config.getString("redis.address");
		this.redisPort = config.getInt("redis.port");
		this.redisPassword = config.getString("redis.password");

		this.kafkaAddress = config.getString("kafka.address");
		this.kafkaPort = config.getInt("kafka.port");
		this.kafkaSchemaRegistryUrl = config.getString("kafka.schema_registry_url");
		this.kafkaGroupId = config.getString("kafka.group_id");

		this.cassandraAddress = config.getString("cassandra.address");
		this.cassandraPort = config.getInt("cassandra.port");
		this.cassandraUsername = config.getString("cassandra.username");
		this.cassandraPassword = config.getString("cassandra.password");
		this.cassandraDatacenter = config.getString("cassandra.datacenter");
		this.cassandraKeyspace = config.getString("cassandra.keyspace");
	}

	public String getStringProperty(String propertyName) {
		return config.getString(propertyName);
	}
}
