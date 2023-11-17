package com.sigma.affinity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.executors.BatchJobTask;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.db.PrivateNetworkPersistence3;

public class ImmutabilityUtil {
	public ImmutabilityUtil(Integer executorThreads) {
		super();
		this.executorThreads = executorThreads;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.affinity.ImmutabilityUtil");
	private Integer executorThreads = 0;

//	public void fetchAndCreateImmutabilityRecords(JdbcTemplate jdbcTemplate,
//			int batchSize, PrivateNetwork2 networkById, List<SigmaAPIDocConfig> sigmaDocFieldConfigList, String tId,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) throws Exception{
//		try {
//		Integer pendingNftCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM SIGMA_DOCUMENT WHERE NFT_CREATION_STATUS = 0", Integer.class);
//		if(pendingNftCount<=0)
//			return;
//		String DOCU_FETCH_SQL = "SELECT * FROM SIGMA_DOCUMENT WHERE TENANT_ID = '"+tId +"' AND NFT_CREATION_STATUS=0 LIMIT "+batchSize+" OFFSET ";
////		int noOfBatches = Math.round(pendingNftCount / batchSize);
//		int noOfBatches = 0;
//		List<BatchJobTask> tasks = new ArrayList<BatchJobTask>();
//		for(int counter = 0; counter <= noOfBatches; counter++) {
//			Integer startIndex = counter * batchSize;
//			String finalSQL = DOCU_FETCH_SQL + startIndex;
//			BatchJobTask batchJobTask = new BatchJobTask(finalSQL, jdbcTemplate, networkById, sigmaDocFieldConfigList, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
//			tasks.add(batchJobTask);
//			if(counter * noOfBatches > pendingNftCount)
//				break;
//		}
//		if(tasks.isEmpty())
//			return;		
//			ExecutorService executorService = Executors.newFixedThreadPool(executorThreads);
//			List<Future<String>> results = executorService.invokeAll(tasks, 180, TimeUnit.MINUTES);
//			executorService.shutdown();
//		//	System.out.println(results);
//		} catch (Exception e) {
//			LOGGER.error("ImmutabilityUtil.fetchAndCreateImmutabilityRecords()", e);
//			throw e;
//		}
//	}
	
	
	public void fetchAndCreateImmutabilityRecords(JdbcTemplate jdbcTemplate,
			int batchSize, List<SigmaAPIDocConfig> sigmaDocFieldConfigList, String tId,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) throws Exception{
		try {
		Integer pendingNftCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM SIGMA_DOCUMENT WHERE NFT_CREATION_STATUS = 0", Integer.class);
		if(pendingNftCount<=0)
			return;
		String DOCU_FETCH_SQL = "SELECT * FROM SIGMA_DOCUMENT WHERE TENANT_ID = '"+tId +"' AND NFT_CREATION_STATUS=0 LIMIT "+batchSize+" OFFSET ";
//		int noOfBatches = Math.round(pendingNftCount / batchSize);
		int noOfBatches = 0;
		List<BatchJobTask> tasks = new ArrayList<BatchJobTask>();
		for(int counter = 0; counter <= noOfBatches; counter++) {
			Integer startIndex = counter * batchSize;
			String finalSQL = DOCU_FETCH_SQL + startIndex;
			BatchJobTask batchJobTask = new BatchJobTask(finalSQL, jdbcTemplate, sigmaDocFieldConfigList, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
			tasks.add(batchJobTask);
			if(counter * noOfBatches > pendingNftCount)
				break;
		}
		if(tasks.isEmpty())
			return;		
			ExecutorService executorService = Executors.newFixedThreadPool(executorThreads);
			List<Future<String>> results = executorService.invokeAll(tasks, 180, TimeUnit.MINUTES);
			executorService.shutdown();
		//	System.out.println(results);
		} catch (Exception e) {
			LOGGER.error("ImmutabilityUtil.fetchAndCreateImmutabilityRecords()", e);
			throw e;
		}
	}
}
