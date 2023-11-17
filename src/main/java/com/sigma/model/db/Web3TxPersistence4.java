package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Web3Block2;
import com.sigma.model.Web3Tx3;
public class Web3TxPersistence4 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.ResourcePersistence2");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO WEB3_TX ( "
			+ "TX_HASH, NONCE, GAS_PRICE, TO_ADDRESS, "
			+ "VALUE, FROM_ADDRESS, BLOCK_HASH, BLOCK_NUMBER) VALUES (?,?,?,?,?,?,?,?)";
	static final String RESOURCE = "SELECT * FROM RESOURCE WHERE CREATED_BY = ?";
	static final String RESOURCE_BY_ID = "SELECT * FROM WEB3_TX LIMIT 10";
	static final String RESOURCE_ALLSELECT = "SELECT * FROM RESOURCE";
	//v
	public int generateResource(Web3Tx3 resource, JdbcTemplate jdbcTemplate)  throws Exception{
		int insert = 0; // v
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getTxHash(), 
					resource.getNonce(), resource.getGasPrice(),resource.getToAddress(),
					resource.getValue(), resource.getFromAddress(), resource.getBlockHash(),
					resource.getBlockNumber());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", resource, exception);
			throw exception;
		}
	}
	public List<Web3Tx3> getResourceList(JdbcTemplate jdbcTemplate) {
		try {
			List<Web3Tx3> resources = jdbcTemplate.query(RESOURCE_BY_ID, 
					new Web3TxRowMapper4());
			if(resources == null || resources.isEmpty())
				return new ArrayList<Web3Tx3>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("ResourcePersistence2.getNFTAsset() ", exception);
			return new ArrayList<Web3Tx3>();
		}
	}
}
