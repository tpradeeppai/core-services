package com.water_services;



import java.util.List;

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


public class Owner {
	
	@JsonProperty("name")
    private String name = null;
	
	@JsonProperty("mobileNumber")
    private String mobileNumber = null;
	
	@JsonProperty("gender")
    private String gender = null;
	
	@JsonProperty("fatherOrHusbandName")
    private String fatherOrHusbandName = null;
	
	@JsonProperty("correspondenceAddress")
    private String correspondenceAddress = null;
	
	
	@JsonProperty("isPrimaryOwner")
    private boolean isPrimaryOwner;
	
	@JsonProperty("ownerShipPercentage")
    private Integer ownerShipPercentage = 0;
	
	@JsonProperty("ownerType")
    private String ownerType = null;
	
	@JsonProperty("documents")
    private List<Document> documents = null;
	
	@JsonProperty("relationship")
    private String relationship = null;
	
	@JsonProperty("additionalDetails")
    private String additionalDetails = null;


}
