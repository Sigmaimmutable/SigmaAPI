package com.sigma.affinity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FormSubmitPlainJava2 {
	public static void main(String[] args) throws Exception {
		new FormSubmitPlainJava2().postFileData("https://u0x08yevep-u0wjblelve-connect.us0-aws.kaleido.io/abis");
	}
	public void postFileData(String apiUrl) throws Exception {
		   URL url = new URL(apiUrl);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Authorization", "Basic dTBidzFtY2xiMDpEVmtaTVRvclZQM095WnpPTmtHYVZtOWFJWlV0Ymc0S0ZRb3ZVSG13dlFr");
		    connection.setDoOutput(true);

		    List<String> formData = new ArrayList<>();
		    formData.add("file=@\"/D://Examples//Sigma_Ent//NFT_working.sol\"");
		    formData.add("compiler=\"0.5\"");
		    formData.add("contract=\"NFT_working.sol:ERC721Full\"");

		    byte[] formDataBytes = String.join("&", formData).getBytes();
		    connection.getOutputStream().write(formDataBytes);

		    int responseCode = connection.getResponseCode();
		    System.out.println("Response code: " + responseCode);

		    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    String line;
		    while ((line = reader.readLine()) != null) {
		      System.out.println(line);
		    }
		    reader.close();
		    connection.disconnect();		
	}
}
