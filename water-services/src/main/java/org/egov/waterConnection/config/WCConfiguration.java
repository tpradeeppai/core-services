package org.egov.waterConnection.config;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

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

@Component

public class WCConfiguration {
	
	
	    @Value("${citizen.allowed.search.params}")
	    private String allowedCitizenSearchParameters;

	    @Value("${employee.allowed.search.params}")
	    private String allowedEmployeeSearchParameters;

}
