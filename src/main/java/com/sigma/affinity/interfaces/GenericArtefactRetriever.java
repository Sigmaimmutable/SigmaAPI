package com.sigma.affinity.interfaces;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.affinity.HttpConnector;
import com.sigma.model.DocumentO;
import com.sigma.model.DocumentO2Job;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.SigmaDocument;
import com.sigma.model.db.DocumentOPersistence4;
import com.sigma.model.db.JobOPersistence5;
import com.sigma.model.db.SigmaDocFieldConfigPersistence6;
import com.sigma.model.db.SigmaDocumentPersistence5;
import com.sigma.model.db.SigmaProps;

public interface GenericArtefactRetriever {
	static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.affinity.interfaces.VeevaImplementor");
	public  default String getAuthenticationToken(SigmaProps props, String propName) throws Exception{
		return postAuthenticationToken(props, propName);
	}
	public  default String postAuthenticationToken(SigmaProps props, String propName) throws Exception{
		new HttpConnector("").skipTrustCertificates();
		URL obj = new URL(props.getAuthUrl());
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes("username="+props.getExtUser()+"&password="+props.getExtPwd());
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		if(responseCode!=200)
			return null;
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		JSONObject sessionInfo = new JSONObject(response.toString());
		String sessionId = sessionInfo.optString(propName);
		return sessionId;
	}
	public  default List<SigmaDocument> getGetDocumentInfo() {
		return null;
	}
	public  default String postGetDocumentInfo() {
		return "";
	}
	public default List<SigmaDocument> findLatestDocuments(SigmaProps props, JdbcTemplate jdbcTemplate,
			List<SigmaAPIDocConfig> sigmaDocFieldConfigList, String jobType) {
		HttpConnector affinity = new HttpConnector(props.getAppUrl());
		try {
					JobOPersistence5 jobOPersistence5 = new JobOPersistence5();

					String latestExecTime = jobOPersistence5.getLatestExecTime(jdbcTemplate, props.getTenantId());
					JSONObject jsonBodyForLatestDocuments = getJSONBodyForLatestDocuments(latestExecTime);
					Map<String, String> requestParams = new HashMap<String, String>();
					requestParams.put("Authorization", props.getSessionId());
					requestParams.put("X-VaultAPI-DescribeQuery", "true");
					requestParams.put("Accept", "application/json");
					requestParams.put("Content-Type", "application/x-www-form-urlencoded");
					String query = jsonBodyForLatestDocuments.getString("q");
					query = "q="+query;
					LOGGER.info("HttpURLConnectionUtil.findLatestDocuments() before fetchLatestDocuments() query{ "+query+" }");
					List<SigmaDocument> fetchLatestDocuments = fetchLatestDocuments(affinity, requestParams, query, jdbcTemplate, sigmaDocFieldConfigList, props, jobType);
					LOGGER.info("HttpURLConnectionUtil.findLatestDocuments() after fetchLatestDocuments()");
					return fetchLatestDocuments;
			
		} catch (Exception e) {
			LOGGER.error("HttpURLConnectionUtil.findLatestDocuments()", e);
			return new ArrayList<SigmaDocument>();
		}
	}
	
	default List<SigmaDocument> fetchLatestDocuments(HttpConnector affinity, 
			Map<String, String> requestParams, String query, JdbcTemplate jdbcTemplate, 
			List<SigmaAPIDocConfig> sigmaDocFieldConfigList, 
			SigmaProps props,String jobType) throws Exception, JsonProcessingException, JsonMappingException {
		String nextPageString = "";
		String latestDocumentDate = null;
		try {
			LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before while");
			List<SigmaDocument> allDocs = new ArrayList<SigmaDocument>();
			boolean flag = false;
		while(true) {
				String urlToFetch = props.getAppUrl() + nextPageString;
				LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() before fetch { "+urlToFetch+" }");
				JSONObject rawTxJson = affinity.invokePostAndGetJson(urlToFetch, query, requestParams,"");
				JSONArray jsonArray = rawTxJson.optJSONArray("data");
				if(jsonArray == null || jsonArray.length() == 0) {
					LOGGER.info("DocumentRetrieve.fetchLatestDocuments() breaking the while as data is empty");
					break;
				}
				if(flag==true)
					break;
				else
					flag = true;
				JSONObject responseDetailsJson = rawTxJson.getJSONObject("responseDetails");
				nextPageString = responseDetailsJson.optString("next_page", "");
				nextPageString = nextPageString.replaceAll("/api/v22.3/query", "");
				for(int counter = 0; counter<jsonArray.length(); counter++) {
					JSONObject oneDocument = jsonArray.getJSONObject(counter);
					JSONObject currentTargetObject = new JSONObject();
					
					for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigList) {
						String extField = sigmaAPIDocConfig.getExtField();
						String sigmaField = sigmaAPIDocConfig.getSigmaField();
						currentTargetObject.put(sigmaField, oneDocument.optString(extField));
						}
					ObjectMapper mapper = new ObjectMapper();
					SigmaDocument parsedDoc = mapper.readValue(currentTargetObject.toString(), SigmaDocument.class);
					
					if(latestDocumentDate==null)
						latestDocumentDate = oneDocument.optString("file_modified_date__v");
					allDocs.add(parsedDoc);
				}

				if(nextPageString == null || nextPageString.trim().isEmpty()) {
					LOGGER.info("Exiting fetch url " + nextPageString + " \n responseDetailsJson" + responseDetailsJson);
					break;	
				}else {
					LOGGER.info("Going to next page with url "+nextPageString);					
				}				
		}
		LOGGER.info("HttpURLConnectionUtil.fetchLatestDocuments() after while");
		return allDocs;
		}catch(Exception exception) {
			LOGGER.error("Error HttpURLConnectionUtil.fetchLatestDocuments()", exception);
			String stackTrace = exception.getMessage();
			if(stackTrace.length()>=4000)
				stackTrace = stackTrace.substring(0, 3999);
			updateJobStatus(0, "N", latestDocumentDate, stackTrace, jdbcTemplate, jobType);
			return  new ArrayList<SigmaDocument>();
		}
	}
	default void updateJobStatus(int noOfRecords, String jobRunStatus, String latestDocumentDate2, String stackTrace, JdbcTemplate jdbcTemplate, String jobType) {
		JobOPersistence5 persistence = new JobOPersistence5();
		DocumentO2Job job = new DocumentO2Job();
		job.setCompanyCode("Veeva");
		job.setErrorSummary(stackTrace);
		job.setJobName("Doc retrieve job");
		job.setJobRunByUser("SIGMA_ADMIN");
		job.setNoOfRecordsProcessed(noOfRecords);
		job.setStatus(jobRunStatus);
		job.setLatestDocumentDate(latestDocumentDate2);
		persistence.generateJobStatus(job, jdbcTemplate, jobType);
	}
	default JSONObject getJSONBodyForLatestDocuments(String previousExecDateTime) {
		JSONObject jsonObject = new JSONObject();
		
		if(previousExecDateTime==null || previousExecDateTime.trim().isEmpty())
			jsonObject.put("q", "select id, global_id__sys, filename__v,id,version_id,source_owner__v,"
					+ "source_document_number__v,application__v," //primary_author__c,
					+ "name__v,type__v,title__v,manufacturer__v,"
					+ "source_vault_id__v,created_by__v,region__v,status__v," //applications__v,
					+ "document_date__c,binder_last_autofiled_date__v,archived_date__sys,"
					+ "file_modified_date__v,file_created_date__v,document_creation_date__v "
					//+ "from documents where file_created_date__v >= '2021-12-14T14:11:26.000Z' "
					+ "from documents "
					+ "order by file_created_date__v  asc");
		else		
			jsonObject.put("q", "select id, global_id__sys, filename__v,id,version_id,source_owner__v,"
				+ "source_document_number__v,application__v," //primary_author__c,
				+ "name__v,type__v,title__v,manufacturer__v,"
				+ "source_vault_id__v,created_by__v,region__v,status__v," //applications__v,
				+ "document_date__c,binder_last_autofiled_date__v,archived_date__sys,"
				+ "file_modified_date__v,file_created_date__v,document_creation_date__v "
				//+ "from documents where file_created_date__v >= '2021-12-14T14:11:26.000Z' "
				+ "from documents where file_created_date__v > '"+previousExecDateTime+"'"
				+ "order by file_created_date__v  asc");
		return jsonObject;
	}
}