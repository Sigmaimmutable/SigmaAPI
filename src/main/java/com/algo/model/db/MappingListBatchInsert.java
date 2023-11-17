package com.algo.model.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.algo.model.MappingList;

public class MappingListBatchInsert implements BatchPreparedStatementSetter{
	private static final Logger LOGGER = LoggerFactory.getLogger("com.algo.model.db.MappingListBatchInsert");
	
		  private List<MappingList> mappingList;
		  public MappingListBatchInsert (List<MappingList> mappingList) {
		    super();
		    this.mappingList= mappingList;
		  }
		  @Override
		  public void setValues(PreparedStatement ps, int i){
		    try {
		    	MappingList user = mappingList.get(i);
		    	ps.setString(1, user.getAlgoAddress());
		    	ps.setString(2, user.getType());
		    	ps.setString(3, user.getMappingAddress());		      
		    } catch (SQLException e) {
		    	LOGGER.error("Error MappingListBatchInsert.setValues() profile", mappingList, e);
		    }
		  }
		  @Override
		  public int getBatchSize() {
		    return mappingList.size();
		  }
		}
