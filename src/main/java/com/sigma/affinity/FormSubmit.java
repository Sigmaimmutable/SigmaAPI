package com.sigma.affinity;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class FormSubmit {
	public String postFileData(String apiUrl, String token,String filePath,
			String contractName, String userName, String password) throws Exception {
		URI uri = URI.create(apiUrl);
		//String encodedCredentials = new String(Base64.getEncoder().encode(token.getBytes()));
		 String encoded = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
	  //    conn.setRequestProperty("Authorization", "Basic " + encoded);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + encoded);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		File file = new File(filePath);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new org.springframework.core.io.FileSystemResource(file));
		body.add("compiler", "0.5");
		body.add("contract", contractName);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(uri, requestEntity, String.class);
		System.out.println("Response: " + response);
		return response;
	}
}
