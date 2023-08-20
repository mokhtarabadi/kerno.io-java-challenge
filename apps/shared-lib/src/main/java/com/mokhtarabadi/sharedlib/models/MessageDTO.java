/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

@Data
public class MessageDTO {
	@SerializedName("acknowledge_id")
	private String acknowledgeId;

	@NonNull private String sender;
	@NonNull private String channel;
	@NonNull private String text;

	private long timestamp;
}
