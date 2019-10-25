package com.water_services;

import java.util.List;

public interface WaterDao {
	void insertEmployee(WaterModel emp);
	void addArticle(WaterModel article);
	void addWaterConnectionList(WaterConnectionRequest waterConnectionRequest);
	boolean articleExists(int title, String category);
	
	 List<WaterModel> getAllArticles();
	 void save(WaterModel waterModel);
	 void saveWaterConnection(WaterConnectionRequest waterConnectionRequest);
	 
	 List<WaterConnection> getLicenses(WaterConnectionSearchCriteria criteria);
}
