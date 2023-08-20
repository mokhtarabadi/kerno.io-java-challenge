/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.events;

import com.mokhtarabadi.wsserver.models.WebSocketMessage;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WebSocketEvent {
	private String recipient;

	private WebSocketMessage message;
}
