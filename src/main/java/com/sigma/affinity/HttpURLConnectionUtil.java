package com.sigma.affinity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.Web3Block2;
import com.sigma.model.Web3Tx3;
import com.sigma.model.db.Web3BlockPersistence3;
import com.sigma.model.db.Web3TxPersistence4;

public class HttpURLConnectionUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.aws.affinity.HttpURLConnectionUtil");
	private Integer numberOfBlocksToProcess;
	private JdbcTemplate jdbcTemplate;
	private String url;

	public HttpURLConnectionUtil(String url, Integer numberOfBlocksToProcess, JdbcTemplate jdbcTemplate) {
		super();
		this.numberOfBlocksToProcess = numberOfBlocksToProcess;
		this.jdbcTemplate = jdbcTemplate;
		this.url = url;
	}

	private static JSONObject getJsonForBlockData(String blockNumber) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "eth_getBlockByNumber");
		jsonObject.put("id", 1);
		JSONArray array = new JSONArray();
		array.put(blockNumber);//v //151
		array.put(false);
		jsonObject.put("params", array);
		return jsonObject;
	}
	
	private static JSONObject getJsonForTxData(String txHash) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "eth_getTransactionByHash");
		jsonObject.put("id", 1);
		JSONArray array = new JSONArray();
		array.put(txHash);//v //151
		array.put(false);
		jsonObject.put("params", array);
		return jsonObject;
	}
	
	private JSONObject getJSONBodyForLatestBlock() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "eth_blockNumber");
		jsonObject.put("id", 1);
		return jsonObject;
	}

	public synchronized void findLatestData() {
		try {
			String getInputForLatestBlock = getJSONBodyForLatestBlock().toString();
			HttpConnector connector = new HttpConnector(url);
			JSONObject latestBlockJson = connector.invokePostAndGetJson(getInputForLatestBlock);
			String blockNumber = latestBlockJson.optString("result", "0");
			Long parseLong = getBlockNumberFromHex(blockNumber);
			List<Web3Block2> blockList = new ArrayList<Web3Block2>();
			Map<String, List<Web3Tx3>> mapOfTxList = new HashMap<String, List<Web3Tx3>>();
			parceBlockInfo(parseLong, blockList, mapOfTxList);
			persistBlocks(blockList, mapOfTxList);
			
		} catch (Exception e) {
			LOGGER.error("HttpURLConnectionUtil.findLatestBlock() Error "
					+ "finding latest Block", e);
		}
	}
	private Long getBlockNumberFromHex(String blockNumber) {
		blockNumber = blockNumber.toUpperCase();
		blockNumber = blockNumber.substring(2, blockNumber.length());
		Long parseLong= Long.parseLong(blockNumber, 16);
		return parseLong;
	}
	private void persistTx(List<Web3Tx3> txList) {
		Web3TxPersistence4 txPersistence3 = new Web3TxPersistence4();
		try {
		for(Web3Tx3 web3Tx2 : txList)
			txPersistence3.generateResource(web3Tx2, jdbcTemplate);
			}catch(Exception exception) {
				LOGGER.error("HttpURLConnectionUtil.persistTx() ", txList, exception);
			}	
	}
	private void persistBlocks(List<Web3Block2> blockList, Map<String, 
			List<Web3Tx3>> mapOfTxList) {
		Web3BlockPersistence3 blockPersistence3 = new Web3BlockPersistence3();
		LatestBlockHolder latestBlockHolder = LatestBlockHolder.getInstance();
		latestBlockHolder.getLatestBlock();
		try {
			Long currentBlockNumber = 0l; 
		for(Web3Block2 web3block2 : blockList) {
			currentBlockNumber = web3block2.getBlockNumber();
			if(currentBlockNumber < latestBlockHolder.getLatestBlock())
				continue;
			blockPersistence3.generateResource(web3block2, jdbcTemplate);
			List<Web3Tx3> list = mapOfTxList.get(web3block2.getBlockHash());
			persistTx(list);
		}
		latestBlockHolder.setLatestBlock(currentBlockNumber);
			}catch(Exception exception) {
				LOGGER.error("HttpURLConnectionUtil.persistBlocks()", blockList, exception);
			}	
	}
	private void parceBlockInfo(Long parseInt, List<Web3Block2> blockList, Map<String, List<Web3Tx3>> mapOfTxList) throws Exception {
		Long maxBlockNumber = parseInt;
		for(int counter = 0; counter<numberOfBlocksToProcess; counter++) {
			JSONObject jsonForBlockData = getJsonForBlockData((maxBlockNumber--)+"");
			HttpConnector connector = new HttpConnector(url);
			JSONObject blockDataJson = connector.invokePostAndGetJson(jsonForBlockData.toString());
			JSONObject jsonBlock = blockDataJson.getJSONObject("result");
			Web3Block2 resource = new Web3Block2();
			String optString = jsonBlock.optString("number");
			Long blockNumberFromHex = getBlockNumberFromHex(optString);
			resource.setBlockNumber(blockNumberFromHex);
			resource.setBlockHash(jsonBlock.optString("hash"));
			resource.setMiner(jsonBlock.optString("miner"));
			resource.setTimeStamp(jsonBlock.optString("timestamp"));
			resource.setGasLimit(jsonBlock.optString("gasLimit"));
			resource.setGasUsed(jsonBlock.optString("gasUsed"));
			resource.setNonce(jsonBlock.optString("nonce"));
			blockList.add(resource);
			JSONArray optJSONArray = jsonBlock.optJSONArray("transactions");
			List<Web3Tx3> currentBlockWeb3TxList = new ArrayList<Web3Tx3>();
			for(int txCounter = 0; txCounter<optJSONArray.length(); txCounter++) {
				String txHash= optJSONArray.optString(txCounter);
				JSONObject jsonForTxData = getJsonForTxData(txHash);
				HttpConnector affinity = new HttpConnector(url);
				JSONObject rawTxJson = affinity.invokePostAndGetJson(jsonForTxData.toString());
				JSONObject txJson = rawTxJson.optJSONObject("result");
		    	Web3Tx3 tx = new Web3Tx3();
		    	tx.setTxHash(txJson.optString("hash"));
		    	tx.setNonce(txJson.optString("nonce"));
		    	tx.setGasPrice(txJson.optString("gasPrice"));
		    	tx.setToAddress(txJson.optString("to"));
		    	tx.setValue(txJson.optString("value"));
		    	tx.setFromAddress(txJson.optString("from"));
		    	tx.setBlockHash(txJson.optString("blockHash"));
		    	tx.setBlockNumber(txJson.optString("blockNumber"));
		    	currentBlockWeb3TxList.add(tx);
			}
			mapOfTxList.put(jsonBlock.optString("hash"), currentBlockWeb3TxList);
		}
	}

}