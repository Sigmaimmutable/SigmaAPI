package com.sigma.model.db;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.HelpandSupport;
import com.sigma.model.JobNotifyEmail;
import com.sigma.model.Notification;
import com.sigma.model.Payment2;
import com.sigma.model.Session;
import com.sigma.model.UserProfile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
public class JobNotifyEmailPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.JobNotifyEmailPersistence");
	static final String INSERT_JOBEMAILNOTIFY_SQL = "INSERT INTO JOB_NOTIFY_EMAIL (TENANT_ID,"
			+ "MAIL_ID) VALUES (?,?)";
	static final String JOBEMAILNOTIFY_TENENT_ID = "SELECT * FROM JOB_NOTIFY_EMAIL WHERE TENANT_ID = ?";		
	
	static final String JOBEMAILNOTIFY_UPDATE_BY_ID = "UPDATE JOB_NOTIFY_EMAIL SET MAIL_ID = ? "
			+ "WHERE TENANT_ID = ? AND ID = ? ";
	

	static final String DELETE_JOBEMAILNOTIFY_BY_EMAIL_ID = "DELETE FROM JOB_NOTIFY_EMAIL WHERE ID = ? ";
	
	public int createSession(JobNotifyEmail jobnotify, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		
		try {
			
			insert = jdbcTemplate.update(INSERT_JOBEMAILNOTIFY_SQL, 
					 jobnotify.getTenantId(), jobnotify.getEmailId());	
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error JobNotifyEmailPersistence.createsession() notification", jobnotify, exception);
			return insert;
		}
	}
	
	
	public int updateJobNotifyEmail(JobNotifyEmail jobnotify, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String tenentId= jobnotify.getTenantId();
			if(tenentId == null || tenentId.trim().isEmpty())
				return 0;	
			update = jdbcTemplate.update(JOBEMAILNOTIFY_UPDATE_BY_ID, 
					jobnotify.getEmailId(), jobnotify.getTenantId(), jobnotify.getUserId()
					);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error JobNotifyEmailPersistence.updateJobNotifyEmail() jobnotify", jobnotify, exception);
			return update;
		}
	}
	
	

	public List<JobNotifyEmail> getMailJobNotifyEmail(JdbcTemplate jdbcTemplate,String tennentId) {
		try {
			List<JobNotifyEmail> jobnotify = jdbcTemplate.query(JOBEMAILNOTIFY_TENENT_ID, new JobNotifyEmailRowMapper(),
					new Object[] {tennentId });
			return jobnotify;
		} catch (Exception exception) {
			LOGGER.error("JobNotifyEmailPersistence.getMailJobNotifyEmail() tennentId {}", exception);
			return null;
		}
	}
	
	public boolean  deleteUserByNotifyEmailId(JdbcTemplate jdbcTemplate, String userid) {
		 try {
		        int rowsAffected = jdbcTemplate.update(DELETE_JOBEMAILNOTIFY_BY_EMAIL_ID, userid);
		        return rowsAffected > 0;
		    } catch (Exception exception) {
		        LOGGER.error("Error occurred while deleting favourite by userId: {}", userid, exception);
		        return false;
		    }
		}
	


	

}
