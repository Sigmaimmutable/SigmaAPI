package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Resource;
public class ResourcePersistence2 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.ResourcePersistence2");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO RESOURCE (ID,"
			+ "RESOURCE_NAME, STATUS, FEE, FEE_TYPE) VALUES (?,?,?,?,?)";
	static final String RESOURCE = "SELECT * FROM RESOURCE WHERE ID = ?";
	static final String RESOURCE_ALLSELECT = "SELECT * FROM RESOURCE";
	static final String RESOURCE_UPDATE = "UPDATE RESOURCE SET RESOURCE_NAME= ?, "
			+ "STATUS = ? , FEE = ?, FEE_TYPE = ? WHERE ID = ? ";
	
	public int updateResource(Resource resource, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= resource.getResourceId();
			if(userId == null || userId.trim().isEmpty())
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE, resource.getResourceName(), 
					resource.getStatus(), resource.getFee(),resource.getFeeType(), 
					resource.getResourceId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resource, exception);
			return update;
		}
		}	
	public int generateResource(Resource resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getResourceId(), resource.getResourceName(), 
					resource.getStatus(), resource.getFee(),resource.getFeeType());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", resource, exception);
			return insert;
		}
	}
	public Resource getResource(JdbcTemplate jdbcTemplate, String id) {
		try {
			Resource resource = jdbcTemplate.queryForObject(RESOURCE, 
					new ResourceRowMapper2(),
					new Object[] { id });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("ResourcePersistence2.getResource() id {}", id, exception);
			return null;
		}
	}
	
	public List<Resource> getResourceList(JdbcTemplate jdbcTemplate) {
			try {
				List<Resource> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
						new ResourceRowMapper2());
				if(resources == null || resources.isEmpty())
					return new ArrayList<Resource>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("ResourcePersistence2.getNFTAsset() ", exception);
				return new ArrayList<Resource>();
			}
		}

}
