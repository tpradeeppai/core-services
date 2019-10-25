package org.egov.waterConnection.repository;

import java.util.List;

import org.egov.waterConnection.model.WaterConnection;
import org.egov.waterConnection.model.WaterConnectionRequest;
import org.egov.waterConnection.model.WaterConnectionSearchCriteria;

public interface WaterDao {
	public void saveWaterConnection(WaterConnectionRequest waterConnectionRequest);
	public List<WaterConnection> getWaterConnectionList(WaterConnectionSearchCriteria criteria);


}
