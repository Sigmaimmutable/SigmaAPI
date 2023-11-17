package com.sigma.model.db;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.JobManagement;
import com.sigma.model.Payment2;
import com.sigma.model.Session;
import com.sigma.model.UserProfile;
import java.sql.Timestamp;
public class JobManagementPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.JobManagementPersistence");
	static final String INSERT_JOBMANAGEMENT_SQL_LOGINTIME = "INSERT INTO JOB_MANAGEMENT (MAIL_ID, ROLE_TYPE, TENNAT_ID, ACTIVITY) VALUES (?,?,?,?)";
	static final String JOBMANAGEMENT_FETCH = "SELECT * FROM JOB_MANAGEMENT ";
	static final String JOBMANAGEMENT_FETCHTIME = "SELECT * FROM JOB_MANAGEMENT ORDER BY id DESC LIMIT 1";
	static final String JOBMANAGEMENT_TENNANt_ID = "SELECT * FROM JOB_MANAGEMENT WHERE TENNAT_ID = ?";
	
			
	
	public int createSession(JobManagement jobmanage, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_JOBMANAGEMENT_SQL_LOGINTIME, 
					 jobmanage.getMailId(), jobmanage.getRoleType(), jobmanage.getTennatId(),
					jobmanage.getActivity());	
//			if(insert>0) {
//				int updateSession = updateSession(session, jdbcTemplate);
//				if(updateSession<=0)
//					LOGGER.info("SessionPersistence6.createsession() => UpdateSession() failed");
//			}
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error JobManagementPersistence.createsession() session", jobmanage, exception);
			return insert;
		}
	}
	

	public List<JobManagement> getAllJobManage(JdbcTemplate jdbcTemplate) {
		try {
			List<JobManagement> jobmanage = jdbcTemplate.query(JOBMANAGEMENT_FETCH , new JobManagementRowMapper(),
					new Object[] { });
			return jobmanage;
		} catch (Exception exception) {
			LOGGER.error("JobManagementPersistence.getAllJobManage()",exception);
			return null;
		}
	}
	public List<JobManagement> getAllJobManageTime(JdbcTemplate jdbcTemplate) {
		try {
			List<JobManagement> jobmanage = jdbcTemplate.query(JOBMANAGEMENT_FETCHTIME , new JobManagementRowMapper(),
					new Object[] { });
			return jobmanage;
		} catch (Exception exception) {
			LOGGER.error("JobManagementPersistence.getAllJobManageTime()",exception);
			return null;
		}
	}


	
	public List<JobManagement> getJobManageWithTennat(JdbcTemplate jdbcTemplate, String tennantId) {
		try {
			List<JobManagement> jobmanage = jdbcTemplate.query(JOBMANAGEMENT_TENNANt_ID, new JobManagementRowMapper(),
					new Object[] { tennantId });
			return jobmanage;
		} catch (Exception exception) {
			LOGGER.error("SessionPersistence6.getMailsession() tennantId {}", tennantId, exception);
			return null;
		}
	}
}
