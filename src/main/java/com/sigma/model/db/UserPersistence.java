package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.UserProfile;
public class UserPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.UserPersistence");
	static final String INSERT_USER_PROFILE_SQL = "INSERT INTO USER_PROFILE (USER_ID,"
			+ "USER_NAME, PASSWORD, VALID_USER, ROLE, USER_CREDITS) VALUES (?,?,?,?,?,?)";
	static final String USER_PROFILE_SELECT = "SELECT * FROM USER_PROFILE WHERE USER_ID = ?";
	static final String USER_PROFILE_ALLSELECT = "SELECT * FROM USER_PROFILE";
	static final String USER_PROFILE_UPDATE = "UPDATE USER_PROFILE SET USER_NAME= ?, "
			+ "PASSWORD = ? , VALID_USER = ?, ROLE = ?, USER_CREDITS = ? WHERE USER_ID = ? ";
	static final String PASSWORD_CHECK = "SELECT * FROM USER_PROFILE WHERE USER_ID = ? AND PASSWORD = ? ";
	public int updateUser(UserProfile profile, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= profile.getUserId();
			if(userId == null || userId.trim().isEmpty())
				return 0;			
			update = jdbcTemplate.update(USER_PROFILE_UPDATE, profile.getUserName(), 
					profile.getPassword(), profile.getValiduser(), profile.getRole(), profile.getUserCredits(),
					profile.getUserId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error UserPersistence.createUserProfile() profile", profile, exception);
			return update;
		}
		}	
	public int createUserProfile(UserProfile profile, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_USER_PROFILE_SQL, profile.getUserId(),
					profile.getUserName(), profile.getPassword(), profile.getValiduser(), profile.getRole(),
					profile.getUserCredits());			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error UserPersistence.createUserProfile() profile", profile, exception);
			return insert;
		}
	}

	public UserProfile getUserProfile(JdbcTemplate jdbcTemplate, String algoAddress) {
		try {
			UserProfile userProfile = jdbcTemplate.queryForObject(USER_PROFILE_SELECT, new UserProfileRowMapper(),
					new Object[] { algoAddress });
			return userProfile;
		} catch (Exception exception) {
			LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", algoAddress, exception);
			return null;
		}
	}
	
	public UserProfile getUserProfileCheck(JdbcTemplate jdbcTemplate, UserProfile userProfile) {
		try {
			UserProfile userProfileFromDb = jdbcTemplate.queryForObject(PASSWORD_CHECK, new UserProfileRowMapper(),
					new Object[] { userProfile.getUserId(), userProfile.getPassword() });
			return userProfileFromDb;
		} catch (Exception exception) {
			LOGGER.error("UserPersistence.getUserProfile() userId {}", userProfile.getUserId(), exception);
			return null;
		}
	}
	
	public List<UserProfile> getUserProfile(JdbcTemplate jdbcTemplate) {
			try {
				//List<UserProfile> userList = new ArrayList<UserProfile>();
				String query = "SELECT * FROM USER_PROFILE";
				List<UserProfile> users = jdbcTemplate.query(query, new UserProfileRowMapper());
				if(users == null || users.isEmpty())
					return new ArrayList<UserProfile>();		
			return users;
			}catch(Exception exception) {
				LOGGER.error("UserPersistence.getNFTAsset() ", exception);
				return new ArrayList<UserProfile>();
			}
		}

}
