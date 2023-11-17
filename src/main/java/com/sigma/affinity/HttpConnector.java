package com.sigma.affinity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnector {
	private String url;
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.aws.affinity.HttpConnector");
	public HttpConnector(String url) {
		super();
		this.url = url;
	}
	public JSONObject invokePostAndGetJson(String postJsonData) throws Exception {
		return invokePostAndGetJson(postJsonData,"");
	}
	public JSONObject invokePostAndGetJson(String postJsonData, String bearerToken) throws Exception {
		Map<String, String> requestProperties = new HashMap<>();
		return invokePostAndGetJson(postJsonData, requestProperties, bearerToken);
	}
	public JSONObject invokePostAndGetJson(String postJsonData, 
			Map<String, String> requestProperties, String bearerToken) throws Exception {
		return invokePostAndGetJson(null, postJsonData, requestProperties, bearerToken);
	}
	public JSONObject invokePostAndGetJson(String resourceUrl, String postJsonData, 
			Map<String, String> requestProperties, String bearerToken) throws Exception {
		return invokePostAndGetJson(resourceUrl, postJsonData, 
				requestProperties, bearerToken, false);
	}
	public JSONObject invokePostAndGetJson(String resourceUrl, String postJsonData, 
			Map<String, String> requestProperties, String bearerToken, boolean isBasicToken) throws Exception {
		 JSONObject defaultJson = new JSONObject();
		try {
		  String targetUrl = resourceUrl !=null ? resourceUrl: url;
		  URL obj = new URL(targetUrl);
		  HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		  skipTrustCertificates();
		  con.setRequestMethod("POST");
		  if(!isBasicToken)
			  con.setRequestProperty("Authorization", "Bearer "+bearerToken);
		  else
			  con.setRequestProperty("Authorization", "Basic "+bearerToken);
		  setRequestParams(con, requestProperties);
		  con.setDoOutput(true);
		  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		  wr.writeBytes(postJsonData.toString());
		  wr.flush();
		  wr.close();
		  int responseCode = con.getResponseCode();
		  LOGGER.info("POST with url { "+targetUrl+" }, isBasicToken="+isBasicToken 
				  +"bearerToken="+bearerToken+"}, postJsonData={"+postJsonData+"}, responseCode = {"+responseCode +"}");//tr
		  String readResponseFromConnection = null;
		 if(responseCode >=200 && responseCode<300) {
			readResponseFromConnection = readResponseFromConnection(con);
		} else if(responseCode >=500 && responseCode<600 ){
			readErrorStream(con);
		 }	 
		 else {
			 readErrorStream(con);
			 throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
		 }
		 if(readResponseFromConnection == null || readResponseFromConnection.trim().isEmpty())
			 return defaultJson;
		 else{
			 JSONObject responseFromNode= new JSONObject(readResponseFromConnection);
			 return responseFromNode;
		 }
		}catch(Exception exception) {
			LOGGER.error("HttpURLConnectionUtil.sendingPostRequest()"
					+ "data {}", postJsonData, exception);
			return defaultJson;
		}
		}
	private void setRequestParams(HttpURLConnection con, Map<String, String> requestProperties) {
		  //con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type","application/json");
		 // con.setRequestProperty("Authorization","Bearer "+bearerToken);
			if(requestProperties == null)
				return;
		 for(Entry<String, String> entry: requestProperties.entrySet()) {
			 String key2 = entry.getKey();
			 String value2 = entry.getValue();
			 con.setRequestProperty(key2, value2);
		 }
	}
	public void skipTrustCertificates() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        try {
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }catch(Exception exception) {
        	LOGGER.error("Error installing trusted anchors", exception);
        }
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
 		
	}
	private void readErrorStream(HttpURLConnection connection) {
		BufferedReader reader = null;
		try {
		  reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		  String line;
		  StringBuilder builder = new StringBuilder();		  
		  while ((line = reader.readLine()) != null) {
		    //System.err.println(line);
		    builder.append(line);
		  }
		  LOGGER.error("Error HttpConnector.readErrorStream() 1" +builder);
		} catch (IOException e) {
		//  System.err.println("Error reading error stream: " + e.getMessage());
		  LOGGER.error("Error HttpConnector.readErrorStream() 2", e);
		} finally {
		  if (reader != null) {
		    try {
		      reader.close();
		    } catch (IOException e) {
		      System.err.println("Error closing error stream: " + e.getMessage());
		      LOGGER.error("Error HttpConnector.readErrorStream() 3", e);
		    }
		  }
		}

	}
	private String readResponseFromConnection(HttpURLConnection con) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String output;
			  StringBuffer response = new StringBuffer();			 
			  while ((output = in.readLine()) != null) {
			   response.append(output);
			  }
			  in.close();
			  return response.toString();
		}
		catch(Exception exception) {
			LOGGER.error("HttpURLConnectionUtil.readResponseFromConnection()"
					+ "con {}", con, exception);
			return null;
		}
	}
	public JSONObject getBlockChainDataUsingPostFromFile(String path) throws Exception {
		File file = new File(path);
		String content = FileUtils.readFileToString(file, "UTF-8");
		JSONObject jsonData = new JSONObject(content);
		return jsonData;
	}
	public String invokeGet(String bearerToken) {
		return invokeGet(bearerToken, "Bearer ");
	}
	public String invokeGet(String bearerToken, String authString) {
		String defaultJson = new String();
        try {
            URL url1 = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("GET");
            String auth = authString +bearerToken;
            con.setRequestProperty("Authorization", auth);
            int responseCode = con.getResponseCode();
  		  	String readResponseFromConnection = null;
  		 if(responseCode >=200 && responseCode<300) {
  			readResponseFromConnection = readResponseFromConnection(con);
  		} else
  			 throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
  		 if(readResponseFromConnection == null || readResponseFromConnection.trim().isEmpty())
  			 return defaultJson;
  		 else{ 
            return readResponseFromConnection;
  		 }
        } catch (Exception e) {
            LOGGER.error("HttpConnector.invokeGet()",e);
            return defaultJson;
        }
    }
	}

