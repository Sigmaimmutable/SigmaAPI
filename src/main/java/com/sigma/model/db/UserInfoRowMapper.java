package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.UserInfo;
import com.sigma.model.UserProfile;

public class UserInfoRowMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
    	UserInfo userInfo = new UserInfo();
    	userInfo.setUserId(rs.getString("ID"));
    	userInfo.setEmailId(rs.getString("EMAIL_ID"));
    	userInfo.setUserName(rs.getString("USER_NAME"));
    	userInfo.setPassword(rs.getString("PASSWORD"));
    	userInfo.setRoleType(rs.getString("ROLE_TYPE"));
    	userInfo.setMethod(rs.getString("METHOD"));
    	userInfo.setTennantId(rs.getString("TENNANT_ID"));
    	userInfo.setOtp(rs.getString("OTP"));
    	userInfo.setExpired(rs.getString("EXPIRED"));
        return userInfo;
    }
}
