package com.sigma.affinity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
	public JSONObject mintNftForDocument(PrivateNetwork2 privateNetwork2,
			SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList) {
		try {
		String mintNftUrl = privateNetwork2.getSmartContractAccessUrl()+"contracts"+"/"+ privateNetwork2.getSmartContractAddress() +
				"/mintNFT?kld-from="	+ privateNetwork2.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
		JSONObject nftInfo = new PolygonEdgeUtil().mintNft(mintNftUrl,
				privateNetwork2.getCreatedByUser(), privateNetwork2.getNetworkName(), 
				documentO, sigmaDocFieldConfigList); //v
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
	private JSONObject mintNft(String mintNftUrl, String userName, String password, 
			SigmaDocument documentO, List<SigmaAPIDocConfig> sigmaDocFieldConfigList) {
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
		for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigList) {
			String sigmaField = sigmaAPIDocConfig.getSigmaField();
			String targetExtField = documentOJson.optString(sigmaField);
			input.put(sigmaField, targetExtField);
			}
		int sizeOfInput = sigmaDocFieldConfigList.size()+1;
		for(int counter=sizeOfInput; counter<=10;counter++) {
			input.put("fVar"+counter, "");
		}
		input.put("fVar10", documentO.getDocChecksum());
		return mintNft(mintNftUrl, userName, password, input);
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
