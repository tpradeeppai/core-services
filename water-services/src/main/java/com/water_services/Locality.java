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

public class Locality {

	@JsonProperty("code")
	private String code = null;
	@JsonProperty("name")
	private String name = null;
	@JsonProperty("label")
	private String label = null;
	@JsonProperty("latitude")
	private String latitude = null;
	@JsonProperty("longitude")
	private String longitude = null;
	@JsonProperty("children")
	private List<Locality> children = null;
	@JsonProperty("materializedPath")
	private String materializedPath = null;
}
