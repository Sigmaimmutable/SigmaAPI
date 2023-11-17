package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.UserVisit;

public class UserVisitRowMapper implements RowMapper<UserVisit> {
    @Override
    public UserVisit mapRow(ResultSet rs, int rowNum) throws SQLException {
    	UserVisit userVisit = new UserVisit();
    	userVisit.setAlgoAddress(rs.getString("ALGO_ADDRESS"));
    	userVisit.setIpAddress(rs.getString("IP_ADDRESS"));
    	userVisit.setNetworkType(rs.getString("NETWORK_TYPE"));
    	userVisit.setWalletType(rs.getString("WALLET_TYPE"));//tr
    	userVisit.setStartDate(rs.getString("START_DATE"));
        return userVisit;
    }
}
