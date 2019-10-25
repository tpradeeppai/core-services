package com.water_services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Source {
	MUNICIPAL_RECORDS("MUNICIPAL_RECORDS"), FIELD_SURVEY("FIELD_SURVEY");

	private String value;

	Source(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name();
	}

	@JsonCreator
	public static Source fromValue(String passedValue) {
		for (Source obj : Source.values()) {

			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}
}
