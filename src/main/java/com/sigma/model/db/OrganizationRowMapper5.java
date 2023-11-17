package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Organization;

public class OrganizationRowMapper5 implements RowMapper<Organization> {
    @Override
    public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Organization invoice2 = new Organization();
    	invoice2.setId(rs.getString("ID"));
    	invoice2.setName(rs.getString("NAME"));
    	invoice2.setCreatedBy(rs.getString("CREATED_BY"));
    	invoice2.setCreatedDate(rs.getDate("CREATED_DATE"));
    	invoice2.setTenantId(rs.getString("TENANT_ID"));
//    	invoice2.setExtUrl(rs.getString("EXT_URL"));
//    	invoice2.setExtUserName(rs.getString("EXT_USER"));
//    	invoice2.setExtPassword(rs.getString("EXT_PASSWORD"));
    	invoice2.setStatus(rs.getInt("STATUS"));
        return invoice2;
    }
}
