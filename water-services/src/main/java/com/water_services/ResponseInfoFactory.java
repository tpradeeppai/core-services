package com.water_services;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@NoArgsConstructor
@Setter
@Builder

@Component
public class ResponseInfoFactory {
	
	  public ResponseInfo createResponseInfoFromRequestInfo(final RequestInfo requestInfo, final Boolean success) {

	        final String apiId = requestInfo != null ? requestInfo.getApiId() : "";
	        final String ver = requestInfo != null ? requestInfo.getVer() : "";
	        Long ts = null;
	        if(requestInfo!=null)
	            ts = requestInfo.getTs();
	        final String resMsgId = "uief87324"; // FIXME : Hard-coded
	        final String msgId = requestInfo != null ? requestInfo.getMsgId() : "";
	        final String responseStatus = success ? "successful" : "failed";

	        return ResponseInfo.builder().apiId(apiId).ver(ver).ts(ts).resMsgId(resMsgId).msgId(msgId).resMsgId(resMsgId)
	                .status(responseStatus).build();
	    }

}
