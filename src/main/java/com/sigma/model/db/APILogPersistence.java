package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.APILog;
public class APILogPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.APILogPersistence");
	static final String INSERT_LOG_SQL = "INSERT INTO API_LOG (IP_ADDRESS, MAIL_ID, "
			+ "API_NAME, METHOD, PAGE) VALUES (?,?,?,?,?)";
	static final String GET_BY_MAIL = "SELECT * FROM API_LOG WHERE MAIL_ID = ?";
	static final String GET_BY_NAME = "SELECT * FROM API_LOG WHERE API_NAME = ?";
	static final String GET_BY_NAME_AND_MAIL = "SELECT * FROM API_LOG WHERE METHOD = ? AND API_NAME = ? AND MAIL_ID = ?";
	
	public int createApiLog(APILog log, JdbcTemplate jdbcTemplate) {
		try {
			int update = jdbcTemplate.update(INSERT_LOG_SQL, log.getIpAddress(), log.getMailId(),
					log.getApiName(), log.getMethod(), log.getPage());
			return update;
		} catch (Exception exception) {
			LOGGER.error("Error recording api logs ", exception);
			return 0;
		}
	}
	
	public List<APILog> getApiLog(JdbcTemplate jdbcTemplate) {
		try {
			//List<UserProfile> userList = new ArrayList<UserProfile>();
			String query1 = "SELECT * FROM API_LOG";
			List<APILog> users = jdbcTemplate.query(query1, new APILogRowMapper());
			if(users == null || users.isEmpty())
				return new ArrayList<APILog>();		
		return users;
		}catch(Exception exception) {
			LOGGER.error("APILogPersistence.getApiLog() ", exception);
			return new ArrayList<APILog>();
		}
	}
	public List<APILog> getApiLogbyMail(JdbcTemplate jdbcTemplate, String mailId) {
		try {
			//List<UserProfile> userList = new ArrayList<UserProfile>();
			
			List<APILog> logs = jdbcTemplate.query(GET_BY_MAIL, new APILogRowMapper(),new Object[] { mailId });
			if(logs == null || logs.isEmpty())
				return new ArrayList<APILog>();		
		return logs;
		}catch(Exception exception) {
			LOGGER.error("APILogPersistence.getApiLogbyMail() ", exception);
			return new ArrayList<APILog>();
		}
	}
	public List<APILog> getApiLogbyName(JdbcTemplate jdbcTemplate, String name) {
		try {
			//List<UserProfile> userList = new ArrayList<UserProfile>();
			
			List<APILog> logs = jdbcTemplate.query(GET_BY_NAME, new APILogRowMapper(),new Object[] { name });
			if(logs == null || logs.isEmpty())
				return new ArrayList<APILog>();		
		return logs;
		}catch(Exception exception) {
			LOGGER.error("APILogPersistence.getApiLogbyName() ", exception);
			return new ArrayList<APILog>();
		}
	}
	public List<APILog> getApiLogbyNameandMail(JdbcTemplate jdbcTemplate, String method, String name, String mail) {
		try {
			//List<UserProfile> userList = new ArrayList<UserProfile>();
			
			List<APILog> logs = jdbcTemplate.query(GET_BY_NAME_AND_MAIL, new APILogRowMapper(),new Object[] { method, name, mail });
			if(logs == null || logs.isEmpty())
				return new ArrayList<APILog>();		
		return logs;
		}catch(Exception exception) {
			LOGGER.error("APILogPersistence.getApiLogbyName() ", exception);
			return new ArrayList<APILog>();
		}
	}
}
