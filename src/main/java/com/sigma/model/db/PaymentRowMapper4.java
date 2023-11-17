package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Payment2;
import com.sigma.model.RawInvoice;

public class PaymentRowMapper4 implements RowMapper<Payment2> {
    @Override
    public Payment2 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Payment2 resource = new Payment2();
    	resource.setId(rs.getString("ID"));
    	resource.setUserId(rs.getString("USER_ID"));
    	resource.setStatus(rs.getString("STATUS"));
    	resource.setAmount(rs.getDouble("AMOUNT"));    
        return resource;
    }
}
