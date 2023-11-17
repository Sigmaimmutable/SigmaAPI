package com.sigma.model.db;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.JobManagement;
import com.sigma.model.JobTrigger;
import com.sigma.model.Payment2;
import com.sigma.model.Session;
import com.sigma.model.UserProfile;
import java.sql.Timestamp;
public class JobTriggerPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.JobTriggerPersistence");
	static final String INSERT_JOBTRIGGER = "INSERT INTO JOB_TRIGGER (JOB_TYPE, TENNAT_ID, JOB_STATUS) VALUES (?,?,?)";
	static final String JOBTRIGGER_FETCH = "SELECT * FROM JOB_TRIGGER ";
	static final String JOBTRIGGER_TENNANT_ID = "SELECT * FROM JOB_TRIGGER WHERE TENNAT_ID = ? AND JOB_TYPE = ?";
	static final String JOBTRIGGER_UPDATE = "UPDATE JOB_TRIGGER SET JOB_STATUS = ? WHERE TENNAT_ID = ? AND JOB_TYPE = ?";
			
	
	public int createSession(JobTrigger jobtrigger, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_JOBTRIGGER, 
					 jobtrigger.getJobType(), jobtrigger.getTennatId(), jobtrigger.getJobstatus()
					);	
//			if(insert>0) {
//				int updateSession = updateSession(session, jdbcTemplate);
//				if(updateSession<=0)
//					LOGGER.info("SessionPersistence6.createsession() => UpdateSession() failed");
//			}
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error JobTriggerPersistence.createsession() session", jobtrigger, exception);
			return insert;
		}
	}
	

	public List<JobTrigger> getAllJobManage(JdbcTemplate jdbcTemplate) {
		try {
			List<JobTrigger> jobtrigger = jdbcTemplate.query(JOBTRIGGER_FETCH , new JobTriggerRowMapper(),
					new Object[] { });
			return jobtrigger;
		} catch (Exception exception) {
			LOGGER.error("JobTriggerPersistence.getAllJobManage()",exception);
			return null;
		}
	}

	
	public boolean getJobManageWithTennat(JdbcTemplate jdbcTemplate, String tennantId, String jobtype ) {
		boolean statusget =false;
		try {
			List<JobTrigger> jobtrigger = jdbcTemplate.query(JOBTRIGGER_TENNANT_ID, new JobTriggerRowMapper(),
					new Object[] { tennantId, jobtype });
			if(jobtrigger != null && !jobtrigger.isEmpty()) {
				for(JobTrigger jobtr:jobtrigger) {
					if(jobtr.getJobstatus()==1) {
						statusget = true;
					}
					else {
						statusget = false;
					}
					
				}
				
			}
			return statusget;
		
		} catch (Exception exception) {
			LOGGER.error("JobTriggerPersistence6.getJobManageWithTennat() tennantId {} jobtype{}", tennantId,jobtype, exception);
			return false;
		}
	}
	
	
//	public Int UpdateJobTrigger(JobTrigger jobtrigger,JdbcTemplate jdbcTemplate ) {
//		Integer statusget =0;
//		try {
//			List<JobTrigger> jobtrigger1 = jdbcTemplate.update(JOBTRIGGER_UPDATE,jobtrigger.getTennatId(),jobtrigger.getJobType(),jobtrigger.getStatus());
//			if(jobtrigger != null && !jobtrigger1.isEmpty()) {
//				for(JobTrigger jobtr:jobtrigger1) {
//					if(jobtr.getStatus()==1) {
//						statusget = 1;
//					}
//					else {
//						statusget = 0;
//					}
//					
//				}
//				
//			}
//			return statusget;
//		
//		} catch (Exception exception) {
//			LOGGER.error("JobTriggerPersistence6.getJobManageWithTennat() tennantId {} jobtype{}", tennantId,jobtype,Jobstatus, exception);
//			return 0;
//		}
//	}
	
	
	public int UpdateJobTrigger(String tennantid,String jobtype,int jobstatus,JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String tennatid = tennantid;
			if(tennatid == null || tennatid.trim().isEmpty())
				return 0;
			
			update = jdbcTemplate.update(JOBTRIGGER_UPDATE,jobstatus,tennatid,jobtype);
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error UserPersistence.updateUserProfile() profile", tennantid, exception);
			return update;
		}
		}
}
