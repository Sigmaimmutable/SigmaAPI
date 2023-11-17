package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.UserVisit;
public class UserVisitPersistence2 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.UserVisitPersistence2");
	static final String INSERT_VISIT_SQL = "INSERT INTO USER_VISITS (IP_ADDRESS, ALGO_ADDRESS, "
			+ "NETWORK_TYPE, WALLET_TYPE) VALUES (?,?,?,?)";
	static final String ALGO_ADDRESS = "SELECT * FROM USER_VISITS WHERE ALGO_ADDRESS = ?";
	public int createUpdateVisit(UserVisit visit, JdbcTemplate jdbcTemplate) {
		try {
			int update = jdbcTemplate.update(INSERT_VISIT_SQL, visit.getIpAddress(), visit.getAlgoAddress(),
					visit.getNetworkType(), visit.getWalletType());
			return update;
		} catch (Exception exception) {
			LOGGER.error("Error recording user visits ", exception);
			return 0;
		}
	}
	
	public List<UserVisit> getUserVisit(JdbcTemplate jdbcTemplate) {
		try {
			//List<UserProfile> userList = new ArrayList<UserProfile>();
			String query1 = "SELECT * FROM USER_VISITS";
			List<UserVisit> users = jdbcTemplate.query(query1, new UserVisitRowMapper());
			if(users == null || users.isEmpty())
				return new ArrayList<UserVisit>();		
		return users;
		}catch(Exception exception) {
			LOGGER.error("UserVisitPersistence.getNFTAsset() ", exception);
			return new ArrayList<UserVisit>();
		}
	}
	public List<UserVisit> getUserVisit1(JdbcTemplate jdbcTemplate, String algoAddress) {
		try {
			//List<UserProfile> userList = new ArrayList<UserProfile>();
			
			List<UserVisit> users = jdbcTemplate.query(ALGO_ADDRESS, new UserVisitRowMapper(),new Object[] { algoAddress });
			if(users == null || users.isEmpty())
				return new ArrayList<UserVisit>();		
		return users;
		}catch(Exception exception) {
			LOGGER.error("UserVisitPersistence.getNFTAsset() ", exception);
			return new ArrayList<UserVisit>();
		}
	}
}
