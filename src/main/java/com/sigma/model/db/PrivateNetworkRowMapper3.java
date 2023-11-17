package com.sigma.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sigma.model.PrivateNetwork2;

public class PrivateNetworkRowMapper3 implements RowMapper<PrivateNetwork2> {
    @Override
    public PrivateNetwork2 mapRow(ResultSet rs, int rowNum) throws SQLException {
    	PrivateNetwork2 privateNetwork = new PrivateNetwork2();
    	privateNetwork.setId(rs.getLong("ID"));
    	privateNetwork.setNetworkName(rs.getString("NETWORK_NAME"));
    	privateNetwork.setChainId(rs.getInt("CHAIN_ID"));
    	privateNetwork.setIpAddress(rs.getString("IP_ADDRESS"));
    	privateNetwork.setStatus(rs.getString("STATUS"));
    	privateNetwork.setCreatedByUser(rs.getString("CREATED_BY_USER"));
    	privateNetwork.setSmartContractAddress(rs.getString("SMART_CONTRACT_ADDRESS"));
    	privateNetwork.setSmartContractAccessUrl(rs.getString("SMART_CONTRACT_ACCESS_URL"));
    	privateNetwork.setSmartContractDefaultWalletAddress(rs.getString("SMART_CONTRACT_DEFAULT_WALLET_ADDRESS"));
    	privateNetwork.setTenantId(rs.getString("TENANT_ID"));
    	privateNetwork.setIpfsUrl(rs.getString("POLY_IPFS_ACCESS_URL"));
    	privateNetwork.setConsortiaId(rs.getString("CONSORTIA_ID"));
    	privateNetwork.setEnvId(rs.getString("ENV_ID"));
    	privateNetwork.setNetworkAPIUrl(rs.getString("POLY_API_CONSOLE_URL"));
        return privateNetwork;
    }
}
