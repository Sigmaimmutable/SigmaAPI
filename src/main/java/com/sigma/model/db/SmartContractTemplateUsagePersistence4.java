package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.SmartContractUsage;
public class SmartContractTemplateUsagePersistence4 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO SMART_CONTRACT_TEMPLATE_USAGE ("
			+ "USER_ID, SMART_CONTRACT_ADDRESS, NETWORK_TYPE, TOKEN_NAME, DEPOSIT_TOKEN_ADDRESS, "
			+ "BOND_TOKEN_ADDRESS, OWNER_ADDRESS, PURCHASE_TOKEN_ADDRESS, "
			+ "STATUS, START_DATE, END_DATE, APP_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	static final String RESOURCE = "SELECT * FROM SMART_CONTRACT_TEMPLATE_USAGE WHERE USER_ID = ?";
	static final String RESOURCE_ALLSELECT = "SELECT * FROM SMART_CONTRACT_TEMPLATE_USAGE";
	static final String RESOURCE_UPDATE1 = "UPDATE SMART_CONTRACT_TEMPLATE_USAGE SET USER_ID = ?, "
			+ " SMART_CONTRACT_ADDRESS = ?,  NETWORK_TYPE = ?, TOKEN_NAME = ?, DEPOSIT_TOKEN_ADDRESS = ?,   "
			+ " BOND_TOKEN_ADDRESS = ?, OWNER_ADDRESS = ?, PURCHASE_TOKEN_ADDRESS = ?, "
			+ " STATUS = ?,  START_DATE = ?,  END_DATE = ?,  APP_ID = ?   WHERE ID = ? ";
	
	public int updateResourceUsage(SmartContractUsage resourceUsage, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= resourceUsage.getUserId();
			if(userId == null || userId.trim().isEmpty())
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE1, resourceUsage.getUserId(), resourceUsage.getSmartContractAddress(),
					resourceUsage.getNetworkType(), resourceUsage.getTokenName(), resourceUsage.getDepositTokenAddress(), resourceUsage.getBondTokenAddress(),
					resourceUsage.getOwnerAddress(), resourceUsage.getPurchaseTokenAddress(), resourceUsage.getStartDate(),
					resourceUsage.getStartDate(), resourceUsage.getEndDate(), resourceUsage.getAppId(), resourceUsage.getId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resourceUsage, exception);
			return update;
		}
		}
	
	public int generateResourceUsage(SmartContractUsage smartContractUsage, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, smartContractUsage.getUserId(), smartContractUsage.getSmartContractAddress(),
					smartContractUsage.getNetworkType(), smartContractUsage.getTokenName(), smartContractUsage.getDepositTokenAddress(), smartContractUsage.getBondTokenAddress(),
					smartContractUsage.getOwnerAddress(), smartContractUsage.getPurchaseTokenAddress(), smartContractUsage.getStartDate(),
					smartContractUsage.getStartDate(), smartContractUsage.getEndDate(), smartContractUsage.getAppId());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", smartContractUsage, exception);
			return insert;
		}
	}
	public List<SmartContractUsage> getResourceByUser(JdbcTemplate jdbcTemplate, String id) {
		try {
			List<SmartContractUsage> resource = jdbcTemplate.query(RESOURCE, 
					new SmartContractUsageTemplateRowMapper3(),
					new Object[] { id });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("ResourcePersistence2.getResource() id {}", id, exception);
			return null;
		}
	}	
	public List<SmartContractUsage> getResourceList(JdbcTemplate jdbcTemplate) {
			try {
				List<SmartContractUsage> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
						new SmartContractUsageTemplateRowMapper3());
				if(resources == null || resources.isEmpty())
					return new ArrayList<SmartContractUsage>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("ResourcePersistence2.getNFTAsset() ", exception);
				return new ArrayList<SmartContractUsage>();
			}
		}
	
}
