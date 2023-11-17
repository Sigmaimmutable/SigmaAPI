package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.DocumentO2Job;

public class JobRowMapper4 implements RowMapper<DocumentO2Job> {
    @Override
    public DocumentO2Job mapRow(ResultSet rs, int rowNum) throws SQLException {
    	DocumentO2Job invoice2 = new DocumentO2Job();
    	invoice2.setId(rs.getLong("id"));
    	invoice2.setJobName(rs.getString("JOB_NAME"));
    	invoice2.setStatus(rs.getString("STATUS"));
    	invoice2.setErrorSummary(rs.getString("ERROR_SUMMARY"));
    	invoice2.setCompanyCode(rs.getString("COMPANY_CODE"));
    	invoice2.setJobRunByUser(rs.getString("JOB_RUN_BY_USER"));
    	invoice2.setStatus(rs.getString("STATUS"));
    	invoice2.setRunStartTime(rs.getDate("RUN_START_TIME"));
    	invoice2.setRunCompletionTime(rs.getDate("RUN_COMPLETION_TIME"));
    	invoice2.setJobRunByUser(rs.getString("JOB_RUN_BY_USER"));
    	invoice2.setLatestDocumentDate(rs.getString("LATEST_DOCUMENT_DATE"));
    	invoice2.setTenantId(rs.getString("TENANT_ID"));
    	invoice2.setJobType(rs.getString("JOB_TYPE"));
        return invoice2;
    }
}
