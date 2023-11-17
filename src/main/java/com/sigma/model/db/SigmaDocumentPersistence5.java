package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sigma.model.DocumentO;
import com.sigma.model.PageRequestBean;
import com.sigma.model.PieBean;
import com.sigma.model.SigmaDocument;
public class SigmaDocumentPersistence5 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.SigmaDocumentPersistence5");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO SIGMA_DOCUMENT "
			+ "(FLEXFIELD_VAR1, FLEXFIELD_VAR2,FLEXFIELD_VAR3,"
			+ "FLEXFIELD_VAR4,FLEXFIELD_VAR5,FLEXFIELD_VAR6,"
			+ "FLEXFIELD_VAR7,FLEXFIELD_VAR8,FLEXFIELD_VAR9,"
			+ "FLEXFIELD_VAR10, TENANT_ID,DOC_CHECKSUM,"
			+ "NFT_CREATION_STATUS,UUID,STATUS,CREATED_BY, JOB_ID, DOC_MD5CHECKSUM) VALUES "
			+ " (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?)";
	
	static final String RESOURCE = "SELECT * FROM SIGMA_DOCUMENT  WHERE ID = ?";
	static final String RESOURCE_BY_TENANT = "SELECT * FROM SIGMA_DOCUMENT  WHERE TENANT_ID = ?  ORDER BY CREATED_DATE DESC LIMIT 20 ";
	static final String RESOURCE_BY_TENANT_WITH_PAGINATION = 
			"SELECT * FROM SIGMA_DOCUMENT  WHERE TENANT_ID = ? "
			+ "##SIGMA_DAYS_LIMIT##"
			+ " ORDER BY CREATED_DATE DESC LIMIT ? OFFSET ? ";
	
	static final String RESOURCE_ALLSELECT = "SELECT * FROM SIGMA_DOCUMENT LIMIT 100";
	
	static final String RESOURCE_UPDATE = "UPDATE SIGMA_DOCUMENT SET "
			+ "NFT_CREATION_STATUS= ?, UUID=?   WHERE ID = ? ";
	static final String SIGMA_DOC_SUMMARY_BY_MONTH_SQL = "SELECT "
			+ "NFT_CREATION_STATUS, COUNT(1) DOC_COUNT FROM SIGMA_DOCUMENT WHERE "
			+ "TENANT_ID = ? AND CREATED_DATE BETWEEN ? AND ? group by NFT_CREATION_STATUS";
	public int updateImmutableRecord(SigmaDocument resource, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String id= resource.getSigmaId();
			if(id == null || id.isEmpty())
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE, resource.getNftCreationStatus(),
					resource.getUuid(), resource.getSigmaId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resource, exception);
			return update;
		}
		}	
		
	public int generateDocument(SigmaDocument rs, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, rs.getfVar1(),
					rs.getfVar2(), rs.getfVar3(), rs.getfVar4(), rs.getfVar5(), rs.getfVar6(),
					rs.getfVar7(), rs.getfVar8(), rs.getfVar9(), rs.getfVar10(), rs.getTenantId(),
					rs.getDocChecksum(), rs.getNftCreationStatus(), rs.getUuid(), rs.getStatus(),
					rs.getCreatedBy(), rs.getJobId(), rs.getMd5Checksum());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error SigmaDocumentPersistence5.generateDocument() profile", rs, exception);
			return insert;
		}
	}

	public SigmaDocument getPaymentList(JdbcTemplate jdbcTemplate, String sigmaId) {
			try {
				SigmaDocument resources = jdbcTemplate.queryForObject(RESOURCE, 
						new SigmaDocumentRowMapper4(),
						new Object[] {sigmaId});
				if(resources == null)
					return new SigmaDocument();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("SigmaDocumentPersistence5.getPaymentList() ", exception);
				return new SigmaDocument();
			}
		}	
	public List<SigmaDocument> getPendingDocumentsBySQL(JdbcTemplate jdbcTemplate, 
			String workSql) {
		try {
			List<SigmaDocument> resources = jdbcTemplate.query(workSql, 
					new SigmaDocumentRowMapper4(),
					new Object[] {});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument>();
		}
	}
	public List<SigmaDocument> getDocumentsByTenant(JdbcTemplate jdbcTemplate, 
			String tId) {
		try {
			List<SigmaDocument> resources = jdbcTemplate.query(RESOURCE_BY_TENANT, 
					new SigmaDocumentRowMapper4(),
					new Object[] {tId});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument>();
		}
	}
	public List<SigmaDocument> getDocumentsByTenantWithPagination(JdbcTemplate jdbcTemplate, 
			PageRequestBean req) {
		List<SigmaDocument> resources = null;
		try {
			if(req.getPriorDays()>0) {
				String query = RESOURCE_BY_TENANT_WITH_PAGINATION.replaceAll("##SIGMA_DAYS_LIMIT##", " AND CREATED_DATE > NOW() - INTERVAL ? DAY");
			resources = jdbcTemplate.query(query, new SigmaDocumentRowMapper4(), 
					new Object[] {req.getTenantId(), req.getPriorDays(), req.getLimit(), req.getStart()});
			}else {
				String query = RESOURCE_BY_TENANT_WITH_PAGINATION.replaceAll("##SIGMA_DAYS_LIMIT##", " ");
			resources = jdbcTemplate.query(query, new SigmaDocumentRowMapper4(),
					new Object[] {req.getTenantId(), req.getLimit(), req.getStart()});
			}
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument>();
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

	public void getMonthlyDocumentSummary(JdbcTemplate jdbcTemplate, PieBean pBean, String tId) {
		try {
			jdbcTemplate.query(SIGMA_DOC_SUMMARY_BY_MONTH_SQL,					
					new ResultSetExtractor<PieBean>() {
						@Override
						public PieBean extractData(ResultSet rs) throws SQLException, DataAccessException {
							while(rs.next()) {
								int statusCode = rs.getInt("NFT_CREATION_STATUS");
								int docCount = rs.getInt("DOC_COUNT");
								if(statusCode==1)
									pBean.setiRecDocs(docCount);
								else
									pBean.setRawDocs(docCount);								
							}
							return null;
						}
				
			},
			new Object[] {tId, pBean.getStart(), pBean.getBw()});

		}catch(Exception exception) {
			LOGGER.error("SigmaDocumentPersistence5.getMonthlyDocumentSummary() ", exception);
		}
	}


}
