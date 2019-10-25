package com.water_services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CreationReason {
	NEWPROPERTY("NEWPROPERTY"), SUBDIVISION("SUBDIVISION");

	private String value;

	CreationReason(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name();
	}

	@JsonCreator
	public static CreationReason fromValue(String passedValue) {
		for (CreationReason obj : CreationReason.values()) {

			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}
}
