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
public class WaterConnection {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("proppropertyerty")
	private Property property;

	@JsonProperty("applicationNo")
	private String applicationNo = null;

	@JsonProperty("applicationStatus")
	private String applicationStatus = null;

	@JsonProperty("status")
	private String status = null;

	@JsonProperty("connectionNo")
	private String connectionNo = null;

	@JsonProperty("oldConnectionNo")
	private String oldConnectionNo = null;

	@JsonProperty("documents")
	private List<Document> documents = null;

	@JsonProperty("connectionCategory")
	private String connectionCategory = null;

	@JsonProperty("rainWaterHarvesting")
	private boolean rainWaterHarvesting;

	@JsonProperty("connectionType")
	private String connectionType = null;

	@JsonProperty("waterSource")
	private String waterSource = null;

	@JsonProperty("meterId")
	private String meterId = null;
	
	@JsonProperty("meterInstallationDate")
	private Long meterInstallationDate = 0l;

}
