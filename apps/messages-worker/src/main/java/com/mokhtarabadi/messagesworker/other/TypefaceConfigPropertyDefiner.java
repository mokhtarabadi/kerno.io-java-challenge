/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.messagesworker.other;

import ch.qos.logback.core.PropertyDefinerBase;
import com.typesafe.config.ConfigFactory;

public class TypefaceConfigPropertyDefiner extends PropertyDefinerBase {

	private String propertyName;

	@Override
	public String getPropertyValue() {
		return ConfigFactory.systemProperties().withFallback(ConfigFactory.load()).getString(propertyName);
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
