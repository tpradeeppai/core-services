package com.water_services;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class WaterModel {
	
	private int connection_id;
	private String water_connection;
	public int getConnection_id() {
		return connection_id;
	}
	public void setConnection_id(int connection_id) {
		this.connection_id = connection_id;
	}
	public String getWater_connection() {
		return water_connection;
	}
	public void setWater_connection(String water_connection) {
		this.water_connection = water_connection;
	}
	

}
