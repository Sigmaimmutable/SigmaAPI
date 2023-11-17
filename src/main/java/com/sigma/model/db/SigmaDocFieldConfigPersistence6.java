package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.DocumentO;
import com.sigma.model.SigmaAPIDocConfig;
public class SigmaDocFieldConfigPersistence6 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.SigmaDocumentPersistence5");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO SIGMA_DOC_FIELD_CONFIG "
			+ "(SIGMA_FIELD, EXT_FIELD,TENANT_ID, STATUS,CREATED_BY) VALUES "
			+ " (?,?,?,?,?)";
	
	static final String RESOURCE = "SELECT * FROM SIGMA_DOC_FIELD_CONFIG  WHERE TENANT_ID = ?";
	
	static final String RESOURCE_ALLSELECT = "SELECT * FROM SIGMA_DOC_FIELD_CONFIG LIMIT 100";
	
	static final String RESOURCE_UPDATE = "UPDATE SIGMA_DOCUMENT SET "
			+ "NFT_CREATION_STATUS= ?, UUID=?   WHERE ID = ? ";
	
	public int updateImmutableRecord(DocumentO resource, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			Long id= resource.getId();
			if(id == null || id == 0)
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE, resource.getNftCreationStatus(),
					resource.getUuid(), resource.getId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error SigmaDocFieldConfigPersistence6.updateResource() profile", resource, exception);
			return update;
		}
		}	
		
	public int generateDocument(SigmaAPIDocConfig resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, 
					resource.getSigmaField(), resource.getExtField(), resource.getTenantId(),
					resource.getStatus(), resource.getCreatedBy());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error SigmaDocFieldConfigPersistence6.generateDocument() profile", resource, exception);
			return insert;
		}
	}

	public List<SigmaAPIDocConfig> getSigmaDocFieldConfigList(JdbcTemplate jdbcTemplate, String tenantId) {
			try {
				List<SigmaAPIDocConfig> resources = jdbcTemplate.query(RESOURCE, 
						new SigmaDocFieldConfigRowMapper4(),
						new Object[] {tenantId});
				if(resources == null || resources.isEmpty())
					return new ArrayList<SigmaAPIDocConfig>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("Error SigmaDocFieldConfigPersistence6.getPaymentList() ", exception);
				return new ArrayList<SigmaAPIDocConfig>();
			}
		}
//	public List<DocumentO> getPendingDocumentsBySQL(JdbcTemplate jdbcTemplate, 
//			String workSql) {
//		try {
//			List<DocumentO> resources = jdbcTemplate.query(workSql, 
//					new DocumentORowMapper3(),
//					new Object[] {});
//			if(resources == null || resources.isEmpty())
//				return new ArrayList<DocumentO>();		
//		return resources;
//		}catch(Exception exception) {
//			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
//			return new ArrayList<DocumentO>();
//		}
//	}

	public List<SigmaAPIDocConfig> getEntireDocsWithLimit(JdbcTemplate jdbcTemplate) {
		try {
			List<SigmaAPIDocConfig> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
					new SigmaDocFieldConfigRowMapper4(),
					new Object[] {});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaAPIDocConfig>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("Error SigmaDocFieldConfigPersistence6.getEntireDocsWithLimit() ", exception);
			return new ArrayList<SigmaAPIDocConfig>();
		}
	}

}
