package com.water_services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OccupancyType {

	OWNER("OWNER"), TENANT("TENANT");

	private String value;

	OccupancyType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name();
	}

	@JsonCreator
	public static OccupancyType fromValue(String passedValue) {
		for (OccupancyType obj : OccupancyType.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}

}
