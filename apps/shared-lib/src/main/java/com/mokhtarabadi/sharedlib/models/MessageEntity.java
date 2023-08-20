/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.models;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.mokhtarabadi.sharedlib.constants.CassandraTables;
import java.util.UUID;
import lombok.Data;

@Entity
@CqlName(CassandraTables.MESSAGES_TABLE)
@Data
public class MessageEntity {

	@ClusteringColumn(1)
	private UUID id;

	@PartitionKey
	private String channel;

	private String sender;
	private String text;

	@ClusteringColumn(0)
	private long timestamp;

	@CqlName("created_at")
	private long createdAt;

	@CqlName("updated_at")
	private long updatedAt;
}
