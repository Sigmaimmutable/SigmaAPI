package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.UserBalance2;

public class UserBalanceRowMapper4 implements RowMapper<UserBalance2> {
    @Override
    public UserBalance2 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	UserBalance2 resource = new UserBalance2();
    	resource.setUserId(rs.getString("USER_ID"));
    	resource.setUserCredits(rs.getDouble("USER_CREDITS"));
        return resource;
    }
}
