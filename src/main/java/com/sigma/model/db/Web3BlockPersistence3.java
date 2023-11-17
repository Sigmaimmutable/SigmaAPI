package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Resource;
import com.sigma.model.Web3Block2;
public class Web3BlockPersistence3 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.ResourcePersistence2");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO WEB3_BLOCK ( "
			+ "BLOCK_NUMBER, BLOCK_HASH, MINER, TIMESTAMP, "
			+ "GAS_LIMIT, GAS_USED, NONCE) VALUES (?,?,?,?,?,?,?)";
	static final String RESOURCE = "SELECT * FROM RESOURCE WHERE CREATED_BY = ?";
	static final String RESOURCE_BY_ID = "SELECT * FROM RESOURCE WHERE ID = ?";
	static final String RESOURCE_ALLSELECT = "SELECT * FROM WEB3_BLOCK LIMIT 10";
	
	public int generateResource(Web3Block2 resource, JdbcTemplate jdbcTemplate)  throws Exception{
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getBlockNumber(), 
					resource.getBlockHash(), resource.getMiner(),resource.getTimeStamp(),
					resource.getGasLimit(), resource.getGasUsed(), resource.getNonce());			
			return insert;	
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", resource, exception);
			return 0;
		//	throw exception;
		}
	}

	public List<Web3Block2> getResourceList(JdbcTemplate jdbcTemplate) {
			try {
				List<Web3Block2> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
						new Web3BlockRowMapper3());
				if(resources == null || resources.isEmpty())
					return new ArrayList<Web3Block2>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("ResourcePersistence2.getNFTAsset() ", exception);
				return new ArrayList<Web3Block2>();
			}
		}
}
