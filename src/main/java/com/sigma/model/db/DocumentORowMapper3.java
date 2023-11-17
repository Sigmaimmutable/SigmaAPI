package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.DocumentO;

public class DocumentORowMapper3 implements RowMapper<DocumentO> {
    @Override
    public DocumentO mapRow(ResultSet rs, int rowNum) throws SQLException {
    	DocumentO invoice2 = new DocumentO();
    	invoice2.setId(rs.getLong("id"));
    	invoice2.setVersion_id(rs.getString("VERSION_ID"));
    	invoice2.setSource_owner__v(rs.getString("SOURCE_OWNER__V"));
    	invoice2.setPrimary_author__c(rs.getString("PRIMARY_AUTHOR__C"));
    	invoice2.setSource_document_number__v(rs.getString("SOURCE_DOCUMENT_NUMBER__V"));
    	invoice2.setApplication__v(rs.getString("APPLICATION__V"));
    	invoice2.setName__v(rs.getString("NAME__V"));
    	invoice2.setType__v(rs.getString("TYPE__V"));
    	invoice2.setTitle__v(rs.getString("TITLE__V"));
    	invoice2.setManufacturer__v(rs.getString("MANUFACTURER__V"));
    	invoice2.setSource_vault_id__v(rs.getString("SOURCE_VAULT_ID__V"));
    	invoice2.setCreated_by__v(rs.getString("CREATED_BY__V"));
    	invoice2.setStatus__v(rs.getString("STATUS__V"));
    	invoice2.setDocument_date__c(rs.getDate("DOCUMENT_DATE__C"));
    	invoice2.setFile_modified_date__v(rs.getDate("FILE_MODIFIED_DATE__V"));
    	invoice2.setFile_created_date__v(rs.getDate("FILE_CREATED_DATE__V"));
    	invoice2.setDocument_creation_date__v(rs.getDate("DOCUMENT_CREATION_DATE__V"));
    	invoice2.setDoc_Checksum(rs.getString("DOC_CHECKSUM"));
    	invoice2.setNftCreationStatus(rs.getString("NFT_CREATION_STATUS"));
    	invoice2.setUuid(rs.getString("UUID"));
        return invoice2;
    }
}
