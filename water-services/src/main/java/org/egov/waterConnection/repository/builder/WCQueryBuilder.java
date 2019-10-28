package org.egov.waterConnection.repository.builder;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.waterConnection.model.Property;
import org.egov.waterConnection.model.WaterConnectionSearchCriteria;
import org.egov.waterConnection.util.WaterServicesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WCQueryBuilder {

	@Autowired
	WaterServicesUtil waterServicesUtil;

	private final static String Query = "select ws.id, ws.id,ws.property_id,ws.applicationno,ws.applicationstatus,ws.status,"
			+ "ws.connectionno,ws.oldconnectionno,ws.documents_id,ws.connectioncategory,ws.rainwaterharvesting,ws.connectiontype,"
			+ "ws.watersource,ws.watersource,ws.meterid,ws.meterinstallationdate" + " from water_service_connection ws";

	public String getSearchQueryString(WaterConnectionSearchCriteria criteria, List<Object> preparedStatement,
			RequestInfo requestInfo) {
		StringBuilder query = new StringBuilder(Query);

		if (criteria.getTenantId() != null && !criteria.getTenantId().isEmpty()) {
			List<String> propertyIds = new ArrayList<>();
			addClauseIfRequired(preparedStatement, query);
			List<Property> propertyList = waterServicesUtil.propertyCallForSearchCriteria(criteria, requestInfo);
			propertyList.forEach(property -> propertyIds.add(property.getId()));
			if (!propertyIds.isEmpty())
				query.append(" property_id in (").append(createQuery(propertyIds)).append(" )");
		}
		if (!CollectionUtils.isEmpty(criteria.getIds())) {
			addClauseIfRequired(preparedStatement, query);
			query.append(" id in (").append(createQuery(criteria.getIds())).append(" )");
			addToPreparedStatement(preparedStatement, criteria.getIds());
		}
		if (criteria.getOldConnectionNumber() != null && !criteria.getOldConnectionNumber().isEmpty()) {
			addClauseIfRequired(preparedStatement, query);
			query.append(" oldconnectionno = ? ");
			preparedStatement.add(criteria.getOldConnectionNumber());
		}

		if (criteria.getOldConnectionNumber() != null && !criteria.getOldConnectionNumber().isEmpty()) {
			addClauseIfRequired(preparedStatement, query);
			query.append(" connectionno = ? ");
			preparedStatement.add(criteria.getConnectionNumber());
		}
		return query.toString();
	}

	private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" OR");
		}
	}

	private String createQuery(List<String> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append(" ?");
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}

	private void addToPreparedStatement(List<Object> preparedStatement, List<String> ids) {
		ids.forEach(id -> {
			preparedStatement.add(id);
		});
	}
}
