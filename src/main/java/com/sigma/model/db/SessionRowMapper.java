package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


import com.sigma.model.Session;

public class SessionRowMapper implements RowMapper<Session> {
    @Override
    public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Session session = new Session();
    	session.setId(rs.getLong("id"));
    	session.setLoginTime(rs.getDate("LOGIN_TIME"));
    	session.setLogoutTime(rs.getDate("LOGOUT_TIME"));
    	session.setMailId(rs.getString("MAIL_ID"));
    	session.setRoleType(rs.getString("ROLE_TYPE"));
    	session.setTennatId(rs.getString("TENNAT_ID"));
    	session.setActivity(rs.getString("ACTIVITY"));
    	session.setStartDate(rs.getString("START_DATE"));
    	return session;
    	
       
    }
}
