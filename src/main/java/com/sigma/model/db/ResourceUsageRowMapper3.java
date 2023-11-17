package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.ResourceUsage;

public class ResourceUsageRowMapper3 implements RowMapper<ResourceUsage> {
    @Override
    public ResourceUsage mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ResourceUsage resource = new ResourceUsage();
    	resource.setId(rs.getString("id"));
    	resource.setUserId(rs.getString("USER_ID"));
    	resource.setResourceId(rs.getString("RESOURCE_ID"));
    	resource.setStartDate(rs.getDate("START_TIME"));
    	resource.setEndTime(rs.getDate("END_TIME"));
    	resource.setStatus(rs.getString("STATUS"));
        return resource;
    }
}
