package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.JobManagement;
import com.sigma.model.Session;

public class JobManagementRowMapper implements RowMapper<JobManagement> {
    @Override
    public JobManagement mapRow(ResultSet rs, int rowNum) throws SQLException {
    	JobManagement jobmanagement = new JobManagement();
    	jobmanagement.setId(rs.getLong("id"));
    	jobmanagement.setLoginTime(rs.getString("LOGIN_TIME"));
    	jobmanagement.setMailId(rs.getString("MAIL_ID"));
    	jobmanagement.setRoleType(rs.getString("ROLE_TYPE"));
    	jobmanagement.setTennatId(rs.getString("TENNAT_ID"));
    	jobmanagement.setActivity(rs.getString("ACTIVITY"));
    	return jobmanagement;
    	
       
    }
}
