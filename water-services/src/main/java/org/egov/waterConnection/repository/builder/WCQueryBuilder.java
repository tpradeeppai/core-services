package org.egov.waterConnection.repository.builder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.egov.waterConnection.model.WaterConnectionSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WCQueryBuilder {

	private final static String Query = "select * from water_service_connection";

	public String getSearchQueryString(WaterConnectionSearchCriteria criteria, List<Object> preparedStatement) {
		StringBuilder query = new StringBuilder(Query);
		if (criteria.getTenantId() != null && !criteria.getTenantId().isEmpty()) {
			addClauseIfRequired(preparedStatement, query);

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
