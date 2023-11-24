package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.APILog;

public class APILogRowMapper implements RowMapper<APILog> {
    @Override
    public APILog mapRow(ResultSet rs, int rowNum) throws SQLException {
    	APILog apilog = new APILog();
    	apilog.setIpAddress(rs.getString("IP_ADDRESS"));
    	apilog.setMailId(rs.getString("MAIL_ID"));
    	apilog.setApiName(rs.getString("API_NAME"));
    	apilog.setCallDate(rs.getString("CALL_DATE"));
    	apilog.setMethod(rs.getString("METHOD"));
    	apilog.setPage(rs.getString("PAGE"));
        return apilog;
    }
}
