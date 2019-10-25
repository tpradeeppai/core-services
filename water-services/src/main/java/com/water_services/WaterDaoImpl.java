package com.water_services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class WaterDaoImpl implements WaterDao {
	
	@Autowired
    private JdbcTemplate jdbcTemplate; 
	
	@Autowired
	private WaterProducer waterProducer;
	
	@Autowired
	private WCQueryBuilder wCQueryBuilder;
	
	
	@Autowired
	private CustomKafkaTemplate<String, Object> kafkaTemplate;
	
	public WaterDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// this method is optional used earlier but now addArticle is being used
    public void insertEmployee(WaterModel emp) {
		 final String sql = "insert into water_connection(connection_id, water_connection) values(?,?)";
		 
	        KeyHolder holder = new GeneratedKeyHolder();
	        SqlParameterSource param = new MapSqlParameterSource()
					.addValue("connection_id", emp.getConnection_id())
					.addValue("water_connection", emp.getWater_connection());
					
	        jdbcTemplate.update(sql,param, holder);
		
	}
   
   @Override
	public void addArticle(WaterModel article) {
		//Add article
		String sql = "INSERT INTO water_connection (connection_id, water_connection) values (?, ?)";
		jdbcTemplate.update(sql, article.getConnection_id(), article.getWater_connection());
		
		
		
		//Fetch article id
		sql = "SELECT connection_id FROM water_connection WHERE connection_id = ? and water_connection=?";
		int articleId = jdbcTemplate.queryForObject(sql, Integer.class, article.getConnection_id(), article.getWater_connection());
		
		//Set article id 
		article.setConnection_id(articleId);
}

@Override
public boolean articleExists(int title, String category) {
	String sql = "SELECT count(*) FROM water_connection WHERE connection_id = ? and water_connection=?";
	int count = jdbcTemplate.queryForObject(sql, Integer.class, title, category);
	if(count == 0) {
		return false;
	} else {
		return true;
	}
}

@Override
public List<WaterModel> getAllArticles() {
	String sql = "SELECT connection_id,water_connection FROM water_connection";
    //RowMapper<Article> rowMapper = new BeanPropertyRowMapper<Article>(Article.class);
	RowMapper<WaterModel> rowMapper = new WaterRowMapper();
	return this.jdbcTemplate.query(sql, rowMapper);
}

@Override
public void save(WaterModel waterModel) {
	waterProducer.push("shriya", waterModel);
}

@Override
public List<WaterConnection> getLicenses(WaterConnectionSearchCriteria criteria) {
    List<Object> preparedStmtList = new ArrayList<>();
    String query = wCQueryBuilder.getWCSearchQuery(criteria, preparedStmtList);
//    log.info("Query: " + query);
  //  List<WaterConnection> licenses =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
   // sortChildObjectsById(licenses);
    return null;
}

@Override
public void saveWaterConnection(WaterConnectionRequest waterConnectionRequest) {
	waterProducer.push("shriya", waterConnectionRequest);
 }


@Override
public void addWaterConnectionList(WaterConnectionRequest waterConnectionRequest) {
	// TODO Auto-generated method stub
	List<WaterConnection> waterConnectionList= waterConnectionRequest.getWaterConnection();
	  wCQueryBuilder.getWCInsertQuery(waterConnectionList);
	// String sql = jdbcTemplate.update(query, article.getConnection_id(), article.getWater_connection());
	
	
	
}


}
