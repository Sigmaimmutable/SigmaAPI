package com.sigma.executors;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.affinity.PolygonEdgeUtil;
import com.sigma.model.DocumentO;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.SigmaDocument;
import com.sigma.model.db.DocumentOPersistence4;
import com.sigma.model.db.SigmaDocumentPersistence5;

public class BatchJobTask implements Callable<String> {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.executors.BatchJobTask");
	  private String workSql;
	  private JdbcTemplate jdbcTemplate;
	  private PrivateNetwork2 privateNetwork2;
	  private List<SigmaAPIDocConfig> sigmaDocFieldConfigList;
	  private String infuraurl;
      private String contractaddress;
      private String privatekey;
      private int chainid;
      private BigInteger gasprice;
      private String nonceapiurl;

//		public BatchJobTask(String workSql, JdbcTemplate jdbcTemplate, PrivateNetwork2 privateNetwork2,
//			List<SigmaAPIDocConfig> sigmaDocFieldConfigList,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
//		super();
//		this.workSql = workSql;
//		this.jdbcTemplate = jdbcTemplate;
//		this.privateNetwork2 = privateNetwork2;
//		this.sigmaDocFieldConfigList = sigmaDocFieldConfigList;
//		this.infuraurl = infuraUrl;
//		this.contractaddress=contractAddress;
//		this.privatekey=privateKey;
//		this.chainid=chainId;
//		this.gasprice=gasPrice;
//		this.nonceapiurl=nonceApiUrl;
//		
//	}
      public BatchJobTask(String workSql, JdbcTemplate jdbcTemplate, 
  			List<SigmaAPIDocConfig> sigmaDocFieldConfigList,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
  		super();
  		this.workSql = workSql;
  		this.jdbcTemplate = jdbcTemplate;
  		this.privateNetwork2 = privateNetwork2;
  		this.sigmaDocFieldConfigList = sigmaDocFieldConfigList;
  		this.infuraurl = infuraUrl;
  		this.contractaddress=contractAddress;
  		this.privatekey=privateKey;
  		this.chainid=chainId;
  		this.gasprice=gasPrice;
  		this.nonceapiurl=nonceApiUrl;
  		
  	}

//		@Override
//	    public String call() throws Exception {
//		  try {
//			  	LOGGER.info("Thread {"+ Thread.currentThread()+"} printing started privateNetwork2" + privateNetwork2);
//			  	SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
//			  	List<SigmaDocument> pendingDocumentsBySQL = sigmaDocumentPersistence5.getPendingDocumentsBySQL(jdbcTemplate, workSql);
//			  	for(SigmaDocument documentO : pendingDocumentsBySQL) {
//			  		PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
//			  		JSONObject nftInfo = polygonEdgeUtil.mintNftForDocument(privateNetwork2, documentO, sigmaDocFieldConfigList, infuraurl, contractaddress, privatekey, chainid, gasprice,nonceapiurl);
//			  		documentO.setUuid(nftInfo.optString("uuid"));
//			  		documentO.setNftCreationStatus(1);
//			  		LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+documentO.getSigmaId()+
//			  				", uuid => "+nftInfo.optString("uuid","Error"));
//			  		sigmaDocumentPersistence5.updateImmutableRecord(documentO, jdbcTemplate);
//			  		LOGGER.info("Thread {"+ Thread.currentThread()+"} waiting for next txn ");
//			  		
//			  	}
//			  	LOGGER.info("Thread {"+ Thread.currentThread()+"} printing completed ");
//			  return "Success";
//		} catch (Exception e) {
//			LOGGER.error("BatchJobTask.call() workSql{}", workSql, e);
//			return "Failure";
//		}
//	  }
      



@Override
	    public String call() throws Exception {
		  try {
//			  	LOGGER.info("Thread {"+ Thread.currentThread()+"} printing started privateNetwork2" + privateNetwork2);
			  	SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
			  	List<SigmaDocument> pendingDocumentsBySQL = sigmaDocumentPersistence5.getPendingDocumentsBySQL(jdbcTemplate, workSql);
			  	for(SigmaDocument documentO : pendingDocumentsBySQL) {
			  		PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
			  		JSONObject nftInfo = polygonEdgeUtil.mintNftForDocument(documentO, sigmaDocFieldConfigList, infuraurl, contractaddress, privatekey, chainid, gasprice,nonceapiurl);
			  		documentO.setUuid(nftInfo.optString("uuid"));
			  		documentO.setNftCreationStatus(1);
			  		LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+documentO.getSigmaId()+
			  				", uuid => "+nftInfo.optString("uuid","Error"));
			  		sigmaDocumentPersistence5.updateImmutableRecord(documentO, jdbcTemplate);
			  		LOGGER.info("Thread {"+ Thread.currentThread()+"} waiting for next txn ");
			  		
			  	}
			  	LOGGER.info("Thread {"+ Thread.currentThread()+"} printing completed ");
			  return "Success";
		} catch (Exception e) {
			LOGGER.error("BatchJobTask.call() workSql{}", workSql, e);
			return "Failure";
		}
	  }
	}

