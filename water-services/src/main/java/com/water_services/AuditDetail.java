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


public class AuditDetail {
	
	@JsonProperty("createdBy")
    private String createdBy = null;
	
	
	@JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;
	
	@JsonProperty("createdTime")
    private int createdTime = 0;
	
	@JsonProperty("lastModifiedTime")
    private int lastModifiedTime = 0;

}
