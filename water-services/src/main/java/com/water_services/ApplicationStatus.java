package com.water_services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {

	REJECTED("REJECTED"), APPROVED("APPROVED"), CANCELED("CANCELED"), INPROGRESS("INPROGRESS");

	private String value;

	ApplicationStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name();
	}

	@JsonCreator
	public static ApplicationStatus fromValue(String passedValue) {
		for (ApplicationStatus obj : ApplicationStatus.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}

}
