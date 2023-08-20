/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.models;

import java.util.List;
import lombok.Data;

@Data
public class ChannelListResponseDTO {
	private List<String> channels;
}
