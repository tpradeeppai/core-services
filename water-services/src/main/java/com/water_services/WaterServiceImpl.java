package com.water_services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.water_services.WaterConnection;
import com.water_services.WaterConnectionSearchCriteria;
import com.water_services.WaterDao;
import com.water_services.WaterModel;




@Component
public class WaterServiceImpl implements WaterService {
	

    Logger logger = LoggerFactory.getLogger(WaterServiceImpl.class);
	@Resource 
	WaterDao waterDao;
	
	 @Autowired
	 private WCValidator wcValidator;
	 
	 @Autowired
	 private WaterServicesUtil waterServicesUtil;
	 
	
	 

    
	 //@Autowired
	// private UserService userService;
	 
	
//	  public void insertEmployee(WaterModel emp) {
//		 
//		
//		employeeDao.insertEmployee(emp);
//
//		
//	}
	
	@Override
	public synchronized boolean addArticle(WaterModel article){
       if (waterDao.articleExists(article.getConnection_id(), article.getWater_connection())) {
    	   return false;
       } else {
    	   waterDao.addArticle(article);
    	   waterDao.save(article);
    	   return true;
       }}
	
	
	@Override
	public List<WaterModel> getAllArticles(){
		return waterDao.getAllArticles();
	}
	
	@Override
	public List<WaterConnection> search(WaterConnectionSearchCriteria criteria, RequestInfo requestInfo){
	        List<WaterConnection> waterConnections;
	        wcValidator.validateSearch(requestInfo,criteria);
	       // enrichmentService.enrichSearchCriteriaWithAccountId(requestInfo,criteria);
	         if(criteria.getMobileNumber()!=null){
	        	 waterConnections = getLicensesFromMobileNumber(criteria,requestInfo);
	         }
	         else {
	        	 waterConnections = getLicensesWithOwnerInfo(criteria,requestInfo);
	         }
	       return waterConnections;
	    }
	
	 private List<WaterConnection> getLicensesFromMobileNumber(WaterConnectionSearchCriteria criteria, RequestInfo requestInfo){
	        List<WaterConnection> waterConnection = new LinkedList<>();
	      //  UserDetailResponse userDetailResponse = userService.getUser(criteria,requestInfo);
	        // If user not found with given user fields return empty list
//	        if(userDetailResponse.getUser().size()==0){
//	            return Collections.emptyList();
//	        }
	        
	      //  enrichmentService.enrichTLCriteriaWithOwnerids(criteria,userDetailResponse);
	        waterConnection = waterDao.getLicenses(criteria);

	        if(waterConnection.size()==0){
	            return Collections.emptyList();
	        }

	        // Add tradeLicenseId of all licenses owned by the user
	      //  criteria=enrichmentService.getTradeLicenseCriteriaFromIds(licenses);
	        //Get all tradeLicenses with ownerInfo enriched from user service
	        waterConnection = getLicensesWithOwnerInfo(criteria,requestInfo);
	        return waterConnection;
	    }
	 
	 public List<WaterConnection> getLicensesWithOwnerInfo(WaterConnectionSearchCriteria criteria,RequestInfo requestInfo){
	        List<WaterConnection> licenses = waterDao.getLicenses(criteria);
	        if(licenses.isEmpty())
	            return Collections.emptyList();
	      //  licenses = enrichmentService.enrichTradeLicenseSearch(licenses,criteria,requestInfo);
	        return licenses;
	    }
	 
	     
	 
	 @Override 
	 public List<WaterConnection> create(WaterConnectionRequest waterConnectionRequest){
		 
		
		 PropertyResponse propertyData =(PropertyResponse) waterServicesUtil.propertyCall(waterConnectionRequest);
		 //enrichmentService.enrichTLCreateRequest(tradeLicenseRequest,mdmsData);
		 
		 logger.info("An INFO Message"+propertyData);
	      
//	        enrichmentService.enrichTLCreateRequest(waterConnectionRequest,propertyData);
		   
		    // waterDao.addWaterConnectionList(waterConnectionRequest);
		    
	         waterDao.saveWaterConnection(waterConnectionRequest);
			 return waterConnectionRequest.getWaterConnection();
	}

	
	 
       
       
 


		
		
		
}
