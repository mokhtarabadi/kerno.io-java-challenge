/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.models;

import lombok.Data;

@Data
public class AuthResponseDTO {
	private boolean success;
	private String token;
}
