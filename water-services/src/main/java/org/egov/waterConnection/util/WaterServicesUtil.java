package org.egov.waterConnection.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.waterConnection.model.AuditDetails;
import org.egov.waterConnection.model.Property;
import org.egov.waterConnection.model.PropertyCriteria;
import org.egov.waterConnection.model.PropertyRequest;
import org.egov.waterConnection.model.PropertyResponse;
import org.egov.waterConnection.model.RequestInfoWrapper;
//import org.egov.waterConnection.model.PropertyRequest;
import org.egov.waterConnection.model.Role;
import org.egov.waterConnection.model.WaterConnectionRequest;
import org.egov.waterConnection.model.WaterConnectionSearchCriteria;
import org.egov.waterConnection.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WaterServicesUtil {

	private ServiceRequestRepository serviceRequestRepository;

	@Value("${egov.property.service.host}")
	private String propertyHost;

	@Value("${egov.property.createendpoint}")
	private String createPropertyEndPoint;

	@Value("${egov.property.searchendpoint}")
	private String searchPropertyEndPoint;

	@Autowired
	public WaterServicesUtil(ServiceRequestRepository serviceRequestRepository) {
		this.serviceRequestRepository = serviceRequestRepository;

	}

	public List<Property> propertyCall(WaterConnectionRequest waterConnectionRequest) {
		RequestInfo requestInfo = waterConnectionRequest.getRequestInfo();
		if (waterConnectionRequest.getWaterConnection().getProperty() != null
				&& waterConnectionRequest.getWaterConnection().getProperty().getId() != null) {
			String propertyId = waterConnectionRequest.getWaterConnection().getProperty().getId();
			List<Property> propertyList = new ArrayList<>();
			propertyList.add(waterConnectionRequest.getWaterConnection().getProperty());
			PropertyRequest propertyReq = getPropertyRequest(requestInfo, propertyList);
			Object result = serviceRequestRepository.fetchResult(getPropertyCreateURL(), propertyReq);
			return getPropertyDetails(result);
		} else {
			String propertyId = waterConnectionRequest.getWaterConnection().getProperty().getId();
			List<Property> propertyList = new ArrayList<>();
			propertyList.add(waterConnectionRequest.getWaterConnection().getProperty());
			PropertyRequest propertyReq = getPropertyRequest(requestInfo, propertyList);
			Object result = serviceRequestRepository.fetchResult(getPropertyCreateURL(), propertyReq);
			return getPropertyDetails(result);
		}
	}

	public List<Property> propertyCallForSearchCriteria(WaterConnectionSearchCriteria waterConnectionSearchCriteria,
			RequestInfo requestInfo) {
		if (waterConnectionSearchCriteria.getTenantId() != null) {
			HashMap<String, Object> propertyRequestObj = new HashMap<>();
			PropertyCriteria propertyCriteria = new PropertyCriteria();
			propertyCriteria.setTenantId(waterConnectionSearchCriteria.getTenantId());
			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(requestInfo);
			RequestInfoWrapper requestInfoWrapper1=getPropertyRequestInfoWrapperSearch(requestInfoWrapper,requestInfo);
			PropertyCriteria propertyCriteria1=getPropertyCriteriaSearch(propertyCriteria,waterConnectionSearchCriteria);
			propertyRequestObj.put("RequestInfoWrapper", requestInfoWrapper1);
			propertyRequestObj.put("PropertyCriteria", propertyCriteria1);
			Object result = serviceRequestRepository.fetchResult(getPropertySearchURL(), propertyRequestObj);
			return getPropertyDetails(result);
		} else {

			HashMap<String, Object> propertyRequestObj = new HashMap<>();
			PropertyCriteria propertyCriteria = new PropertyCriteria();
			propertyCriteria.setTenantId(waterConnectionSearchCriteria.getTenantId());
			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(requestInfo);
			RequestInfoWrapper requestInfoWrapper1=getPropertyRequestInfoWrapperSearch(requestInfoWrapper,requestInfo);
			PropertyCriteria propertyCriteria1=getPropertyCriteriaSearch(propertyCriteria,waterConnectionSearchCriteria);
			propertyRequestObj.put("RequestInfoWrapper", requestInfoWrapper1);
			propertyRequestObj.put("PropertyCriteria", propertyCriteria1);
			Object result = serviceRequestRepository.fetchResult(getPropertySearchURL(), propertyRequestObj);
			return getPropertyDetails(result);
		}
	}

	private RequestInfoWrapper getPropertyRequestInfoWrapperSearch(RequestInfoWrapper requestInfoWrapper,RequestInfo requestInfo) {
		RequestInfoWrapper requestInfoWrapper_new = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		return requestInfoWrapper_new;
	}
	
	private PropertyCriteria  getPropertyCriteriaSearch(PropertyCriteria propertyCriteria,WaterConnectionSearchCriteria waterConnectionSearchCriteria){
		PropertyCriteria propertyCriteria_new=	PropertyCriteria.builder().tenantId(waterConnectionSearchCriteria.getTenantId()).build();
		return propertyCriteria_new;
	}

	private List<Property> getPropertyDetails(Object result) {
		ObjectMapper mapper = new ObjectMapper();
		PropertyResponse propertyResponse = mapper.convertValue(result, PropertyResponse.class);
		return propertyResponse.getProperties();
	}

	private PropertyRequest getPropertyRequest(RequestInfo requestInfo, List<Property> propertyList) {
		PropertyRequest propertyReq = PropertyRequest.builder().requestInfo(requestInfo).properties(propertyList)
				.build();
		return propertyReq;
	}

	public StringBuilder getPropertyCreateURL() {
		return new StringBuilder().append(propertyHost).append(createPropertyEndPoint);
	}

	public StringBuilder getPropertySearchURL() {
		return new StringBuilder().append(propertyHost).append(searchPropertyEndPoint);
	}

	public MdmsCriteriaReq prepareMdMsRequest(String tenantId, String moduleName, List<String> names, String filter,
			RequestInfo requestInfo) {
		List<MasterDetail> masterDetails = new ArrayList<>();
		MasterDetail masterDetail;

		names.forEach(name -> {
			masterDetails.add(MasterDetail.builder().name(name).filter(filter).build());
		});

		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(moduleName).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}
	
	
	
}
