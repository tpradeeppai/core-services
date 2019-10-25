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
public class Unit {
	

	@JsonProperty("id")
    private String id = null;
	
	@JsonProperty("tenantId")
    private String tenantId = null;
	
	@JsonProperty("floorNo")
    private String floorNo = null;
	
	@JsonProperty("unitType")
    private String unitType = null;
	
	@JsonProperty("usageCategory")
    private String usageCategory = null;
	
	@JsonProperty("occupancyType")
    private String occupancyType = null;
	
	@JsonProperty("occupancyDate")
    private int occupancyDate = 0;
	
	@JsonProperty("additionalDetails")
    private String additionalDetails = null;
	
}
