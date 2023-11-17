package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.JobNotifyEmail;
import com.sigma.model.UserInfo;
import com.sigma.model.UserProfile;

public class JobNotifyEmailRowMapper implements RowMapper<JobNotifyEmail> {
    @Override
    public JobNotifyEmail mapRow(ResultSet rs, int rowNum) throws SQLException {
    	JobNotifyEmail jobNotifyEmail = new JobNotifyEmail();
    	jobNotifyEmail.setUserId(rs.getString("ID"));
    	jobNotifyEmail.setEmailId(rs.getString("MAIL_ID"));
    	jobNotifyEmail.setTenantId(rs.getString("TENANT_ID"));
        return jobNotifyEmail;
    }
}
