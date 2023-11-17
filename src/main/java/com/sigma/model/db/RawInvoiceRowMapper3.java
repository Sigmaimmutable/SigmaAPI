package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.RawInvoice;

public class RawInvoiceRowMapper3 implements RowMapper<RawInvoice> {
    @Override
    public RawInvoice mapRow(ResultSet rs, int rowNum) throws SQLException {
    	RawInvoice resource = new RawInvoice();
    	resource.setId(rs.getString("ID"));
    	resource.setUserId(rs.getString("USER_ID"));
    	resource.setResourceId(rs.getString("RESOURCE_ID"));
    	resource.setFee(rs.getDouble("FEE"));
        return resource;
    }
}
