package com.water_services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class WaterConnectionResponse {
	
	  @JsonProperty("ResponseInfo")
      private ResponseInfo responseInfo = null;
	  
      @JsonProperty("WaterConnection")
      @Valid
      private List<WaterConnection> waterConnection = null;


      public WaterConnectionResponse addLicensesItem(WaterConnection waterConnectionItems) {
          if (this.waterConnection == null) {
          this.waterConnection = new ArrayList<>();
          }
      this.waterConnection.add(waterConnectionItems);
      return this;
      }
	


}
