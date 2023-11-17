package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Resource;
import com.sigma.model.SmartContractUsage;
import com.sigma.model.UserProfile;

public class SmartContractUsageTemplateRowMapper3 implements RowMapper<SmartContractUsage> {
    @Override
    public SmartContractUsage mapRow(ResultSet rs, int rowNum) throws SQLException {
    	SmartContractUsage resource = new SmartContractUsage();
    	resource.setId(rs.getString("id"));
    	resource.setUserId(rs.getString("USER_ID"));
    	resource.setSmartContractAddress(rs.getString("SMART_CONTRACT_ADDRESS"));
    	resource.setNetworkType(rs.getString("NETWORK_TYPE"));
    	resource.setTokenName(rs.getString("TOKEN_NAME"));
    	resource.setDepositTokenAddress(rs.getString("DEPOSIT_TOKEN_ADDRESS"));
    	resource.setBondTokenAddress(rs.getString("BOND_TOKEN_ADDRESS"));
    	resource.setOwnerAddress(rs.getString("OWNER_ADDRESS"));
    	resource.setStatus(rs.getString("STATUS"));
    	resource.setStartDate(rs.getLong("START_DATE"));
    	resource.setEndDate(rs.getLong("END_DATE"));
    	resource.setAppId(rs.getLong("APP_ID"));
        return resource;
    }
}
