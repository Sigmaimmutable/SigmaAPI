package com.sigma.model.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.JobNotifyEmail;
import com.sigma.model.UserInfo;
import com.sigma.model.UserProfile;
public class UserInfoPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.UserInfoPersistence");
	static final String INSERT_USER_PROFILE_SQL = "INSERT INTO USER_INFO (EMAIL_ID, USER_NAME, PASSWORD, ROLE_TYPE, METHOD, TENNANT_ID, OTP) VALUES (?,?,?,?,?,?,?)";
	static final String USER_PROFILE_SELECT = "SELECT * FROM USER_INFO WHERE EMAIL_ID = ?";
	static final String USER_PROFILE_SELECT_BY_TENNATID = "SELECT * FROM USER_INFO WHERE TENNANT_ID = ? LIMIT 10 OFFSET ? ";
	
	static final String USER_PROFILE_SELECT_BY_TENNATID_ALL = "SELECT * FROM USER_INFO WHERE TENNANT_ID = ?" ;
	
	static final String USER_PROFILE_ALLSELECT = "SELECT * FROM USER_INFO";
	static final String USER_PROFILE_UPDATE = "UPDATE USER_INFO SET PASSWORD= ?, "
			+ "METHOD = ?  WHERE EMAIL_ID = ? ";
	static final String PASSWORD_CHECK = "SELECT * FROM USER_INFO WHERE EMAIL_ID = ? AND BINARY PASSWORD = ? ";
	
	static final String DELETE_USERINFO_BY_EMAIL_ID = "DELETE FROM USER_INFO WHERE EMAIL_ID = ? ";
	
	static final String USER_PROFILE_OTP = "UPDATE USER_INFO SET OTP = ? "
			+ " WHERE EMAIL_ID = ? ";
	
	static final String USER_PROFILE_PASSSWORD = "UPDATE USER_INFO SET PASSWORD = ? "
			+ " WHERE EMAIL_ID = ? ";
	static final String USER_PROFILE_ROLE = "UPDATE USER_INFO SET ROLE_TYPE = ? "
			+ " WHERE EMAIL_ID = ? ";
	static final String USER_PROFILE_BY_TENNATID = "SELECT * FROM JOB_NOTIFY_EMAIL WHERE TENANT_ID = ? ";
	static final String UPDATE_OTP_EXPIRED = "UPDATE USER_INFO SET EXPIRED = ? WHERE EMAIL_ID = ? ";

	public int updateUser(UserInfo userInfo, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= userInfo.getEmailId();
			if(userId == null || userId.trim().isEmpty())
				return 0;			
			update = jdbcTemplate.update(USER_PROFILE_UPDATE, userInfo.getPassword(), 
					userInfo.getMethod(), userInfo.getEmailId());				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error UserInfoPersistence.createUserProfile() Info", userInfo, exception);
			return update;
		}
		}	
	public int createUserInfo(UserInfo userInfo, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		int insertedValue = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_USER_PROFILE_SQL, userInfo.getEmailId(),
					userInfo.getUserName(), userInfo.getPassword(), userInfo.getRoleType(), userInfo.getMethod(),
					userInfo.getTennantId(), userInfo.getOtp());
			if(insert == 1) {
				URL url = new URL("https://api.sendgrid.com/v3/mail/send");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Authorization", "Bearer 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");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setDoOutput(true);

	            String requestBody = "{\"personalizations\": [{\"to\": [{\"email\": \"" + userInfo.getEmailId() + "\"}]}], \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"Sigma Welcomes You!\", \"content\": [{\"type\": \"text/plain\", \"value\": \"You are Added as "+ userInfo.getRoleType() + " \\nClick here " + "http://sigmapoc-appui.vercel.app/sign-up" + " to Access the App.\"}]}";
	            connection.getOutputStream().write(requestBody.getBytes());

	            int responseCode = connection.getResponseCode();

	            if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
	                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String line;
	                StringBuilder response = new StringBuilder();

	                while ((line = reader.readLine()) != null) {
	                    response.append(line);
	                }

	                reader.close();
	               

	                System.out.println("Response: " + response.toString());
	                insertedValue = 1;
	            } else {
	                System.out.println("Request failed with response code: " + responseCode);
	                insertedValue = 0; // or any other appropriate value indicating failure
	            }
			}
			return insertedValue;			
		} catch (Exception exception) {
			LOGGER.error("Error UserPersistence.createUserProfile() profile", userInfo, exception);
			return insert;
		}
	}

	public List<UserInfo> getUserInfoByEmail(JdbcTemplate jdbcTemplate, String emailId) {
	    try {
	    	   List<UserInfo>  userInfo = jdbcTemplate.query(USER_PROFILE_SELECT,
	                new Object[] { emailId },new UserInfoRowMapper());

	        List<UserInfo> filteredSignupList = new ArrayList<>(); // Corrected declaration

	        // Filter the required fields and add to the new list
	        for (UserInfo signup : userInfo) { // Note: You can't iterate directly over a non-iterable object
	            UserInfo filteredSignup = new UserInfo();
	            filteredSignup.setUserId(signup.getUserId());
	            filteredSignup.setUserName(signup.getUserName());
	            filteredSignup.setEmailId(signup.getEmailId());
	            filteredSignup.setRoleType(signup.getRoleType());
	            filteredSignup.setTennantId(signup.getTennantId()); // Corrected typo: "TennantId" to "TenantId"
	            filteredSignup.setMethod(signup.getMethod());
	            filteredSignupList.add(filteredSignup);
	        }

	        return filteredSignupList;
	    } catch (Exception exception) {
	        LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", emailId, exception);
	        return null;
	    }
	}
	public String  getUserInfoWithEmailCheck(JdbcTemplate jdbcTemplate, String emailId) {
	    try {
	    	UserInfo userInfo = jdbcTemplate.queryForObject(USER_PROFILE_SELECT, new UserInfoRowMapper(),
					new Object[] { emailId });

	        if(userInfo == null) {
	        	 return "N";	
	        }
	        else {
	        	
	       
		    	String pwd= userInfo.getPassword();
				if(pwd == null || pwd.trim().isEmpty())
				{
				    return "N";	
				}
				
				else {
					  return "Y";	
				}
	        }
	        // Filter the required fields and add to the new list
	      

	    
	    } catch (Exception exception) {
	        LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", emailId, exception);
	        return null;
	    }
	}

//	public UserInfo getUserInfoByEmal(JdbcTemplate jdbcTemplate, String emailId) {
//		try {
//			UserInfo userInfo = jdbcTemplate.queryForObject(USER_PROFILE_SELECT, new UserInfoRowMapper(),
//					new Object[] { emailId });
//			// Create a new list to hold filtered Signup objects
//	       UserInfo filteredSignupList = new ArrayList<>();
//
//	        // Filter the required fields and add to the new list
//	        for (UserInfo signup : userInfo) {
//	        	UserInfo filteredSignup = new UserInfo();
//	            filteredSignup.setUserName(signup.getUserName());
//	            filteredSignup.setEmailId(signup.getEmailId());
//	            filteredSignup.setRoleType(signup.getRoleType());
//	            filteredSignup.setTennantId(signup.getTennantId());
//	            filteredSignup.setMethod(signup.getMethod());
//	            filteredSignupList.add(filteredSignup);
//	        }
//
//	        return filteredSignupList;
////			return userInfo;
//		} catch (Exception exception) {
//			LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", emailId, exception);
//			return null;
//		}
//	}
	
	public List<UserInfo> getUserInfoBytennatId(JdbcTemplate jdbcTemplate, String tennantId, int limit) {
		try {
			
			List<UserInfo> userInfo = jdbcTemplate.query(USER_PROFILE_SELECT_BY_TENNATID, new UserInfoRowMapper(),
					new Object[] { tennantId, limit });
			return userInfo;
		} catch (Exception exception) {
			LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", tennantId, exception);
			return null;
		}
	}
	
	
	
	
	public List<UserInfo> getUserInfoBytennatIdAll(JdbcTemplate jdbcTemplate, String tennantId) {
		try {
			
			List<UserInfo> userInfo = jdbcTemplate.query(USER_PROFILE_SELECT_BY_TENNATID_ALL, new UserInfoRowMapper(),
					new Object[] { tennantId });
			return userInfo;
		} catch (Exception exception) {
			LOGGER.error("UserPersistence.getUserProfile() algoAddress {}", tennantId, exception);
			return null;
		}
	}
	
	public UserInfo getUserProfileCheck(JdbcTemplate jdbcTemplate, UserInfo userInfo) {
		try {
			UserInfo userProfileFromDb = jdbcTemplate.queryForObject(PASSWORD_CHECK, new UserInfoRowMapper(),
					new Object[] { userInfo.getEmailId(), userInfo.getPassword() });
			return userProfileFromDb;
		} catch (Exception exception) {
			LOGGER.error("UserPersistence.getUserProfile() userId {}", userInfo.getEmailId(), exception);
			return null;
		}
	}
	
	public List<UserInfo> getUserInfo(JdbcTemplate jdbcTemplate) {
			try {
				//List<UserProfile> userList = new ArrayList<UserProfile>();
				String query = "SELECT * FROM USER_INFO";
				List<UserInfo> users = jdbcTemplate.query(query, new UserInfoRowMapper());
				if(users == null || users.isEmpty())
					return new ArrayList<UserInfo>();		
			return users;
			}catch(Exception exception) {
				LOGGER.error("UserPersistence.getNFTAsset() ", exception);
				return new ArrayList<UserInfo>();
			}
		}
	
	public boolean  deleteUserByEmailId(JdbcTemplate jdbcTemplate, String mailId) {
		 try {
		        int rowsAffected = jdbcTemplate.update(DELETE_USERINFO_BY_EMAIL_ID, mailId);
		        return rowsAffected > 0;
		    } catch (Exception exception) {
		        LOGGER.error("Error occurred while deleting favourite by emailId: {}", mailId, exception);
		        return false;
		    }
		}
	
	public static int updateuserOtp(String mailId,String otp, JdbcTemplate jdbcTemplate) {
		int update=0;
			try {
				String userId= mailId;
				if(userId == null || userId.trim().isEmpty())
					return 0;			
				update = jdbcTemplate.update(USER_PROFILE_OTP, otp, 
						 mailId);	
				if(update == 1) {
					int update2 = jdbcTemplate.update(UPDATE_OTP_EXPIRED,"False",mailId);	
				}
					return update;
			}catch(Exception exception) {
				LOGGER.error("Error Update Failed", mailId, exception);
				return update;
			}
		}

	public boolean resetUserpassword(JdbcTemplate jdbcTemplate, String mailId) {
	    try {
	        String otp = generateOTP(6);
	        System.out.println("Generated OTP: " + otp);
	        try {
	            URL url = new URL("https://api.sendgrid.com/v3/mail/send");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Authorization", "Bearer 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");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setDoOutput(true);

	            String requestBody = "{\"personalizations\": [{\"to\": [{\"email\": \"" + mailId + "\"}]}], \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"OTP for Login\", \"content\": [{\"type\": \"text/plain\", \"value\": \"Your OTP is:"+ otp + " \\nClick " + "https://sigmatestversion.vercel.app/reset-submission" + " to reset the Password.\"}]}";
	            connection.getOutputStream().write(requestBody.getBytes());

	            int responseCode = connection.getResponseCode();

	            if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
	                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String line;
	                StringBuilder response = new StringBuilder();

	                while ((line = reader.readLine()) != null) {
	                    response.append(line);
	                }

	                reader.close();
	                int update = updateuserOtp( mailId, otp, jdbcTemplate);

	                System.out.println("Response: " + response.toString());
	                return true;
	            } else {
	                System.out.println("Request failed with response code: " + responseCode);
	                return false; // or any other appropriate value indicating failure
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            return false; // or any other appropriate value indicating failure
	        }

	        // int rowsAffected = jdbcTemplate.update(DELETE_USERINFO_BY_EMAIL_ID, mailId);
	    } catch (Exception exception) {
	        LOGGER.error("Error occurred while deleting favourite by emailId: {}", mailId, exception);
	        return false; // or any other appropriate value indicating failure
	    }
	}

	public static String generateOTP(int length) {
        // Define the characters that can be used in the OTP
        String chars = "0123456789";
        StringBuilder otp = new StringBuilder();

        // Create an instance of Random class
        Random random = new Random();

        // Generate the OTP
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            otp.append(chars.charAt(index));
        }

        return otp.toString();
    }
	
	
	public static int updatePassword(UserInfo userInfo, String mailid, JdbcTemplate jdbcTemplate) {
		int update=0;
			try {
				String query = "SELECT * FROM USER_INFO WHERE EMAIL_ID = ? ";
				UserInfo users = jdbcTemplate.queryForObject(query, new UserInfoRowMapper(), new Object[] { userInfo.getEmailId() });
				System.out.println("Generated OTP: " + users.getEmailId());
				String usersOtp = users.getOtp();
				String enteredOtp = userInfo.getOtp();
				if(usersOtp.equals(enteredOtp)) {
					update = jdbcTemplate.update(USER_PROFILE_PASSSWORD, userInfo.getPassword(), 
							mailid);
					if(update == 1) {
						int update2 = jdbcTemplate.update(UPDATE_OTP_EXPIRED,"True",mailid);
//						return update;
					}
					return update;
				}
				else {	
					return update;	
				}
				
					
			}catch(Exception exception) {
				LOGGER.error("Error Update Failed",  exception);
				return update;
			}
		}
	public static int updateRole(UserInfo userInfo, String mailid, JdbcTemplate jdbcTemplate) {
		int update=0;
			try {
				
				update = jdbcTemplate.update(USER_PROFILE_ROLE, userInfo.getRoleType(), 
						mailid);	
				return update;
			
				
					
			}catch(Exception exception) {
				LOGGER.error("Error Update Failed",  exception);
				return update;
			}
		}
	public List<String> getEmailIdsByTennantId(JdbcTemplate jdbcTemplate, String tennantId, int noOfDocuments, String latestDocumentDate) {
		List<String> sentEmailIds = new ArrayList<>();
		try {
	        List<JobNotifyEmail> jobNotifyEmail = jdbcTemplate.query(USER_PROFILE_BY_TENNATID, new JobNotifyEmailRowMapper(), new Object[]{tennantId});

	        List<String> emailIds = new ArrayList<>();
	        for (JobNotifyEmail jobNotifyEmailVar : jobNotifyEmail) {
	            emailIds.add(jobNotifyEmailVar.getEmailId());
	        }
	        
	        if (emailIds != null && !emailIds.isEmpty()) {
	        	try {
	        		StringBuilder emailIdsJson = new StringBuilder();
	                emailIdsJson.append("[");
	                for (String emailId : emailIds) {
	                    if (emailIdsJson.length() > 1) {
	                        emailIdsJson.append(",");
	                    }
	                    emailIdsJson.append("\"").append(emailId).append("\"");
	                }
	                emailIdsJson.append("]");
		            URL url = new URL("https://api.sendgrid.com/v3/mail/send");
		            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		            connection.setRequestMethod("POST");
		            connection.setRequestProperty("Authorization", "Bearer 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");
		            connection.setRequestProperty("Content-Type", "application/json");
		            connection.setDoOutput(true);
//[{\"to\": [{\"email\": \"" + "martinagracy28@gmail.com" + "\"}]}],
//		            String requestBody = "{\"personalizations\": [{\"to\": [{\"email\": \"" + "martinagracy28@gmail.com" + "\"}]}], \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"OTP for Login\", \"content\": [{\"type\": \"text/plain\", \"value\": \"Your OTP is:"+ "otp" + " \\nClick " + "http://sigmapoc-appui.vercel.app/reset-submission" + " to reset the Password.\"}]}";
		            StringBuilder personalizationsJson = new StringBuilder();
	                personalizationsJson.append("[");
	                for (String email : emailIds) {
	                    if (personalizationsJson.length() > 1) {
	                        personalizationsJson.append(",");
	                    }
	                    personalizationsJson.append("{\"to\": [{\"email\": \"").append(email).append("\"}]}");
	                    sentEmailIds.add(email); // Track the sent email IDs
	                }
	                personalizationsJson.append("]");

	                String requestBody = "{\"personalizations\": " + personalizationsJson.toString() + ", \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"Document Job Information\", \"content\": [{\"type\": \"text/plain\", \"value\": \"Your document fetch job is completed successfully, Total number of documents fetched today: " + noOfDocuments + "\\nJob completed by " + latestDocumentDate + "\"}]}";
		            connection.getOutputStream().write(requestBody.getBytes());

		            int responseCode = connection.getResponseCode();

		            if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
		                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		                String line;
		                StringBuilder response = new StringBuilder();

		                while ((line = reader.readLine()) != null) {
		                    response.append(line);
		                }

		                reader.close();
//		                int update = updateuserOtp( mailId, otp, jdbcTemplate);

		                System.out.println("Response: " + response);
//		                return true;
		            } else {
		                System.out.println("Request failed with response code: " + responseCode);
//		                return false; // or any other appropriate value indicating failure
		            }

		        } catch (Exception e) {
		            e.printStackTrace();
//		            return false; // or any other appropriate value indicating failure
		        }
	        }

	        return emailIds;
	    } catch (Exception exception) {
	        LOGGER.error("UserPersistence.getEmailIdsByTennantId() tennantId {}", tennantId, exception);
	        return null;
	    }
	}
	

	
	
	public List<String> getEmailIdsJobByTennantId(JdbcTemplate jdbcTemplate, String tennantId, int noOfDocuments, String latestDocumentDate) {
		List<String> sentEmailIds = new ArrayList<>();
		try {
	        List<JobNotifyEmail> jobNotifyEmail = jdbcTemplate.query(USER_PROFILE_BY_TENNATID, new JobNotifyEmailRowMapper(), new Object[]{tennantId});

	        List<String> emailIds = new ArrayList<>();
	        for (JobNotifyEmail jobNotifyEmailVar : jobNotifyEmail) {
	            emailIds.add(jobNotifyEmailVar.getEmailId());
	        }
	        
	        if (emailIds != null && !emailIds.isEmpty()) {
	        	try {
	        		StringBuilder emailIdsJson = new StringBuilder();
	                emailIdsJson.append("[");
	                for (String emailId : emailIds) {
	                    if (emailIdsJson.length() > 1) {
	                        emailIdsJson.append(",");
	                    }
	                    emailIdsJson.append("\"").append(emailId).append("\"");
	                }
	                emailIdsJson.append("]");
		            URL url = new URL("https://api.sendgrid.com/v3/mail/send");
		            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		            connection.setRequestMethod("POST");
		            connection.setRequestProperty("Authorization", "Bearer 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");
		            connection.setRequestProperty("Content-Type", "application/json");
		            connection.setDoOutput(true);
//[{\"to\": [{\"email\": \"" + "martinagracy28@gmail.com" + "\"}]}],
//		            String requestBody = "{\"personalizations\": [{\"to\": [{\"email\": \"" + "martinagracy28@gmail.com" + "\"}]}], \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"OTP for Login\", \"content\": [{\"type\": \"text/plain\", \"value\": \"Your OTP is:"+ "otp" + " \\nClick " + "http://sigmapoc-appui.vercel.app/reset-submission" + " to reset the Password.\"}]}";
		            StringBuilder personalizationsJson = new StringBuilder();
	                personalizationsJson.append("[");
	                for (String email : emailIds) {
	                    if (personalizationsJson.length() > 1) {
	                        personalizationsJson.append(",");
	                    }
	                    personalizationsJson.append("{\"to\": [{\"email\": \"").append(email).append("\"}]}");
	                    sentEmailIds.add(email); // Track the sent email IDs
	                }
	                personalizationsJson.append("]");

	                String requestBody = "{\"personalizations\": " + personalizationsJson.toString() + ", \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"NFT Conversion Job Information\", \"content\": [{\"type\": \"text/plain\", \"value\": \"Your NFT Conversion job is completed successfully, Total number of NFT  Converted today: " + noOfDocuments + "\\nJob completed by " + latestDocumentDate + "\"}]}";
		            connection.getOutputStream().write(requestBody.getBytes());

		            int responseCode = connection.getResponseCode();

		            if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
		                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		                String line;
		                StringBuilder response = new StringBuilder();

		                while ((line = reader.readLine()) != null) {
		                    response.append(line);
		                }

		                reader.close();
//		                int update = updateuserOtp( mailId, otp, jdbcTemplate);

		                System.out.println("Response: " + response);
//		                return true;
		            } else {
		                System.out.println("Request failed with response code: " + responseCode);
//		                return false; // or any other appropriate value indicating failure
		            }

		        } catch (Exception e) {
		            e.printStackTrace();
//		            return false; // or any other appropriate value indicating failure
		        }
	        }

	        return emailIds;
	    } catch (Exception exception) {
	        LOGGER.error("UserPersistence.getEmailIdsByTennantId() tennantId {}", tennantId, exception);
	        return null;
	    }
	}
	

}
