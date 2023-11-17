package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.Web3Block2;

public class Web3BlockRowMapper3 implements RowMapper<Web3Block2> {
    @Override
    public Web3Block2 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Web3Block2 resource = new Web3Block2();
    	resource.setBlockNumber(rs.getLong("BLOCK_NUMBER"));
    	resource.setBlockHash(rs.getString("BLOCK_HASH"));
    	resource.setMiner(rs.getString("MINER"));
    	resource.setTimeStamp(rs.getString("TIMESTAMP"));
    	resource.setGasLimit(rs.getString("GAS_LIMIT"));
    	resource.setGasUsed(rs.getString("GAS_USED"));
    	resource.setNonce(rs.getString("NONCE"));
        return resource;
    }
}
