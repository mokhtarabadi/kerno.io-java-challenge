/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.controllers;

import com.mokhtarabadi.sharedlib.models.MessageEvent;

public interface MessageController {

	void saveMessage(MessageEvent message);

}
