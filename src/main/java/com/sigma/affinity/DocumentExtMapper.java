package com.sigma.affinity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.SigmaDocument;

public class DocumentExtMapper {
	public JSONArray getUserView(List<SigmaDocument> docs, List<SigmaAPIDocConfig> configs) {
		JSONArray docsArray = new JSONArray();
		for(SigmaDocument document : docs) {
			docsArray.put(getUserView(document, configs));
//			JSONObject docJson = null;
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.enable(SerializationFeature.INDENT_OUTPUT);
//			try {
//				String json = mapper.writeValueAsString(document);
//				docJson = new JSONObject(json);
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//			JSONObject targetJson = new JSONObject();
//			for(SigmaAPIDocConfig conf : configs) {
//				String extField = conf.getExtField();
//				String sigmaField = conf.getSigmaField();
//				String sigmaValue = docJson.optString(sigmaField);
//				targetJson.put(extField, sigmaValue);
//			}
//			docsArray.put(targetJson);
		}
		return docsArray;
	}
	public JSONObject getUserView(SigmaDocument doc, List<SigmaAPIDocConfig> configs) {
			JSONObject docJson = null;
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			try {
				String json = mapper.writeValueAsString(doc);
				docJson = new JSONObject(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			JSONObject targetJson = new JSONObject();
			for(SigmaAPIDocConfig conf : configs) {
				String extField = conf.getExtField();
				String sigmaField = conf.getSigmaField();
				String sigmaValue = docJson.optString(sigmaField);
				targetJson.put(extField, sigmaValue);
			}
			String sigmaId= docJson.optString("sigmaId");
			targetJson.put("sigmaId", sigmaId);
		return targetJson;
	}
}
