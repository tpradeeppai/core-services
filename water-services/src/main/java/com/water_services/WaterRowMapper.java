package com.water_services;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class WaterRowMapper implements RowMapper<WaterModel> {

	public WaterModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		WaterModel emp = new WaterModel();
		emp.setConnection_id(rs.getInt("connection_id"));
		emp.setWater_connection(rs.getString("water_connection"));
	
      return emp;
	}

}
