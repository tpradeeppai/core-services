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


public class Dimension {
	
	@JsonProperty("dimensionDescription")
	private String DimensionDescription= null;
	
}
