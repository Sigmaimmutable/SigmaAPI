package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.affinity.UniqueIdGenerator;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.Resource;
public class PrivateNetworkPersistence3 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.PrivateNetworkPersistence3");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO PRIVATE_NETWORK ("
			+ " ID, NETWORK_NAME, CHAIN_ID, IP_ADDRESS, STATUS, CREATED_BY_USER,"
			+ "SMART_CONTRACT_ADDRESS, SMART_CONTRACT_ACCESS_URL, "
			+ "SMART_CONTRACT_DEFAULT_WALLET_ADDRESS, TENANT_ID, "
			+ " POLY_IPFS_ACCESS_URL, POLY_API_CONSOLE_URL) VALUES (?,?,?,?,?,?,?,?,?,?,?, ?)";
	static final String RESOURCE = "SELECT * FROM PRIVATE_NETWORK WHERE ID = ?";
	static final String RESOURCE_BY_TENNAT = "SELECT * FROM PRIVATE_NETWORK WHERE TENANT_ID = ?";
	static final String RESOURCE_ALLSELECT = "SELECT * FROM PRIVATE_NETWORK";
	static final String PRIVATE_NETWORK_SQL = "SELECT * FROM PRIVATE_NETWORK "
			+ "WHERE ID = ?";
	static final String PRIVATE_NETWORK_BY_TENANT_SQL = "SELECT * FROM PRIVATE_NETWORK "
			+ "WHERE STATUS='Y' AND TENANT_ID = ?";
	
	static final String RESOURCE_UPDATE = "UPDATE PRIVATE_NETWORK SET CREATED_BY_USER = ?, "
			+ "NETWORK_NAME = ? , SMART_CONTRACT_ACCESS_URL = ?, SMART_CONTRACT_ADDRESS = ?,"
			+ " SMART_CONTRACT_DEFAULT_WALLET_ADDRESS = ?, STATUS = ?, POLY_IPFS_ACCESS_URL = ? ,"
			+ " CONSORTIA_ID = ?, ENV_ID = ?  WHERE ID = ? ";
	
	public int updateResource(PrivateNetwork2 resource, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			Long userId= resource.getId();
			if(userId == null || userId < 0)
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE, resource.getCreatedByUser(),
					resource.getNetworkName(), resource.getSmartContractAccessUrl(),
					resource.getSmartContractAddress(), resource.getSmartContractDefaultWalletAddress(),
					resource.getStatus(), resource.getIpfsUrl(), resource.getConsortiaId(), resource.getEnvId(), 
					resource.getId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error PrivateNetworkPersistence3.updateResource() profile", resource, exception);
			return update;
		}
		}
	
	public PrivateNetwork2 generateResource(PrivateNetwork2 resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getId(), 
					resource.getNetworkName(), resource.getChainId(), resource.getIpAddress(), 
					resource.getStatus(), resource.getCreatedByUser(), resource.getSmartContractAddress(),
					resource.getSmartContractAccessUrl(), resource.getSmartContractDefaultWalletAddress(),
					resource.getTenantId(), resource.getIpfsUrl(), resource.getNetworkAPIUrl());			
			if(insert>0)
				return resource;
			else
				return null;			
		} catch (Exception exception) {
			LOGGER.error("Error PrivateNetworkPersistence3.generateResource() profile", resource, exception);
			return null;
		}
	}
	public List<PrivateNetwork2> getResource(JdbcTemplate jdbcTemplate, String id)  throws Exception{
		try {
			List<PrivateNetwork2> resource = jdbcTemplate.query(RESOURCE, 
					new PrivateNetworkRowMapper3(),
					new Object[] { id });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("PrivateNetworkPersistence3.getResource() id {}", id, exception);
			throw exception;
		}
	}
	
	public List<PrivateNetwork2> getResourcebytennatId(JdbcTemplate jdbcTemplate, String id)  throws Exception{
		try {
			List<PrivateNetwork2> resource = jdbcTemplate.query(RESOURCE_BY_TENNAT, 
					new PrivateNetworkRowMapper3(),
					new Object[] { id });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("PrivateNetworkPersistence3.getResource() id {}", id, exception);
			throw exception;
		}
	}
	public PrivateNetwork2 getNetworkById(JdbcTemplate jdbcTemplate, Integer networkId)  throws Exception{
		try {
			PrivateNetwork2 resource = jdbcTemplate.queryForObject(PRIVATE_NETWORK_SQL, 
					new PrivateNetworkRowMapper3(),
					new Object[] { networkId });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("PrivateNetworkPersistence3.getResource() id {}", networkId, exception);
			throw exception;
		}
	}	
	public PrivateNetwork2 getNetworkByTenant(JdbcTemplate jdbcTemplate, String tenantId)  throws Exception{
		try {
			PrivateNetwork2 resource = jdbcTemplate.queryForObject(PRIVATE_NETWORK_BY_TENANT_SQL, 
					new PrivateNetworkRowMapper3(),
					new Object[] { tenantId });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("PrivateNetworkPersistence3.getResource() id {}", tenantId, exception);
			return null;
		}
	}	
	public List<PrivateNetwork2> getResourceList(JdbcTemplate jdbcTemplate) {
			try {
				List<PrivateNetwork2> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
						new PrivateNetworkRowMapper3());
				if(resources == null || resources.isEmpty())
					return new ArrayList<PrivateNetwork2>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("PrivateNetworkPersistence3.getResourceList() ", exception);
				return new ArrayList<PrivateNetwork2>();
			}

	}
}