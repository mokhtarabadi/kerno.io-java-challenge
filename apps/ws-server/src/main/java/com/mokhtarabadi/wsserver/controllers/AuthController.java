/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.controllers;

import com.mokhtarabadi.wsserver.models.AuthRequestDTO;

public interface AuthController {

	boolean authenticate(AuthRequestDTO dto);
}
