package com.water_services;

import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
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
public class WaterConnectionSearchCriteria {
	
	    @JsonProperty("tenantId")
	    private String tenantId;

	    @JsonProperty("status")
	    private String status;

	    @JsonProperty("ids")
	    private List<String> ids;

	    @JsonProperty("connectionNo")
	    private List<String> connectionNo;

	    @JsonProperty("oldConnectionNo")
	    private List<String> oldConnectionNo;

        @JsonProperty("mobileNumber")
	    private String mobileNumber;

	    @JsonIgnore
	    private String accountId;
	    
        @JsonProperty("fromDate")
	    private Long fromDate = 0L;

	    @JsonProperty("toDate")
	    private Long toDate = 0L;


	    @JsonProperty("offset")
	    private Integer offset;

	    @JsonProperty("limit")
	    private Integer limit;

	    @JsonIgnore
	    private List<String> ownerIds;


	    public boolean isEmpty() {
	        return (this.tenantId == null && this.status == null && this.ids == null && this.connectionNo == null
	                && this.oldConnectionNo == null && this.mobileNumber == null &&
	                this.fromDate == null && this.toDate == null && this.ownerIds == null
	        );
	    }

	    public boolean tenantIdOnly() {
	    	 return (this.tenantId == null && this.status == null && this.ids == null && this.connectionNo == null
		                && this.oldConnectionNo == null && this.mobileNumber == null &&
		                this.fromDate == null && this.toDate == null && this.ownerIds == null
		        );
	    }
	
	

}
