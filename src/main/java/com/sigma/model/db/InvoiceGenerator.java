package com.sigma.model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.sigma.model.Invoice2;
import com.sigma.model.RawInvoice;
import com.sigma.model.UserBalance2;

public class InvoiceGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.model.db.InvoiceGenerator");
	static final String FETCH_INVOICE_SQL = " SELECT RU.ID, RU.USER_ID, "
		+ "RU.RESOURCE_ID, RE.FEE  FROM RESOURCE_USAGE RU, RESOURCE RE "
			+ "WHERE RU.RESOURCE_ID = RE.ID AND RU.STATUS!='PAID' ORDER BY RU.USER_ID, RU.RESOURCE_ID";
	static final String USER_BALANCE_SQL="SELECT USER_ID, USER_CREDITS FROM USER_PROFILE "
			+ " WHERE VALID_USER='Y' and USER_CREDITS > 0";
	static final String UPDATE_USER_BALANCE_SQL = "UPDATE USER_PROFILE SET "
			+ " USER_CREDITS = ? WHERE USER_ID = ?";
	static final String FETCH_LATEST_INVOICE_MONTH = "SELECT MAX(MONTH(INVOICE_DATE)) FROM INVOICE";
	protected List<RawInvoice> generateInvoice(JdbcTemplate jdbcTemplate) {
			List<RawInvoice> rawInvoiceList = null;
			try {			
				rawInvoiceList = jdbcTemplate.query(FETCH_INVOICE_SQL, new RawInvoiceRowMapper3());			
				return rawInvoiceList;			
			} catch (Exception exception) {
				LOGGER.error("Error ResourcePersistence2.createUserProfile() profile", exception);
				return rawInvoiceList;
			}
		}
	public Collection<Invoice2> generateInvoiceOutput(JdbcTemplate jdbcTemplate) {
		List<RawInvoice> rawInvoiceList = generateInvoice(jdbcTemplate);
		Map<String, Invoice2> invMap = new HashMap<String, Invoice2>();
		for(RawInvoice rawInvoice : rawInvoiceList){
			String userId = rawInvoice.getUserId();
			Invoice2 invoice2 = invMap.get(userId);
			if(invoice2 == null) {
				invoice2 = new Invoice2();
				invoice2.setUserId(rawInvoice.getUserId());
				invoice2.setUsageIds(rawInvoice.getId());
				invoice2.setBillAmount(rawInvoice.getFee());
				invoice2.setStatus("Pending");
				invoice2.setInvoiceDate(new Date());
				invoice2.setDueDate(Calendar.getInstance().getTime());
				invMap.put(userId, invoice2);
			}else {
				String userId2 = rawInvoice.getId();
				userId2 += ","+invoice2.getUsageIds();
				invoice2.setUsageIds(userId2);
				Double fee = rawInvoice.getFee();
				fee += invoice2.getBillAmount();
				invoice2.setBillAmount(fee);
			}			
		}
		Collection<Invoice2> values = invMap.values();
		adjustUserCredits(values, jdbcTemplate);
		persistInvoiceRecords(values, jdbcTemplate);
		return values;
	}
	private void persistInvoiceRecords(Collection<Invoice2> values, JdbcTemplate jdbcTemplate) {
		
		for(Invoice2 invoice2: values) {
			try {
					jdbcTemplate.update(new PreparedStatementCreator() {		    	  
			            public PreparedStatement createPreparedStatement(Connection con)
			                    throws SQLException {
			                PreparedStatement stmt = con.prepareStatement(
			                		"INSERT into INVOICE(USER_ID, BILL_AMOUNT, INVOICE_DATE,"
			                		+ "DUE_DATE, STATUS, USAGE_ID_LIST) VALUES (?, ?, ?,?,?,?)");
			                stmt.setString(1, invoice2.getUserId());
			                stmt.setDouble(2, invoice2.getBillAmount());
			                stmt.setDate(3, new java.sql.Date(invoice2.getInvoiceDate().getTime()));
			                stmt.setDate(4, new java.sql.Date(invoice2.getDueDate().getTime()));
			                stmt.setString(5, invoice2.getStatus());
			                stmt.setString(6, invoice2.getUsageIds());		                
			                return stmt;
		            }
		        });
			}catch(Exception exception) {
				LOGGER.error("InvoiceGenerator.persistInvoiceRecords() Error persisting the invoice records", exception);
			}
		}
	}
	public void adjustUserCredits(Collection<Invoice2> values, JdbcTemplate jdbcTemplate) {
		try {
		List<UserBalance2> userBalances = jdbcTemplate.query(USER_BALANCE_SQL, new UserBalanceRowMapper4());
		Map<String, Double> userBalanceMap = new HashMap<String, Double>();
		for(UserBalance2 userBalance : userBalances)
			userBalanceMap.put(userBalance.getUserId(), userBalance.getUserCredits());
		for(Invoice2 invoice2: values) {
			String invoiceUserId = invoice2.getUserId();
			Double userAvailableBalance = 0.0;
			userAvailableBalance = userBalanceMap.getOrDefault(invoiceUserId, 0.0);
			boolean userBalanceZero = false;
			if(userAvailableBalance == 0.0)
				userBalanceZero = true;
			Double latestBillsAmount = invoice2.getBillAmount();
			userAvailableBalance = userAvailableBalance - latestBillsAmount;
			Double userBalanceToUpdate = 0.0;
			
			if(userAvailableBalance >= 0) {
				userBalanceToUpdate = userAvailableBalance;
				invoice2.setStatus("PAID");
			}
			else {
				invoice2.setBillAmount(userAvailableBalance * - 1);
				userBalanceToUpdate = 0.0;
			}			
			int update = 0;
			if(!userBalanceZero)
				update = jdbcTemplate.update(UPDATE_USER_BALANCE_SQL, userBalanceToUpdate, invoiceUserId);
			if(update<0)
				LOGGER.info("Error updating the user balance against latest invoice auto payment");
		}
		}catch(Exception exception) {
			LOGGER.error("InvoiceGenerator.adjustUserCredits()", exception);
		}
		
	}
	public boolean shouldGenerateInvoice(JdbcTemplate jdbcTemplate) {
		Integer lastInvoiceMonth = 0;
		Integer currentMonth = 0;
		boolean shouldGenerate = false;
		try {
				lastInvoiceMonth = jdbcTemplate.queryForObject(FETCH_LATEST_INVOICE_MONTH, Integer.class);
				Calendar calendar = Calendar.getInstance();
				currentMonth = calendar.get(Calendar.MONTH) + 1;
				shouldGenerate = lastInvoiceMonth < currentMonth;
				LOGGER.info("InvoiceGenerator.shouldGenerateInvoice()  lastInvoiceMonth = {},  currentMonth = {}, returning shouldGenerate = {} ", lastInvoiceMonth,
						currentMonth, shouldGenerate);
				return shouldGenerate;
		}catch(Exception exception) {
			LOGGER.error("InvoiceGenerator.shouldGenerateInvoice()  lastInvoiceMonth = {},  currentMonth = {}, returning shouldGenerate = {} ", lastInvoiceMonth,
					currentMonth, shouldGenerate, exception);
			return false;
		}
	}
}
