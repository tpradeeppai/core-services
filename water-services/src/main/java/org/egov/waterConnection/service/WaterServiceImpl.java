package org.egov.waterConnection.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.waterConnection.model.Property;
//import org.egov.tl.web.models.TradeLicense;
//import org.egov.tl.web.models.TradeLicenseSearchCriteria;
//import org.egov.tl.web.models.user.UserDetailResponse;
import org.egov.waterConnection.model.PropertyResponse;
import org.egov.waterConnection.model.WaterConnection;
import org.egov.waterConnection.model.WaterConnectionRequest;
import org.egov.waterConnection.model.WaterConnectionSearchCriteria;
import org.egov.waterConnection.repository.WaterDao;
import org.egov.waterConnection.repository.builder.WCQueryBuilder;
import org.egov.waterConnection.util.WaterServicesUtil;
import org.egov.waterConnection.validator.ValidateProperty;
import org.egov.waterConnection.validator.WaterConnectionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterServiceImpl implements WaterService {

	Logger logger = LoggerFactory.getLogger(WaterServiceImpl.class);

	@Autowired
	WaterDao waterDao;

	@Autowired
	WaterServicesUtil waterServicesUtil;

	@Autowired
	WaterConnectionValidator waterConnectionValidator;
	
	@Autowired
	ValidateProperty validateProperty;

	@Override
	public List<WaterConnection> createWaterConnection(WaterConnectionRequest waterConnectionRequest) {
		enrichWaterConnection(waterConnectionRequest);
		waterDao.saveWaterConnection(waterConnectionRequest);
		return Arrays.asList(waterConnectionRequest.getWaterConnection());
	}

	public void enrichWaterConnection(WaterConnectionRequest waterConnectionRequest) {
		List<Property> propertyList = waterServicesUtil.propertyCall(waterConnectionRequest);
		if (propertyList != null && !propertyList.isEmpty())
			waterConnectionRequest.getWaterConnection().setProperty(propertyList.get(0));
	}

	public List<WaterConnection> search(WaterConnectionSearchCriteria criteria, RequestInfo requestInfo) {
		List<WaterConnection> waterConnectionList;

		List<Property> propertyList = waterServicesUtil.propertyCallForSearchCriteria(criteria, requestInfo);
        waterConnectionList = getWaterConnectionsList(criteria, requestInfo);
        return waterConnectionList;
	}

	public List<WaterConnection> getWaterConnectionsList(WaterConnectionSearchCriteria criteria,
			RequestInfo requestInfo) {
		List<WaterConnection> waterConnectionList = waterDao.getWaterConnectionList(criteria,requestInfo);
		if (waterConnectionList.isEmpty())
			return Collections.emptyList();
		return waterConnectionList;
	}

	@Override
	public List<WaterConnection> updateWaterConnection(WaterConnectionRequest waterConnectionRequest) {
		waterConnectionValidator.validateWaterConnection(waterConnectionRequest, true);
		validateProperty.validatePropertyCriteria(waterConnectionRequest);
		waterDao.updateWaterConnection(waterConnectionRequest);
		return Arrays.asList(waterConnectionRequest.getWaterConnection());
	}
}
