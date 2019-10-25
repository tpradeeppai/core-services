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


public class ConstructionDetail {
	
	
	
	@JsonProperty("id")
    private String id = null;
	
	@JsonProperty("totalUnitArea")
    private Double totalUnitArea = 0.0;
	
	@JsonProperty("builtUpArea")
    private Double builtUpArea = 0.0;
	
	@JsonProperty("carpetArea")
    private Double carpetArea = 0.0;
	
	@JsonProperty("superBuiltUpArea")
    private Double superBuiltUpArea = 0.0;
	
	@JsonProperty("constructionType")
    private String constructionType = null;
	
	@JsonProperty("constructionDate")
    private int constructionDate=0;
	
	@JsonProperty("dimensions")
    private String dimensions = null;
	
	@JsonProperty("auditDetails")
    private AuditDetail auditDetails = null;
	
	@JsonProperty("additionalDetails")
    private String additionalDetails = null;
	
	
}
