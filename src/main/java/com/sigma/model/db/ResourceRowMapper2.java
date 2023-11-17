package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Resource;
import com.sigma.model.UserProfile;

public class ResourceRowMapper2 implements RowMapper<Resource> {
    @Override
    public Resource mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Resource resource = new Resource();
    	resource.setResourceId(rs.getString("id"));
    	resource.setResourceName(rs.getString("RESOURCE_NAME"));
    	resource.setStatus(rs.getString("STATUS"));
    	resource.setFee(rs.getDouble("FEE"));
    	resource.setFeeType(rs.getString("FEE_TYPE"));
        return resource;
    }
}
