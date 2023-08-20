/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UnsubscribeResponseDTO {
	private boolean success;

	@SerializedName("error_message")
	private String errorMessage;
}
