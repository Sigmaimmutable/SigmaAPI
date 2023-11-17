package com.sigma.model.db;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Notification;
import com.sigma.model.Payment2;
import com.sigma.model.Session;
import com.sigma.model.UserProfile;
import java.sql.Timestamp;
public class NotificationPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.NotificationPersistence");
	static final String INSERT_NOTIFICATION_SQL = "INSERT INTO NOTIFICATION (TITLE,"
			+ "DESCRIPTIONS, MAIL_ID, EPOCHTIME, TENNAT_ID, STATUSES) VALUES (?,?,?,?,?,?)";
	static final String NOTIFICATION_MAIL_ID = "SELECT * FROM NOTIFICATION WHERE MAIL_ID = ?";
	static final String NOTIFICATION_MAIL_ID_BY_STATUS = "SELECT * FROM NOTIFICATION WHERE MAIL_ID = ? AND STATUSES = ? ";
	static final String NOTIFICATION_UPDATE = "UPDATE NOTIFICATION SET STATUSES = ? "
			+ "WHERE MAIL_ID = ?";
	static final String NOTIFICATION_UPDATE_BY_ID = "UPDATE NOTIFICATION SET STATUSES = ? "
			+ "WHERE MAIL_ID = ? AND ID = ? ";
	
	public int createSession(Notification notification, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_NOTIFICATION_SQL, 
					notification.getTitle(), notification.getDescriptions(), notification.getMailId(), notification.getEpochtime(), notification.getTennatId(),
					notification.getStatuses());	
//			if(insert>0) {
//				int updateSession = updateSession(session, jdbcTemplate);
//				if(updateSession<=0)
//					LOGGER.info("SessionPersistence6.createsession() => UpdateSession() failed");
//			}
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error NotificationPersistence.createsession() notification", notification, exception);
			return insert;
		}
	}
	public int updateNotification(Notification notification, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String mailId= notification.getMailId();
			if(mailId == null || mailId.trim().isEmpty())
				return 0;	
			update = jdbcTemplate.update(NOTIFICATION_UPDATE, 
					notification.getStatuses(), notification.getMailId()
					);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error NotificationPersistence.createsession() notification", notification, exception);
			return update;
		}
	}
	
	public int updateNotificationById(Notification notification, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String mailId= notification.getMailId();
			if(mailId == null || mailId.trim().isEmpty())
				return 0;	
			update = jdbcTemplate.update(NOTIFICATION_UPDATE_BY_ID, 
					notification.getStatuses(), notification.getMailId(), notification.getId()
					);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error NotificationPersistence.createsession() notification", notification, exception);
			return update;
		}
	}

	public List<Notification> getMailNotification(JdbcTemplate jdbcTemplate, String mailId) {
		try {
			List<Notification> notification = jdbcTemplate.query(NOTIFICATION_MAIL_ID, new NotificationRowMapper(),
					new Object[] { mailId });
			return notification;
		} catch (Exception exception) {
			LOGGER.error("NotificationPersistence.getMailsession() mailId {}", mailId, exception);
			return null;
		}
	}
	
	public List<Notification> getMailNotificationByStatus(JdbcTemplate jdbcTemplate, String mailId, String status) {
		try {
			List<Notification> notification = jdbcTemplate.query(NOTIFICATION_MAIL_ID_BY_STATUS, new NotificationRowMapper(),
					new Object[] { mailId, status });
			return notification;
		} catch (Exception exception) {
			LOGGER.error("NotificationPersistence.getMailsession() mailId {}", mailId, exception);
			return null;
		}
	}


	

}
