package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.SigmaAPIDocConfig;

public class SigmaDocFieldConfigRowMapper4 implements RowMapper<SigmaAPIDocConfig> {
    @Override
    public SigmaAPIDocConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
    	SigmaAPIDocConfig invoice2 = new SigmaAPIDocConfig();
    	invoice2.setId(rs.getString("ID"));
    	invoice2.setSigmaField(rs.getString("SIGMA_FIELD"));
    	invoice2.setExtField(rs.getString("EXT_FIELD"));
    	invoice2.setTenantId(rs.getString("TENANT_ID"));
    	invoice2.setStatus(rs.getString("STATUS"));
    	invoice2.setCreatedBy(rs.getString("CREATED_BY"));//v
        return invoice2;
    }
}
