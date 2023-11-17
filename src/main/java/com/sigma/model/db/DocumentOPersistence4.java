package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.DocumentO;
import com.sigma.model.Payment2;
public class DocumentOPersistence4 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.DocumentOPersistence4");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO DOCUMENT (FILENAME__V,ID,VERSION_ID,"
			+ "SOURCE_OWNER__V,PRIMARY_AUTHOR__C,SOURCE_DOCUMENT_NUMBER__V,"
			+ "APPLICATION__V,NAME__V,TYPE__V,TITLE__V,MANUFACTURER__V,"
			+ "SOURCE_VAULT_ID__V,CREATED_BY__V,APPLICATIONS__V,REGION__V,"
			+ " STATUS__V,DOCUMENT_DATE__C,BINDER_LAST_AUTOFILED_DATE__V,"
			+ "ARCHIVED_DATE__SYS,FILE_MODIFIED_DATE__V,FILE_CREATED_DATE__V,"
			+ "DOCUMENT_CREATION_DATE__V, UUID,  "
			+ "NFT_CREATION_STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	static final String RESOURCE = "SELECT * FROM DOCUMENT  WHERE ID = ?";
	
	static final String RESOURCE_ALLSELECT = "SELECT * FROM DOCUMENT LIMIT 100";
	
	static final String RESOURCE_UPDATE = "UPDATE DOCUMENT SET "
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
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resource, exception);
			return update;
		}
		}	
		
	public int generateDocument(DocumentO resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getFilename__v(),
					resource.getId(), resource.getVersion_id(), resource.getSource_owner__v(),
					resource.getPrimary_author__c(), resource.getSource_document_number__v(),
					resource.getApplication__v(), resource.getName__v(), resource.getType__v(),
					resource.getTitle__v(), resource.getManufacturer__v(), resource.getSource_vault_id__v(),
					resource.getCreated_by__v(), resource.getApplications__v(), resource.getRegion__v(),
					resource.getStatus__v(), resource.getDocument_date__c(), resource.getBinder_last_autofiled_date__v(),
					resource.getArchived_date__sys(), resource.getFile_modified_date__v(), resource.getFile_created_date__v(),
					resource.getDocument_creation_date__v(), resource.getUuid(),
					resource.getNftCreationStatus());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", resource, exception);
			return insert;
		}
	}

	public List<DocumentO> getPaymentList(JdbcTemplate jdbcTemplate, String userId) {
			try {
				List<DocumentO> resources = jdbcTemplate.query(RESOURCE, 
						new DocumentORowMapper3(),
						new Object[] {userId});
				if(resources == null || resources.isEmpty())
					return new ArrayList<DocumentO>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
				return new ArrayList<DocumentO>();
			}
		}
	public List<DocumentO> getPendingDocumentsBySQL(JdbcTemplate jdbcTemplate, 
			String workSql) {
		try {
			List<DocumentO> resources = jdbcTemplate.query(workSql, 
					new DocumentORowMapper3(),
					new Object[] {});
			if(resources == null || resources.isEmpty())
				return new ArrayList<DocumentO>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<DocumentO>();
		}
	}

	public List<DocumentO> getEntireDocsWithLimit(JdbcTemplate jdbcTemplate) {
		try {
			List<DocumentO> resources = jdbcTemplate.query(RESOURCE_ALLSELECT, 
					new DocumentORowMapper3(),
					new Object[] {});
			if(resources == null || resources.isEmpty())
				return new ArrayList<DocumentO>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<DocumentO>();
		}
	}

}
