/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.controllers;

import com.mokhtarabadi.wsserver.models.AuthRequestDTO;
import com.mokhtarabadi.wsserver.repositories.UserRepository;
import dorkbox.messageBus.MessageBus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

	@NonNull private MessageBus messageBus;
	@NonNull private UserRepository userRepository;

	@Override
	public boolean authenticate(AuthRequestDTO dto) {
		return userRepository.isUserExists(dto.getUsername());
	}
}
