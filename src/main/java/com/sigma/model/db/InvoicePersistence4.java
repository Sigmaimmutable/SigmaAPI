package com.sigma.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Invoice2;
import com.sigma.model.Payment2;
import com.sigma.model.UserProfile;
public class InvoicePersistence4 {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.InvoicePersistence4");
	static final String INSERT_RESOURCE_SQL = "INSERT INTO PAYMENT (USER_ID, "
			+ " STATUS, AMOUNT) VALUES (?,?,?)";
	
	static final String RESOURCE = "SELECT * FROM PAYMENT WHERE USER_ID = ?";
	/*
	static final String RESOURCE_ALLSELECT = "SELECT * FROM PAYMENT";
	*/
	static final String RESOURCE_UPDATE = "UPDATE INVOICE SET "
			+ "STATUS= 'PAID' WHERE USER_ID = ? ";
	
	static final String SELECT_INVOICE_SQL = "SELECT * FROM INVOICE WHERE USER_ID = ? AND "
			+ " ID = (SELECT MAX(ID) FROM INVOICE WHERE USER_ID = ?)"; //
	
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
	public List<Invoice2> getPendingInvoices(JdbcTemplate jdbcTemplate, String userId) {
		List<Invoice2> invoiceList = null;
		try {
			invoiceList = jdbcTemplate.query(SELECT_INVOICE_SQL, new InvoiceRowMapper2(),
					new Object[] { userId, userId});
			if(invoiceList == null || invoiceList.isEmpty()) {
				Invoice2 	invoice1 = new Invoice2();
				invoiceList.add(invoice1);
			}			
			return invoiceList;
		} catch (Exception exception) {
			LOGGER.error("InvoicePersistence4.getPendingInvoices() userId {}", userId, exception);
			Invoice2 	invoice1 = new Invoice2();
			invoiceList.add(invoice1);
			return invoiceList;
		}
	}
}
