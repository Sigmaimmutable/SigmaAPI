package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.affinity.UniqueIdGenerator;
import com.sigma.model.TenantDocSource2;
public class TenantDocSourcePersistence7 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.TenantDocSourcePersistence7");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO TENANT_DOC_SOURCE (ID, NAME, "
			+ " CREATED_BY,  TENANT_ID, EXT_URL, EXT_USER, EXT_PASSWORD) VALUES (?,?,?,?,?,?,?)";
	static final String UPDATE_RESOURCE_SQL = "UPDATE TENANT_DOC_SOURCE SET EXT_URL = ?, EXT_USER = ?, EXT_PASSWORD = ? WHERE TENANT_ID = ? ";
	static final String RESOURCE = "SELECT * FROM TENANT_DOC_SOURCE WHERE ID = ? AND STATUS=1";
	static final String RESOURCES = "SELECT * FROM TENANT_DOC_SOURCE WHERE STATUS = 1 AND TENANT_ID = ?";
	public TenantDocSource2 generateTenantSource(TenantDocSource2 resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM TENANT_DOC_SOURCE", Integer.class);
		if(maxId == null)
			maxId = 1;
		else
			maxId += 1;
		resource.setId(maxId+"");
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getId(), 
					resource.getName(), resource.getCreatedBy(), resource.getTenantId(), 
					resource.getExtUrl(), resource.getExtUserName(), resource.getExtPassword());
			if(insert > 0)
				return resource;
			else
				return new TenantDocSource2();
				
		} catch (Exception exception) {
			LOGGER.error("Error OrganizationPersistence6.generateJobStatus() profile", resource, exception);
			return resource;
		}
	}
	public TenantDocSource2 updateOrg(TenantDocSource2 resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		TenantDocSource2 defaultOrg = new TenantDocSource2();
		try {			
			insert = jdbcTemplate.update(UPDATE_RESOURCE_SQL, resource.getExtUrl(),
					resource.getExtUserName(), resource.getExtPassword(), resource.getTenantId());			
			if(insert > 0)
				return resource;
			else
				return defaultOrg;				
		} catch (Exception exception) {
			LOGGER.error("Error OrganizationPersistence6.updateOrg() resource", resource, exception);
			return defaultOrg;
		}
	}
	public TenantDocSource2 getOrganizationInfo(JdbcTemplate jdbcTemplate, String id) {
			try {
				TenantDocSource2 resources = jdbcTemplate.queryForObject(RESOURCE, 
						new TenantDocSourceRowMapper6(),
						new Object[] { id });
				if(resources == null)
					return new TenantDocSource2();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("OrganizationPersistence6.getResourceList() ", exception);
				return new TenantDocSource2();
			}
		}
	public List<TenantDocSource2> getOrganizationList(JdbcTemplate jdbcTemplate,
			String tenantId) {
		try {
			List<TenantDocSource2> resources = jdbcTemplate.query(RESOURCES, 
					new TenantDocSourceRowMapper6(),
					new Object[] {tenantId});
			if(resources == null)
				return new ArrayList<TenantDocSource2>();	
		return resources;
		}catch(Exception exception) {
			LOGGER.error("OrganizationPersistence6.getResourceList() ", exception);
			return new ArrayList<TenantDocSource2>();
		}
	}
}
