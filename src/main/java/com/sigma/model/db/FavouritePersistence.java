package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Favourite;
import com.sigma.model.Payment2;
import com.sigma.model.Session;
import com.sigma.model.UserProfile;
public class FavouritePersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.FavouritePersistence");
	static final String INSERT_SESSION_SQL = "INSERT INTO FAVOURITE (EMAIL_ID,"
			+ "DOC_ID, FILE_NAME, DOC_NAME, DOC_STATUS) VALUES (?,?,?,?,?)";
	static final String SESSION_MAIL_ID = "SELECT * FROM FAVOURITE WHERE EMAIL_ID = ? LIMIT 10 OFFSET ? ";
	static final String SESSION_UPDATE = "UPDATE FAVOURITE SET LOGIN_TIME= ?, "
			+ "LOGOUT_TIME = ?, ACTIVITY = ? WHERE MAIL_ID = ? ";
	
	static final String DELETE_FAVOURITE_BY_EMAIL_ID = "DELETE FROM FAVOURITE WHERE EMAIL_ID = ? AND DOC_ID = ?";
	
	

	
	public int createFavourite(Favourite favourite, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_SESSION_SQL, favourite.getEmailId(),
					favourite.getDocId(), favourite.getFileName(), favourite.getDocName(), favourite.getDocStatus());	
//			if(insert>0) {
//				int updateSession = updateSession(session, jdbcTemplate);
//				if(updateSession<=0)
//					LOGGER.info("SessionPersistence6.createsession() => UpdateSession() failed");
//			}
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error SessionPersistence6.createsession() session", favourite, exception);
			return insert;
		}
	}
	public boolean  deleteFavouriteByEmailId(JdbcTemplate jdbcTemplate, String mailId, String docId) {
		 try {
		        int rowsAffected = jdbcTemplate.update(DELETE_FAVOURITE_BY_EMAIL_ID, mailId, docId);
		        return rowsAffected > 0;
		    } catch (Exception exception) {
		        LOGGER.error("Error occurred while deleting favourite by emailId: {}", mailId, exception);
		        return false;
		    }
		}	

	public List<Favourite> getFavouriteByEmailId(JdbcTemplate jdbcTemplate, String mailId, int limit) {
		try {
			List<Favourite> favourite = jdbcTemplate.query(SESSION_MAIL_ID, new FvouriteRowMapper(),
					new Object[] { mailId, limit });
			return favourite;
		} catch (Exception exception) {
			LOGGER.error("SessionPersistence6.getMailsession() mailId {}", mailId, exception);
			return null;
		}
	}

	

}
