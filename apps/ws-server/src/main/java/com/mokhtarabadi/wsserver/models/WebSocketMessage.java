/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WebSocketMessage {

	public enum MessageType {
		@SerializedName("AUTH")
		AUTH, @SerializedName("CHANNELS")
		CHANNELS, @SerializedName("SUBSCRIBE")
		SUBSCRIBE, @SerializedName("UNSUBSCRIBE")
		UNSUBSCRIBE, @SerializedName("MESSAGE")
		MESSAGE, @SerializedName("SNAPSHOT")
		SNAPSHOT, @SerializedName("BROADCAST")
		BROADCAST, @SerializedName("ACKNOWLEDGE")
		ACKNOWLEDGE
	}

	private MessageType type;
	private JsonElement data;
}
