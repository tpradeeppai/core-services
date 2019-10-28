package org.egov.waterConnection.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.waterConnection.model.Property;
import org.egov.waterConnection.model.WaterConnectionRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ValidateProperty {

	public void validatePropertyCriteria(WaterConnectionRequest waterConnectionRequest) {
		Map<String, String> errorMap = new HashMap<>();
		Property property = new WaterConnectionRequest().getWaterConnection().getProperty();
		if (property.getId() == null || !property.getId().isEmpty()) {
			errorMap.put("INVALID PROPERTY", "WaterConnection cannot be updated without propertyId");
		}
		if (property.getTenantId() == null || !property.getTenantId().isEmpty()) {
			errorMap.put("INVALID PROPERTY", "WaterConnection cannot be updated without tenantId");
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
}
