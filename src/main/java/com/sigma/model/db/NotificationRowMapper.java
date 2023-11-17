package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Notification;
import com.sigma.model.Session;

public class NotificationRowMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Notification notification = new Notification();
    	notification.setId(rs.getLong("ID"));
    	notification.setTitle(rs.getString("TITLE"));
    	notification.setDescriptions(rs.getString("DESCRIPTIONS"));
    	notification.setMailId(rs.getString("MAIL_ID"));
    	notification.setEpochtime(rs.getLong("EPOCHTIME"));
    	notification.setTennatId(rs.getString("TENNAT_ID"));
    	notification.setStatuses(rs.getBoolean("STATUSES"));
    	return notification;
    	
       
    }
}
