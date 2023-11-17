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
import java.net.URLEncoder;
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
    
//    public JSONObject createIRecSigma(InputStream inputStream, String fileName, PrivateNetwork2 networkById, String privateIPFSurl) throws Exception{
//    	try {
//            LOGGER.info("Uploading file to private IPFS started for file => " + fileName);
//            new HttpConnector(null).skipTrustCertificates();
//
//            String boundary = "------------------------abcdef1234567890";
//            String ipfsUrl = privateIPFSurl;
//
//            HttpURLConnection connection = (HttpURLConnection) new URL(ipfsUrl).openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//            
//            // Set your API key as an authorization header
////            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//
//            OutputStream outputStream = connection.getOutputStream();
//            writeBoundary(outputStream, boundary);
//            writeContentDisposition(outputStream, "file", fileName);
//            writeFile(inputStream, outputStream);
//            writeBoundary(outputStream, boundary, true);
//
//            int responseCode = connection.getResponseCode();
//            String readResponseFromConnection = "";
//
//            if (responseCode >= 200 && responseCode < 300) {
//                readResponseFromConnection = readResponseFromConnection(connection);
//                
//             // Assuming your response from private IPFS contains a JSON with a field "cid"
//                JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//                String ipfsCid = jsonResponse.optString("cid");
//                
//                String encodedDestinationPath = URLEncoder.encode(fileName, "UTF-8");
//                
//                // Continue with the next HTTP POST request
//                String apiUrl = "http://13.58.159.144:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url = new URL(apiUrl);
//
//                HttpURLConnection connection1 = (HttpURLConnection) url.openConnection();
//                connection1.setRequestMethod("POST");
//                connection1.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode1 = connection1.getResponseCode();
//
//                if (responseCode1 >= 200 && responseCode1 < 300) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
//                    String line;
//                    StringBuilder response1 = new StringBuilder();
//                    while ((line = reader.readLine()) != null) {
//                        response1.append(line);
//                    }
//                    reader.close();
//
//                    System.out.println("Response Code: " + responseCode1);
//                    System.out.println("Response Data: " + response1.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request: " + responseCode1);
//                }
//
//                connection1.disconnect();
//            } else if (responseCode >= 300 && responseCode < 500) {
//                readErrorStream(connection);
//            } else {
//                throw new Exception("Error response code from IPFS API: " + responseCode);
//            }
//
//            JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//            LOGGER.info("File uploaded to private IPFS completed for file => " + fileName +
//                    ", Response code => " + responseCode + ", Response = " + readResponseFromConnection);
//            return jsonResponse;
//        } catch (Exception exception) {
//            LOGGER.error("Error uploading file to private IPFS =>", exception);
//            return new JSONObject();
//        }
//    }
    
//    public JSONObject createIRecSigma(InputStream inputStream, String fileName, PrivateNetwork2 networkById, String privateIPFSurl) throws Exception{
//    	try {
//            LOGGER.info("Uploading file to private IPFS started for file => " + fileName);
//            new HttpConnector(null).skipTrustCertificates();
//
//            String boundary = "------------------------abcdef1234567890";
//            String ipfsUrl = privateIPFSurl;
//
//            HttpURLConnection connection = (HttpURLConnection) new URL(ipfsUrl).openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//            
//            // Set your API key as an authorization header
////            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//
//            OutputStream outputStream = connection.getOutputStream();
//            writeBoundary(outputStream, boundary);
//            writeContentDisposition(outputStream, "file", fileName);
//            writeFile(inputStream, outputStream);
//            writeBoundary(outputStream, boundary, true);
//
//            int responseCode = connection.getResponseCode();
//            String readResponseFromConnection = "";
//
//            if (responseCode >= 200 && responseCode < 300) {
//                readResponseFromConnection = readResponseFromConnection(connection);
//                
//             // Assuming your response from private IPFS contains a JSON with a field "cid"
//                JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//                String ipfsCid = jsonResponse.optString("cid");
//                
//                String encodedDestinationPath = URLEncoder.encode(fileName, "UTF-8");
//                
//                // Continue with the next HTTP POST request
//                String apiUrl = "http://13.58.159.144:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url = new URL(apiUrl);
//
//                HttpURLConnection connection1 = (HttpURLConnection) url.openConnection();
//                connection1.setRequestMethod("POST");
//                connection1.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode1 = connection1.getResponseCode();
//
//                if (responseCode1 >= 200 && responseCode1 < 300) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
//                    String line;
//                    StringBuilder response1 = new StringBuilder();
//                    while ((line = reader.readLine()) != null) {
//                        response1.append(line);
//                    }
//                    reader.close();
//
//                    System.out.println("Response Code: " + responseCode1);
//                    System.out.println("Response Data: " + response1.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request: " + responseCode1);
//                }
//
//                connection1.disconnect();
//                
//             // Continue with the next HTTP POST request
//                String apiUrl2 = "http://3.22.223.131:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url2 = new URL(apiUrl2);
//
//                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
//                connection2.setRequestMethod("POST");
//                connection2.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode2 = connection2.getResponseCode();
//
//                if (responseCode2 >= 200 && responseCode2 < 300) {
//                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
//                    String line;
//                    StringBuilder response2 = new StringBuilder();
//                    while ((line = reader2.readLine()) != null) {
//                        response2.append(line);
//                    }
//                    reader2.close();
//
//                    System.out.println("Response Code2: " + responseCode2);
//                    System.out.println("Response Data2: " + response2.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request2: " + responseCode2);
//                }
//
//                connection2.disconnect();
//                
//             // Continue with the next HTTP POST request
//                String apiUrl3 = "http://3.138.196.187:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url3 = new URL(apiUrl3);
//
//                HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
//                connection3.setRequestMethod("POST");
//                connection3.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode3 = connection3.getResponseCode();
//
//                if (responseCode3 >= 200 && responseCode3 < 300) {
//                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()));
//                    String line;
//                    StringBuilder response3 = new StringBuilder();
//                    while ((line = reader3.readLine()) != null) {
//                        response3.append(line);
//                    }
//                    reader3.close();
//
//                    System.out.println("Response Code3: " + responseCode3);
//                    System.out.println("Response Data3: " + response3.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request3: " + responseCode3);
//                }
//
//                connection3.disconnect();
//            } else if (responseCode >= 300 && responseCode < 500) {
//                readErrorStream(connection);
//            } else {
//                throw new Exception("Error response code from IPFS API: " + responseCode);
//            }
//
//            JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//            LOGGER.info("File uploaded to private IPFS completed for file => " + fileName +
//                    ", Response code => " + responseCode + ", Response = " + readResponseFromConnection);
//            return jsonResponse;
//        } catch (Exception exception) {
//            LOGGER.error("Error uploading file to private IPFS =>", exception);
//            return new JSONObject();
//        }
//    }
    
    public JSONObject createIRecSigma(InputStream inputStream, String fileName, String privateIPFSurl, String ec2IP1, String ec2IP2, String ec2IP3) throws Exception{
    	try {
            LOGGER.info("Uploading file to private IPFS started for file => " + fileName);
            new HttpConnector(null).skipTrustCertificates();

            String boundary = "------------------------abcdef1234567890";
            String ipfsUrl = privateIPFSurl;

            HttpURLConnection connection = (HttpURLConnection) new URL(ipfsUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            
            // Set your API key as an authorization header
//            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            OutputStream outputStream = connection.getOutputStream();
            writeBoundary(outputStream, boundary);
            writeContentDisposition(outputStream, "file", fileName);
            writeFile(inputStream, outputStream);
            writeBoundary(outputStream, boundary, true);

            int responseCode = connection.getResponseCode();
            String readResponseFromConnection = "";

            if (responseCode >= 200 && responseCode < 300) {
                readResponseFromConnection = readResponseFromConnection(connection);
                
             // Assuming your response from private IPFS contains a JSON with a field "cid"
                JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
                String ipfsCid = jsonResponse.optString("cid");
                
                String encodedDestinationPath = URLEncoder.encode(fileName, "UTF-8");
                
                try {
                // Continue with the next HTTP POST request
                String apiUrl = "http://"+ec2IP1+":5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;

                URL url = new URL(apiUrl);

                HttpURLConnection connection1 = (HttpURLConnection) url.openConnection();
                connection1.setRequestMethod("POST");
                connection1.setRequestProperty("Content-Type", "application/json"); // Set content type if needed

                int responseCode1 = connection1.getResponseCode();

                if (responseCode1 >= 200 && responseCode1 < 300) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                    String line;
                    StringBuilder response1 = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response1.append(line);
                    }
                    reader.close();

                    System.out.println("Response Code: " + responseCode1);
                    System.out.println("Response Data: " + response1.toString());
                } else {
                    System.out.println("Error response code from the second POST request: " + responseCode1);
                }

                connection1.disconnect();
                }catch(Exception exception1) {
                	System.out.println("Error: ec2 one"+exception1);
                }
                
                try {
             // Continue with the next HTTP POST request
                String apiUrl2 = "http://"+ec2IP2+":5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;

                URL url2 = new URL(apiUrl2);

                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setRequestMethod("POST");
                connection2.setRequestProperty("Content-Type", "application/json"); // Set content type if needed

                int responseCode2 = connection2.getResponseCode();

                if (responseCode2 >= 200 && responseCode2 < 300) {
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                    String line;
                    StringBuilder response2 = new StringBuilder();
                    while ((line = reader2.readLine()) != null) {
                        response2.append(line);
                    }
                    reader2.close();

                    System.out.println("Response Code2: " + responseCode2);
                    System.out.println("Response Data2: " + response2.toString());
                } else {
                    System.out.println("Error response code from the second POST request2: " + responseCode2);
                }

                connection2.disconnect();
                }catch(Exception exception2) {
                	System.out.println("Error: ec2 two"+exception2);
                }
                
                try {
             // Continue with the next HTTP POST request
                String apiUrl3 = "http://"+ec2IP3+":5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;

                URL url3 = new URL(apiUrl3);

                HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
                connection3.setRequestMethod("POST");
                connection3.setRequestProperty("Content-Type", "application/json"); // Set content type if needed

                int responseCode3 = connection3.getResponseCode();

                if (responseCode3 >= 200 && responseCode3 < 300) {
                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()));
                    String line;
                    StringBuilder response3 = new StringBuilder();
                    while ((line = reader3.readLine()) != null) {
                        response3.append(line);
                    }
                    reader3.close();

                    System.out.println("Response Code3: " + responseCode3);
                    System.out.println("Response Data3: " + response3.toString());
                } else {
                    System.out.println("Error response code from the second POST request3: " + responseCode3);
                }

                connection3.disconnect();
                }catch(Exception exception3) {
                	System.out.println("Error: ec2 three"+exception3);
                }
            } else if (responseCode >= 300 && responseCode < 500) {
                readErrorStream(connection);
            } else {
                throw new Exception("Error response code from IPFS API: " + responseCode);
            }

            JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
            LOGGER.info("File uploaded to private IPFS completed for file => " + fileName +
                    ", Response code => " + responseCode + ", Response = " + readResponseFromConnection);
            return jsonResponse;
        } catch (Exception exception) {
            LOGGER.error("Error uploading file to private IPFS =>", exception);
            return new JSONObject();
        }
    }

//    public JSONObject createIRecPrivate(InputStream inputStream, String fileName, PrivateNetwork2 networkById, String sessionId, String ipfsUrlPrivate) throws Exception{
//    	try {
//            LOGGER.info("Uploading file to private IPFS started for file => " + fileName);
//            new HttpConnector(null).skipTrustCertificates();
//
//            String boundary = "------------------------abcdef1234567890";
//            String ipfsUrl = ipfsUrlPrivate;
//
//            HttpURLConnection connection = (HttpURLConnection) new URL(ipfsUrl).openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//            
//            // Set your API key as an authorization header
////            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//
//            OutputStream outputStream = connection.getOutputStream();
//            writeBoundary(outputStream, boundary);
//            writeContentDisposition(outputStream, "file", fileName);
//            writeFile(inputStream, outputStream);
//            writeBoundary(outputStream, boundary, true);
//
//            int responseCode = connection.getResponseCode();
//            String readResponseFromConnection = "";
//
//            if (responseCode >= 200 && responseCode < 300) {
//                readResponseFromConnection = readResponseFromConnection(connection);
//            } else if (responseCode >= 300 && responseCode < 500) {
//                readErrorStream(connection);
//            } else {
//                throw new Exception("Error response code from IPFS API: " + responseCode);
//            }
//
//            JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//            LOGGER.info("File uploaded to private IPFS completed for file => " + fileName +
//                    ", Response code => " + responseCode + ", Response = " + readResponseFromConnection);
//            return jsonResponse;
//        } catch (Exception exception) {
//            LOGGER.error("Error uploading file to private IPFS =>", exception);
//            return new JSONObject();
//        }
//    }
    
//    public JSONObject createIRecPrivate(InputStream inputStream, String fileName, PrivateNetwork2 networkById, String sessionId, String ipfsUrlPrivate) throws Exception {
//        try {
//            LOGGER.info("Uploading file to private IPFS started for file => " + fileName);
//            new HttpConnector(null).skipTrustCertificates();
//
//            String boundary = "------------------------abcdef1234567890";
//            String ipfsUrl = ipfsUrlPrivate;
//
//            HttpURLConnection connection = (HttpURLConnection) new URL(ipfsUrl).openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//            // Set your API key as an authorization header
//            // connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//
//            OutputStream outputStream = connection.getOutputStream();
//            writeBoundary(outputStream, boundary);
//            writeContentDisposition(outputStream, "file", fileName);
//            writeFile(inputStream, outputStream);
//            writeBoundary(outputStream, boundary, true);
//
//            int responseCode = connection.getResponseCode();
//            String readResponseFromConnection = "";
//
//            if (responseCode >= 200 && responseCode < 300) {
//                readResponseFromConnection = readResponseFromConnection(connection);
//
//                // Assuming your response from private IPFS contains a JSON with a field "cid"
//                JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//                String ipfsCid = jsonResponse.optString("cid");
//                
//                String encodedDestinationPath = URLEncoder.encode(fileName, "UTF-8");
//                
//                // Continue with the next HTTP POST request
//                String apiUrl = "http://13.58.159.144:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url = new URL(apiUrl);
//
//                HttpURLConnection connection1 = (HttpURLConnection) url.openConnection();
//                connection1.setRequestMethod("POST");
//                connection1.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode1 = connection1.getResponseCode();
//
//                if (responseCode1 >= 200 && responseCode1 < 300) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
//                    String line;
//                    StringBuilder response1 = new StringBuilder();
//                    while ((line = reader.readLine()) != null) {
//                        response1.append(line);
//                    }
//                    reader.close();
//
//                    System.out.println("Response Code: " + responseCode1);
//                    System.out.println("Response Data: " + response1.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request: " + responseCode1);
//                }
//
//                connection1.disconnect();
//                
//             // Continue with the next HTTP POST request
//                String apiUrl2 = "http://3.22.223.131:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url2 = new URL(apiUrl2);
//
//                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
//                connection2.setRequestMethod("POST");
//                connection2.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode2 = connection2.getResponseCode();
//
//                if (responseCode2 >= 200 && responseCode2 < 300) {
//                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
//                    String line;
//                    StringBuilder response2 = new StringBuilder();
//                    while ((line = reader2.readLine()) != null) {
//                        response2.append(line);
//                    }
//                    reader2.close();
//
//                    System.out.println("Response Code2: " + responseCode2);
//                    System.out.println("Response Data2: " + response2.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request2: " + responseCode2);
//                }
//
//                connection2.disconnect();
//                
//             // Continue with the next HTTP POST request
//                String apiUrl3 = "http://3.138.196.187:5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;
//
//                URL url3 = new URL(apiUrl3);
//
//                HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
//                connection3.setRequestMethod("POST");
//                connection3.setRequestProperty("Content-Type", "application/json"); // Set content type if needed
//
//                int responseCode3 = connection3.getResponseCode();
//
//                if (responseCode3 >= 200 && responseCode3 < 300) {
//                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()));
//                    String line;
//                    StringBuilder response3 = new StringBuilder();
//                    while ((line = reader3.readLine()) != null) {
//                        response3.append(line);
//                    }
//                    reader3.close();
//
//                    System.out.println("Response Code3: " + responseCode3);
//                    System.out.println("Response Data3: " + response3.toString());
//                } else {
//                    System.out.println("Error response code from the second POST request3: " + responseCode3);
//                }
//
//                connection3.disconnect();
//
//            } else if (responseCode >= 300 && responseCode < 500) {
//                readErrorStream(connection);
//            } else {
//                throw new Exception("Error response code from IPFS API: " + responseCode);
//            }
//
//            JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
//            LOGGER.info("File uploaded to private IPFS completed for file => " + fileName +
//                    ", Response code => " + responseCode + ", Response = " + jsonResponse.toString());
//
//            return jsonResponse;
//        } catch (Exception exception) {
//            LOGGER.error("Error uploading file to private IPFS =>", exception);
//            return new JSONObject();
//        }
//    }    
    
    
    public JSONObject createIRecPrivate(InputStream inputStream, String fileName, String sessionId, String ipfsUrlPrivate, String ec2IP1, String ec2IP2, String ec2IP3) throws Exception {
        try {
            LOGGER.info("Uploading file to private IPFS started for file => " + fileName + ec2IP1);
            new HttpConnector(null).skipTrustCertificates();

            String boundary = "------------------------abcdef1234567890";
            String ipfsUrl = ipfsUrlPrivate;

            HttpURLConnection connection = (HttpURLConnection) new URL(ipfsUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Set your API key as an authorization header
            // connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            OutputStream outputStream = connection.getOutputStream();
            writeBoundary(outputStream, boundary);
            writeContentDisposition(outputStream, "file", fileName);
            writeFile(inputStream, outputStream);
            writeBoundary(outputStream, boundary, true);

            int responseCode = connection.getResponseCode();
            String readResponseFromConnection = "";

            if (responseCode >= 200 && responseCode < 300) {
                readResponseFromConnection = readResponseFromConnection(connection);

                // Assuming your response from private IPFS contains a JSON with a field "cid"
                JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
                String ipfsCid = jsonResponse.optString("cid");
                
                String encodedDestinationPath = URLEncoder.encode(fileName, "UTF-8");
                
                try {
                // Continue with the next HTTP POST request
                String apiUrl = "http://"+ec2IP1+":5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;

                URL url = new URL(apiUrl);

                HttpURLConnection connection1 = (HttpURLConnection) url.openConnection();
                connection1.setRequestMethod("POST");
                connection1.setRequestProperty("Content-Type", "application/json"); // Set content type if needed

                int responseCode1 = connection1.getResponseCode();

                if (responseCode1 >= 200 && responseCode1 < 300) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                    String line;
                    StringBuilder response1 = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response1.append(line);
                    }
                    reader.close();

                    System.out.println("Response Code: " + responseCode1);
                    System.out.println("Response Data: " + response1.toString());
                } else {
                    System.out.println("Error response code from the second POST request: " + responseCode1);
                }

                connection1.disconnect();
                }catch(Exception exception1) {
                	System.out.println("Error in Ec2 one: " + exception1);
                }
                
                try {
             // Continue with the next HTTP POST request
                String apiUrl2 = "http://"+ec2IP2+":5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;

                URL url2 = new URL(apiUrl2);

                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setRequestMethod("POST");
                connection2.setRequestProperty("Content-Type", "application/json"); // Set content type if needed

                int responseCode2 = connection2.getResponseCode();

                if (responseCode2 >= 200 && responseCode2 < 300) {
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                    String line;
                    StringBuilder response2 = new StringBuilder();
                    while ((line = reader2.readLine()) != null) {
                        response2.append(line);
                    }
                    reader2.close();

                    System.out.println("Response Code2: " + responseCode2);
                    System.out.println("Response Data2: " + response2.toString());
                } else {
                    System.out.println("Error response code from the second POST request2: " + responseCode2);
                }

                connection2.disconnect();
                }catch(Exception exception2) {
                	System.out.println("Error in Ec2 two: " + exception2);
                }
                
                try {
             // Continue with the next HTTP POST request
                String apiUrl3 = "http://"+ec2IP3+":5001/api/v0/files/cp?arg=/ipfs/" + ipfsCid + "&arg=/" + encodedDestinationPath;

                URL url3 = new URL(apiUrl3);

                HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection();
                connection3.setRequestMethod("POST");
                connection3.setRequestProperty("Content-Type", "application/json"); // Set content type if needed

                int responseCode3 = connection3.getResponseCode();

                if (responseCode3 >= 200 && responseCode3 < 300) {
                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()));
                    String line;
                    StringBuilder response3 = new StringBuilder();
                    while ((line = reader3.readLine()) != null) {
                        response3.append(line);
                    }
                    reader3.close();

                    System.out.println("Response Code3: " + responseCode3);
                    System.out.println("Response Data3: " + response3.toString());
                } else {
                    System.out.println("Error response code from the second POST request3: " + responseCode3);
                }

                connection3.disconnect();
                }catch(Exception exception3) {
                	System.out.println("Error in Ec2 three: " + exception3);
                }

            } else if (responseCode >= 300 && responseCode < 500) {
                readErrorStream(connection);
            } else {
                throw new Exception("Error response code from IPFS API: " + responseCode);
            }

            JSONObject jsonResponse = new JSONObject(readResponseFromConnection);
            LOGGER.info("File uploaded to private IPFS completed for file => " + fileName +
                    ", Response code => " + responseCode + ", Response = " + jsonResponse.toString());

            return jsonResponse;
        } catch (Exception exception) {
            LOGGER.error("Error uploading file to private IPFS =>", exception);
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