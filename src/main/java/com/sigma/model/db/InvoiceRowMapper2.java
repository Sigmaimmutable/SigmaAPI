package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Invoice2;

public class InvoiceRowMapper2 implements RowMapper<Invoice2> {
    @Override
    public Invoice2 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Invoice2 invoice2 = new Invoice2();
    	invoice2.setId(rs.getString("id"));
    	invoice2.setUserId(rs.getString("USER_ID"));
    	invoice2.setBillAmount(rs.getDouble("BILL_AMOUNT"));
    	invoice2.setInvoiceDate(rs.getDate("INVOICE_DATE"));
    	invoice2.setDueDate(rs.getDate("DUE_DATE"));
    	invoice2.setStatus(rs.getString("STATUS"));
    	invoice2.setUsageIds(rs.getString("USAGE_ID_LIST"));
        return invoice2;
    }
}
