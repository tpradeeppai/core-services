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


public class Document {
	
    @JsonProperty("id")
    private String id = null;
	
	@JsonProperty("documentType")
    private String documentType = null;
	
	@JsonProperty("fileStore")
    private String fileStore = null;
	
	@JsonProperty("documentUid")
    private String documentUid = null;
	
	@JsonProperty("additionalDetails")
    private String additionalDetails = null;
	
}
