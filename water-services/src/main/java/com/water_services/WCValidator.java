package com.water_services;

import java.util.Arrays;
import java.util.List;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Component;

import com.water_services.CustomException;
import com.water_services.WCConfiguration;
import com.water_services.WaterConnectionSearchCriteria;



@Component
public class WCValidator {
	
	 private WCConfiguration config;
	
     public void validateSearch(RequestInfo requestInfo,WaterConnectionSearchCriteria criteria){
//        if(!requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" )&& criteria.isEmpty())
//            throw new CustomException("INVALID SEARCH","Search without any paramters is not allowed");
//
//        if(!requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" )&& criteria.tenantIdOnly())
//            throw new CustomException("INVALID SEARCH","Search based only on tenantId is not allowed");
//
//        if(!requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" )&& !criteria.tenantIdOnly()
//                && criteria.getTenantId()==null)
//            throw new CustomException("INVALID SEARCH","TenantId is mandatory in search");
//
//        if(requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" ) && !criteria.isEmpty()
//                && !criteria.tenantIdOnly() && criteria.getTenantId()==null)
//            throw new CustomException("INVALID SEARCH","TenantId is mandatory in search");
//
//        if(requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" )&& criteria.tenantIdOnly())
//            throw new CustomException("INVALID SEARCH","Search only on tenantId is not allowed");
//
//        String allowedParamStr = null;
//
//        if(requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" ))
//            allowedParamStr = config.getAllowedCitizenSearchParameters();
//        else if(requestInfo.getUserInfo().getType().equalsIgnoreCase("EMPLOYEE" ))
//            allowedParamStr = config.getAllowedEmployeeSearchParameters();
//        else throw new CustomException("INVALID SEARCH","The userType: "+requestInfo.getUserInfo().getType()+
//                    " does not have any search config");
//
//        if(StringUtils.isEmpty(allowedParamStr) && !criteria.isEmpty())
//            throw new CustomException("INVALID SEARCH","No search parameters are expected");
//        else {
//            List<String> allowedParams = Arrays.asList(allowedParamStr.split(","));
//            validateSearchParams(criteria, allowedParams);
//        }
    }
    
    private void validateSearchParams(WaterConnectionSearchCriteria criteria,List<String> allowedParams){

//        if(criteria.getConnectionNo()!=null && !allowedParams.contains("connectionNo"))
//            throw new CustomException("INVALID SEARCH","Search on applicationNumber is not allowed");
//
//        if(criteria.getTenantId()!=null && !allowedParams.contains("tenantId"))
//            throw new CustomException("INVALID SEARCH","Search on tenantId is not allowed");
//
//        if(criteria.getToDate()!=null && !allowedParams.contains("toDate"))
//            throw new CustomException("INVALID SEARCH","Search on toDate is not allowed");
//
//        if(criteria.getFromDate()!=null && !allowedParams.contains("fromDate"))
//            throw new CustomException("INVALID SEARCH","Search on fromDate is not allowed");
//
//        if(criteria.getStatus()!=null && !allowedParams.contains("status"))
//            throw new CustomException("INVALID SEARCH","Search on Status is not allowed");
//
//        if(criteria.getIds()!=null && !allowedParams.contains("ids"))
//            throw new CustomException("INVALID SEARCH","Search on ids is not allowed");
//
//        if(criteria.getMobileNumber()!=null && !allowedParams.contains("mobileNumber"))
//            throw new CustomException("INVALID SEARCH","Search on mobileNumber is not allowed");
//
//        
//        if(criteria.getOldConnectionNo()!=null && !allowedParams.contains("oldConnectionNo"))
//            throw new CustomException("INVALID SEARCH","Search on oldLicenseNumber is not allowed");
//
//        if(criteria.getOffset()!=null && !allowedParams.contains("offset"))
//            throw new CustomException("INVALID SEARCH","Search on offset is not allowed");
//
//        if(criteria.getLimit()!=null && !allowedParams.contains("limit"))
//            throw new CustomException("INVALID SEARCH","Search on limit is not allowed");

    }

}
