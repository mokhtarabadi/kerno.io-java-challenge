/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.helpers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateIndex;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.mokhtarabadi.sharedlib.constants.CassandraTables;
import com.mokhtarabadi.sharedlib.dao.MessageDAO;
import com.mokhtarabadi.sharedlib.mappers.DataMapper;
import com.mokhtarabadi.sharedlib.mappers.DataMapperBuilder;
import java.net.InetSocketAddress;
import lombok.Getter;

@Getter
public class CassandraHelper {

	private CqlSession cqlSession;

	private MessageDAO messageDAO;

	private CassandraHelper() {

	}

	private static class Holder {
		private static final CassandraHelper INSTANCE = new CassandraHelper();
	}

	public static CassandraHelper getInstance() {
		return Holder.INSTANCE;
	}

	public void initConnection(String address, int port, String username, String password, String datacenter,
			String keyspace) {
		CqlSessionBuilder builder = new CqlSessionBuilder();
		builder.withAuthCredentials(username, password);
		builder.addContactPoint(new InetSocketAddress(address, port));
		builder.withLocalDatacenter(datacenter);
		cqlSession = builder.build();

		createKeyspace(keyspace);
		createMessagesTable(keyspace);

		createDAOs(keyspace);
	}

	private void createKeyspace(String keyspace) {
		CreateKeyspace createKeyspaceQuery = SchemaBuilder.createKeyspace(keyspace).ifNotExists().withSimpleStrategy(3);

		SimpleStatement statement = createKeyspaceQuery.build();
		cqlSession.execute(statement);
	}

	private void createMessagesTable(String keyspace) {
		CreateTableWithOptions createTableQuery = SchemaBuilder.createTable(keyspace, CassandraTables.MESSAGES_TABLE)
				.ifNotExists().withPartitionKey("channel", DataTypes.TEXT)
				.withClusteringColumn("timestamp", DataTypes.BIGINT).withClusteringColumn("id", DataTypes.UUID)
				.withColumn("sender", DataTypes.TEXT).withColumn("text", DataTypes.TEXT)
				.withColumn("created_at", DataTypes.BIGINT).withColumn("updated_at", DataTypes.BIGINT)
				.withClusteringOrder("timestamp", ClusteringOrder.DESC);

		SimpleStatement statement = createTableQuery.build();
		cqlSession.execute(statement);

		CreateIndex senderIndex = SchemaBuilder.createIndex("sender_idx").ifNotExists()
				.onTable(keyspace, CassandraTables.MESSAGES_TABLE).andColumn("sender");
		cqlSession.execute(senderIndex.build());
	}

	private void createDAOs(String keyspace) {
		DataMapper dataMapper = new DataMapperBuilder(cqlSession).build();
		messageDAO = dataMapper.messageDAO(CqlIdentifier.fromCql(keyspace), CassandraTables.MESSAGES_TABLE);
	}

	public void close() {
		cqlSession.close();
	}
}
