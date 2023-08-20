/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class SubscribeRequestDTO {
	@NonNull private String user;
	@NonNull private String channel;
}
