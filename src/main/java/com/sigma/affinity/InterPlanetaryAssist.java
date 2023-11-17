package com.sigma.affinity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.sigma.model.PrivateNetwork2;
import com.sigma.model.db.SigmaProps;

public class InterPlanetaryAssist {
	/*
	public static void main(String[] args) {
		HttpConnector connector = new HttpConnector("");
		connector.skipTrustCertificates();
		try {
			String sessionId = "100683FD48ACCDA6FA995DB2AB4922E100B80B0D50A68D74DA5FEEE78EE335368CA9D76E03C013FD5B2DDB238CB10A4B04DE368778BF44941509673C2DF85F8B";
			new InterPlanetaryAssist().getAndPersistIPFSFile(535+"", "Test_IPFS2.pdf", sessionId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	private String calculateMD5Checksum(byte[] data) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
//	        byte[] buffer = new byte[4096];
//	        int bytesRead;
//	        
//	        while ((bytesRead = inputStream.read(buffer)) != -1) {
//	            md.update(buffer, 0, bytesRead);
//	        }
	        
	        byte[] hashBytes = md.digest(data);

	        // Convert the byte array to a hexadecimal representation
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hashBytes) {
	            hexString.append(String.format("%02x", b));
	        }
	        return hexString.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

public JSONObject getAndPersistIPFSFile(String docId, String fileName, String sessionId, 
		PrivateNetwork2 networkById, SigmaProps props) throws Exception{ //v tr
	
	
	new HttpConnector(null).skipTrustCertificates(); 
//    URL obj = new URL(props.getExtFileUrl() +"/"+ docId + "/file"); // v
	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Authorization", sessionId);
    con.setRequestProperty("Accept", "application/json");
    int responseCode = con.getResponseCode();
    JSONObject createIRec = null;
    CrateIPFS crateIPFS = new CrateIPFS();
    JSONObject result = new JSONObject();
    InputStream inputStream = null;
//    InputStream inputStream2 = null;
  	 if(responseCode >=200 && responseCode<300) {
  	    //commented code for instant file write to a pdf file in local disk
  	    /*
  	    FileOutputStream outputStream = new FileOutputStream("D:\\Examples\\Sigma_Ent\\files\\int1.pdf");
  	    byte[] buffer = new byte[4096];
  	    int bytesRead = -1;
  	    while ((bytesRead = inputStream.read(buffer)) != -1) {
  	        outputStream.write(buffer, 0, bytesRead);
  	    }
  	    outputStream.close();
  	*/
  	    inputStream = con.getInputStream();
  	// Read the entire stream into a byte array
  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  	  byte[] buffer = new byte[4096];
  	  int bytesRead;
  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
  	  }
  	  byte[] data = byteArrayOutputStream.toByteArray();

  	  // Calculate MD5 checksum on data
  	  String md5Checksum = calculateMD5Checksum(data);

  	  // Use data for createIRec
  	  createIRec = crateIPFS.createIRec(new ByteArrayInputStream(data), fileName, networkById, sessionId);
  	    
  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
  	    result.put("createIRec", createIRec.optString("Hash"));
  	    
  	  // Calculate the MD5 checksum of the document content
//	    String documentContent = oneDocument.optString(inputStream); // Change to the actual field name
  	    
//  	    inputStream2 = con.getInputStream();
	    //String md5Checksum = calculateMD5Checksum(inputStream);
  	   
        result.put("md5Checksum", md5Checksum);
	} else if(responseCode >=300 && responseCode<500 ){
		crateIPFS.readErrorStream(con);
	 }	 
	 else
		throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
  	 inputStream.close();
  	return result;
}
//public JSONObject getAndPersistIPFSsingleFile(String docId, String fileName, 
//		PrivateNetwork2 networkById,MultipartFile file,String ipfsUrl ) throws Exception{ //v tr
//	
//	
//	new HttpConnector(null).skipTrustCertificates(); 
//
////	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v
//
//    JSONObject createIRec = null;
//    CrateIPFS crateIPFS = new CrateIPFS();
//    JSONObject result = new JSONObject();
//
//  	  try (InputStream inputStream = file.getInputStream()) {
//  	// Read the entire stream into a byte array
//  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//  	  byte[] buffer = new byte[4096];
//  	  int bytesRead;
//  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
//  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
//  	  }
//  	  byte[] data = byteArrayOutputStream.toByteArray();
//
//  	  // Calculate MD5 checksum on data
//  	  String md5Checksum = calculateMD5Checksum(data);
//
//  	  // Use data for createIRec
//  	  createIRec = crateIPFS.createIRecSigma(new ByteArrayInputStream(data), fileName, networkById, ipfsUrl);
//  	    
//  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
//  	    result.put("createIRec", createIRec.optString("cid"));
//  	    
//
//  	   
//        result.put("md5Checksum", md5Checksum);
//  	  }catch (Exception e) {
//          // Handle other exceptions here
//          throw new Exception("An error occurred: " + e.getMessage());
//      }
////		throw new Exception ("Error response code from web3 responseCode {}");
//		return result;
//  	  
//}
public JSONObject getAndPersistIPFSsingleFile(String docId, String fileName, 
		MultipartFile file,String ipfsUrl,String ec2IP1, String ec2IP2, String ec2IP3 ) throws Exception{ //v tr
	
	
	new HttpConnector(null).skipTrustCertificates(); 

//	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v

    JSONObject createIRec = null;
    CrateIPFS crateIPFS = new CrateIPFS();
    JSONObject result = new JSONObject();

  	  try (InputStream inputStream = file.getInputStream()) {
  	// Read the entire stream into a byte array
  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  	  byte[] buffer = new byte[4096];
  	  int bytesRead;
  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
  	  }
  	  byte[] data = byteArrayOutputStream.toByteArray();

  	  // Calculate MD5 checksum on data
  	  String md5Checksum = calculateMD5Checksum(data);

  	  // Use data for createIRec
  	  createIRec = crateIPFS.createIRecSigma(new ByteArrayInputStream(data), fileName, ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
  	    
  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
  	    result.put("createIRec", createIRec.optString("cid"));
  	    

  	   
        result.put("md5Checksum", md5Checksum);
  	  }catch (Exception e) {
          // Handle other exceptions here
          throw new Exception("An error occurred: " + e.getMessage());
      }
//		throw new Exception ("Error response code from web3 responseCode {}");
		return result;
  	  
}


//public JSONObject getAndPersistIPFSFilePrivate(String docId, String fileName, String sessionId, 
//		PrivateNetwork2 networkById, SigmaProps props, String ipfsUrl) throws Exception{ //v tr
//	
//	
//	new HttpConnector(null).skipTrustCertificates(); 
////    URL obj = new URL(props.getExtFileUrl() +"/"+ docId + "/file"); // v
//	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v
//    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//    con.setRequestMethod("GET");
//    con.setRequestProperty("Authorization", sessionId);
//    con.setRequestProperty("Accept", "application/json");
//    int responseCode = con.getResponseCode();
//    JSONObject createIRec = null;
//    CrateIPFS crateIPFS = new CrateIPFS();
//    JSONObject result = new JSONObject();
//    InputStream inputStream = null;
////    InputStream inputStream2 = null;
//  	 if(responseCode >=200 && responseCode<300) {
//  	    //commented code for instant file write to a pdf file in local disk
//  	    /*
//  	    FileOutputStream outputStream = new FileOutputStream("D:\\Examples\\Sigma_Ent\\files\\int1.pdf");
//  	    byte[] buffer = new byte[4096];
//  	    int bytesRead = -1;
//  	    while ((bytesRead = inputStream.read(buffer)) != -1) {
//  	        outputStream.write(buffer, 0, bytesRead);
//  	    }
//  	    outputStream.close();
//  	*/
//  	    inputStream = con.getInputStream();
//  	// Read the entire stream into a byte array
//  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//  	  byte[] buffer = new byte[4096];
//  	  int bytesRead;
//  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
//  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
//  	  }
//  	  byte[] data = byteArrayOutputStream.toByteArray();
//
//  	  // Calculate MD5 checksum on data
//  	  String md5Checksum = calculateMD5Checksum(data);
//
//  	  // Use data for createIRec
//  	  createIRec = crateIPFS.createIRecPrivate(new ByteArrayInputStream(data), fileName, networkById, sessionId, ipfsUrl);
//  	    
//  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
//  	    result.put("createIRec", createIRec.optString("cid"));
//  	    
//  	  // Calculate the MD5 checksum of the document content
////	    String documentContent = oneDocument.optString(inputStream); // Change to the actual field name
//  	    
////  	    inputStream2 = con.getInputStream();
//	    //String md5Checksum = calculateMD5Checksum(inputStream);
//  	   
//        result.put("md5Checksum", md5Checksum);
//	} else if(responseCode >=300 && responseCode<500 ){
//		crateIPFS.readErrorStream(con);
//	 }	 
//	 else
//		throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
//  	 inputStream.close();
//  	return result;
//}


public JSONObject getAndPersistIPFSFilePrivate(String docId, String fileName, String sessionId, 
		SigmaProps props, String ipfsUrl,String ec2IP1, String ec2IP2, String ec2IP3) throws Exception{ //v tr
	
	
	new HttpConnector(null).skipTrustCertificates(); 
//    URL obj = new URL(props.getExtFileUrl() +"/"+ docId + "/file"); // v
	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Authorization", sessionId);
    con.setRequestProperty("Accept", "application/json");
    int responseCode = con.getResponseCode();
    JSONObject createIRec = null;
    CrateIPFS crateIPFS = new CrateIPFS();
    JSONObject result = new JSONObject();
    InputStream inputStream = null;
//    InputStream inputStream2 = null;
  	 if(responseCode >=200 && responseCode<300) {
  	    //commented code for instant file write to a pdf file in local disk
  	    /*
  	    FileOutputStream outputStream = new FileOutputStream("D:\\Examples\\Sigma_Ent\\files\\int1.pdf");
  	    byte[] buffer = new byte[4096];
  	    int bytesRead = -1;
  	    while ((bytesRead = inputStream.read(buffer)) != -1) {
  	        outputStream.write(buffer, 0, bytesRead);
  	    }
  	    outputStream.close();
  	*/
  	    inputStream = con.getInputStream();
  	// Read the entire stream into a byte array
  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  	  byte[] buffer = new byte[4096];
  	  int bytesRead;
  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
  	  }
  	  byte[] data = byteArrayOutputStream.toByteArray();

  	  // Calculate MD5 checksum on data
  	  String md5Checksum = calculateMD5Checksum(data);

  	  // Use data for createIRec
  	  createIRec = crateIPFS.createIRecPrivate(new ByteArrayInputStream(data), fileName, sessionId, ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
  	    
  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
  	    result.put("createIRec", createIRec.optString("cid"));
  	    
  	  // Calculate the MD5 checksum of the document content
//	    String documentContent = oneDocument.optString(inputStream); // Change to the actual field name
  	    
//  	    inputStream2 = con.getInputStream();
	    //String md5Checksum = calculateMD5Checksum(inputStream);
  	   
        result.put("md5Checksum", md5Checksum);
	} else if(responseCode >=300 && responseCode<500 ){
		crateIPFS.readErrorStream(con);
	 }	 
	 else
		throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
  	 inputStream.close();
  	return result;
}

//public JSONObject getAndPersistIPFSsingleFilePrivate(String docId, String fileName, 
//		PrivateNetwork2 networkById,byte[] binaryData, String ipfsUrl) throws Exception{ //v tr
//	
//	
//	new HttpConnector(null).skipTrustCertificates(); 
//
////	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v
//
//    JSONObject createIRec = null;
//    CrateIPFS crateIPFS = new CrateIPFS();
//    JSONObject result = new JSONObject();
//
//  	  try (InputStream inputStream = new ByteArrayInputStream(binaryData)) {
//  	// Read the entire stream into a byte array
//  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//  	  byte[] buffer = new byte[4096];
//  	  int bytesRead;
//  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
//  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
//  	  }
//  	  byte[] data = byteArrayOutputStream.toByteArray();
//
//  	  // Calculate MD5 checksum on data
//  	  String md5Checksum = calculateMD5Checksum(data);
//
//  	  // Use data for createIRec
//  	  createIRec = crateIPFS.createIRecSigma(new ByteArrayInputStream(data), fileName, networkById, ipfsUrl);
//  	    
//  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
//  	    result.put("createIRec", createIRec.optString("cid"));
//  	    
//
//  	   
//        result.put("md5Checksum", md5Checksum);
//  	  }catch (Exception e) {
//          // Handle other exceptions here
//          throw new Exception("An error occurred: " + e.getMessage());
//      }
////		throw new Exception ("Error response code from web3 responseCode {}");
//		return result;
//  	  
//}

public JSONObject getAndPersistIPFSsingleFilePrivate(String docId, String fileName, 
		byte[] binaryData, String ipfsUrl,String ec2IP1, String ec2IP2, String ec2IP3) throws Exception{ //v tr
	
	
	new HttpConnector(null).skipTrustCertificates(); 

//	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v

    JSONObject createIRec = null;
    CrateIPFS crateIPFS = new CrateIPFS();
    JSONObject result = new JSONObject();

  	  try (InputStream inputStream = new ByteArrayInputStream(binaryData)) {
  	// Read the entire stream into a byte array
  	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  	  byte[] buffer = new byte[4096];
  	  int bytesRead;
  	  while ((bytesRead = inputStream.read(buffer)) != -1) {
  	      byteArrayOutputStream.write(buffer, 0, bytesRead);
  	  }
  	  byte[] data = byteArrayOutputStream.toByteArray();

  	  // Calculate MD5 checksum on data
  	  String md5Checksum = calculateMD5Checksum(data);

  	  // Use data for createIRec
  	  createIRec = crateIPFS.createIRecSigma(new ByteArrayInputStream(data), fileName, ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
  	    
  	    //createIRec = crateIPFS.createIRec(inputStream, fileName, networkById, sessionId);	
  	    result.put("createIRec", createIRec.optString("cid"));
  	    

  	   
        result.put("md5Checksum", md5Checksum);
  	  }catch (Exception e) {
          // Handle other exceptions here
          throw new Exception("An error occurred: " + e.getMessage());
      }
//		throw new Exception ("Error response code from web3 responseCode {}");
		return result;
  	  
}


}

