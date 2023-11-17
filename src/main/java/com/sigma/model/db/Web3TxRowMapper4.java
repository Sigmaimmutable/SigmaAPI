package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Web3Tx3;

public class Web3TxRowMapper4 implements RowMapper<Web3Tx3> {
    @Override
    public Web3Tx3 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Web3Tx3 resource = new Web3Tx3();
    	resource.setTxHash(rs.getString("TX_HASH"));
    	resource.setNonce(rs.getString("NONCE"));
    	resource.setGasPrice(rs.getString("GAS_PRICE"));
    	resource.setToAddress(rs.getString("TO_ADDRESS"));
    	resource.setValue(rs.getString("VALUE"));
    	resource.setFromAddress(rs.getString("FROM_ADDRESS"));
    	resource.setBlockHash(rs.getString("BLOCK_HASH"));
    	resource.setBlockNumber(rs.getString("BLOCK_NUMBER"));    	
    	return resource;    	
    }
}
