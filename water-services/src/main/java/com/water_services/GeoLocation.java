package com.water_services;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Builder


public class GeoLocation {
    
	@JsonProperty("latitude")
	private Double latitude = 0.0;
	
	@JsonProperty("longitude")
	private Double longitude = 0.0;

	@JsonProperty("additionalDetails")
	private String additionalDetails = null;
}
