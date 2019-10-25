package com.water_services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;


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
public class WaterConnectionRequest {
	   @JsonProperty("RequestInfo")
       private RequestInfo requestInfo = null;

       @JsonProperty("waterConnection")
        private List<WaterConnection> waterConnection = null;


       public WaterConnectionRequest addWaterConnectionItem(WaterConnection waterConnection) {
           if (this.waterConnection == null) {
           this.waterConnection = new ArrayList<>();
           }
       this.waterConnection.add(waterConnection);
       return this;
       }
      

}
