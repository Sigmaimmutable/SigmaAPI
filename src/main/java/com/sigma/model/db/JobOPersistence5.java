package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.DocumentO2Job;
import com.sigma.model.PageRequestBean;
import com.sigma.model.SigmaDocument;
public class JobOPersistence5 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.DocumentOPersistence4");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO VE_JOB (ID, JOB_NAME,"
			+ "STATUS,ERROR_SUMMARY, COMPANY_CODE,NO_RECORDS_PROCESSED,"
			+ "JOB_RUN_BY_USER, LATEST_DOCUMENT_DATE, TENANT_ID, JOB_TYPE) VALUES (?,?,?,?,?,?,?,?,?,?)";
	static final String RESOURCE = "SELECT * FROM VE_JOB WHERE TENANT_ID = ? ";
	static final String GET_LATEST_RUN_TIME_SQL = "SELECT LATEST_DOCUMENT_DATE "
			+ " FROM VE_JOB WHERE id = (SELECT MAX(id -1) FROM VE_JOB WHERE JOB_NAME='DOC_FETCH') AND TENANT_ID = ? "
			+ "AND JOB_NAME='DOC_FETCH' ";
	static final String RESOURCE_UPDATE = "UPDATE VE_JOB SET STATUS= 'Y', "
			+ "ERROR_SUMMARY=?, NO_RECORDS_PROCESSED =?, "
			+ "LATEST_DOCUMENT_DATE = ?  WHERE ID = ?";
	static final String JOB_ID_FETCH = "SELECT IFNULL(MAX(ID),0) + 1 LATEST_VALUE FROM VE_JOB";
	static final String RESOURCE_BY_TENANT_WITH_PAGINATION = 
			"SELECT * FROM VE_JOB  WHERE TENANT_ID = ?  ORDER BY RUN_START_TIME "
			+ "DESC LIMIT ? OFFSET ?";
	
	
	public String getLatestExecTime(JdbcTemplate jdbcTemplate, String tenantId) {
		String queryLastExecTime = null;
		try {
			 queryLastExecTime = jdbcTemplate.queryForObject(GET_LATEST_RUN_TIME_SQL,new Object[]{ tenantId },String.class);
			return queryLastExecTime;
		}catch(Exception exception) {
			LOGGER.error("Error PaymentPersistence3.getLatestExecTime()", exception);
			return queryLastExecTime;
		}
	}
	public Long getLatestId(JdbcTemplate jdbcTemplate) {
		Long queryLastExecTime = null;
		try {
			queryLastExecTime = jdbcTemplate.queryForObject(JOB_ID_FETCH, Long.class);
			return queryLastExecTime;
		}catch(Exception exception) {
			LOGGER.error("Error PaymentPersistence3.getLatestExecTime()", exception);
			return queryLastExecTime;
		}
	}		
	public Long generateJobStatus(DocumentO2Job resource, JdbcTemplate jdbcTemplate, String jobType) {
		int insert = 0;
		Long latestId = getLatestId(jdbcTemplate);
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, latestId,resource.getJobName(),
					resource.getStatus(), resource.getErrorSummary(), resource.getCompanyCode(),
					resource.getNoOfRecordsProcessed(), resource.getJobRunByUser(), 
					resource.getLatestDocumentDate(), resource.getTenantId(), jobType);
			if(insert>0)
				return latestId;
			else
				return Long.valueOf(insert+"");			
		} catch (Exception exception) {
			LOGGER.error("Error JobOPersistence5.generateJobStatus() profile", resource, exception);
			return Long.valueOf(insert+"");
		}
	}

	public List<DocumentO2Job> getPaymentList(JdbcTemplate jdbcTemplate, String userId) {
			try {
				List<DocumentO2Job> resources = jdbcTemplate.query(RESOURCE, 
						new JobRowMapper4(),
						new Object[] { userId });
				if(resources == null || resources.isEmpty())
					return new ArrayList<DocumentO2Job>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
				return new ArrayList<DocumentO2Job>();
			}
		}
	public int updateJobStatus(DocumentO2Job job, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			Long id= job.getId();
			if(id == null || id < 0)
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE,job.getErrorSummary(),
					job.getNoOfRecordsProcessed(), job.getLatestDocumentDate(), id);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", job, exception);
			return update;
		}
		
	}
	public List<DocumentO2Job> getDocumentsByTenantWithPagination(JdbcTemplate jdbcTemplate, 
			PageRequestBean req) {
		try {
			List<DocumentO2Job> resources = jdbcTemplate.query(RESOURCE_BY_TENANT_WITH_PAGINATION, 
					new JobRowMapper4(),
					new Object[] {req.getTenantId(), req.getLimit(), req.getStart()});
			if(resources == null || resources.isEmpty())
				return new ArrayList<DocumentO2Job>();		
		return resources;
		}catch(Exception exception) {
			LOGGER.error("JobOPersistence5.getDocumentsByTenantWithPagination() ", exception);
			return new ArrayList<DocumentO2Job>();
		}
	}
	
	public int getMakeIrecCount(JdbcTemplate jdbcTemplate, String jobName) {
	    try {
	        String sql = "SELECT COUNT(*) FROM VE_JOB WHERE JOB_NAME = ?";
	        int count = jdbcTemplate.queryForObject(sql, Integer.class, jobName);
	        return count;
	    } catch (Exception exception) {
	        LOGGER.error("Error in getting MAKE_IREC count from the database", exception);
	        return 0;
	    }
	}
	
	public String getLastUpdatedDate(JdbcTemplate jdbcTemplate) {
	    try {
	        String sql = "SELECT MAX(LATEST_DOCUMENT_DATE) AS last_updated_date FROM VE_JOB";
	        return jdbcTemplate.queryForObject(sql, String.class);
	    } catch (Exception exception) {
	        LOGGER.error("Error in getting the last updated date from the database", exception);
	        return null;
	    }
	}
	public String getRunStartTime(JdbcTemplate jdbcTemplate) {
	    try {
	        String sql = "SELECT RUN_START_TIME FROM VE_JOB WHERE ID =0";
	        return jdbcTemplate.queryForObject(sql, String.class);
	    } catch (Exception exception) {
	        LOGGER.error("Error in getting the last updated date from the database", exception);
	        return null;
	    }
	}


}
