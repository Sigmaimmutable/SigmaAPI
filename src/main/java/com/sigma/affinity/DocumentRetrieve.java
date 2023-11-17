package com.sigma.affinity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sigma.affinity.interfaces.GenericArtefactRetriever;
import com.sigma.affinity.interfaces.VeevaImplementor;
import com.sigma.model.DocumentO2Job;
import com.sigma.model.Organization;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.SigmaDocument;
import com.sigma.model.db.JobOPersistence5;
import com.sigma.model.db.SigmaDocumentPersistence5;
import com.sigma.model.db.SigmaProps;
import com.sigma.model.db.UserInfoPersistence;

public class DocumentRetrieve {	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.affinity.DocumentRetrieve");
	public void findLatestDocuments(SigmaProps props, JdbcTemplate jdbcTemplate,
			Organization org, List<SigmaAPIDocConfig> sigmaDocFieldConfigList, PrivateNetwork2 networkById, String jobType) throws Exception, JsonProcessingException, JsonMappingException {
		String latestDocumentDate = null;
		Integer noOfDocuments = 0;
		Long jobId = 0l;
		try {
//			UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//			List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, org.getTenantId(), 5, "23/07/2023");
			jobId = updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, org, true, 0l, "DOC_FETCH",jobType);
			LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before while");
			List<SigmaDocument> docs = new ArrayList<SigmaDocument>();
			GenericArtefactRetriever retriever = new VeevaImplementor();
			String sessionId = retriever.postAuthenticationToken(props, "sessionId");
			props.setSessionId(sessionId);
			docs = retriever.findLatestDocuments(props, jdbcTemplate,sigmaDocFieldConfigList,jobType);
			SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
			int testLimit = 100;
			String docupdated_time=null;
			for(SigmaDocument document : docs) {
				if(document.getfVar7()!=null||!document.getfVar7().isEmpty()) {
					document.setTenantId(props.getTenantId());
					document.setCreatedBy(org.getCreatedBy());
					document.setJobId(jobId);
					document.setNftCreationStatus(0);
					if(props.getIpfsEnabled()) {
						InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
						JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSFile(document.getfVar6(), document.getfVar1(),
								sessionId, networkById, props);
						String hash = ipfsInfo.optString("createIRec");
						document.setDocChecksum(hash);		
					
					    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
					    document.setMd5Checksum(md5Checksum);
					}
					sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
					noOfDocuments ++;
					docupdated_time=document.getfVar7();
					if(noOfDocuments > testLimit)
						break;	
				}
				else {
					noOfDocuments ++;
				}
				
//				LOGGER.info("Persisted the doc {"+document.getId()+"}");						
			}
		latestDocumentDate=docupdated_time;

		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after while noOfDocuments {}"+noOfDocuments, noOfDocuments);
//		if(latestDocumentDate == null || latestDocumentDate.trim().isEmpty()) {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//			Date date = new Date();
//			String formattedStringData = dateFormat.format(date);
//			latestDocumentDate = formattedStringData;
//		}
		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before updateJobStatus ",latestDocumentDate);
		updateJobStatus(noOfDocuments, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, org, false, jobId, "DOC_FETCH",jobType);
		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after updateJobStatus ", latestDocumentDate);
		UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
		List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, org.getTenantId(), noOfDocuments, latestDocumentDate);

//		System.out.print("Hello, ");
//		for (String emailId : emailIds) {
//		    System.out.printf("%s ", emailId);
//		}
		}catch(Exception exception) {
			LOGGER.error("Error HttpURLConnectionUtil.fetchLatestDocuments()", exception);
			String stackTrace = exception.getMessage();
			if(stackTrace.length()>=4000)
				stackTrace = stackTrace.substring(0, 3999);
			updateJobStatus(0, "N", latestDocumentDate, stackTrace, jdbcTemplate, org, false, jobId, "DOC_FETCH",jobType) ;
		}
	}
	
//	public void findLatestDocumentsPrivate(SigmaProps props, JdbcTemplate jdbcTemplate,
//			Organization org, List<SigmaAPIDocConfig> sigmaDocFieldConfigList, PrivateNetwork2 networkById, String jobType, String ipfsUrl) throws Exception, JsonProcessingException, JsonMappingException {
//		String latestDocumentDate = null;
//		Integer noOfDocuments = 0;
//		Long jobId = 0l;
//		try {
////			UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
////			List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, org.getTenantId(), 5, "23/07/2023");
//			jobId = updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, org, true, 0l, "DOC_FETCH",jobType);
//			LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before while");
//			List<SigmaDocument> docs = new ArrayList<SigmaDocument>();
//			GenericArtefactRetriever retriever = new VeevaImplementor();
//			String sessionId = retriever.postAuthenticationToken(props, "sessionId");
//			props.setSessionId(sessionId);
//			docs = retriever.findLatestDocuments(props, jdbcTemplate,sigmaDocFieldConfigList,jobType);
//			SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
//			int testLimit = 100;
//			String docupdated_time=null;
//			for(SigmaDocument document : docs) {
//				if(document.getfVar7()!=null||!document.getfVar7().isEmpty()) {
//					document.setTenantId(props.getTenantId());
//					document.setCreatedBy(org.getCreatedBy());
//					document.setJobId(jobId);
//					document.setNftCreationStatus(0);
//					if(props.getIpfsEnabled()) {
//						InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
//						JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSFilePrivate(document.getfVar6(), document.getfVar1(),
//								sessionId, networkById, props,ipfsUrl);
//						String hash = ipfsInfo.optString("createIRec");
//						document.setDocChecksum(hash);		
//					
//					    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
//					    document.setMd5Checksum(md5Checksum);
//					}
//					sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
//					noOfDocuments ++;
//					docupdated_time=document.getfVar7();
//					if(noOfDocuments > testLimit)
//						break;	
//				}
//				else {
//					noOfDocuments ++;
//				}
//				
////				LOGGER.info("Persisted the doc {"+document.getId()+"}");						
//			}
//		latestDocumentDate=docupdated_time;
//
//		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after while noOfDocuments {}"+noOfDocuments, noOfDocuments);
////		if(latestDocumentDate == null || latestDocumentDate.trim().isEmpty()) {
////			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
////			Date date = new Date();
////			String formattedStringData = dateFormat.format(date);
////			latestDocumentDate = formattedStringData;
////		}
//		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before updateJobStatus ",latestDocumentDate);
//		updateJobStatus(noOfDocuments, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, org, false, jobId, "DOC_FETCH",jobType);
//		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after updateJobStatus ", latestDocumentDate);
//		UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//		List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, org.getTenantId(), noOfDocuments, latestDocumentDate);
//
////		System.out.print("Hello, ");
////		for (String emailId : emailIds) {
////		    System.out.printf("%s ", emailId);
////		}
//		}catch(Exception exception) {
//			LOGGER.error("Error HttpURLConnectionUtil.fetchLatestDocuments()", exception);
//			String stackTrace = exception.getMessage();
//			if(stackTrace.length()>=4000)
//				stackTrace = stackTrace.substring(0, 3999);
//			updateJobStatus(0, "N", latestDocumentDate, stackTrace, jdbcTemplate, org, false, jobId, "DOC_FETCH",jobType) ;
//		}
//	}
	public void findLatestDocumentsPrivate(SigmaProps props, JdbcTemplate jdbcTemplate,
			Organization org, List<SigmaAPIDocConfig> sigmaDocFieldConfigList, String jobType, String ipfsUrl,String ec2IP1, String ec2IP2, String ec2IP3) throws Exception, JsonProcessingException, JsonMappingException {
		String latestDocumentDate = null;
		Integer noOfDocuments = 0;
		Long jobId = 0l;
		try {
//			UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//			List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, org.getTenantId(), 5, "23/07/2023");
			jobId = updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, org, true, 0l, "DOC_FETCH",jobType);
			LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before while");
			List<SigmaDocument> docs = new ArrayList<SigmaDocument>();
			GenericArtefactRetriever retriever = new VeevaImplementor();
			String sessionId = retriever.postAuthenticationToken(props, "sessionId");
			props.setSessionId(sessionId);
			docs = retriever.findLatestDocuments(props, jdbcTemplate,sigmaDocFieldConfigList,jobType);
			SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
			int testLimit = 100;
			String docupdated_time=null;
			for(SigmaDocument document : docs) {
				if(document.getfVar7()!=null||!document.getfVar7().isEmpty()) {
					document.setTenantId(props.getTenantId());
					document.setCreatedBy(org.getCreatedBy());
					document.setJobId(jobId);
					document.setNftCreationStatus(0);
					if(props.getIpfsEnabled()) {
						InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
						JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSFilePrivate(document.getfVar6(), document.getfVar1(),
								sessionId, props,ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
						String hash = ipfsInfo.optString("createIRec");
						document.setDocChecksum(hash);		
					
					    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
					    document.setMd5Checksum(md5Checksum);
					}
					sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
					noOfDocuments ++;
					docupdated_time=document.getfVar7();
					if(noOfDocuments > testLimit)
						break;	
				}
				else {
					noOfDocuments ++;
				}
				
//				LOGGER.info("Persisted the doc {"+document.getId()+"}");						
			}
		latestDocumentDate=docupdated_time;

		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after while noOfDocuments {}"+noOfDocuments, noOfDocuments);
//		if(latestDocumentDate == null || latestDocumentDate.trim().isEmpty()) {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//			Date date = new Date();
//			String formattedStringData = dateFormat.format(date);
//			latestDocumentDate = formattedStringData;
//		}
		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before updateJobStatus ",latestDocumentDate);
		updateJobStatus(noOfDocuments, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, org, false, jobId, "DOC_FETCH",jobType);
		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after updateJobStatus ", latestDocumentDate);
		UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
		List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, org.getTenantId(), noOfDocuments, latestDocumentDate);

//		System.out.print("Hello, ");
//		for (String emailId : emailIds) {
//		    System.out.printf("%s ", emailId);
//		}
		}catch(Exception exception) {
			LOGGER.error("Error HttpURLConnectionUtil.fetchLatestDocuments()", exception);
			String stackTrace = exception.getMessage();
			if(stackTrace.length()>=4000)
				stackTrace = stackTrace.substring(0, 3999);
			updateJobStatus(0, "N", latestDocumentDate, stackTrace, jdbcTemplate, org, false, jobId, "DOC_FETCH",jobType) ;
		}
	}

	public Long updateJobStatus(int noOfRecords, String jobRunStatus, 
			String latestDocumentDate2, String stackTrace, JdbcTemplate jdbcTemplate, 
			Organization org, boolean insertFlag, Long jobId, String jobName, String jobType) {
		Long generateJobStatus = 0l;
		JobOPersistence5 persistence = new JobOPersistence5();
		DocumentO2Job job = new DocumentO2Job();
		if(insertFlag) {
			job.setCompanyCode(org.getName());
			job.setJobRunByUser(org.getCreatedBy()+"_ADMIN");
			job.setJobName(jobName);
			job.setTenantId(org.getTenantId());
			job.setStatus("P");
			generateJobStatus = persistence.generateJobStatus(job, jdbcTemplate, jobType);
		}
		else {
			job.setId(jobId);		
			job.setErrorSummary(stackTrace);
			job.setNoOfRecordsProcessed(noOfRecords);
			job.setStatus(jobRunStatus);
			job.setLatestDocumentDate(latestDocumentDate2);
			persistence.updateJobStatus(job, jdbcTemplate);
		}
		return generateJobStatus;
	}
	
}