/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.events;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrokerEvent {
	public enum EventType {
		@SerializedName("PUBLISH_MESSAGE")
		PUBLISH_MESSAGE
	}

	private EventType type;
	private Object data;
}
