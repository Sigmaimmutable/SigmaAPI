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
import com.sigma.model.SigmaDocument2;
public class SigmaDocumentPersistence52 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.SigmaDocumentPersistence52");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO SIGMA_DOCUMENT_TWO "
			+ "(TENANT_ID,DOC_CHECKSUM,"
			+ "NFT_CREATION_STATUS,UUID,STATUS,CREATED_BY, DOC_MD5CHECKSUM, FILE_NAME, MAIL_ID) VALUES "
			+ " (?,?,?,?,?,?,?,?,?)";
	
	static final String RESOURCE = "SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE ID = ?";
	static final String RESOURCE_BY_TENANT = "SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE TENANT_ID = ?  ORDER BY CREATED_DATE DESC LIMIT 20 ";
	static final String RESOURCE_BY_TENANT_WITH_PAGINATION = 
			"SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE TENANT_ID = ? "
			+ "##SIGMA_DAYS_LIMIT##"
			+ " ORDER BY CREATED_DATE DESC LIMIT ? OFFSET ? ";
	
	static final String RESOURCE_ALLSELECT = "SELECT * FROM SIGMA_DOCUMENT_TWO LIMIT 100";
	
	static final String RESOURCE_UPDATE = "UPDATE SIGMA_DOCUMENT_TWO SET "
			+ "NFT_CREATION_STATUS= ?, STATUS=? WHERE UUID=? ";
	static final String SIGMA_DOC_SUMMARY_BY_MONTH_SQL = "SELECT "
			+ "NFT_CREATION_STATUS, COUNT(1) DOC_COUNT FROM SIGMA_DOCUMENT_TWO WHERE "
			+ "TENANT_ID = ? AND CREATED_DATE BETWEEN ? AND ? group by NFT_CREATION_STATUS";
	public int updateImmutableRecord(SigmaDocument2 resource, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String id= resource.getUuid();
			if(id == null || id.isEmpty())
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE, resource.getNftCreationStatus(),
					resource.getStatus(), resource.getUuid());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resource, exception);
			return update;
		}
		}	
		
	public int generateDocument(SigmaDocument2 rs, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, rs.getTenantId(),
					rs.getDocChecksum(), rs.getNftCreationStatus(), rs.getUuid(), rs.getStatus(),
					rs.getCreatedBy(), rs.getMd5Checksum(), rs.getFileName(), rs.getMailId());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error SigmaDocumentPersistence52.generateDocument() profile", rs, exception);
			return insert;
		}
	}

	public SigmaDocument2 getPaymentList(JdbcTemplate jdbcTemplate, String sigmaId) {
			try {
				SigmaDocument2 resources = jdbcTemplate.queryForObject(RESOURCE, 
						new SigmaDocumentRowMapper42(),
						new Object[] {sigmaId});
				if(resources == null)
					return new SigmaDocument2();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("SigmaDocumentPersistence52.getPaymentList() ", exception);
				return new SigmaDocument2();
			}
		}	
	
	public Integer getSigmaDoc2CountbyTid(JdbcTemplate jdbcTemplate, String tennantid) {
		String query = "SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE TENANT_ID = ?";
		try {
			List<SigmaDocument2> resources = jdbcTemplate.query(query, 
					new SigmaDocumentRowMapper42(),
					new Object[] {tennantid});
			if(resources == null)
				return 0;		
		return resources.size();
		}catch(Exception exception) {
			LOGGER.error("SigmaDocument Count error ", exception);
			return 0;
		}
	}	
	
	public Integer getSigmaDoc2CountbyMail(JdbcTemplate jdbcTemplate, String mail) {
		String query = "SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE MAIL_ID = ?";
		try {
			List<SigmaDocument2> resources = jdbcTemplate.query(query, 
					new SigmaDocumentRowMapper42(),
					new Object[] {mail});
			if(resources == null)
				return 0;		
		return resources.size();
		}catch(Exception exception) {
			LOGGER.error("SigmaDocument Count error ", exception);
			return 0;
		}
	}	
	
	public List<SigmaDocument2> getDocumentsByMail(JdbcTemplate jdbcTemplate, 
			String mail) {
		String query = "SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE MAIL_ID = ? ORDER BY CREATED_DATE";
		try {
			List<SigmaDocument2> resources = jdbcTemplate.query(query, 
					new SigmaDocumentRowMapper42(),
					new Object[] {mail});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument2>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getDocumentsByMail() ", exception);
			return new ArrayList<SigmaDocument2>();
		}
	}
	
	public List<SigmaDocument2> getPendingDocumentsBySQL(JdbcTemplate jdbcTemplate, 
			String workSql) {
		try {
			List<SigmaDocument2> resources = jdbcTemplate.query(workSql, 
					new SigmaDocumentRowMapper42(),
					new Object[] {});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument2>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument2>();
		}
	}
	public List<SigmaDocument2> getDocumentsByTenant(JdbcTemplate jdbcTemplate, 
			String tId) {
		try {
			List<SigmaDocument2> resources = jdbcTemplate.query(RESOURCE_BY_TENANT, 
					new SigmaDocumentRowMapper42(),
					new Object[] {tId});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument2>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument2>();
		}
	}
	public List<SigmaDocument2> getDocumentsByTenantWithPagination(JdbcTemplate jdbcTemplate, 
			PageRequestBean req) {
		List<SigmaDocument2> resources = null;
		try {
			if(req.getPriorDays()>0) {
				String query = RESOURCE_BY_TENANT_WITH_PAGINATION.replaceAll("##SIGMA_DAYS_LIMIT##", " AND CREATED_DATE > NOW() - INTERVAL ? DAY");
			resources = jdbcTemplate.query(query, new SigmaDocumentRowMapper42(), 
					new Object[] {req.getTenantId(), req.getPriorDays(), req.getLimit(), req.getStart()});
			}else {
				String query = RESOURCE_BY_TENANT_WITH_PAGINATION.replaceAll("##SIGMA_DAYS_LIMIT##", " ");
			resources = jdbcTemplate.query(query, new SigmaDocumentRowMapper42(),
					new Object[] {req.getTenantId(), req.getLimit(), req.getStart()});
			}
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument2>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument2>();
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
			LOGGER.error("SigmaDocumentPersistence52.getMonthlyDocumentSummary() ", exception);
		}
	}
	public List<SigmaDocument2> getDocumentsByTenant2(JdbcTemplate jdbcTemplate, 
			String tId, String limit) {
		String query = "SELECT * FROM SIGMA_DOCUMENT_TWO  WHERE TENANT_ID = ?  ORDER BY CREATED_DATE DESC LIMIT " + limit;
		try {
			List<SigmaDocument2> resources = jdbcTemplate.query(query, 
					new SigmaDocumentRowMapper42(),
					new Object[] {tId});
			if(resources == null || resources.isEmpty())
				return new ArrayList<SigmaDocument2>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
			return new ArrayList<SigmaDocument2>();
		}
	}


}
