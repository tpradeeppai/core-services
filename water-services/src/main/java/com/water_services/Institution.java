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


public class Institution {
	
	
	@JsonProperty("id")
    private String id = null;
	
	@JsonProperty("tenantId")
    private String tenantId = null;
	
	@JsonProperty("type")
    private String type = null;
	
	
	@JsonProperty("designation")
    private String designation = null;
	
	@JsonProperty("nameOfAuthorizedPerson")
    private String nameOfAuthorizedPerson = null;
	
	@JsonProperty("additionalDetails")
    private String additionalDetails = null;
	
	
	

}
