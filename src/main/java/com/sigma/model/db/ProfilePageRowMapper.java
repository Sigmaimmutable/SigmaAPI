package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.ProfilePage;
import com.sigma.model.UserProfile;

public class ProfilePageRowMapper implements RowMapper<ProfilePage> {
    @Override
    public ProfilePage mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ProfilePage profilePage = new ProfilePage();
    	profilePage.setUserId(rs.getString("ID"));
    	profilePage.setFirstName(rs.getString("FIRST_NAME"));
    	profilePage.setLastName(rs.getString("LAST_NAME"));
    	profilePage.setMobileNumber(rs.getString("MOBILE_NUMBER"));
    	profilePage.setProfilePic(rs.getString("PROFILE_PIC"));
    	profilePage.setGender(rs.getString("GENDER"));
    	profilePage.setState(rs.getString("STATE"));
    	profilePage.setCountry(rs.getString("COUNTRY"));
    	profilePage.setLaunguage(rs.getString("USER_LANGUAGE"));
    	profilePage.setTimeZone(rs.getString("TIME_ZONE"));
    	profilePage.setEmailId(rs.getString("EMAIL_ID"));
        return profilePage;
    }
}
