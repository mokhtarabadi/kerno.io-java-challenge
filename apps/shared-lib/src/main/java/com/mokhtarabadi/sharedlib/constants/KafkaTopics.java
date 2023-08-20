/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTopics {
	// ws-to-worker
	public final String WS_SERVER_TO_MESSAGES_WORKER = "ws-to-worker";
	// worker-to-ws
	public final String MESSAGES_WORKER_TO_WS_SERVER = "worker-to-ws";
}
