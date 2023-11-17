package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Payment2;
public class PaymentPersistence3 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.PaymentPersistence3");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO PAYMENT (USER_ID, "
			+ " STATUS, AMOUNT) VALUES (?,?,?)";	
	static final String RESOURCE = "SELECT * FROM PAYMENT WHERE USER_ID = ?";
	static final String RESOURCE_UPDATE = "UPDATE INVOICE SET "
			+ "STATUS= 'PAID' WHERE USER_ID = ? ";
	public int updatePayment(Payment2 resource, JdbcTemplate jdbcTemplate) {
		int update=0;
		try {
			String userId= resource.getUserId();
			if(userId == null || userId.trim().isEmpty())
				return 0;			
			update = jdbcTemplate.update(RESOURCE_UPDATE, userId);				
				return update;
		}catch(Exception exception) {
			LOGGER.error("Error ResourcePersistence.updateResource() profile", resource, exception);
			return update;
		}
		}	
		
	public int generatePayment(Payment2 resource, JdbcTemplate jdbcTemplate) {
		int insert = 0;
		try {			
			insert = jdbcTemplate.update(INSERT_RESOURCE_SQL, resource.getUserId(), resource.getStatus(),
					resource.getAmount());
			if(insert>0) {
				int updatePayment = updatePayment(resource, jdbcTemplate);
				if(updatePayment<=0)
					LOGGER.info("PaymentPersistence3.generatePayment() => updatePayment() failed");
			}
			return insert;			
		} catch (Exception exception) {
			LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", resource, exception);
			return insert;
		}
	}
	/*
	public Resource getResource(JdbcTemplate jdbcTemplate, String id) {
		try {
			Resource resource = jdbcTemplate.queryForObject(RESOURCE, 
					new ResourceRowMapper2(),
					new Object[] { id });
			return resource;
		} catch (Exception exception) {
			LOGGER.error("ResourcePersistence2.getResource() id {}", id, exception);
			return null;
		}
	}
	*/
	public List<Payment2> getPaymentList(JdbcTemplate jdbcTemplate, String userId) {
			try {
				List<Payment2> resources = jdbcTemplate.query(RESOURCE, 
						new PaymentRowMapper4(),
						new Object[] { userId });
				if(resources == null || resources.isEmpty())
					return new ArrayList<Payment2>();		
			return resources;
			}catch(Exception exception) {
				LOGGER.error("PaymentPersistence3.getResourceList() ", exception);
				return new ArrayList<Payment2>();
			}
		}

}
