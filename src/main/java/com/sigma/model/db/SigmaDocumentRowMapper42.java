package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.SigmaDocument2;

public class SigmaDocumentRowMapper42 implements RowMapper<SigmaDocument2> {
    @Override
    public SigmaDocument2 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	SigmaDocument2 invoice2 = new SigmaDocument2();
    	invoice2.setSigmaId(rs.getString("ID"));
    	invoice2.setTenantId(rs.getString("TENANT_ID"));
    	invoice2.setDocChecksum(rs.getString("DOC_CHECKSUM"));
    	invoice2.setNftCreationStatus(rs.getInt("NFT_CREATION_STATUS"));
    	invoice2.setUuid(rs.getString("UUID"));
    	invoice2.setStatus(rs.getString("STATUS"));
    	invoice2.setCreatedBy(rs.getString("CREATED_BY"));
    	invoice2.setCreatedDate(rs.getDate("CREATED_DATE"));
    	invoice2.setMd5Checksum(rs.getString("DOC_MD5CHECKSUM"));
    	invoice2.setFileName(rs.getString("FILE_NAME"));
    	invoice2.setMailId(rs.getString("MAIL_ID"));
        return invoice2;
    }
}
