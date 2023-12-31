package com.sigma.affinity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sigma.model.PrivateNetwork2;

public class CrateIPFS {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.aws.affinity.CrateIPFS");
//	String url = "https://u0quri4drx-u0wfa6luww-ipfs.us0-aws.kaleido.io/api/v0/add";
	String authorization = "Basic dTBpeG44bmgycjplLTNkaDdzWTlwaDhaM0VSdTFMNkozbEQwc1IyTXlzd3N6S3o4bHhsZERr";
	
    public CrateIPFS() {
		super();
	}
//	public CrateIPFS(String url, String authorization) {
//		super();
//		this.url = url;
//		this.authorization = authorization;
//	}
	public static void main(String[] args) throws Exception {
  //  	new CrateIPFS().createIRec(null, null);
//		new CrateIPFS().getIpfsFile("QmcuJ9MbJku5Tx3x664ZdZFnuqXctV9BrG1wwbLKZYkTtf", 
//				"D:\\Examples\\Sigma_Ent\\files\\ipfs_file_4.pdf");
    }
	public byte[] getIpfsFile(String url, String token) {
		try {
	    new HttpConnector(null).skipTrustCertificates();
	    URL obj = new URL(url); // v
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Authorization", "Basic " + token);
//	    con.setRequestProperty("Accept", "application/pdf");//new added
	    int responseCode = con.getResponseCode();
	    System.out.println("Response Code : " + responseCode);
	    InputStream inputStream = con.getInputStream();
	    //commented code for instant file write to a pdf file in local disk
	    /*
	    File file = new File(targetFilePath);
	    FileOutputStream outputStream = new FileOutputStream(file);
	    byte[] buffer = new byte[4096];
	    int bytesRead = -1;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
	        outputStream.write(buffer, 0, bytesRead);
	    }
	    outputStream.close();
*/
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	 // Byte array to store data temporarily
	 byte[] buffer = new byte[1024];

	 // Read data from the input stream and write it to the ByteArrayOutputStream
	 int bytesRead;
	 while ((bytesRead = inputStream.read(buffer)) != -1) {
	     outputStream.write(buffer, 0, bytesRead);
	 }

	 // Convert the ByteArrayOutputStream to a byte array
	 byte[] data = outputStream.toByteArray();
	 System.out.printf("ipfs",data);
	 return data;
	 
		}catch(Exception e) {
			LOGGER.error("CrateIPFS.getIpfsFile() fileHash =>"+ url);
			return null;
		}
	}
	
	public byte[] getIpfsFilenew(String url, String token) {
		try {
	    new HttpConnector(null).skipTrustCertificates();
	    URL obj = new URL(url); // v
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Authorization", "Basic " + token);
//	    con.setRequestProperty("Accept", "application/pdf");//new added
	    int responseCode = con.getResponseCode();
	    System.out.println("Response Code : " + responseCode);
//	    InputStream inputStream = con.getInputStream();
	    InputStream dataStream = con.getInputStream();
        byte[] data = IOUtils.toByteArray(dataStream);
	    //commented code for instant file write to a pdf file in local disk
	    /*
	    File file = new File(targetFilePath);
	    FileOutputStream outputStream = new FileOutputStream(file);
	    byte[] buffer = new byte[4096];
	    int bytesRead = -1;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
	        outputStream.write(buffer, 0, bytesRead);
	    }
	    outputStream.close();
*/
//	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//	 // Byte array to store data temporarily
//	 byte[] buffer = new byte[1024];
//
//	 // Read data from the input stream and write it to the ByteArrayOutputStream
//	 int bytesRead;
//	 while ((bytesRead = inputStream.read(buffer)) != -1) {
//	     outputStream.write(buffer, 0, bytesRead);
//	 }
//
//	 // Convert the ByteArrayOutputStream to a byte array
//	 byte[] data = outputStream.toByteArray();
	 System.out.printf("ipfs",data);
	 return data;
	 
		}catch(Exception e) {
			LOGGER.error("CrateIPFS.getIpfsFile() fileHash =>"+ url);
			return null;
		}
	}

	public FileInputStream getIpfsFileStream(String url, String token) {
	    try {
	        new HttpConnector(null).skipTrustCertificates();
	        URL obj = new URL(url);
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Authorization", "Basic " + token);

	        int responseCode = con.getResponseCode();
	        System.out.println("Response Code: " + responseCode);
	        InputStream inputStream = con.getInputStream();

	        // Read the data into a ByteArrayOutputStream as before
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        byte[] data = outputStream.toByteArray();
	        outputStream.close();

	        // Create a temporary file from the byte array data
	        File tempFile = File.createTempFile("tempfile", ".pdf");
	        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
	        fileOutputStream.write(data);
	        fileOutputStream.close();

	        // Return the FileInputStream for the temporary file
	        return new FileInputStream(tempFile);

	    } catch (Exception e) {
	        LOGGER.error("CrateIPFS.getIpfsFileStream() fileHash => " + url);
	        return null;
	    }
	}
    public JSONObject createIRec(InputStream inputStream, String fileName, PrivateNetwork2 networkById, String sessionId) throws Exception{
    	try {
    		LOGGER.info("CrateIPFS.createIRec() iRec creation started for file => "+fileName);
    	new HttpConnector(null).skipTrustCertificates();
        String boundary = "------------------------abcdef1234567890";
        String polyIpfsUrl = networkById.getIpfsUrl()+"add";        
		String encoded = Base64.getEncoder().encodeToString((networkById.getCreatedByUser() + ":" 
        + networkById.getNetworkName()).getBytes());
        HttpURLConnection connection = (HttpURLConnection) new URL(polyIpfsUrl).openConnection(); //v
        connection.setRequestMethod("POST"); //tr
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Basic "+encoded);
        OutputStream outputStream = connection.getOutputStream();
            writeBoundary(outputStream, boundary);
            writeContentDisposition(outputStream, "file", fileName);
            writeFile(inputStream, outputStream);
            writeBoundary(outputStream, boundary, true);

        int responseCode = connection.getResponseCode();
        String readResponseFromConnection = "" ;
   	 if(responseCode >=200 && responseCode<300) {
 		readResponseFromConnection = readResponseFromConnection(connection);
 	} else if(responseCode >=300 && responseCode<500 ){
 		readErrorStream(connection);
 	 }
 	 else
 		throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
   	 	JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
 //       System.out.println("Response code =>" + responseCode);
  //      System.out.println("readResponseFromConnection =>"+ readResponseFromConnection);
        LOGGER.info("CrateIPFS.createIRec() iRec creation complete for file => "+fileName +
        		"Response code =>" + responseCode +", response ="+readResponseFromConnection);
        return jsonResponse;
    	}catch(Exception exception) {
    		LOGGER.error("CrateIPFS.createIRec() fileHash =>", exception);
    		return new JSONObject();
    	}
    }
    private void writeBoundary(OutputStream outputStream, String boundary) throws IOException {
        writeBoundary(outputStream, boundary, false);
    }

    private void writeBoundary(OutputStream outputStream, String boundary, boolean end) throws IOException {
        outputStream.write(("--" + boundary + (end ? "--" : "") + "\r\n").getBytes(StandardCharsets.UTF_8));
    }

    private void writeContentDisposition(OutputStream outputStream, String name, String filename) throws IOException {
        outputStream.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write("Content-Type: text/plain\r\n\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private void writeFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
    void readErrorStream(HttpURLConnection connection) {
    	BufferedReader reader = null;
    	try {
    	  reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
    	  String line;
    	  LOGGER.error("Error creating ipfs record ");
    	  while ((line = reader.readLine()) != null) {
    		  LOGGER.error(line);
    	  }
    	} catch (IOException e) {
    		LOGGER.error("Error creating ipfs record ",  e);
    	} finally {
    	  if (reader != null) {
    	    try {
    	      reader.close();
    	    } catch (IOException e) {
    	    	LOGGER.error("Error creating ipfs record ",  e);
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
//    		LOGGER.error("HttpURLConnectionUtil.readResponseFromConnection()"
//    				+ "con {}", con, exception);
    		return null;
    	}
    }
}