package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.affinity.UniqueIdGenerator;
import com.sigma.model.Organization;
public class OrganizationPersistence6 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.DocumentOPersistence4");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO ORG (NAME, "
			+ " CREATED_BY,  TENANT_ID) VALUES (?,?,?)";
//	static final String UPDATE_RESOURCE_SQL = "UPDATE ORG SET EXT_URL = ?, EXT_USER = ?, EXT_PASSWORD = ? WHERE TENANT_ID = ? ";
	static final String RESOURCE = "SELECT * FROM ORG WHERE TENANT_ID = ? AND STATUS=1";
	static final String RESOURCES = "SELECT * FROM ORG WHERE STATUS=1";
	static final String ALL_RESOURCES = "SELECT * FROM ORG " ;
	public Organization generateJobStatus(Organization resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		String uuid = UniqueIdGenerator.uuid();
		resource.setTenantId(uuid);
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getName(),
					resource.getCreatedBy(), resource.getTenantId());			
			if(insert>0)
				resource.setTenantId(uuid);		
			return resource;
				
		} catch (Exception exception) {
			LOGGER.error("Error OrganizationPersistence6.generateJobStatus() profile", resource, exception);
			return resource;
		}
	}
	/*
	public Organization updateOrg(Organization resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		Organization defaultOrg = new Organization();
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
	*/
	public Organization getOrganizationInfo(JdbcTemplate jdbcTemplate, String tenantId) {
			try {
				Organization resources = jdbcTemplate.queryForObject(RESOURCE, 
						new OrganizationRowMapper5(),
						new Object[] { tenantId });
				if(resources == null)
					return new Organization();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("OrganizationPersistence6.getResourceList() ", exception);
				return new Organization();
			}
		}
	public List<Organization> getOrganizationList(JdbcTemplate jdbcTemplate) {
		try {
			List<Organization> resources = jdbcTemplate.query(RESOURCES, 
					new OrganizationRowMapper5(),
					new Object[] { });
			if(resources == null)
				return new ArrayList<Organization>();	
		return resources;
		}catch(Exception exception) {
			LOGGER.error("OrganizationPersistence6.getResourceList() ", exception);
			return new ArrayList<Organization>();
		}
	}
	
	public List<Organization> ListAllOrganization(JdbcTemplate jdbcTemplate) {
		try {
			List<Organization> resources = jdbcTemplate.query(ALL_RESOURCES, 
					new OrganizationRowMapper5());
			if(resources == null)
				return new ArrayList<Organization>();	
		return resources;
		}catch(Exception exception) {
			LOGGER.error("OrganizationPersistence6.getResourceList() ", exception);
			return new ArrayList<Organization>();
		}
	}
}
