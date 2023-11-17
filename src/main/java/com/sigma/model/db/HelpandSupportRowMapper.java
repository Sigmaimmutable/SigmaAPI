package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.HelpandSupport;
import com.sigma.model.Notification;
import com.sigma.model.Session;

public class HelpandSupportRowMapper implements RowMapper<HelpandSupport> {
    @Override
    public HelpandSupport mapRow(ResultSet rs, int rowNum) throws SQLException {
    	HelpandSupport helpandsupport = new HelpandSupport();
    	helpandsupport.setId(rs.getLong("ID"));
    	helpandsupport.setTicket(rs.getString("TICKET"));
    	helpandsupport.setDescriptions(rs.getString("DESCRIPTIONS"));
    	helpandsupport.setFirstName(rs.getString("FIRST_NAME"));
    	helpandsupport.setLastName(rs.getString("LAST_NAME"));
    	helpandsupport.setMailId(rs.getString("MAIL_ID"));
    	helpandsupport.setTicketRaisetime(rs.getDate("TICKET_RAISETIME"));
    	helpandsupport.setTennatId(rs.getString("TENNAT_ID"));
    	helpandsupport.setStatuses(rs.getBoolean("STATUSES"));
    	helpandsupport.setAssignee(rs.getString("ASSIGNEE"));
    	return helpandsupport;
    	
       
    }
}
