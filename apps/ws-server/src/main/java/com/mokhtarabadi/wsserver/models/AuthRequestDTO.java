/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.models;

import lombok.Data;

@Data
public class AuthRequestDTO {
	private String username;
	private String password;
}
