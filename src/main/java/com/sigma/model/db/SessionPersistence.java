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
public class SessionPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.SessionPersistence");
	static final String INSERT_SESSION_SQL = "INSERT INTO SESSION (LOGIN_TIME,"
			+ "LOGOUT_TIME, MAIL_ID, ROLE_TYPE, TENNAT_ID, ACTIVITY) VALUES (?,?,?,?,?,?)";
	static final String INSERT_SESSION_SQL_LOGINTIME = "INSERT INTO SESSION (LOGOUT_TIME,"
			+ " MAIL_ID, ROLE_TYPE, TENNAT_ID, ACTIVITY) VALUES (?,?,?,?,?)";
	static final String SESSION_MAIL_ID = "SELECT * FROM SESSION WHERE MAIL_ID = ? LIMIT 10 OFFSET ?";
	static final String SESSION_FETCH = "SELECT * FROM SESSION LIMIT 10 OFFSET ?";
	static final String SESSION_UPDATE = "UPDATE SESSION SET LOGOUT_TIME = ?, "
			+ " ACTIVITY = ? WHERE MAIL_ID = ? ";
	
	public int createSession(Session session, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_SESSION_SQL_LOGINTIME, 
					session.getLogoutTime(), session.getMailId(), session.getRoleType(), session.getTennatId(),
					session.getActivity());	
//			if(insert>0) {
//				int updateSession = updateSession(session, jdbcTemplate);
//				if(updateSession<=0)
//					LOGGER.info("SessionPersistence6.createsession() => UpdateSession() failed");
//			}
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error SessionPersistence6.createsession() session", session, exception);
			return insert;
		}
	}
	public int updateSession(Session session, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String mailId= session.getMailId();
			if(mailId == null || mailId.trim().isEmpty())
				return 0;	
			Instant currentTimestamp = Instant.now();
			Timestamp sqlTimestamp = Timestamp.from(currentTimestamp);
			update = jdbcTemplate.update(SESSION_UPDATE,  sqlTimestamp,
					 session.getActivity(), session.getMailId()
					);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error SessionPersistence6.createsession() session", session, exception);
			return update;
		}
		}	

	public List<Session> getAllSessionManage(JdbcTemplate jdbcTemplate,int limit) {
		try {
			List<Session> session = jdbcTemplate.query(SESSION_FETCH , new SessionRowMapper(),
					new Object[] {limit });
			return session;
		} catch (Exception exception) {
			LOGGER.error("SessionPersistence6.getAllSessionManage()",exception);
			return null;
		}
	}
	public List<Session> getMailsession(JdbcTemplate jdbcTemplate, String mailId,int limit) {
		try {
			List<Session> session = jdbcTemplate.query(SESSION_MAIL_ID, new SessionRowMapper(),
					new Object[] { mailId,limit});
			return session;
		} catch (Exception exception) {
			LOGGER.error("SessionPersistence6.getMailsession() mailId {}", mailId, exception);
			return null;
		}
	}

	

}
