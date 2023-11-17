package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.UserProfile;

public class UserProfileRowMapper implements RowMapper<UserProfile> {
    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
    	UserProfile userProfile = new UserProfile();
    	userProfile.setUserKey(rs.getString("id"));
    	userProfile.setUserId(rs.getString("USER_ID"));
    	userProfile.setUserName(rs.getString("USER_NAME"));
    	userProfile.setCreationTime(rs.getString("CREATION_TIME"));
    	userProfile.setPassword(rs.getString("PASSWORD"));
    	userProfile.setValiduser(rs.getString("VALID_USER"));
    	userProfile.setRole(rs.getString("ROLE"));
    	userProfile.setUserCredits(rs.getDouble("USER_CREDITS"));
        return userProfile;
    }
}
