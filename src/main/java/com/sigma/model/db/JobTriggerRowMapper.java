package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.JobManagement;
import com.sigma.model.JobTrigger;
import com.sigma.model.Session;

public class JobTriggerRowMapper implements RowMapper<JobTrigger> {
    @Override
    public JobTrigger mapRow(ResultSet rs, int rowNum) throws SQLException {
    	JobTrigger jobtrigger = new JobTrigger();
    	jobtrigger.setId(rs.getLong("id"));
   
    	jobtrigger.setJobType(rs.getString("JOB_TYPE"));
    	jobtrigger.setTennatId(rs.getString("TENNAT_ID"));
    	jobtrigger.setJobstatus(rs.getInt("JOB_STATUS"));
    	return jobtrigger;
    	
       
    }
}
