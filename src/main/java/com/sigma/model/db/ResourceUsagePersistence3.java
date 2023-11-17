package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.ResourceUsage;
public class ResourceUsagePersistence3 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO RESOURCE_USAGE (ID,"
			+ "USER_ID, RESOURCE_ID, STATUS) VALUES (?,?,?,?)";
	static final String RESOURCE = "SELECT * FROM RESOURCE_USAGE WHERE ID = ?";
	static final String RESOURCE_ALLSELECT = "SELECT * FROM RESOURCE_USAGE";
	static final String RESOURCE_UPDATE1 = "UPDATE RESOURCE_USAGE SET END_TIME= ?, "
			+ "STATUS = ?  WHERE ID = ? ";
	static final String RESOURCE_UPDATE2 = "UPDATE RESOURCE_USAGE SET  STATUS = ? WHERE ID = ?";
	
	public int updateResourceUsage(ResourceUsage resourceUsage, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= resourceUsage.getResourceId();
			if(userId == null || userId.trim().isEmpty())
				return 0;
			String status = resourceUsage.getStatus();
			if(status == null || status.trim().isEmpty())
				status = "STOP";
			update = jdbcTemplate.update(RESOURCE_UPDATE1, resourceUsage.getEndTime(), status, resourceUsage.getId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resourceUsage, exception);
			return update;
		}
		}
	public int updateResourceUsage2Paid(ResourceUsage resourceUsage, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= resourceUsage.getResourceId();
			if(userId == null || userId.trim().isEmpty())
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE2, resourceUsage.getStatus(), resourceUsage.getResourceId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resourceUsage, exception);
			return update;
		}
		}	
	public int generateResourceUsage(ResourceUsage resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getId(), resource.getUserId(),
					resource.getResourceId(), resource.getStatus());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", resource, exception);
			return insert;
		}
	}
	public ResourceUsage getResource(JdbcTemplate jdbcTemplate, String id) {
		try {
			ResourceUsage resource = jdbcTemplate.queryForObject(RESOURCE, 
					new ResourceUsageRowMapper3(),
					new Object[] { id });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("ResourcePersistence2.getResource() id {}", id, exception);
			return null;
		}
	}	
	public List<ResourceUsage> getResourceList(JdbcTemplate jdbcTemplate) {
			try {
				List<ResourceUsage> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
						new ResourceUsageRowMapper3());
				if(resources == null || resources.isEmpty())
					return new ArrayList<ResourceUsage>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("ResourcePersistence2.getNFTAsset() ", exception);
				return new ArrayList<ResourceUsage>();
			}
		}
	
}
