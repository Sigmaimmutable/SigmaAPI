package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.SigmaDocument;

public class SigmaDocumentRowMapper4 implements RowMapper<SigmaDocument> {
    @Override
    public SigmaDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
    	SigmaDocument invoice2 = new SigmaDocument();
    	invoice2.setSigmaId(rs.getString("ID"));
    	invoice2.setfVar1(rs.getString("FLEXFIELD_VAR1"));
    	invoice2.setfVar2(rs.getString("FLEXFIELD_VAR2"));
    	invoice2.setfVar3(rs.getString("FLEXFIELD_VAR3"));
    	invoice2.setfVar4(rs.getString("FLEXFIELD_VAR4"));
    	invoice2.setfVar5(rs.getString("FLEXFIELD_VAR5"));
    	invoice2.setfVar6(rs.getString("FLEXFIELD_VAR6"));
    	invoice2.setfVar7(rs.getString("FLEXFIELD_VAR7"));
    	invoice2.setfVar8(rs.getString("FLEXFIELD_VAR8"));
    	invoice2.setfVar9(rs.getString("FLEXFIELD_VAR9"));
    	invoice2.setfVar10(rs.getString("FLEXFIELD_VAR10"));
    	invoice2.setTenantId(rs.getString("TENANT_ID"));
    	invoice2.setDocChecksum(rs.getString("DOC_CHECKSUM"));
    	invoice2.setNftCreationStatus(rs.getInt("NFT_CREATION_STATUS"));
    	invoice2.setUuid(rs.getString("UUID"));
    	invoice2.setStatus(rs.getString("STATUS"));
    	invoice2.setCreatedBy(rs.getString("CREATED_BY"));
    	invoice2.setCreatedDate(rs.getDate("CREATED_DATE"));
    	invoice2.setJobId(rs.getLong("JOB_ID"));//JOB_ID tr
    	invoice2.setMd5Checksum(rs.getString("DOC_MD5CHECKSUM"));
        return invoice2;
    }
}
