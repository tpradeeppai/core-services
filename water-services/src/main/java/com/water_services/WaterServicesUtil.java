package com.water_services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WaterServicesUtil {
	  
	  private ServiceRequestRepository serviceRequestRepository;
	  
	  @Value("${egov.property.service.host}")
	  private String propertyHost;
	  
	  @Value("${egov.property.service.context.path}")
	  private String propertyContextPath;
	  
	  @Value("${egov.property.searchendpoint}")
	  private String propertyEndPoint;
	  
	  @Autowired
	    public WaterServicesUtil(ServiceRequestRepository serviceRequestRepository
	                    ) {
	 
	        this.serviceRequestRepository = serviceRequestRepository;
	        
	    }
	
	
	  public Object propertyCall(WaterConnectionRequest waterConnectionRequest){
	        RequestInfo requestInfo = waterConnectionRequest.getRequestInfo();
	        //String propertyId = waterConnectionRequest.getWaterConnection().get(0).getProperty().getPropertyId();
	        List<Property> propertyList= new ArrayList<>();
	        
	        int n=waterConnectionRequest.getWaterConnection().size();
	        for(int i=0;i<n;i++){
	        	  propertyList.add(waterConnectionRequest.getWaterConnection().get(i).getProperty());	    
	        	}
	        PropertyRequest propertyReq = getPropertyRequest(requestInfo,propertyList);
	        Object result = serviceRequestRepository.fetchResult(getPropertyCreateURL(), propertyReq);
	        return result;
	    }
	  
	  private PropertyRequest getPropertyRequest(RequestInfo requestInfo,List<Property> propertyList){
		  PropertyRequest propertyReq = PropertyRequest.builder().requestInfo(requestInfo).properties(propertyList).build();
	        return propertyReq;
	    }
	  
	
	  public StringBuilder getPropertyCreateURL() {
	        return new StringBuilder().append(propertyHost).append(propertyContextPath).append(propertyEndPoint);
	    }
	 
	  public AuditDetails getAuditDetails(String by, Boolean isCreate) {
	        Long time = System.currentTimeMillis();
	        if(isCreate)
	            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
	        else
	            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
	    }
	  


}
