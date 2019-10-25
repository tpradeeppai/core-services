package com.water_services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Relationship {
	
	FATHER("FATHER"),HUSBAND("HUSBAND");

	private String value;

	Relationship(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
    public String toString() {
        return name();
    }

	@JsonCreator
	public static Relationship fromValue(String passedValue) {
		for (Relationship obj : Relationship.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}


}
