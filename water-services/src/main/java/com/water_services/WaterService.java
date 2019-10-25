package com.water_services;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.water_services.WaterConnection;
import com.water_services.WaterConnectionSearchCriteria;
import com.water_services.WaterModel;

public interface WaterService {
	//void insertEmployee(WaterModel emp);
	boolean addArticle(WaterModel article);
	List<WaterModel> getAllArticles();
	
	List<WaterConnection> create(WaterConnectionRequest waterConnectionRequest);
	
    List<WaterConnection> search(WaterConnectionSearchCriteria criteria, RequestInfo requestInfo);
    
}
