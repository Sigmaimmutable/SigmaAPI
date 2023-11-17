package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Favourite;
import com.sigma.model.Session;

public class FvouriteRowMapper implements RowMapper<Favourite> {
    @Override
    public Favourite mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Favourite favourite = new Favourite();
    	favourite.setUserId(rs.getString("ID"));
    	favourite.setEmailId(rs.getString("EMAIL_ID"));
    	favourite.setDocId(rs.getString("DOC_ID"));
    	favourite.setFileName(rs.getString("FILE_NAME"));
    	favourite.setDocName(rs.getString("DOC_NAME"));
    	favourite.setDocStatus(rs.getString("DOC_STATUS"));
    	return favourite;
    	
       
    }
}
