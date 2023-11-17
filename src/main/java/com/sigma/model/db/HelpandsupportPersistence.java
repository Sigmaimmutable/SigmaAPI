package com.sigma.model.db;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.HelpandSupport;
import com.sigma.model.Notification;
import com.sigma.model.Payment2;
import com.sigma.model.Session;
import com.sigma.model.UserProfile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
public class HelpandsupportPersistence {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.HelpandsupportPersistence");
	static final String INSERT_HELPANDSUPPORT_SQL = "INSERT INTO HELPANDSUPPORT (TICKET,"
			+ "DESCRIPTIONS, FIRST_NAME, LAST_NAME, MAIL_ID, TENNAT_ID, STATUSES, ASSIGNEE) VALUES (?,?,?,?,?,?,?,?)";
	static final String HELPANDSUPPORT_MAIL_ID = "SELECT * FROM HELPANDSUPPORT LIMIT 10 OFFSET ?";
	static final String HELPANDSUPPORT_MAIL_ID_BY_STATUS = "SELECT * FROM HELPANDSUPPORT WHERE MAIL_ID = ? AND STATUSES = ? ";
	static final String HELPANDSUPPORT_UPDATE_BY_ID = "UPDATE HELPANDSUPPORT SET STATUSES = ? "
			+ "WHERE MAIL_ID = ? AND ID = ? ";
	
	static final String HELPANDSUPPORT_ASSIGNEE_UPDATE_BY_ID = "UPDATE HELPANDSUPPORT SET ASSIGNEE = ? "
			+ "WHERE MAIL_ID = ? AND ID = ? ";
	
	
	public int createSession(HelpandSupport helpandsupport, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		int insertedValue = 0;
		try {
			
			insert = jdbcTemplate.update(INSERT_HELPANDSUPPORT_SQL, 
					helpandsupport.getTicket(), helpandsupport.getDescriptions(),helpandsupport.getFirstName(),helpandsupport.getLastName(),helpandsupport.getMailId(),
					helpandsupport.getTennatId(),helpandsupport.getStatuses(), helpandsupport.getAssignee());	
			if(insert == 1) {
				URL url = new URL("https://api.sendgrid.com/v3/mail/send");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Authorization", "Bearer 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");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setDoOutput(true);

	            String requestBody = "{\"personalizations\": [{\"to\": [{\"email\": \"" + helpandsupport.getMailId() + "\"}]}], \"from\": {\"email\": \"immutablesigma@gmail.com\"}, \"subject\":\"Help and support acknowlegdement\", \"content\": [{\"type\": \"text/plain\", \"value\": \"Thank you for submitting your ticket. We would like to inform you that it has been received by Sigma Admin. We appreciate your patience, and a member of our team will be in touch with you shortly. \"}]}";
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
			LOGGER.error("Error HelpandsupportPersistence.createsession() notification", helpandsupport, exception);
			return insert;
		}
	}
	
	
	public int updateHelpandsupportById(HelpandSupport helpandsupport, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String mailId= helpandsupport.getMailId();
			if(mailId == null || mailId.trim().isEmpty())
				return 0;	
			update = jdbcTemplate.update(HELPANDSUPPORT_UPDATE_BY_ID, 
					helpandsupport.getStatuses(), helpandsupport.getMailId(), helpandsupport.getId()
					);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error HelpandSupportPersistence.createsession() helpandsupport", helpandsupport, exception);
			return update;
		}
	}
	
	public int updateAssigneeHelpandsupportById(HelpandSupport helpandsupport, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String mailId= helpandsupport.getMailId();
			if(mailId == null || mailId.trim().isEmpty())
				return 0;	
			update = jdbcTemplate.update(HELPANDSUPPORT_ASSIGNEE_UPDATE_BY_ID, 
					helpandsupport.getAssignee(), helpandsupport.getMailId(), helpandsupport.getId()
					);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error HelpandSupportPersistence.createsession() helpandsupport", helpandsupport, exception);
			return update;
		}
	}

	public List<HelpandSupport> getMailHelpandSupport(JdbcTemplate jdbcTemplate,int limit) {
		try {
			List<HelpandSupport> helpandsupport = jdbcTemplate.query(HELPANDSUPPORT_MAIL_ID, new HelpandSupportRowMapper(),
					new Object[] {limit });
			return helpandsupport;
		} catch (Exception exception) {
			LOGGER.error("HelpandSupportPersistence.getMailsession() mailId {}", exception);
			return null;
		}
	}
	
	public List<HelpandSupport> getMailHelpandSupportByStatus(JdbcTemplate jdbcTemplate, String mailId, String status) {
		try {
			List<HelpandSupport> helpandsupport = jdbcTemplate.query(HELPANDSUPPORT_MAIL_ID_BY_STATUS, new HelpandSupportRowMapper(),
					new Object[] { mailId, status });
			return helpandsupport;
		} catch (Exception exception) {
			LOGGER.error("HelpandSupportPersistence.getMailsession() mailId {}", mailId, exception);
			return null;
		}
	}


	

}
