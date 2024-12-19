package com.sigma.affinity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.model.DocumentO;
import com.sigma.model.ImmutableRec;
import com.sigma.model.InfraBean;
import com.sigma.model.NodeBean;
import com.sigma.model.PageRequestBean;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.SigmaDocument;
import com.sigma.model.db.PrivateNetworkPersistence3;
import com.sigma.model.db.SigmaDocFieldConfigPersistence6;

public class PolygonEdgeUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.affinity.PolygonEdgeUtil");
	public JSONObject createConsortium(String url, String token) {
		JSONObject defaultJson = new JSONObject();
		HttpConnector connector = new HttpConnector(url);
		JSONObject jsonInput = new JSONObject();
		jsonInput.put("name", "api101");
		jsonInput.put("description", "Automation is Great");
		try {
			JSONObject invokePostAndGetJson = connector.invokePostAndGetJson(jsonInput.toString(), token);
			return invokePostAndGetJson;
		} catch (Exception e) {
			LOGGER.error("Error during PolygonEdgeUtil.createConsortium()", e);
			return defaultJson;  
		}
	} 
	public JSONArray getConsortiumInfo(String url, String token) {
		try {
		String url1 = url+"api/v1/consortia";
		HttpConnector connector = new HttpConnector(url1);
		String invokeGet = connector.invokeGet(token);
		 JSONArray responseFromNode= new JSONArray(invokeGet);
		return responseFromNode;
		}catch(Exception exception) {
			LOGGER.error("Error PolygonEdgeUtil.getConsortiumInfo()", exception);
			return null;
		}
	}
	public JSONObject makeEnvironment(String url, String token, String consortiaId) {
		JSONObject defaultResponse = new JSONObject();
		String url1 = url+"api/v1/consortia/"+consortiaId+"/environments";
		HttpConnector connector = new HttpConnector(url1);
		JSONObject input = new JSONObject();
		input.put("name", "API PE 1");
		input.put("provider", "polygon-edge");
		input.put("consensus_type", "ibft");
		try {
			JSONObject environmentResponse = connector.invokePostAndGetJson(input.toString(), token);
			return environmentResponse;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.makeEnvironment()", e);
			return defaultResponse;
		}
	}
	public InfraBean getNodeDetails(PrivateNetwork2 privateNetwork2, String pbcToken) {
		String url =privateNetwork2.getNetworkAPIUrl() + "consortia/"+privateNetwork2.getConsortiaId() 
				+"/environments/"+privateNetwork2.getEnvId()+"/nodes/";
		HttpConnector connector = new HttpConnector(url);
		connector.skipTrustCertificates();
		String nodeInfo = connector.invokeGet(pbcToken);
		JSONArray nodeJson = new JSONArray(nodeInfo);
		List<NodeBean> nodes = new ArrayList<NodeBean>();
		for(int counter=0; counter<nodeJson.length();counter++) {
			JSONObject jsonObject = nodeJson.getJSONObject(counter);
			NodeBean node = new NodeBean();
			node.setName(jsonObject.optString("name"));
			node.setNodeId(jsonObject.optString("_id"));
			node.setState(jsonObject.optString("state"));
			node.setProvider(jsonObject.optString("provider"));
			node.setConsensusType(jsonObject.optString("consensusType"));
			node.setCreated_at(jsonObject.optString("created_at"));
			node.setUpdated_at(jsonObject.optString("updated_at"));
			node.setRole(jsonObject.optString("role"));
			node.setSize(jsonObject.optString("size"));
			nodes.add(node);
		}
		String envInfo = connector.invokeGet(pbcToken);
		JSONArray envJson = new JSONArray(nodeInfo);
		InfraBean envs = new InfraBean();
		if(envJson.length()>0)
		{
			JSONObject jsonObject = envJson.getJSONObject(0);
			envs.setName(jsonObject.optString("name"));
			envs.setProvider(jsonObject.optString("provider"));
			envs.setChainId(jsonObject.optString("chain_id"));
			envs.setCreated_at(jsonObject.optString("created_at"));
			envs.setUpdated_at(jsonObject.optString("updated_at"));
			envs.setPaused_at(jsonObject.optString("paused_at"));
			envs.setResumed_at(jsonObject.optString("resumed_at"));
		}
		envs.setNoOfNodes(nodes.size());
		envs.setNodes(nodes);
		System.out.println(nodeJson);
		System.err.println(envInfo);
		return envs;
	}
	public PrivateNetwork2 makePermissionedBlockChain(String url, String token, 
			String filePath, String contractName) {
		try {
			HttpConnector connector = new HttpConnector("");
			connector.skipTrustCertificates();
		// 1 create consortium
		//JSONObject getConsortium = new PolygonEdgeUtil().createConsortium(url+"api/v1/consortia", token);
		
		// 2. Get consortium
		JSONArray consortiumInfo = new PolygonEdgeUtil().getConsortiumInfo(url, token);
		JSONObject consortiumObject = consortiumInfo.getJSONObject(0);
		String consortiumId = consortiumObject.optString("_id","");
		
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain consortiumId {}",
				consortiumId);
		// 3. create environment
		Map<String, String> requestProperties = new HashMap<String, String>();
		requestProperties.put("Authorization", "Bearer "+token);
		JSONObject makeEnvironment = new PolygonEdgeUtil().makeEnvironment(url, token,consortiumId);
		String envId = makeEnvironment.optString("_id");
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain envId {}", envId);
		// 3.2 get network
		JSONObject network = new PolygonEdgeUtil().getNetwork(url, token, consortiumId);
		String membershipId = network.optString("_id","");
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain membershipId {}", membershipId);
		// 4. make node
		JSONObject nodeDetails = new PolygonEdgeUtil().makeNode(url, token,consortiumId,envId, membershipId);
		String nodeId = nodeDetails.optString("_id");
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain nodeId {}", nodeId);
		// 5. create appcreds
		JSONObject makeAppCreds = new PolygonEdgeUtil().makeAppCreds(url, token, 
				consortiumId, envId, membershipId);
		String userName = makeAppCreds.optString("username");
		String password =makeAppCreds.optString("password");		
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain makeAppCreds "
				+ "userName {}", userName);
		// 6. compile contract

		String compileResponse = null;
		String compileUrl = "";
		try {
			LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain Waiting for node !!!");
			Thread.sleep(1000*60*3);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String baseSmartContractUrl ="https://"+envId+"-"+nodeId+"-connect.us0-aws.kaleido.io/";
		try {
		compileUrl = baseSmartContractUrl+"abis"; 	
			compileResponse = new FormSubmit().postFileData(compileUrl, token, filePath, 
					contractName, userName, password);
			
			LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain compile ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject compileResponseJson = new JSONObject(compileResponse);
		String compileId = compileResponseJson.optString("id", "");
		
		// 7. fetch default address
		
		JSONObject defaultAddressInfo = new PolygonEdgeUtil().getDefaultAddress(url, 
				consortiumId, envId, nodeId, token);
		String defaultAddress = defaultAddressInfo.optString("first_user_account","");
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain defaultAddress {}",
				defaultAddress);
		//7. deploy a smart contract
		String fromAddress = defaultAddress;
		String deployUrl = compileUrl+"/"+ compileId +"?kld-from="	+ fromAddress +"&kld-sync";
		JSONObject smartContractInfo = new PolygonEdgeUtil().deploySmartContract(deployUrl,
				userName, password);
		String contractAddress = smartContractInfo.optString("contractAddress","");
		LOGGER.info("PolygonEdgeUtil.makePermissionedBlockChain deploy smart contract !");

		// 8. mint nft
		PrivateNetwork2 privateNetwork2 = new PrivateNetwork2();
		privateNetwork2.setCreatedByUser(userName);
		privateNetwork2.setNetworkName(password);
		privateNetwork2.setSmartContractAccessUrl(baseSmartContractUrl);
		privateNetwork2.setSmartContractAddress(contractAddress);
		privateNetwork2.setSmartContractDefaultWalletAddress(fromAddress);
		privateNetwork2.setStatus("Y");
		privateNetwork2.setConsortiaId(consortiumId);
		privateNetwork2.setEnvId(envId);
		privateNetwork2.setNetworkAPIUrl(url);
		return privateNetwork2;		
		}catch(Exception exception) {
			LOGGER.error("Error PolygonEdgeUtil.createdPermissionedBlockChain()", exception);
			return null;
		}
	}
	public JSONObject mintNftForDocument(PrivateNetwork2 privateNetwork2,
			DocumentO documentO) {
		try {
		String mintNftUrl = privateNetwork2.getSmartContractAccessUrl()+"contracts"+"/"+ privateNetwork2.getSmartContractAddress() +
				"/mintNFT?kld-from="	+ privateNetwork2.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
		JSONObject nftInfo = new PolygonEdgeUtil().mintNft(mintNftUrl,
				privateNetwork2.getCreatedByUser(), privateNetwork2.getNetworkName(), documentO); //v
		return  nftInfo;
		}catch(Exception exception) {
			LOGGER.error("Error PolygonEdgeUtil.mintNftForDocument()", exception);
			return null;
		}
	}
//	public JSONObject mintNftForDocument(PrivateNetwork2 privateNetwork2,
//			SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
//		try {
//		String mintNftUrl = privateNetwork2.getSmartContractAccessUrl()+"contracts"+"/"+ privateNetwork2.getSmartContractAddress() +
//				"/mintNFT?kld-from="	+ privateNetwork2.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
//		JSONObject nftInfo = new PolygonEdgeUtil().mintNft(mintNftUrl,
//				privateNetwork2.getCreatedByUser(), privateNetwork2.getNetworkName(), 
//				documentO, sigmaDocFieldConfigList, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl); //v
//		return  nftInfo;
//		}catch(Exception exception) {
//			LOGGER.error("Error PolygonEdgeUtil.mintNftForDocument()", exception);
//			return null;
//		}
//	}
	
	public JSONObject mintNftForDocument(
			SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
		try {
//		String mintNftUrl = privateNetwork2.getSmartContractAccessUrl()+"contracts"+"/"+ privateNetwork2.getSmartContractAddress() +
//				"/mintNFT?kld-from="	+ privateNetwork2.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
		JSONObject nftInfo = new PolygonEdgeUtil().mintNftSui( 
				documentO, sigmaDocFieldConfigList, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl); //v
		return  nftInfo;
		}catch(Exception exception) {
			LOGGER.error("Error PolygonEdgeUtil.mintNftForDocument()", exception);
			return null;
		}
	}
	private JSONObject mintNft(String mintNftUrl, String userName, String password, 
			DocumentO documentO) {
		JSONObject input = new JSONObject();
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
		input.put("tokenKey", uuidAsString);
		input.put("id", Integer.valueOf(documentO.getId()+""));
		input.put("versionid", documentO.getVersion_id());
		//TODO replace the below global id with actual field for primary key 
		input.put("globalid", "gid1");
		return mintNft(mintNftUrl, userName, password, input);
	}
//	private JSONObject mintNft(String mintNftUrl, String userName, String password, 
//			SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
//		JSONObject input = new JSONObject();
//        UUID uuid = UUID.randomUUID();
//        String uuidAsString = uuid.toString();
//		input.put("tokenKey", uuidAsString);
//		ObjectMapper mapper = new ObjectMapper();
//		String writeValueAsString =
//				null;
//		try {
//			writeValueAsString = mapper.writeValueAsString(documentO);
//		} catch (JsonProcessingException e) {
//			LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", documentO, e);
//			return new JSONObject();
//		}
//		JSONObject documentOJson = new JSONObject(writeValueAsString);		
//		for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigList) {
//			String sigmaField = sigmaAPIDocConfig.getSigmaField();
//			String targetExtField = documentOJson.optString(sigmaField);
//			input.put(sigmaField, targetExtField);
//			}
//		int sizeOfInput = sigmaDocFieldConfigList.size()+1;
//		for(int counter=sizeOfInput; counter<=10;counter++) {
//			input.put("fVar"+counter, "");
//		}
//		// Create an array of strings
//        //String[] values = new String[]{documentO.getDocChecksum(),documentO.getMd5Checksum()};
//		input.put("fVar10", documentO.getDocChecksum());
//        input.put("fVar11", documentO.getMd5Checksum());
//	//return mintNft(mintNftUrl, userName, password, input);
//	return mintNftEthereum(mintNftUrl, userName, password, input, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
//	}

	private JSONObject mintNftSui(
	        SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList, String infuraUrl, 
	        String contractAddress, String privateKey, int chainId, BigInteger gasPrice, String nonceApiUrl) {
	    JSONObject input = new JSONObject();
	    UUID uuid = UUID.randomUUID();
	    String uuidAsString = uuid.toString();
	    input.put("tokenKey", uuidAsString);
	    ObjectMapper mapper = new ObjectMapper();
	    String writeValueAsString = null;

	    try {
	        writeValueAsString = mapper.writeValueAsString(documentO);
	    } catch (JsonProcessingException e) {
	        LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", documentO, e);
	        return new JSONObject();
	    }

	    JSONObject documentOJson = new JSONObject(writeValueAsString);
	    for (SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigList) {
	        String sigmaField = sigmaAPIDocConfig.getSigmaField();
	        String targetExtField = documentOJson.optString(sigmaField);
	        input.put(sigmaField, targetExtField);
	    }

	    int sizeOfInput = sigmaDocFieldConfigList.size() + 1;
	    for (int counter = sizeOfInput; counter <= 10; counter++) {
	        input.put("fVar" + counter, "");
	    }

	    input.put("fVar10", documentO.getDocChecksum());
	    input.put("fVar11", documentO.getMd5Checksum());

	    try {
	        // Use HttpURLConnection for the POST request
	        URL url = new URL("https://sigma-sui-api.vercel.app/mintnft");
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setRequestProperty("Accept", "application/json");
	        con.setDoOutput(true);
	        con.setFollowRedirects(true);

	        // Write the input JSON to the request body
	        try (OutputStream os = con.getOutputStream()) {
	            byte[] inputBytes = input.toString().getBytes(StandardCharsets.UTF_8);
	            os.write(inputBytes, 0, inputBytes.length);
	        }

	        // Get the response
	        int responseCode = con.getResponseCode();
	        System.out.println("Request Body: " + input.toString());
	        System.out.println("Response Code : " + responseCode);
	        System.out.println("Redirect Location: " + con.getHeaderField("Location"));

	        InputStream inputStream = (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) ?
	                con.getInputStream() : con.getErrorStream();
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	        
	        String responseBody = reader.lines().collect(Collectors.joining("\n"));
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode rootNode = objectMapper.readTree(responseBody);
	        
	        System.out.println("Raw Response Body: " + responseBody);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        byte[] data = outputStream.toByteArray();

	        String response = new String(data, StandardCharsets.UTF_8);
	        LOGGER.info("sigma api respone", response);

	        // Add a 2-second delay after the request
	        Thread.sleep(2000);

	        if (responseCode >= 200 && responseCode < 300) {
	            JSONObject environmentResponse = new JSONObject();
	            environmentResponse.put("uuid", input.optString("tokenKey"));
	            environmentResponse.put("objectId", rootNode.get("ObjectId").asText());
	            return environmentResponse;
	        }
	        return new JSONObject();

	    } catch (Exception e) {
	        LOGGER.error("Error making POST request: {}", e.getMessage(), e);
	        return new JSONObject();
	    }
	}
	
	private JSONObject mintNft(
			SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
		JSONObject input = new JSONObject();
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
		input.put("tokenKey", uuidAsString);
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString =
				null;
		try {
			writeValueAsString = mapper.writeValueAsString(documentO);
		} catch (JsonProcessingException e) {
			LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", documentO, e);
			return new JSONObject();
		}
		JSONObject documentOJson = new JSONObject(writeValueAsString);		
		for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigList) {
			String sigmaField = sigmaAPIDocConfig.getSigmaField();
			String targetExtField = documentOJson.optString(sigmaField);
			input.put(sigmaField, targetExtField);
			}
		int sizeOfInput = sigmaDocFieldConfigList.size()+1;
		for(int counter=sizeOfInput; counter<=10;counter++) {
			input.put("fVar"+counter, "");
		}
		// Create an array of strings
        //String[] values = new String[]{documentO.getDocChecksum(),documentO.getMd5Checksum()};
		input.put("fVar10", documentO.getDocChecksum());
        input.put("fVar11", documentO.getMd5Checksum());
	//return mintNft(mintNftUrl, userName, password, input);
	return mintNftEthereum(input, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
	}

	private JSONObject mintNft(String mintNftUrl, String userName, String password, JSONObject input) {
		JSONObject defaultValue = new JSONObject();
		HttpConnector connector = new HttpConnector(mintNftUrl); //v
		//v
		String encoded = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
		JSONObject environmentResponse = null;
		connector.skipTrustCertificates();
		try {
			 environmentResponse = connector.invokePostAndGetJson(mintNftUrl, 
					input.toString(), null, encoded, true);
			 if(!environmentResponse.isEmpty())
				 environmentResponse.put("uuid", input.optString("tokenKey"));
			return environmentResponse;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.mintNft()", e);
			return defaultValue;
		}
	}
	public long generateNoncevalue(String nonceApiUrl) throws Exception {
		long result = 0L;

		try {
            // Define the API URL
        	new HttpConnector(null).skipTrustCertificates();
        	
            //String apiUrl1 = "https://api-testnet.polygonscan.com/api?module=proxy&action=eth_getTransactionCount&address=0xdc61dE4fED82E2CDbC5E31156c4dA41389Ae1e22&tag=latest&apikey=8QXANC66JIGIZITJE5YGMDZT1HMC26487U";
            URL obj = new URL(nonceApiUrl); 
    	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
         
    	    //con.setRequestProperty("User-Agent", "Mozilla/5.0");
            // Set the request method to GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
    	    System.out.println("Response Code : " + responseCode);
            
    	 // Read the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(con.getInputStream());

            // Extract the "result" field
            if (responseJson.has("result")) {
//                result = responseJson.get("result").asText();
            	 String hexNumber = responseJson.get("result").asText();
                 result = Long.parseLong(hexNumber.substring(2), 16); 
            }
            
        } catch (Exception e) {
            // Handle exceptions
            LOGGER.error("HttpConnector.invokeGet()", e);
//            return defaultJson;
        }
		return result;
	}
//	public long generateNoncevalue(String nonceApiUrl) throws Exception {
//		long result = 0L;
//
//		try {
//			 // Create the JSON request body
////	        String jsonRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"eth_getTransactionCount\", \"params\": [\"0xdc61dE4fED82E2CDbC5E31156c4dA41389Ae1e22\", \"latest\"], \"id\": \"1\"}";
//			String jsonRequest = "{\n" +
//                    "  \"jsonrpc\": \"2.0\",\n" +
//                    "  \"method\": \"eth_getTransactionCount\",\n" +
//                    "  \"params\": [\"" + "0xdc61dE4fED82E2CDbC5E31156c4dA41389Ae1e22,latest"+ "\"],\n" +
//                    "  \"id\": 1\n" +
//                    "}";
//	        URL obj = new URL(nonceApiUrl);
//	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//	        // Set the request method to POST
//	        con.setRequestMethod("POST");
//
//	        // Set request headers
////	        con.setRequestProperty("Accept", "application/json");
//	        con.setRequestProperty("Content-Type", "application/json");
//
//	        // Enable input/output streams
////	        con.setDoInput(true);
//	        con.setDoOutput(true);
//
//	        // Write the JSON request to the output stream
//	        try (OutputStream os = con.getOutputStream()) {
//	            byte[] input = jsonRequest.getBytes("utf-8");
//	            os.write(input, 0, input.length);
//	        }
//
//	        int responseCode = con.getResponseCode();
//	        //System.out.println("Response Code: " + responseCode);
//	        LOGGER.info("ResponseCode {"+ responseCode +"}" );
//	        if (responseCode == HttpURLConnection.HTTP_OK) {
//	            // Read the JSON response
//	            ObjectMapper objectMapper = new ObjectMapper();
//	            JsonNode responseJson = objectMapper.readTree(con.getInputStream());
//	            LOGGER.info("responseJson {"+ responseJson +"}" );
//	            // Extract the "result" field
//	            if (responseJson.has("result")) {
//	                String hexNumber = responseJson.get("result").asText();
//	                result = Long.parseLong(hexNumber.substring(2), 16);
//	            }
//	        } else {
//	            // Handle the response code accordingly
//	        }
//	        return result;
//        } catch (Exception e) {
//            // Handle exceptions
//            LOGGER.error("HttpConnector.invokeGet()", e);
////            return defaultJson;
//        }
//		return result;
//	}


	private JSONObject mintNftEthereum(JSONObject input,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
		long result = 0L;
		 JSONObject environmentResponse = new JSONObject();
		 
		//String infuraUrl = "https://polygon-mumbai.infura.io/v3/7591ca9e4ccc415faf028b9dff4c7ce2"; 
//		 String infuraUrl = "https://avalanche-fuji-c-chain.publicnode.com"; // Replace with your Infura URL
//        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
        Web3j web3 = Web3j.build(new HttpService(infuraUrl)); // Replace with your Ethereum node URL


        //String contractAddress = "0xBc0d6D21B33B96fc4ae34a9C7690Fb94dB2477A2"; // Replace with your contract's address
        String abiJson = "[\r\n"
        		+ "	{\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"name\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"symbol\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"constructor\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"anonymous\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"approved\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"Approval\",\r\n"
        		+ "		\"type\": \"event\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"anonymous\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"operator\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": false,\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"approved\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"ApprovalForAll\",\r\n"
        		+ "		\"type\": \"event\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"approve\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string[]\",\r\n"
        		+ "				\"name\": \"data\",\r\n"
        		+ "				\"type\": \"string[]\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"mintNFT\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"safeTransferFrom\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bytes\",\r\n"
        		+ "				\"name\": \"_data\",\r\n"
        		+ "				\"type\": \"bytes\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"safeTransferFrom\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"approved\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"setApprovalForAll\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"anonymous\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"Transfer\",\r\n"
        		+ "		\"type\": \"event\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"transferFrom\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"balanceOf\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"getApproved\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"tokenKey\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"getNFT\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"components\": [\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"uint256\",\r\n"
        		+ "						\"name\": \"tokenId\",\r\n"
        		+ "						\"type\": \"uint256\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"address\",\r\n"
        		+ "						\"name\": \"tokenOwner\",\r\n"
        		+ "						\"type\": \"address\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar1\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar2\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar3\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar4\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar5\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar6\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar7\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar8\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar9\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar10\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar11\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					}\r\n"
        		+ "				],\r\n"
        		+ "				\"internalType\": \"struct ERC721Full.properties\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"tuple\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"operator\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"isApprovedForAll\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [],\r\n"
        		+ "		\"name\": \"name\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"ownerOf\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bytes4\",\r\n"
        		+ "				\"name\": \"interfaceId\",\r\n"
        		+ "				\"type\": \"bytes4\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"supportsInterface\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [],\r\n"
        		+ "		\"name\": \"symbol\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"index\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"tokenByIndex\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"index\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"tokenOfOwnerByIndex\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"tokenURI\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [],\r\n"
        		+ "		\"name\": \"totalSupply\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	}\r\n"
        		+ "]";
        //String privateKey = "8c8a822798b85b2401632b75804655cc6be30495f03518f057279b4e8083b2b9"; // Replace with your private key
        
        Credentials credentials = Credentials.create(privateKey);

        RawTransactionManager transactionManager = new RawTransactionManager(web3, credentials);

        String functionName = "mintNFT"; 
        List<Utf8String> utf8StringData  = convertJsonToUtf8String(input);

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new org.web3j.abi.datatypes.DynamicArray<>(
                utf8StringData
        ));
//        List<Utf8String> utf8StringData  = convertJsonToUtf8String(input);
//        List<Type> inputParameters = convertUtf8StringListToTypeList(utf8StringData);

//        List<Type> inputParameters = Arrays.asList(
//                new Utf8String("tokenKey"),			        		
//                new Utf8String("fVar1"),
//                new Utf8String("fVar2"),
//                new Utf8String("fVar3"),
//                new Utf8String("fVar4"),
//                new Utf8String("fVar5"),
//                new Utf8String("fVar6"),
//                new Utf8String("fVar7"),
//                new Utf8String("fVar8"),
//                new Utf8String("fVar9"),
//                new Utf8String("fVar10")
//            );
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        
        Function function = new Function(
                functionName,
                inputParameters,
                outputParameters
            );
        String encodedFunction = FunctionEncoder.encode(function);					
								
         

        // Send the raw transaction
       
        try {
        	LOGGER.info("Thread {"+ Thread.currentThread()+"} MintEThereumChecking =>  "+
	  				", uuid => "+input.optString("uuid","Error"));
     	Thread.sleep(1000*15);        	
        	result = generateNoncevalue(nonceApiUrl);
        	LOGGER.info("resultnonce {"+ result +"}" );
//        	BigInteger nonce = web3.ethGetTransactionCount("0xd72558ab56489747360657ab4802176ce18b49e5", DefaultBlockParameterName.PENDING).send().getTransactionCount();

        	BigInteger bigInteger = new BigInteger(Long.toString(result));
//        	BigInteger incrementedNonce = bigInteger.add(BigInteger.ONE); 
//        	long chainId = 80001; 
        	//BigInteger gasPrice = BigInteger.valueOf(gasPrice);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
            		bigInteger,
            		gasPrice,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddress,
                    encodedFunction
//                    BigInteger.ZERO // Value to send with the transaction (usually "0" for function calls)
                );
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,chainId,credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            //String url = infuraUrl;
            LOGGER.info("Thread {"+ Thread.currentThread()+"} Signedmessage =>  "+
	  				", uuid => "+input.optString("uuid","Error"));
//            String url ="https://avalanche-fuji-c-chain.publicnode.com";
            // Create an instance of URL and open a connection
            URL obj = new URL(infuraUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set the request method to POST
            con.setRequestMethod("POST");

            // Set headers
            con.setRequestProperty("Content-Type", "application/json");

            // Enable input and output streams
            con.setDoOutput(true);

            // Create the JSON request body
            String jsonBody = "{\n" +
                    "  \"jsonrpc\": \"2.0\",\n" +
                    "  \"method\": \"eth_sendRawTransaction\",\n" +
                    "  \"params\": [\"" + hexValue + "\"],\n" +
                    "  \"id\": 1\n" +
                    "}";
            LOGGER.info("Thread {"+ Thread.currentThread()+"} hexValue =>  "+hexValue+
	  				", uuid => "+input.optString("uuid","Error"));
            // Write the JSON request body to the output stream
            try (OutputStream os = con.getOutputStream()) {
                byte[] input1 = jsonBody.getBytes("utf-8");
                os.write(input1, 0, input1.length);
                LOGGER.info("Thread {"+ Thread.currentThread()+"} outstream =>  "+os+
    	  				", uuid => "+input.optString("uuid","Error"));
            }

            // Get the HTTP response code
            int responseCode = con.getResponseCode();
           
            System.out.println("Response Code : " + responseCode);
            LOGGER.info("Thread {"+ Thread.currentThread()+"} responseCode =>  "+responseCode+
	  				", tokenKey => "+input.optString("tokenKey","Error"));
            // Read the response content
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                LOGGER.info("responseCode {"+ response.toString() +"}" );
                // Print the response content
                System.out.println(response.toString());
                JSONObject jsonObject = new JSONObject(response.toString().trim());
//              String hash1 = jsonObject.getString("result");
              String hash = jsonObject.optString("result","exception");
//                try {
//                    if (response != null) {
//                        String uuid = input.optString("tokenKey");
//                        if (uuid != null && !uuid.isEmpty()) {
//                            environmentResponse.put("uuid", uuid);
//                        } else {
//                            // Handle the case where "uuid" is null or empty
//                            LOGGER.error("UUID is null or empty in the input.");
//                        }
//                        return environmentResponse;
//                    } else {
//                        // Handle the case where "response" is null
//                        LOGGER.error("Response is null.");
//                    }
//                } catch (Exception e) {
//                    LOGGER.error("Error occurred: " + e.getMessage(), e);
//                }
// 
           	 if(hash != null) {
           		 
           				 environmentResponse.put("uuid", input.optString("tokenKey"));
           			     return environmentResponse;
           	 }
   		} 
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
//        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

        
//        if (!ethSendTransaction.hasError()) {
//            System.out.println("Transaction successful! Transaction hash: " + ethSendTransaction.getTransactionHash());
//        } else {
//            System.out.println("Transaction failed! Error: " + ethSendTransaction.getError().getMessage());
//        }
        
		return environmentResponse;
	}
	
	
	public JSONObject mintNftEthereumSigmacompliance(JSONObject input,String infuraUrl,String contractAddress,String privateKey,int chainId,BigInteger gasPrice,String nonceApiUrl) {
		long result = 0L;
		 JSONObject environmentResponse = new JSONObject();
		 
		//String infuraUrl = "https://polygon-mumbai.infura.io/v3/7591ca9e4ccc415faf028b9dff4c7ce2"; 
//		 String infuraUrl = "https://avalanche-fuji-c-chain.publicnode.com"; // Replace with your Infura URL
//        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
        Web3j web3 = Web3j.build(new HttpService(infuraUrl)); // Replace with your Ethereum node URL


        //String contractAddress = "0xBc0d6D21B33B96fc4ae34a9C7690Fb94dB2477A2"; // Replace with your contract's address
        String abiJson = "[\r\n"
        		+ "	{\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"name\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"symbol\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"constructor\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"anonymous\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"approved\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"Approval\",\r\n"
        		+ "		\"type\": \"event\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"anonymous\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"operator\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": false,\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"approved\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"ApprovalForAll\",\r\n"
        		+ "		\"type\": \"event\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"approve\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string[]\",\r\n"
        		+ "				\"name\": \"data\",\r\n"
        		+ "				\"type\": \"string[]\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"mintNFT\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"safeTransferFrom\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bytes\",\r\n"
        		+ "				\"name\": \"_data\",\r\n"
        		+ "				\"type\": \"bytes\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"safeTransferFrom\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"approved\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"setApprovalForAll\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"anonymous\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"indexed\": true,\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"Transfer\",\r\n"
        		+ "		\"type\": \"event\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": false,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"from\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"to\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"transferFrom\",\r\n"
        		+ "		\"outputs\": [],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"nonpayable\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"balanceOf\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"getApproved\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"tokenKey\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"getNFT\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"components\": [\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"uint256\",\r\n"
        		+ "						\"name\": \"tokenId\",\r\n"
        		+ "						\"type\": \"uint256\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"address\",\r\n"
        		+ "						\"name\": \"tokenOwner\",\r\n"
        		+ "						\"type\": \"address\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar1\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar2\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar3\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar4\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar5\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar6\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar7\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar8\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar9\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar10\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					},\r\n"
        		+ "					{\r\n"
        		+ "						\"internalType\": \"string\",\r\n"
        		+ "						\"name\": \"fVar11\",\r\n"
        		+ "						\"type\": \"string\"\r\n"
        		+ "					}\r\n"
        		+ "				],\r\n"
        		+ "				\"internalType\": \"struct ERC721Full.properties\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"tuple\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"operator\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"isApprovedForAll\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [],\r\n"
        		+ "		\"name\": \"name\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"ownerOf\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bytes4\",\r\n"
        		+ "				\"name\": \"interfaceId\",\r\n"
        		+ "				\"type\": \"bytes4\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"supportsInterface\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"bool\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"bool\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [],\r\n"
        		+ "		\"name\": \"symbol\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"index\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"tokenByIndex\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"address\",\r\n"
        		+ "				\"name\": \"owner\",\r\n"
        		+ "				\"type\": \"address\"\r\n"
        		+ "			},\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"index\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"tokenOfOwnerByIndex\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"tokenId\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"name\": \"tokenURI\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"string\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"string\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	},\r\n"
        		+ "	{\r\n"
        		+ "		\"constant\": true,\r\n"
        		+ "		\"inputs\": [],\r\n"
        		+ "		\"name\": \"totalSupply\",\r\n"
        		+ "		\"outputs\": [\r\n"
        		+ "			{\r\n"
        		+ "				\"internalType\": \"uint256\",\r\n"
        		+ "				\"name\": \"\",\r\n"
        		+ "				\"type\": \"uint256\"\r\n"
        		+ "			}\r\n"
        		+ "		],\r\n"
        		+ "		\"payable\": false,\r\n"
        		+ "		\"stateMutability\": \"view\",\r\n"
        		+ "		\"type\": \"function\"\r\n"
        		+ "	}\r\n"
        		+ "]";
        //String privateKey = "8c8a822798b85b2401632b75804655cc6be30495f03518f057279b4e8083b2b9"; // Replace with your private key
        
        Credentials credentials = Credentials.create(privateKey);

        RawTransactionManager transactionManager = new RawTransactionManager(web3, credentials);

        String functionName = "mintNFT"; 
        List<Utf8String> utf8StringData  = convertJsonToUtf8String(input);

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new org.web3j.abi.datatypes.DynamicArray<>(
                utf8StringData
        ));
//        List<Utf8String> utf8StringData  = convertJsonToUtf8String(input);
//        List<Type> inputParameters = convertUtf8StringListToTypeList(utf8StringData);

//        List<Type> inputParameters = Arrays.asList(
//                new Utf8String("tokenKey"),			        		
//                new Utf8String("fVar1"),
//                new Utf8String("fVar2"),
//                new Utf8String("fVar3"),
//                new Utf8String("fVar4"),
//                new Utf8String("fVar5"),
//                new Utf8String("fVar6"),
//                new Utf8String("fVar7"),
//                new Utf8String("fVar8"),
//                new Utf8String("fVar9"),
//                new Utf8String("fVar10")
//            );
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        
        Function function = new Function(
                functionName,
                inputParameters,
                outputParameters
            );
        String encodedFunction = FunctionEncoder.encode(function);					
								
         

        // Send the raw transaction
       
        try {
        	LOGGER.info("Thread {"+ Thread.currentThread()+"} MintEThereumChecking =>  "+
	  				", uuid => "+input.optString("uuid","Error"));
     	Thread.sleep(1000*15);        	
        	result = generateNoncevalue(nonceApiUrl);
//        	BigInteger nonce = web3.ethGetTransactionCount("0xd72558ab56489747360657ab4802176ce18b49e5", DefaultBlockParameterName.PENDING).send().getTransactionCount();

        	BigInteger bigInteger = new BigInteger(Long.toString(result));
//        	BigInteger incrementedNonce = bigInteger.add(BigInteger.ONE); 
        	//long chainId = 80001; 
        	//BigInteger gasPrice = BigInteger.valueOf(2500000016L);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
            		bigInteger,
            		gasPrice,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddress,
                    encodedFunction
//                    BigInteger.ZERO // Value to send with the transaction (usually "0" for function calls)
                );
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,chainId,credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            //String url = "https://polygon-mumbai.infura.io/v3/7591ca9e4ccc415faf028b9dff4c7ce2";
            LOGGER.info("Thread {"+ Thread.currentThread()+"} Signedmessage =>  "+
	  				", uuid => "+input.optString("uuid","Error"));
//            String url ="https://avalanche-fuji-c-chain.publicnode.com";
            // Create an instance of URL and open a connection
            URL obj = new URL(infuraUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set the request method to POST
            con.setRequestMethod("POST");

            // Set headers
            con.setRequestProperty("Content-Type", "application/json");

            // Enable input and output streams
            con.setDoOutput(true);

            // Create the JSON request body
            String jsonBody = "{\n" +
                    "  \"jsonrpc\": \"2.0\",\n" +
                    "  \"method\": \"eth_sendRawTransaction\",\n" +
                    "  \"params\": [\"" + hexValue + "\"],\n" +
                    "  \"id\": 1\n" +
                    "}";
            LOGGER.info("Thread {"+ Thread.currentThread()+"} hexValue =>  "+hexValue+
	  				", uuid => "+input.optString("uuid","Error"));
            // Write the JSON request body to the output stream
            try (OutputStream os = con.getOutputStream()) {
                byte[] input1 = jsonBody.getBytes("utf-8");
                os.write(input1, 0, input1.length);
                LOGGER.info("Thread {"+ Thread.currentThread()+"} outstream =>  "+os+
    	  				", uuid => "+input.optString("uuid","Error"));
            }

            // Get the HTTP response code
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            LOGGER.info("Thread {"+ Thread.currentThread()+"} responseCode =>  "+responseCode+
	  				", tokenKey => "+input.optString("tokenKey","Error"));
            // Read the response content
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Print the response content
                System.out.println(response.toString());
                JSONObject jsonObject = new JSONObject(response.toString().trim());
//              String hash1 = jsonObject.getString("result");
              String hash = jsonObject.optString("result","exception");
//                try {
//                    if (response != null) {
//                        String uuid = input.optString("tokenKey");
//                        if (uuid != null && !uuid.isEmpty()) {
//                            environmentResponse.put("uuid", uuid);
//                        } else {
//                            // Handle the case where "uuid" is null or empty
//                            LOGGER.error("UUID is null or empty in the input.");
//                        }
//                        return environmentResponse;
//                    } else {
//                        // Handle the case where "response" is null
//                        LOGGER.error("Response is null.");
//                    }
//                } catch (Exception e) {
//                    LOGGER.error("Error occurred: " + e.getMessage(), e);
//                }
// 
           	 if(hash != null) {
           		 
           				 environmentResponse.put("uuid", input.optString("tokenKey"));
           			     return environmentResponse;
           	 }
   		} 
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
//        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

        
//        if (!ethSendTransaction.hasError()) {
//            System.out.println("Transaction successful! Transaction hash: " + ethSendTransaction.getTransactionHash());
//        } else {
//            System.out.println("Transaction failed! Error: " + ethSendTransaction.getError().getMessage());
//        }
        
		return environmentResponse;
	}
	private List<Utf8String> convertJsonToUtf8String(JSONObject input) {
        List<Utf8String> utf8StringList = new ArrayList<>();

        utf8StringList.add(new Utf8String(input.optString("tokenKey")));
        utf8StringList.add(new Utf8String(input.optString("fVar1")));
        utf8StringList.add(new Utf8String(input.optString("fVar2")));
        utf8StringList.add(new Utf8String(input.optString("fVar3")));
        utf8StringList.add(new Utf8String(input.optString("fVar4")));
        utf8StringList.add(new Utf8String(input.optString("fVar5")));
        utf8StringList.add(new Utf8String(input.optString("fVar6")));
        utf8StringList.add(new Utf8String(input.optString("fVar7")));
        utf8StringList.add(new Utf8String(input.optString("fVar8")));
        utf8StringList.add(new Utf8String(input.optString("fVar9")));
        utf8StringList.add(new Utf8String(input.optString("fVar10")));
        utf8StringList.add(new Utf8String(input.optString("fVar11")));
       
        return utf8StringList;
    }
	
	private List<Type> convertUtf8StringListToTypeList(List<Utf8String> utf8StringList) {
        List<Type> typeList = new ArrayList<>();
        typeList.addAll(utf8StringList);
        return typeList;
    }
	private JSONObject getDefaultAddress(String url, String consortiaId, String envId, 
			String nodeId2, String token2) {
		try {
		String url1 = url+"api/v1/consortia/"+consortiaId+"/environments/"+envId+"/nodes/"+nodeId2;
		HttpConnector connector = new HttpConnector(url1);
		String invokeGet = connector.invokeGet(token2);
		 JSONObject networkObject = new JSONObject(invokeGet);
		return networkObject;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.getDefaultAddress()", e);
			return null;
		}
	}
	private JSONObject deploySmartContract(String url, String userName, String password) {
		JSONObject defaultValue = new JSONObject();
		HttpConnector connector = new HttpConnector(url);
		JSONObject input = new JSONObject();
		input.put("name", "name1");
		input.put("symbol", "symbol1");
		//v
		String encoded = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
		try {
			JSONObject environmentResponse = connector.invokePostAndGetJson(url, 
					input.toString(), null, encoded, true);
			return environmentResponse;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.deploySmartContract()", e);
			return defaultValue;
		}
	}
	private JSONObject getNetwork(String url, String token, String consortiumId) {
		try {
		String url1 = url+"api/v1/consortia/"+consortiumId+"/memberships";
		HttpConnector connector = new HttpConnector(url1);
		String invokeGet = connector.invokeGet(token);
		 JSONArray responseFromNode= new JSONArray(invokeGet);
		 JSONObject networkObject = responseFromNode.getJSONObject(0);
		return networkObject;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.getNetwork()", e);
			return null;
		}
	}
	private JSONObject makeAppCreds(String url, String bearerToken, String consortiaId, String envId, String membershipId) {
		JSONObject defaultValue = new JSONObject();
		String url1 = url+"api/v1/consortia/"+consortiaId+"/environments/"+envId+"/appcreds";
		HttpConnector connector = new HttpConnector(url1);
		JSONObject input = new JSONObject();
		input.put("environment_id", envId);
		input.put("membership_id", membershipId);
		try {
			JSONObject environmentResponse = connector.invokePostAndGetJson(input.toString(), bearerToken);
			return environmentResponse;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.makeAppCreds()", e);
			return defaultValue;
		}
	}
	
	public String getTransactionsByPage(String bearerToken, PageRequestBean pr, 
			JdbcTemplate jdbcTemplate) {
		PrivateNetworkPersistence3 pnp = new PrivateNetworkPersistence3();
		try {
		PrivateNetwork2 nw = pnp.getNetworkByTenant(jdbcTemplate, pr.getTenantId());
		String url1 = nw.getNetworkAPIUrl()+"ledger/"+nw.getConsortiaId() +"/"+nw.getEnvId()+"/addresses/"
				+nw.getSmartContractAddress()+"/transactions?start="+pr.getStart()+"&limit="+
				pr.getLimit();
		HttpConnector connector = new HttpConnector(url1);
		String invokeGet = connector.invokeGet(bearerToken);
		return invokeGet;
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.makeAppCreds()", e);
			return "";
		}
	}
	private JSONObject makeNode(String url, String token, String consortiaId, String envId, String membershipId) {
		JSONObject defaultResponse = new JSONObject();
		String url1 = url+"api/v1/consortia/"+consortiaId+"/environments/"+envId+"/nodes";
		HttpConnector connector = new HttpConnector(url1);
		JSONObject input = new JSONObject();
		input.put("membership_id", membershipId);
		input.put("name", "peAPI1");
		input.put("init_consensus_role", "non-signer");
		try {
			JSONObject environmentResponse = connector.invokePostAndGetJson(input.toString(),token);
			return environmentResponse;  
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.makeNode()", e);
			return defaultResponse;
		}
	}
	public JSONObject getImmutableRec(String uuid, PrivateNetwork2 networkById, JdbcTemplate jdbcTemplate) {
		List<SigmaAPIDocConfig> sigmaDocFieldConfigList = null;
		try {
			SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
			sigmaDocFieldConfigList = sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, networkById.getTenantId());
			if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty())
				return new JSONObject();			
		} catch (Exception e) {
			LOGGER.error("PolygonEdgeUtil.getImmutableRec() uuid {}, networkId{} ", uuid, networkById,e);
		}
		LOGGER.info("PolygonEdgeUtil.getImmutableRec() networkById {}" + networkById);
		String invokeGet = "";
		JSONObject finalOutput = null;
		String encoded = Base64.getEncoder().encodeToString(
				(networkById.getCreatedByUser() + ":" + networkById.getNetworkName()).getBytes());
		try {
			String targetUrl = networkById.getSmartContractAccessUrl() + "contracts/" +
			networkById.getSmartContractAddress()+"/getNFT?tokenKey=" +		
			java.net.URLEncoder.encode(uuid, "UTF-8");
			HttpConnector connector = new HttpConnector(targetUrl);
			invokeGet = connector.invokeGet(encoded, "Basic ");
			JSONObject outputJson = new JSONObject(invokeGet);			
			JSONObject jsonOutput = outputJson.getJSONObject("output");
			finalOutput = new JSONObject();
			for(SigmaAPIDocConfig config : sigmaDocFieldConfigList) {
				String sigmaField = config.getSigmaField();
				String extField = config.getExtField();
				String sigmaValue = jsonOutput.optString(sigmaField);
				finalOutput.put(extField, sigmaValue);
			}
			finalOutput.put("docChecksum", jsonOutput.optString("fVar10"));
			LOGGER.info("PolygonEdgeUtil.getImmutableRec() readValue {}" + jsonOutput);
			} catch (Exception e) {
				LOGGER.error("PolygonEdgeUtil.getImmutableRec() invokeGet{} ", invokeGet, e);
			}
		return finalOutput;
		}
	
		public String getTransactionsBlocksByPage(String bearerToken, PageRequestBean pr, 
				JdbcTemplate jdbcTemplate) {
			PrivateNetworkPersistence3 pnp = new PrivateNetworkPersistence3();
			try {
			PrivateNetwork2 nw = pnp.getNetworkByTenant(jdbcTemplate, pr.getTenantId());
			String url1 = nw.getNetworkAPIUrl()+"ledger/"+nw.getConsortiaId() +"/"+nw.getEnvId()+"/blocks?start="+pr.getStart()+"&limit="+
					pr.getLimit();
			HttpConnector connector = new HttpConnector(url1);
			String invokeGet = connector.invokeGet(bearerToken);
			return invokeGet;
			} catch (Exception e) {
				LOGGER.error("PolygonEdgeUtil.makeAppCreds()", e);
				return "";
			}
		}
		
		public String getNFTDeatils(String bearerToken, PageRequestBean pr, String id,
				JdbcTemplate jdbcTemplate) {
			PrivateNetworkPersistence3 pnp = new PrivateNetworkPersistence3();
			try {
			PrivateNetwork2 nw = pnp.getNetworkByTenant(jdbcTemplate, pr.getTenantId());
			String url1 = nw.getSmartContractAccessUrl()+"contracts/"+nw.getSmartContractAddress() +"/getNFT?tokenKey="+id;
			String encoded = Base64.getEncoder().encodeToString(
					(nw.getCreatedByUser() + ":" + nw.getNetworkName()).getBytes());
			HttpConnector connector = new HttpConnector(url1);
			String invokeGet = connector.invokeGet(encoded, "Basic ");
//			String invokeGet = connector.invokeGet(bearerToken);
			return invokeGet;
			} catch (Exception e) {
				LOGGER.error("PolygonEdgeUtil.makeAppCreds()", e);
				return "";
			}
		}
}
