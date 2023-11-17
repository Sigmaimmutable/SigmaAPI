package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import com.algo.files.FileSystemStorageService;
import com.algo.model.db.MappingBatchInsertUtil;
import com.algo.nft.model.NFTUtil;
import com.sigma.model.ProfilePage;
import com.sigma.model.UserProfile;
public class ProfilePagePersistence {
	private static final String PROFILE2 = "profile";
	private FileSystemStorageService storageService;
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.ProfilePagePersistence");
	static final String INSERT_USER_PROFILE_SQL = "INSERT INTO PROFILE_PAGE (FIRST_NAME,"
			+ "LAST_NAME, MOBILE_NUMBER, PROFILE_PIC, GENDER, STATE, COUNTRY, USER_LANGUAGE, TIME_ZONE, EMAIL_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
	static final String USER_PROFILE_SELECT = "SELECT * FROM PROFILE_PAGE WHERE EMAIL_ID = ?";
	
	static final String USER_PROFILE_UPDATE = "UPDATE PROFILE_PAGE SET FIRST_NAME= ?, "
			+ "LAST_NAME = ? , MOBILE_NUMBER = ?, PROFILE_PIC = ?, GENDER = ?, STATE = ?, COUNTRY = ?, USER_LANGUAGE = ?, TIME_ZONE = ?  WHERE EMAIL_ID = ? ";
	
	public int updateUser(ProfilePage profile, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			MappingBatchInsertUtil mappingBatchInsertUtil = new MappingBatchInsertUtil();
			String userId = profile.getEmailId();
			if(userId == null || userId.trim().isEmpty())
				return 0;	
			NFTUtil nftUtil =  new NFTUtil(storageService);
			
			String profileImagePath = nftUtil.saveImageAndGetURL(PROFILE2, profile.getEmailId(), profile.getProfilePic());	
			update = jdbcTemplate.update(USER_PROFILE_UPDATE, profile.getFirstName(), 
					profile.getLastName(), profile.getMobileNumber(), profileImagePath, profile.getGender(), profile.getState(),
					profile.getCountry(), profile.getLaunguage(), profile.getTimeZone(), profile.getEmailId());	
			
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error UserPersistence.createUserProfile() profile", profile, exception);
			return update;
		}
		}	
	public int createUserProfile(ProfilePage profile, JdbcTemplate jdbcTemplate) {
		
		int insert = 0;
		try {
			MappingBatchInsertUtil mappingBatchInsertUtil = new MappingBatchInsertUtil();
			String algoAddress = profile.getEmailId();
			if(algoAddress == null || algoAddress.trim().isEmpty())
				return 0;
			NFTUtil nftUtil =  new NFTUtil(storageService);
			String profileImagePath = nftUtil.saveImageAndGetURL(PROFILE2, algoAddress, profile.getProfilePic());			
						
			insert = jdbcTemplate.update(INSERT_USER_PROFILE_SQL, profile.getFirstName(),
						profile.getLastName(), profile.getMobileNumber(), profileImagePath, 
						profile.getGender(), profile.getState(), profile.getCountry(), 
						profile.getLaunguage(), profile.getTimeZone(), profile.getEmailId());
			
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error UserPersistence.createUserProfile() profile", profile, exception);
			return insert;
		}
	}

	public ProfilePage getUserProfileByEmail(JdbcTemplate jdbcTemplate, String emailId) {
		try {
			ProfilePage userProfile = jdbcTemplate.queryForObject(USER_PROFILE_SELECT, new ProfilePageRowMapper(),
					new Object[] { emailId });
			updateUserProfile(jdbcTemplate, emailId, userProfile);
			return userProfile;
		} catch (Exception exception) {
			LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", emailId, exception);
			return null;
		}
	}

	private void updateUserProfile(JdbcTemplate jdbcTemplate, String algoAddress, ProfilePage userProfile) {
		String readFileToString = getImageAsString(userProfile.getProfilePic());
//		userProfile.setBgvImageAsString(null);
		userProfile.setProfilePic(readFileToString);		
			

	}
	
	private String getImageAsString(String serverImagePath) {
		String readFileToString = "";
		try {
			Resource loadAsResource = storageService.loadAsResource(serverImagePath);
			readFileToString = storageService.asString(loadAsResource);						
		}catch(Exception exception) {
			LOGGER.error("Error reading the file with path serverImagePath", serverImagePath, exception);
		}
		return readFileToString;
	}
	

	
	
	public FileSystemStorageService getStorageService() {
		return storageService;
	}
	public void setStorageService(FileSystemStorageService storageService) {
		this.storageService = storageService;
	}
	private String nftImageRepositoryLocation = null;
	
	
	public String getNftImageRepositoryLocation() {
		return nftImageRepositoryLocation;
	}
	public void setNftImageRepositoryLocation(String nftImageRepositoryLocation) {
		this.nftImageRepositoryLocation = nftImageRepositoryLocation;
	}

}
