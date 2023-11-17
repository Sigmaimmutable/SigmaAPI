package com.algo.model.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import com.algo.model.MappingList;

public class MappingBatchInsertUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger("MappingBatchInsertUtil");
	public void insertMappingList(String type, JdbcTemplate jdbcTemplate, List<String> mapingListToInsert, String algoAddress) {
		if(mapingListToInsert!=null && !mapingListToInsert.isEmpty()) {
			String query = "INSERT INTO ASSET_ADDRESS_MAP "
					+ "(ALGO_ADDRESS, MAPPING_TYPE, MAPPING_ADDRESS) VALUES (?,?,?)";
			List<MappingList> mappingList = new ArrayList<MappingList>();
			for(String oneFollowing : mapingListToInsert) {
				if(rowExists(jdbcTemplate, type, algoAddress, oneFollowing))
					continue;
				MappingList oneMap= new MappingList(type, algoAddress, oneFollowing);
				mappingList.add(oneMap);
			}
			if(!mappingList.isEmpty())
			 jdbcTemplate.batchUpdate(query, new MappingListBatchInsert(mappingList));					
		}
	}
	private boolean rowExists(JdbcTemplate jdbcTemplate, String type, String algoAddress, String oneFollowing) {
		String sql = "SELECT count(1) FROM ASSET_ADDRESS_MAP WHERE ALGO_ADDRESS = ?  "
				+ "AND MAPPING_TYPE = ? AND  MAPPING_ADDRESS =  ?";
		boolean exists = false;
		try {
		int count = jdbcTemplate.queryForObject(sql, new Object[] {algoAddress,  type,  oneFollowing}, Integer.class);
		exists = count > 0;
		}catch(Exception exception) {
			LOGGER.error("MappingBatchInsertUtil.rowExists() type{} algoAddress{} oneFollowing{}", type, algoAddress, 
					oneFollowing, exception);
		}
		return exists;
	}
	public List<String> getMappingList(JdbcTemplate jdbcTemplate, String algoAddress,
			String mappingType) {
		String query = "SELECT MAPPING_ADDRESS FROM ASSET_ADDRESS_MAP WHERE ALGO_ADDRESS = ?"
				+ " AND MAPPING_TYPE = ?";
		List<String> mappingList = jdbcTemplate.queryForList(query, String.class, algoAddress, mappingType);
		return mappingList;
	}
}
