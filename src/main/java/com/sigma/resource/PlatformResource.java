package com.sigma.resource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.algo.files.FileSystemStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.affinity.CrateIPFS;
import com.sigma.affinity.DocumentExtMapper;
import com.sigma.affinity.DocumentRetrieve;
import com.sigma.affinity.HttpConnector;
import com.sigma.affinity.HttpURLConnectionUtil;
import com.sigma.affinity.ImmutabilityUtil;
import com.sigma.affinity.InterPlanetaryAssist;
import com.sigma.affinity.PieUtil;
import com.sigma.affinity.PolygonEdgeUtil;
import com.sigma.affinity.UniqueIdGenerator;
import com.sigma.affinity.interfaces.GenericArtefactRetriever;
import com.sigma.affinity.interfaces.VeevaImplementor;
import com.sigma.model.DocumentO;
import com.sigma.model.DocumentO2Job;
import com.sigma.model.Favourite;
import com.sigma.model.HelpandSupport;
import com.sigma.model.InfraBean;
import com.sigma.model.Invoice2;
import com.sigma.model.JobManagement;
import com.sigma.model.JobNotifyEmail;
import com.sigma.model.JobTrigger;
import com.sigma.model.Notification;
import com.sigma.model.Organization;
import com.sigma.model.PageRequestBean;
import com.sigma.model.Payment2;
import com.sigma.model.PieBean;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.ProfilePage;
import com.sigma.model.Resource;
import com.sigma.model.ResourceUsage;
import com.sigma.model.Session;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.SigmaDocument;
import com.sigma.model.SigmaDocument2;
import com.sigma.model.SmartContractUsage;
import com.sigma.model.TenantDocSource2;
import com.sigma.model.UserInfo;
import com.sigma.model.UserProfile;
import com.sigma.model.UserVisit;
import com.sigma.model.Web3Block2;
import com.sigma.model.Web3Tx3;
import com.sigma.model.db.DocumentOPersistence4;
import com.sigma.model.db.FavouritePersistence;
import com.sigma.model.db.HelpandsupportPersistence;
import com.sigma.model.db.InvoiceGenerator;
import com.sigma.model.db.InvoicePersistence4;
import com.sigma.model.db.JobManagementPersistence;
import com.sigma.model.db.JobNotifyEmailPersistence;
import com.sigma.model.db.JobOPersistence5;
import com.sigma.model.db.JobTriggerPersistence;
import com.sigma.model.db.NotificationPersistence;
import com.sigma.model.db.OrganizationPersistence6;
import com.sigma.model.db.PaymentPersistence3;
import com.sigma.model.db.PrivateNetworkPersistence3;
import com.sigma.model.db.ProfilePagePersistence;
import com.sigma.model.db.ResourcePersistence2;
import com.sigma.model.db.ResourceUsagePersistence3;
import com.sigma.model.db.SessionPersistence;
import com.sigma.model.db.SigmaDocFieldConfigPersistence6;
import com.sigma.model.db.SigmaDocumentPersistence5;
import com.sigma.model.db.SigmaDocumentPersistence52;
import com.sigma.model.db.SigmaProps;
import com.sigma.model.db.SmartContractTemplateUsagePersistence4;
import com.sigma.model.db.TenantDocSourcePersistence7;
import com.sigma.model.db.UserInfoPersistence;
import com.sigma.model.db.UserPersistence;
import com.sigma.model.db.UserVisitPersistence2;
import com.sigma.model.db.Web3BlockPersistence3;
import com.sigma.model.db.Web3TxPersistence4;

@RestController
public class PlatformResource {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.resource.PlatformResource");
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${web3.default.url:http://3.133.134.36:10002/}")
	private String web3RpcURl;	
	@Value("${web3.default.blocks.to.pricess:10}")
	private Integer noOfBlocksToProcess;
	@Value("${sigma.default.batch.size.nft.create:10}")
	private Integer nftBatchSize;
	@Value("${nft.image.storage.path:/home/usr}")
	private String nftImageRepositoryLocation;
	 
 	@Value("${blockchain.infuraUrl}")
private String infuraUrl;
 	@Value("${blockchain.transactiondataUrl}")
 	private String transactiondataUrl;

@Value("${blockchain.contractAddress}")
private String contractAddress;

@Value("${blockchain.privateKey}")
private String privateKey;

@Value("${blockchain.chainId}")
private int chainId;

@Value("${blockchain.gasPrice}")
private BigInteger gasPrice;

@Value("${blockchain.nonceApiUrl}")
private String nonceApiUrl;
@Value("${blockchain.account}")
	private String account;
@Value("${ec2IP1}")
private String ec2IP1;
@Value("${ec2IP2}")
private String ec2IP2;
@Value("${ec2IP3}")
private String ec2IP3;
	@Autowired
	private FileSystemStorageService storageService;
	
	@PostMapping(value = "/v1/userinfo")
	public ResponseEntity<String> generateUserInfo(@RequestBody UserProfile userProfile) throws Exception {
		try {
			JSONObject responseJson = new JSONObject();
			UserPersistence userPersistence = new UserPersistence();
			int insertCount = userPersistence.createUserProfile(userProfile, jdbcTemplate);
			if (insertCount == 1)
				responseJson.put("result", "User Create / Update Successful !");
			else
				responseJson.put("result", "User Create / Update Failed !");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}

	@PutMapping(value = "/v1/userinfo")
	public ResponseEntity<String> updateUserInfo(@RequestBody UserProfile userProfile) throws Exception {
		try {
			JSONObject responseJson = new JSONObject();
			UserPersistence userPersistence = new UserPersistence();
			int insertCount = userPersistence.updateUser(userProfile, jdbcTemplate);
			if (insertCount == 1)
				responseJson.put("result", "User Create / Update Successful !");
			else
				responseJson.put("result", "User Create / Update Failed !");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}
	@GetMapping(value = "/v1/userinfo/{id}")
	public ResponseEntity<UserProfile> getUserInfo(@PathVariable("id") String id) throws Exception {
		try {
			UserPersistence userPersistence = new UserPersistence();
			UserProfile userProfile = userPersistence.getUserProfile(jdbcTemplate, id);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}

	@PostMapping(value = "/v1/userexists")
	public ResponseEntity<String> generateUserInfoCheck(@RequestBody UserProfile userProfile) throws Exception {
		try {
			JSONObject responseJson = new JSONObject();
			UserPersistence userPersistence = new UserPersistence();
			UserProfile userProfileFromDb = userPersistence.getUserProfileCheck(jdbcTemplate, userProfile);
			if (userProfileFromDb != null)
				responseJson.put("result", "Y");
			else
				responseJson.put("result", "N");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}
	
	@GetMapping(value = "/v1/userinfo")
	public ResponseEntity<List<UserProfile>> getUserInfo() throws Exception {
		try {
			UserPersistence userPersistence = new UserPersistence();
			List<UserProfile> userProfile = userPersistence.getUserProfile(jdbcTemplate);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}
	
	// Resources
	@PostMapping(value = "/v1/resource")
	public ResponseEntity<String> generateResource(@RequestBody Resource resource) throws Exception {
		try {
			JSONObject responseJson = new JSONObject();
			ResourcePersistence2 resourcePersistence = new ResourcePersistence2();
			int insertCount = resourcePersistence.generateResource(resource, jdbcTemplate);
			if (insertCount == 1)
				responseJson.put("result", "resource Create / Update Successful !");
			else
				responseJson.put("result", "resource Create / Update Failed !");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}
	@PutMapping(value = "/v1/resource")
	public ResponseEntity<String> updateResource(@RequestBody Resource resource) throws Exception {
		try {
			JSONObject responseJson = new JSONObject();
			ResourcePersistence2 resourcePersistence = new ResourcePersistence2();
			int insertCount = resourcePersistence.updateResource(resource, jdbcTemplate);
			if (insertCount == 1)
				responseJson.put("result", "resource Create / Update Successful !");
			else
				responseJson.put("result", "resource Create / Update Failed !");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}
	@GetMapping(value = "/v1/resource/{id}")
	public ResponseEntity<Resource> getResourceInfo(@PathVariable("id") String id) throws Exception {
		try {
			ResourcePersistence2 resourcePersistence = new ResourcePersistence2();
			Resource resource = resourcePersistence.getResource(jdbcTemplate, id);
			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}

	@GetMapping(value = "/v1/resource")
	public ResponseEntity<List<Resource>> getResourceList() throws Exception {
		try {
			ResourcePersistence2 resourcePersistence = new ResourcePersistence2();
			List<Resource> userProfile = resourcePersistence.getResourceList(jdbcTemplate);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Error while getting the location risk result.", exception);
			throw new Exception("Error while getting the location risk result");
		}
	}
	
	// Resource Usage
		@PostMapping(value = "/v1/reusage")
		public ResponseEntity<String> generateResourceUsage(@RequestBody ResourceUsage resourceUsage) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				ResourceUsagePersistence3 resourcePersistence = new ResourceUsagePersistence3();
				int insertCount = resourcePersistence.generateResourceUsage(resourceUsage, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PutMapping(value = "/v1/reusage/endusage")
		public ResponseEntity<String> updateResourceUsage(@RequestBody ResourceUsage resourceUsage) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				ResourceUsagePersistence3 resourcePersistence = new ResourceUsagePersistence3();
				int insertCount = resourcePersistence.updateResourceUsage(resourceUsage, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PutMapping(value = "/v1/reusage/endusagepay")
		public ResponseEntity<String> updateResourceUsagePay(@RequestBody ResourceUsage resourceUsage) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				ResourceUsagePersistence3 resourcePersistence = new ResourceUsagePersistence3();
				int insertCount = resourcePersistence.updateResourceUsage2Paid(resourceUsage, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/reusage/{id}")
		public ResponseEntity<ResourceUsage> getResourceUsageInfo(@PathVariable("id") String id) throws Exception {
			try {
				ResourceUsagePersistence3 resourcePersistence = new ResourceUsagePersistence3();
				ResourceUsage resource = resourcePersistence.getResource(jdbcTemplate, id);
				return new ResponseEntity<>(resource, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/reusage")
		public ResponseEntity<List<ResourceUsage>> getResourceUsageList() throws Exception {
			try {
				ResourceUsagePersistence3 resourcePersistence = new ResourceUsagePersistence3();
				List<ResourceUsage> userProfile = resourcePersistence.getResourceList(jdbcTemplate);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
// payment start
		@PostMapping(value = "/v1/payment")
		public ResponseEntity<String> generatePayment(@RequestBody Payment2 payment) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				PaymentPersistence3 resourcePersistence = new PaymentPersistence3();
				int insertCount = resourcePersistence.generatePayment(payment, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
	/*
		@PutMapping(value = "/v1/resource")
		public ResponseEntity<String> updateResource(@RequestBody Resource resource) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				ResourcePersistence2 resourcePersistence = new ResourcePersistence2();
				int insertCount = resourcePersistence.updateResource(resource, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/resource/{id}")
		public ResponseEntity<Resource> getResourceInfo(@PathVariable("id") String id) throws Exception {
			try {
				ResourcePersistence2 resourcePersistence = new ResourcePersistence2();
				Resource resource = resourcePersistence.getResource(jdbcTemplate, id);
				return new ResponseEntity<>(resource, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
*/
		@GetMapping(value = "/v1/payment/{userId}")
		public ResponseEntity<List<Payment2>> getPaymentList(@PathVariable("userId") String userId) throws Exception {
			try {
				PaymentPersistence3 resourcePersistence = new PaymentPersistence3();
				List<Payment2> userProfile = resourcePersistence.getPaymentList(jdbcTemplate, userId);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
/// payment end

		// invoice
		@GetMapping(value = "/v1/invoice/{userid}")
		public ResponseEntity<Invoice2> getInvoiceListforUser(@PathVariable("userid") String userId) throws Exception {
			try {
				InvoicePersistence4 invoicePersistence4 = new InvoicePersistence4();
				List<Invoice2> invoice = invoicePersistence4.getPendingInvoices(jdbcTemplate, userId);			
				return new ResponseEntity<>(invoice.get(0), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		// invoice

		// Smart contract template usage start 
		// Resources
		@PostMapping(value = "/v1/templateUsage")
		public ResponseEntity<String> generateSmartContractTemplateUsage(@RequestBody SmartContractUsage resource) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				SmartContractTemplateUsagePersistence4 resourcePersistence = new SmartContractTemplateUsagePersistence4();
				int insertCount = resourcePersistence.generateResourceUsage(resource, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "SmartContractUsage Create / Update Successful !");
				else
					responseJson.put("result", "SmartContractUsage Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while creating SmartContractUsage", exception);
				throw new Exception("Error while creating SmartContractUsage");
			}
		}
		@PutMapping(value = "/v1/templateUsage")
		public ResponseEntity<String> updateSmartContractTemplateUsage(@RequestBody SmartContractUsage resource) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				SmartContractTemplateUsagePersistence4 resourcePersistence = new SmartContractTemplateUsagePersistence4();
				int insertCount = resourcePersistence.updateResourceUsage(resource, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "SmartContractUsage Create / Update Successful !");
				else
					responseJson.put("result", "SmartContractUsage Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while creating SmartContractUsage", exception);
				throw new Exception("Error while creating SmartContractUsage");
			}
		}
		@GetMapping(value = "/v1/templateUsage/{id}")
		public ResponseEntity<List<SmartContractUsage>> getSmartContractTemplateUsage(@PathVariable("id") String id) throws Exception {
			try {
				SmartContractTemplateUsagePersistence4 resourcePersistence = new SmartContractTemplateUsagePersistence4();
				List<SmartContractUsage> resource = resourcePersistence.getResourceByUser(jdbcTemplate, id);
				return new ResponseEntity<>(resource, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/templateUsage")
		public ResponseEntity<List<SmartContractUsage>> getSmartContractResourceList() throws Exception {
			try {
				SmartContractTemplateUsagePersistence4 resourcePersistence = new SmartContractTemplateUsagePersistence4();
				List<SmartContractUsage> userProfile = resourcePersistence.getResourceList(jdbcTemplate);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		// Smart contract template usage end
		// User Visits
		@PostMapping(value = "/v1/userVisitRecord")
		public ResponseEntity<String> userVisitsRecorder(@RequestBody UserVisit visit) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				 UserVisitPersistence2 userVisitPersistence= new UserVisitPersistence2();//v
				int insertCount = userVisitPersistence.createUpdateVisit(visit, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/uservisit")
		public ResponseEntity<List<UserVisit>> getUserInfo1() throws Exception {
			try {
				UserVisitPersistence2 userVisitPersistence = new UserVisitPersistence2();
				List<UserVisit> userVisit = userVisitPersistence.getUserVisit(jdbcTemplate);
				return new ResponseEntity<>(userVisit, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/uservisit/{id}")
		public ResponseEntity<List<UserVisit>>  getUserVisit(@PathVariable("id") String id) throws Exception {
			try {
				UserVisitPersistence2 userVisitPersistence2 = new UserVisitPersistence2();
				List<UserVisit> userVisit = userVisitPersistence2.getUserVisit1(jdbcTemplate, id);
				return new ResponseEntity<>(userVisit, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		// generate invoice
		@PostMapping(value = "/v1/generateinvoice")
		public ResponseEntity<String> generateInvoice() throws Exception {
			try {
				
				 ExecutorService executorService = Executors.newSingleThreadExecutor();
				 Runnable runnable = new Runnable() {					
					@Override
					public void run() {
						new InvoiceGenerator().generateInvoiceOutput(jdbcTemplate);						
					}
				};
				 executorService.submit(runnable);
				return new ResponseEntity<>("Y", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while generating generateInvoice ()", exception);
				throw new Exception("Error while generateInvoice()");
			}
		}
		/// Private Network
		@Value("${sigma.pbc.provider.api.url:https://console.kaleido.io/}")
		private String pbcProviderUrl;
//		@Value("${sigma.pbc.nft.smartcontract.file.path:D:\\Examples\\Sigma_Ent\\NFT_working_v2.sol}")
		@Value("${sigma.pbc.nft.smartcontract.file.path:D:\\documents\\NFT_working_v2.sol}")
		private String nftSmartContractPath;
		@Value("${sigma.pbc.nft.smartcontract.name:NFT_working_v2.sol:ERC721Full}")
		private String nftSmartContractName;
//		@Value("${sigma.pbc.token:u0b3mfl03e-u1W3TK4W3T0eQkx7VkeFYLA8YYMdmAIN1ZnGcuE/p4s=}")
//		@Value("${sigma.pbc.token:u0u8yeotu4-32bL666D2KFGsfR0qEP8ZBRb6v+g2L/7wcCwf3n4uck=}")
		@Value("${sigma.pbc.token:u0flkjpo2c-4MmPccx339y9Cd2bD5OGho0Hmlk+m9BxLiuAB9m4sIU=}")
		private String pbcToken;
		@PostMapping(value = "/v1/network")
		public ResponseEntity<PrivateNetwork2> generateNetwork(@RequestBody PrivateNetwork2 privateNetwork) throws Exception {
			try {
				final PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();	
				String input =""+new java.util.Date();
				final long crc32Checksum = UniqueIdGenerator.getCRC32Checksum(input.getBytes());
				PrivateNetwork2 privateNetwork2 = new PrivateNetwork2();
				privateNetwork2.setId(crc32Checksum);
				privateNetwork2.setStatus("P");
				privateNetwork2.setTenantId(privateNetwork.getTenantId());
				privateNetwork2.setNetworkName(privateNetwork.getNetworkName());
				PrivateNetwork2 insertRec = privateNetworkPersistence3.generateResource(privateNetwork2, jdbcTemplate);
				
				ExecutorService executor = Executors.newFixedThreadPool(10);
				executor.submit(new Runnable() {					
					@Override
					public void run() {						
						PrivateNetwork2 privateNetwork2 = polygonEdgeUtil.makePermissionedBlockChain(
								pbcProviderUrl, pbcToken, nftSmartContractPath, nftSmartContractName);
						privateNetwork2.setId(crc32Checksum);
						privateNetwork2.setStatus("Y");
						privateNetworkPersistence3.updateResource(privateNetwork2, jdbcTemplate);
					}
				});				
				executor.shutdown();				
				String message = "Sit back and relax. Network with id {"+crc32Checksum 
						+ "} will be generated. Please use the GET request {**/v1/network/"+crc32Checksum+ 
						"} to get the network details, It takes approximately takes 3 to 5 mins.";
				insertRec.setStatus(message);
				return new ResponseEntity<>(insertRec, HttpStatus.OK);
			} catch (Exception exception) {			
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/network/{id}")
		public ResponseEntity<List<PrivateNetwork2>> getNetwork(@PathVariable("id") String id) throws Exception {
			try {
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3(); //v
				List<PrivateNetwork2> privateNetwork2 = privateNetworkPersistence3.getResource(jdbcTemplate, id);
				return new ResponseEntity<>(privateNetwork2, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/network")
		public ResponseEntity<List<PrivateNetwork2>> getNetwork() throws Exception {
			try {
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3(); //v
				List<PrivateNetwork2> privateNetwork2 = privateNetworkPersistence3.getResourceList(jdbcTemplate);
				return new ResponseEntity<>(privateNetwork2, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/networkbytennatid/{id}")
		public ResponseEntity<List<PrivateNetwork2>> getNetworkbytennatid(@PathVariable("id") String id) throws Exception {
			try {
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3(); //v
				List<PrivateNetwork2> privateNetwork2 = privateNetworkPersistence3.getResourcebytennatId(jdbcTemplate, id);
				return new ResponseEntity<>(privateNetwork2, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		/// Private Network

		// Get Blocks for a network
		
//		@GetMapping(value = "/v1/blocks")
		public ResponseEntity<List<Web3Block2>> getBlocks() throws Exception {
			try {
				Web3BlockPersistence3 web3BlockPersistence3 = new Web3BlockPersistence3();
				List<Web3Block2> blockList = web3BlockPersistence3.getResourceList(jdbcTemplate);
				return new ResponseEntity<>(blockList , HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}		
//		@GetMapping(value = "/v1/tx")
		public ResponseEntity<List<Web3Tx3>> getTx() throws Exception {
			try {
				Web3TxPersistence4 web3TxPersistence3 = new Web3TxPersistence4();
				List<Web3Tx3> blockList = web3TxPersistence3.getResourceList(jdbcTemplate);
				return new ResponseEntity<>(blockList , HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
//		@PostMapping(value = "/v1/blockpersist")
		public ResponseEntity<String> persistBlockInfo() throws Exception {
			try {
				LOGGER.info("Started persistBlockInfo !");
				HttpURLConnectionUtil util = new HttpURLConnectionUtil(web3RpcURl, noOfBlocksToProcess, jdbcTemplate);
				util.findLatestData();
				LOGGER.info("Completed persistBlockInfo !");
				return new ResponseEntity<>("Successful !" , HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PostMapping(value = "/v1/job")
		public ResponseEntity<List<DocumentO2Job>> getJobsDetails(@RequestBody PageRequestBean bean) throws Exception {
			try {
				JobOPersistence5 jobOPersistence5 = new JobOPersistence5();
				List<DocumentO2Job> privateNetwork2 = jobOPersistence5.getDocumentsByTenantWithPagination(jdbcTemplate, bean);
				return new ResponseEntity<>(privateNetwork2, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		// tx fetch from block provider
		@PostMapping(value = "/v1/transactions")
		public ResponseEntity<String> getTransactions(@RequestBody PageRequestBean bean) throws Exception {
			try {
				PolygonEdgeUtil peu123 = new PolygonEdgeUtil();
				String transactionsByPage = peu123.getTransactionsByPage(pbcToken, bean, jdbcTemplate);
				return new ResponseEntity<String>(transactionsByPage, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}		
		//
		//@GetMapping(value = "/v1/docs/{id}")
		public ResponseEntity<List<DocumentO>> getDocDetails(@PathVariable("id") String id) throws Exception {
			try {
				DocumentOPersistence4 documentOPersistence4 = new DocumentOPersistence4(); 
				List<DocumentO> privateNetwork2 = documentOPersistence4.getPaymentList(jdbcTemplate, id);
				return new ResponseEntity<>(privateNetwork2, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		//@GetMapping(value = "/v1/docs")
		public ResponseEntity<List<DocumentO>> getEntireDocs() throws Exception {
			try {
				DocumentOPersistence4 documentOPersistence4 = new DocumentOPersistence4(); 
				List<DocumentO> privateNetwork2 = documentOPersistence4.getEntireDocsWithLimit(jdbcTemplate);
				return new ResponseEntity<>(privateNetwork2, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}		
		@Value("${sigma.run.docfetch.job.enabled:false}")
		private boolean runDocumentFetchJob;		
		@PostMapping(value = "/v1/docfetch/{tenantid}")
		public ResponseEntity<String> triggerDocumentFetch(@PathVariable("tenantid") String tenantId,
				@RequestBody String id) throws Exception {
			try {
				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,tenantId,"DOC_FETCH");
				
				if(!jobcurrentstatus) {
					LOGGER.info("Exiting the docfetch as it is disabled by flag runDocumentFetchJob {" + jobcurrentstatus + "}");
					return new ResponseEntity<>("Exiting the docfetch as it is disabled by flag runDocumentFetchJob { !" + jobcurrentstatus + "}", HttpStatus.OK);
				}
//				if(!runDocumentFetchJob)
//				{
//					LOGGER.info("Exiting the docfetch as it is disabled by flag runDocumentFetchJob {" + runDocumentFetchJob + "}");
//					return new ResponseEntity<>("Exiting the docfetch as it is disabled by flag runDocumentFetchJob { !" + runDocumentFetchJob + "}", HttpStatus.OK);
//				}
				ExecutorService executor = Executors.newFixedThreadPool(10);
				executor.submit(new Runnable() {					
					@Override
					public void run() {		
				LOGGER.info("populateDocumentData start()");
				DocumentRetrieve documentRetrieve = new DocumentRetrieve();
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				Organization organizationInfo = organizationPersistence6.getOrganizationInfo(jdbcTemplate, tenantId);
				
				TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
				TenantDocSource2 source = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, id);
			//	List<TenantDocSource2> sources = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
				//TenantDocSource2 tenantSourceInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
			//	for(TenantDocSource2 source : sources) {
					if(source.getStatus() != 1)
					try{
						throw new Exception("Invalid tenant source configuration !");						
					}catch(Exception exception) {
						LOGGER.error("Invalid config ", exception);
					}
					SigmaProps props = new SigmaProps(source.getExtUrl()+"/api/v22.3/auth", source.getExtUrl()+"/api/v22.3/query", 
							source.getExtUserName() , source.getExtPassword(), tenantId, true, source.getExtUrl()+"/api/v22.3/objects/documents/");
					Organization org = new Organization();
					org.setTenantId(tenantId);
					SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
					List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
							sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, org.getTenantId());
					try {
							PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
							PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organizationInfo.getTenantId());
							documentRetrieve.findLatestDocuments(props, jdbcTemplate, organizationInfo, sigmaDocFieldConfigList, networkById,"Yes");
					}catch (Exception e) {
							LOGGER.error("PlatformResource.triggerDocumentFetch()", e);
					}
			//	} // doc source for loop
					LOGGER.info("populateDocumentData completed ...");
				} // run method
					
				});				
				executor.shutdown();
				return new ResponseEntity<>("Successfully started doc fetch !", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@Value("${sigma.immutable.job.thread.count:3}")
		private Integer executorThreadCount;
		@Value("${sigma.run.immutable.job.enabled:false}")
		private boolean runImmutableJob;
//		@PostMapping(value = "/v1/finalisedocs")
//		public ResponseEntity<String> invokeIRecJobs() throws Exception{
//			LOGGER.info("Generate And Persist NFT started ...");
////			Long jobId = 1l;
////			if(!runImmutableJob) {
////				LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob = "+runImmutableJob);
////				return new ResponseEntity<>("Immutable Record job existed as it is disabled !", HttpStatus.OK);
////			}
//			ExecutorService executor = Executors.newFixedThreadPool(10);
//			executor.submit(new Runnable() {					
//				@Override
//				public void run() {						
//			OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//			List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//			DocumentRetrieve documentRet = new DocumentRetrieve();
//			for(Organization organization : organizationList) {	
//				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"MAKE_IREC");
//				
//				if(!jobcurrentstatus) {
//					LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob ="+jobcurrentstatus);
//					return;
//				}
//				Long jobId= documentRet.updateJobStatus(0, "P", "", "Started the job !", jdbcTemplate, organization,
//						true, 1l, "MAKE_IREC","Yes");
//				LOGGER.info("Generate And Persist NFT started organization = "+ organization.getTenantId());
//				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//					LOGGER.info("Generate And Persist NFT exited sigmaDocFieldConfigList is empty or null ");
//					continue;
//				}
//				ImmutabilityUtil immutabilityUtil = new ImmutabilityUtil(executorThreadCount);
//				LOGGER.info("Generate And Persist NFT initiated with org id "+ organization.getId());
//				try {
//					PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//					PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//					immutabilityUtil.fetchAndCreateImmutabilityRecords(jdbcTemplate, 
//							nftBatchSize, networkById, sigmaDocFieldConfigList, organization.getTenantId());
//				} catch (Exception e) {
//					LOGGER.error("PlatformResource.invokeIRecJobs()", e);
//				}
//				documentRet.updateJobStatus(0, "Y", "", "Job Complete !", jdbcTemplate, organization,
//						false, jobId, "MAKE_IREC","Yes");
//				LOGGER.info("Generate And Persist NFT completed with org id "+ organization.getId());
//				UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//				Date date = new Date();
//				String formattedStringData = dateFormat.format(date);
//				
//				List<String> emailIds = userInfoPersistence.getEmailIdsJobByTennantId(jdbcTemplate,organization.getTenantId(), 10, formattedStringData);
//				}			
//			LOGGER.info("Generate And Persist NFT ended ...");
//				}
//			});				
//			executor.shutdown();
//			return new ResponseEntity<>("Immutable Record job initiated successfully !", HttpStatus.OK);
//		}
		
		
//		@PostMapping(value = "/v1/finalisedocs")
//		public ResponseEntity<String> invokeIRecJobs() throws Exception{
//			LOGGER.info("Generate And Persist NFT started ...");
////			Long jobId = 1l;
////			if(!runImmutableJob) {
////				LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob = "+runImmutableJob);
////				return new ResponseEntity<>("Immutable Record job existed as it is disabled !", HttpStatus.OK);
////			}
//			ExecutorService executor = Executors.newFixedThreadPool(10);
//			executor.submit(new Runnable() {					
//				@Override
//				public void run() {						
//			OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//			List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//			DocumentRetrieve documentRet = new DocumentRetrieve();
//			for(Organization organization : organizationList) {	
//				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"MAKE_IREC");
//				
//				if(!jobcurrentstatus) {
//					LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob ="+jobcurrentstatus);
//					continue;
//				}
//				Long jobId= documentRet.updateJobStatus(0, "P", "", "Started the job !", jdbcTemplate, organization,
//						true, 1l, "MAKE_IREC","Yes");
//				LOGGER.info("Generate And Persist NFT started organization = "+ organization.getTenantId());
//				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//					LOGGER.info("Generate And Persist NFT exited sigmaDocFieldConfigList is empty or null ");
//					continue;
//				}
//				ImmutabilityUtil immutabilityUtil = new ImmutabilityUtil(executorThreadCount);
//				LOGGER.info("Generate And Persist NFT initiated with org id "+ organization.getId());
//				try {
//					PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//					PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//					immutabilityUtil.fetchAndCreateImmutabilityRecords(jdbcTemplate, 
//							nftBatchSize, networkById, sigmaDocFieldConfigList, organization.getTenantId(), infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
//				} catch (Exception e) {
//					LOGGER.error("PlatformResource.invokeIRecJobs()", e);
//				}
//				documentRet.updateJobStatus(0, "Y", "", "Job Complete !", jdbcTemplate, organization,
//						false, jobId, "MAKE_IREC","Yes");
//				LOGGER.info("Generate And Persist NFT completed with org id "+ organization.getId());
//				UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//				Date date = new Date();
//				String formattedStringData = dateFormat.format(date);
//				
//				List<String> emailIds = userInfoPersistence.getEmailIdsJobByTennantId(jdbcTemplate,organization.getTenantId(), 10, formattedStringData);
//				}			
//			LOGGER.info("Generate And Persist NFT ended ...");
//				}
//			});				
//			executor.shutdown();
//			return new ResponseEntity<>("Immutable Record job initiated successfully !", HttpStatus.OK);
//		}
		@PostMapping(value = "/v1/finalisedocs")
		public ResponseEntity<String> invokeIRecJobs() throws Exception{
			LOGGER.info("Generate And Persist NFT started ...");
//			Long jobId = 1l;
//			if(!runImmutableJob) {
//				LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob = "+runImmutableJob);
//				return new ResponseEntity<>("Immutable Record job existed as it is disabled !", HttpStatus.OK);
//			}
			ExecutorService executor = Executors.newFixedThreadPool(10);
			executor.submit(new Runnable() {					
				@Override
				public void run() {						
			OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
			List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
			DocumentRetrieve documentRet = new DocumentRetrieve();
			for(Organization organization : organizationList) {	
				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"MAKE_IREC");
				
				if(!jobcurrentstatus) {
					LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob ="+jobcurrentstatus);
					continue;
				}
				Long jobId= documentRet.updateJobStatus(0, "P", "", "Started the job !", jdbcTemplate, organization,
						true, 1l, "MAKE_IREC","Yes");
				LOGGER.info("Generate And Persist NFT started organization = "+ organization.getTenantId());
				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
					LOGGER.info("Generate And Persist NFT exited sigmaDocFieldConfigList is empty or null ");
					continue;
				}
				ImmutabilityUtil immutabilityUtil = new ImmutabilityUtil(executorThreadCount);
				LOGGER.info("Generate And Persist NFT initiated with org id "+ organization.getId());
				try {
//					PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//					PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
					immutabilityUtil.fetchAndCreateImmutabilityRecords(jdbcTemplate, 
							nftBatchSize, sigmaDocFieldConfigList, organization.getTenantId(), infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
				} catch (Exception e) {
					LOGGER.error("PlatformResource.invokeIRecJobs()", e);
				}
				documentRet.updateJobStatus(0, "Y", "", "Job Complete !", jdbcTemplate, organization,
						false, jobId, "MAKE_IREC","Yes");
				LOGGER.info("Generate And Persist NFT completed with org id "+ organization.getId());
				UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				Date date = new Date();
				String formattedStringData = dateFormat.format(date);
				
				List<String> emailIds = userInfoPersistence.getEmailIdsJobByTennantId(jdbcTemplate,organization.getTenantId(), 10, formattedStringData);
				}			
			LOGGER.info("Generate And Persist NFT ended ...");
				}
			});				
			executor.shutdown();
			return new ResponseEntity<>("Immutable Record job initiated successfully !", HttpStatus.OK);
		}



		/// org
		@PostMapping(value = "/v1/org")
		public ResponseEntity<Organization> createOrg(@RequestBody Organization rawOrg) throws Exception {
			try {
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				Organization org = organizationPersistence6.generateJobStatus(rawOrg, jdbcTemplate);
				if(org.getTenantId() != null)
					return new ResponseEntity<Organization>(org, HttpStatus.OK);
				else
					return new ResponseEntity<Organization>(org, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/org/{id}")
		public ResponseEntity<Organization> createOrg(@PathVariable("id") String id) throws Exception {
			try {
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				Organization org = organizationPersistence6.getOrganizationInfo(jdbcTemplate, id);
				if(org != null)
					return new ResponseEntity<Organization>(org, HttpStatus.OK);
				else
					return new ResponseEntity<Organization>(new Organization(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/org")
		public ResponseEntity<List<Organization>> listallOrg() throws Exception {
			try {
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				List<Organization> org = organizationPersistence6.ListAllOrganization(jdbcTemplate);
				return new ResponseEntity<List<Organization>>(org, HttpStatus.OK);
				
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		/*
		@PutMapping(value = "/v1/org")
		public ResponseEntity<Organization> updateOrg(@RequestBody Organization org) throws Exception {
			try {
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				Organization resultOrg = organizationPersistence6.updateOrg(org, jdbcTemplate);
				return new ResponseEntity<Organization>(resultOrg, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		*/
		/// tenant source
		@PostMapping(value = "/v1/tenantsource")
		public ResponseEntity<TenantDocSource2> createTenantSource(@RequestBody TenantDocSource2 rawOrg) throws Exception {
			try {
				TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
				TenantDocSource2 generateTenantSource = tenantDocSourcePersistence7.generateTenantSource(rawOrg, jdbcTemplate);
				if(generateTenantSource != null)
					return new ResponseEntity<TenantDocSource2>(generateTenantSource, HttpStatus.OK);
				else
					return new ResponseEntity<TenantDocSource2>(generateTenantSource, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/tenantsource/{id}")
		public ResponseEntity<List<TenantDocSource2>> getTenantSource(@PathVariable("id") String id) throws Exception {
			try {
				TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
				List<TenantDocSource2> org = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, id);
				if(org != null)
					return new ResponseEntity<List<TenantDocSource2>>(org, HttpStatus.OK);
				else
					return new ResponseEntity<List<TenantDocSource2>>(new ArrayList<TenantDocSource2>(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}

		//tenant source
		
		/// sigma doc
		@PostMapping(value = "/v1/sigmadoc")
		public ResponseEntity<String> makeSigmaDoc(@RequestBody SigmaDocument sigmaDoc) throws Exception {
			try {
				SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
				int generateJobStatus = sigmaDocumentPersistence5.generateDocument(sigmaDoc, jdbcTemplate);
				if(generateJobStatus >0)
					return new ResponseEntity<String>("Success", HttpStatus.OK);
				else
					return new ResponseEntity<String>("Failed", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/sigmadoc/{id}")
		public ResponseEntity<String> getSigmaDoc(@PathVariable("id") String id) throws Exception {
			try {
				SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
				SigmaDocument sigmaDocument = sigmaDocumentPersistence5.getPaymentList(jdbcTemplate, id);
				if(sigmaDocument.getSigmaId() == null)
					return new ResponseEntity<String>(sigmaDocument.toString(), HttpStatus.OK); 
				String tenantId = sigmaDocument.getTenantId();
				DocumentExtMapper deMapper = new DocumentExtMapper();
				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, tenantId);
				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
					LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
					throw new Exception("Invalid configuration for the tenant id =>" + tenantId);
				}				
				JSONObject userView = deMapper.getUserView(sigmaDocument, sigmaDocFieldConfigList);
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PrivateNetwork2 privateNetwork2= privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);				
				PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
				JSONObject immutableRec = null;
				String uuid = sigmaDocument.getUuid();
				if(uuid !=null && !uuid.isEmpty()) {
					immutableRec = polygonEdgeUtil.getImmutableRec(uuid, privateNetwork2, jdbcTemplate);
					if(immutableRec != null)						
						//sigmaDocument.setPolyProps(immutableRec.toString());
						userView.put("polyProps", immutableRec.toString());
				}
				return new ResponseEntity<String>(userView.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PostMapping(value = "/v1/sigmadocfilebyhash")
		public ResponseEntity<byte[]> getSigmaDocByHash(@RequestBody SigmaProps props) throws Exception {
			try {
				String tenantId = props.getTenantId();
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);
				if(networkByTenant == null) {
					byte [] defaultValue = null; 
					return new ResponseEntity<byte[]>(defaultValue, HttpStatus.NOT_FOUND);
				}
				CrateIPFS crateIPFS = new CrateIPFS();
				String getUrl = networkByTenant.getIpfsUrl()+"cat/"+props.getIpfsHash();
				String encoded = Base64.getEncoder().encodeToString((networkByTenant.getCreatedByUser() + ":" 
				        + networkByTenant.getNetworkName()).getBytes());				
				byte[] ipfsFile = crateIPFS.getIpfsFile(getUrl, encoded);
		/*		
				 //-- this code is to test the byte[] data is correct 
				FileOutputStream fileOutputStream = new FileOutputStream("D:\\Examples\\Sigma_Ent\\files\\ipfs_file_5-07032023.pdf");
				fileOutputStream.write(ipfsFile);
				fileOutputStream.close();
			*/	
				return new ResponseEntity<byte[]>(ipfsFile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				byte [] defaultValue = null; 
				return new ResponseEntity<byte[]>(defaultValue, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// spring file
		
		@PostMapping(value = "/v1/sigmadocfilebyhash1")
		public ResponseEntity<org.springframework.core.io.Resource> getSigmaDocByHashResource(@RequestBody SigmaProps props) throws Exception {
			org.springframework.core.io.Resource retValue = new ByteArrayResource(new byte [] {});
			try {
				String tenantId = props.getTenantId();
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);
				if(networkByTenant == null) {
					byte [] defaultValue = null; 
					return new ResponseEntity<org.springframework.core.io.Resource>(retValue, HttpStatus.NOT_FOUND);
				}
				CrateIPFS crateIPFS = new CrateIPFS();
				String getUrl = networkByTenant.getIpfsUrl()+"cat/"+props.getIpfsHash();
				String encoded = Base64.getEncoder().encodeToString((networkByTenant.getCreatedByUser() + ":" 
				        + networkByTenant.getNetworkName()).getBytes());				
				byte[] ipfsFile = crateIPFS.getIpfsFile(getUrl, encoded);
		/*		
				 //-- this code is to test the byte[] data is correct 
				FileOutputStream fileOutputStream = new FileOutputStream("D:\\Examples\\Sigma_Ent\\files\\ipfs_file_5-07032023.pdf");
				fileOutputStream.write(ipfsFile);
				fileOutputStream.close();
			*/				
				retValue = new ByteArrayResource(ipfsFile);
				//return new ResponseEntity<org.springframework.core.io.Resource>(retValue, HttpStatus.OK);
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + retValue.getFilename() + "\"")
						.body(retValue);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				 
				return new ResponseEntity<org.springframework.core.io.Resource>(retValue, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//spring file
		
		@PostMapping(value = "/v1/sigmadocfilebyhashtype")
		public ResponseEntity<byte[]> getSigmaDocByHashType(@RequestBody SigmaProps props) throws Exception {
			try {
				String tenantId = props.getTenantId();
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);
				if(networkByTenant == null) {
					byte [] defaultValue = null; 
					return new ResponseEntity<byte[]>(defaultValue, HttpStatus.NOT_FOUND);
				}
				CrateIPFS crateIPFS = new CrateIPFS();
				String getUrl = networkByTenant.getIpfsUrl()+"cat/"+props.getIpfsHash();
				String encoded = Base64.getEncoder().encodeToString((networkByTenant.getCreatedByUser() + ":" 
				        + networkByTenant.getNetworkName()).getBytes());				
				byte[] ipfsFile = crateIPFS.getIpfsFile(getUrl, encoded);
				/*
				 //-- this code is to test the byte[] data is correct 
				FileOutputStream fileOutputStream = new FileOutputStream("D:\\Examples\\Sigma_Ent\\files\\ipfs_file_5-07032023.pdf");
				fileOutputStream.write(ipfsFile);
				fileOutputStream.close();
				*/
			    HttpHeaders headers = new HttpHeaders();
			    headers.setContentType(MediaType.APPLICATION_PDF);
			    // Here you have to set the actual filename of your pdf
			    String filename = "output.pdf";
			    headers.setContentDispositionFormData(filename, filename);
			    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			    ResponseEntity<byte[]> response = new ResponseEntity<>(ipfsFile, headers, HttpStatus.OK);
			    return response;			    
				//return new ResponseEntity<byte[]>(ipfsFile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				byte [] defaultValue = null; 
				return new ResponseEntity<byte[]>(defaultValue, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		@PostMapping(value = "/v1/sigmadocfilebyhashtype1/{id}")
		public ResponseEntity<byte[]> getSigmaDocByHashType1(@RequestBody SigmaProps props, @PathVariable("id") String id) throws Exception {
		    try {
		    	LOGGER.info("PROPS",props);
		        String tenantId = props.getTenantId();
		        PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
		        PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);
		        if (networkByTenant == null) {
		            byte[] defaultValue = null;
		            return new ResponseEntity<>(defaultValue, HttpStatus.NOT_FOUND);
		        }
		        CrateIPFS crateIPFS = new CrateIPFS();
		        String getUrl = networkByTenant.getIpfsUrl() + "cat/" + props.getIpfsHash();
		        String encoded = Base64.getEncoder().encodeToString((networkByTenant.getCreatedByUser() + ":"
		                + networkByTenant.getNetworkName()).getBytes());
		        byte[] ipfsFile = crateIPFS.getIpfsFile(getUrl, encoded);

		        MediaType mediaType;
		        String fileExtension = getFileExtension(id);
		        switch (fileExtension) {
		            case "pdf":
		                mediaType = MediaType.APPLICATION_PDF;
		                break;
		            case "csv":
		                mediaType = MediaType.TEXT_PLAIN;
		                break;
		            case "docx":
		                mediaType = MediaType.APPLICATION_JSON_UTF8;
		                break;
		            case "jpg":
		            case "jpeg":
		                mediaType = MediaType.IMAGE_JPEG;
		                break;
		            case "png":
		                mediaType = MediaType.IMAGE_PNG;
		                break;
		            default:
		                mediaType = MediaType.APPLICATION_OCTET_STREAM;
		                break;
		        }

		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(mediaType);
//		        String filename = "output." + fileExtension;
//		        headers.setContentDispositionFormData(filename, filename);
		        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		        ResponseEntity<byte[]> response = new ResponseEntity<>(ipfsFile, headers, HttpStatus.OK);
		        LOGGER.info(" response PDF",response);
		        return response;
		    } catch (Exception exception) {
		        LOGGER.error("Error while getting the location risk result.", exception);
		        byte[] defaultValue = null;
		        return new ResponseEntity<>(defaultValue, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

		private String getFileExtension(String fileType) {
		    switch (fileType.toLowerCase()) {
		        case "pdf":
		            return "pdf";
		        case "csv":
		            return "csv";
		        case "word":
		            return "docx";
		        case "image":
		            return "jpg";
		        default:
		            return "";
		    }
		}

		//@GetMapping(value = "/v1/sigmadocbytid/{tid}")
		public ResponseEntity<List<SigmaDocument>> getSigmaDetails(@PathVariable("tid") String tId) throws Exception {
			try {
				SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
				List<SigmaDocument> generateJobStatus = sigmaDocumentPersistence5.getDocumentsByTenant(jdbcTemplate, tId);
				if(generateJobStatus != null)
					return new ResponseEntity<List<SigmaDocument>>(generateJobStatus, HttpStatus.OK);
				else
					return new ResponseEntity<List<SigmaDocument>>(new ArrayList<SigmaDocument>(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PostMapping(value = "/v1/sigmadocbytid")
		public ResponseEntity<String> getSigmaDetailsByPage(
				@RequestBody PageRequestBean req) throws Exception {
			try {				
				SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
				List<SigmaDocument> generateJobStatus = sigmaDocumentPersistence5.getDocumentsByTenantWithPagination(jdbcTemplate, 
						req);
				DocumentExtMapper deMapper = new DocumentExtMapper();
				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, req.getTenantId());
				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
					LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
					throw new Exception("Invalid configuration for the tenant id =>" +req.getTenantId());
				}
				
				JSONArray userView = deMapper.getUserView(generateJobStatus, sigmaDocFieldConfigList);
				if(generateJobStatus != null)
					return new ResponseEntity<String>(userView.toString(), HttpStatus.OK);
				else
					return new ResponseEntity<String>(new String("Error retrieving the documents"), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		/// sigma doc field config
		@PostMapping(value = "/v1/sigmafieldconf/{tid}")
		public ResponseEntity<String> makeSigmaFieldConfig(@PathVariable("tid") String tid,
				@RequestBody List<String> sigmaDocConfigs) throws Exception {
			try {
				SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				int generateDocument = 0;			
				int fieldCounter = 1;
				for(String configField: sigmaDocConfigs) {
					SigmaAPIDocConfig config = new SigmaAPIDocConfig();
					config.setSigmaField("fVar"+((fieldCounter++)));
					config.setTenantId(tid);
					config.setStatus("Y");
					config.setExtField(configField);
					generateDocument += sigmaDocFieldConfigPersistence6.generateDocument(config, jdbcTemplate);
				}
				if(generateDocument >0)
					return new ResponseEntity<String>("Request Successful!, sigmafieldconf entries made with {"+generateDocument+"} records.", HttpStatus.OK);
				else
					return new ResponseEntity<String>("Failed", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/sigmafieldconf/{tid}")
		public ResponseEntity<List<SigmaAPIDocConfig>> getSigmaFieldConfig(@PathVariable("tid") String id) throws Exception {
			try {
				SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				List<SigmaAPIDocConfig> generateDocument = sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, id);
				if(generateDocument != null)
					return new ResponseEntity<List<SigmaAPIDocConfig>>(generateDocument, HttpStatus.OK);
				else
					return new ResponseEntity<List<SigmaAPIDocConfig>>(new ArrayList<SigmaAPIDocConfig>(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
	
		// csv reader
		@PostMapping(value = "/v1/uploadsigmafieldconfig")
		public ResponseEntity<String> uploadSigmaFieldConfig(@RequestParam("file") MultipartFile file) throws Exception {
			try {
				InputStream inputStream = file.getInputStream();
				List<String> configs = new BufferedReader(new InputStreamReader(inputStream,
					          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
				SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				Organization organizationInfo = new Organization();
				organizationInfo.setCreatedBy("TEST_ADMIN");
				organizationInfo.setTenantId("6ab257b6-4a67-4420-b90b-ef9206919c2f");
				Integer fieldCounter = 0;
				for(String config : configs) {
					String[] split = config.split(",");
					if(fieldCounter++ == 0)
						continue;
					String extConfig = split[0];
					SigmaAPIDocConfig inputConfig = new SigmaAPIDocConfig();
					inputConfig.setCreatedBy(organizationInfo.getCreatedBy());
					inputConfig.setSigmaField("fVar"+(fieldCounter-1));//tr v
					inputConfig.setExtField(extConfig);
					inputConfig.setStatus("Y");
					inputConfig.setTenantId(organizationInfo.getTenantId());					
					int generateDocument = sigmaDocFieldConfigPersistence6.generateDocument(inputConfig, jdbcTemplate);
					System.out.println(generateDocument);
				}
				return new ResponseEntity<String>("Successfully created, use **/v1/sigmafieldconf/{id} to get the config", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		
		// csv reader
//				@PostMapping(value = "/v1/uploadsigmafieldconfig/{tid}/{mail}")
//				public ResponseEntity<String> uploadSigmaFieldConfig(@RequestParam("file") MultipartFile file, @PathVariable("tid") String id,@PathVariable("mail") String id1) throws Exception {
//					try {
//						InputStream inputStream = file.getInputStream();
//						List<String> configs = new BufferedReader(new InputStreamReader(inputStream,
//							          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
//						SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//						Organization organizationInfo = new Organization();
//						organizationInfo.setCreatedBy("TEST_ADMIN");
//						organizationInfo.setTenantId(id);
//						Integer fieldCounter = 0;
//						List<SigmaAPIDocConfig> sigmaDocFieldConfigListLocal = new ArrayList<>();
//						for(String config : configs) {
//							String[] split = config.split(",");
//							if(fieldCounter++ == 0)
//								continue;
//							if (split.length == 0) {
//						        // Skip empty or invalid entries
//						        continue;
//						    }
//							String extConfig = split[0];
//							SigmaAPIDocConfig inputConfig = new SigmaAPIDocConfig();
//							inputConfig.setCreatedBy(organizationInfo.getCreatedBy());
//							inputConfig.setSigmaField("fVar"+(fieldCounter-1));//tr v
//							inputConfig.setExtField(extConfig);
//							inputConfig.setStatus("Y");
//							inputConfig.setTenantId(organizationInfo.getTenantId());
//							
//							sigmaDocFieldConfigListLocal.add(inputConfig);
//							
//							int generateDocument = sigmaDocFieldConfigPersistence6.generateDocument(inputConfig, jdbcTemplate);
//							System.out.println(generateDocument);
//							
//						}
////						Application app = new Application();
////						app.SinglefileIpfsUpload(file);
//						LOGGER.info("for loop executed");
//						try {
////							InputStream inputStream = file.getInputStream();
//						DocumentRetrieve documentRetrieve = new DocumentRetrieve();
//						OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//						List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//						for(Organization organization : organizationList) {	
////							JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
////							boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
////							
////							if(!jobcurrentstatus) {
////								LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
////								return;
////							}
////							SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//							List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//									sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//							if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//								LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
//								continue;
//							}
//							String tenantId = organization.getTenantId();
////							TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
////							List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
////							for(TenantDocSource2 source : organizationList2) {
////								if(source.getStatus() != 1)
////									continue;
//							//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//							PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//							PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//					
//							
//								String latestDocumentDate = null;
////								Long jobId = documentRetrieve.updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, organization, true, 0l, "DOC_FETCH","No");
//								
//								SigmaDocumentPersistence52 sigmaDocumentPersistence5 = new SigmaDocumentPersistence52();
//								SigmaDocument2 document = new SigmaDocument2();
//								document.setTenantId(tenantId);
//								document.setCreatedBy(organization.getCreatedBy());
////								document.setJobId(jobId);
//								document.setNftCreationStatus(0);
//								document.setMailId(id1);
//								
//								UUID uuid = UUID.randomUUID();
//						        String uuidAsString = uuid.toString();
//						        document.setUuid(uuidAsString);
//						        
//								InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
//								JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSsingleFile("1", file.getOriginalFilename(),
//										networkById, file);
//								String hash = ipfsInfo.optString("createIRec");
//								document.setDocChecksum(hash);		
//							
//							    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
//							    document.setMd5Checksum(md5Checksum);
//							    document.setFileName(file.getOriginalFilename());
//							    sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
//							    
//							    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//								Date date = new Date();
//								String formattedStringData = dateFormat.format(date);
//								latestDocumentDate = formattedStringData;
////							    documentRetrieve.updateJobStatus(1, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, organization, false, jobId, "DOC_FETCH","No");
//							    
//							    UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//								List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, organization.getTenantId(), 1, latestDocumentDate);
//				
////					input JSONObject creation
//								JSONObject input = new JSONObject();
//						        
//								input.put("tokenKey", uuidAsString);
//								ObjectMapper mapper = new ObjectMapper();
//								String writeValueAsString =
//										null;
//								try {
//									writeValueAsString = mapper.writeValueAsString(document);
//								} catch (JsonProcessingException e) {
//									LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", document, e);
////									return new JSONObject();
//								}
//								JSONObject documentOJson = new JSONObject(writeValueAsString);
//								for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigListLocal) {
//									String sigmaField = sigmaAPIDocConfig.getSigmaField();
//									String targetExtField = documentOJson.optString(sigmaField);
//									input.put(sigmaField, targetExtField);
//									}
//								int sizeOfInput = sigmaDocFieldConfigListLocal.size()+1;
//								for(int counter=sizeOfInput; counter<=10;counter++) {
//									input.put("fVar"+counter, "");
//								}
//								input.put("fVar10", document.getDocChecksum());
//								
//								String mintNftUrl = networkById.getSmartContractAccessUrl()+"contracts"+"/"+ networkById.getSmartContractAddress() +
//										"/mintNFT?kld-from="	+ networkById.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
//								
//								PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
//								JSONObject nftInfo = polygonEdgeUtil.mintNftEthereumSigmacompliance(mintNftUrl, networkById.getCreatedByUser(), networkById.getNetworkName(), input);
//								
//								
//						  		document.setNftCreationStatus(1);
//						  		document.setStatus(nftInfo.optString("txhash","ERROR"));
//						  		
//								LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+document.getSigmaId()+
//						  				", uuid => "+nftInfo.optString("uuid","Error"));
//								
//								sigmaDocumentPersistence5.updateImmutableRecord(document, jdbcTemplate);
//
//
//							
//							} // tenant doc source
////							} 
//						}
//						catch (Exception e) {
//							LOGGER.error("Application.populateDocumentData()", e);
//						}
//						return new ResponseEntity<String>("Successfully created, use **/v1/sigmafieldconf/{id} to get the config", HttpStatus.OK);
//					} catch (Exception exception) {
//						LOGGER.error("Error while getting the location risk result.", exception);
//						throw new Exception("Error while getting the location risk result");
//					}
//				}
		

//@PostMapping(value = "/v1/uploadsigmafieldconfig/{tid}/{mail}")
//				public ResponseEntity<String> uploadSigmaFieldConfig(@RequestParam("file") MultipartFile file, @PathVariable("tid") String id,@PathVariable("mail") String id1) throws Exception {
//					try {
//						InputStream inputStream = file.getInputStream();
//						List<String> configs = new BufferedReader(new InputStreamReader(inputStream,
//							          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
//						SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//						Organization organizationInfo = new Organization();
//						organizationInfo.setCreatedBy("TEST_ADMIN");
//						organizationInfo.setTenantId(id);
//						Integer fieldCounter = 0;
//						List<SigmaAPIDocConfig> sigmaDocFieldConfigListLocal = new ArrayList<>();
//						for(String config : configs) {
//							String[] split = config.split(",");
//							if(fieldCounter++ == 0)
//								continue;
//							if (split.length == 0) {
//						        // Skip empty or invalid entries
//						        continue;
//						    }
//							String extConfig = split[0];
//							SigmaAPIDocConfig inputConfig = new SigmaAPIDocConfig();
//							inputConfig.setCreatedBy(organizationInfo.getCreatedBy());
//							inputConfig.setSigmaField("fVar"+(fieldCounter-1));//tr v
//							inputConfig.setExtField(extConfig);
//							inputConfig.setStatus("Y");
//							inputConfig.setTenantId(organizationInfo.getTenantId());
//							
//							sigmaDocFieldConfigListLocal.add(inputConfig);
//							
////							int generateDocument = sigmaDocFieldConfigPersistence6.generateDocument(inputConfig, jdbcTemplate);
////							System.out.println(generateDocument);
////							
//						}
////						Application app = new Application();
////						app.SinglefileIpfsUpload(file);
//						LOGGER.info("for loop executed");
//						try {
////							InputStream inputStream = file.getInputStream();
//						DocumentRetrieve documentRetrieve = new DocumentRetrieve();
//						OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//						List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//						for(Organization organization : organizationList) {	
////							JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
////							boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
////							
////							if(!jobcurrentstatus) {
////								LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
////								return;
////							}
////							SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//							List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//									sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//							if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//								LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
//								continue;
//							}
//							String tenantId = organization.getTenantId();
////							TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
////							List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
////							for(TenantDocSource2 source : organizationList2) {
////								if(source.getStatus() != 1)
////									continue;
//							//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//							PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//							PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//					
//							
//								String latestDocumentDate = null;
////								Long jobId = documentRetrieve.updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, organization, true, 0l, "DOC_FETCH","No");
//								
//								SigmaDocumentPersistence52 sigmaDocumentPersistence5 = new SigmaDocumentPersistence52();
//								SigmaDocument2 document = new SigmaDocument2();
//								document.setTenantId(tenantId);
//								document.setCreatedBy(organization.getCreatedBy());
////								document.setJobId(jobId);
//								document.setNftCreationStatus(0);
//								document.setMailId(id1);
//								
//								UUID uuid = UUID.randomUUID();
//						        String uuidAsString = uuid.toString();
//						        document.setUuid(uuidAsString);
//						        
//								InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
//								JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSsingleFile("1", file.getOriginalFilename(),
//										networkById, file, ipfsUrl);
//								String hash = ipfsInfo.optString("createIRec");
//								document.setDocChecksum(hash);		
//							
//							    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
//							    document.setMd5Checksum(md5Checksum);
//							    document.setFileName(file.getOriginalFilename());
//							    sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
//							    
//							    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//								Date date = new Date();
//								String formattedStringData = dateFormat.format(date);
//								latestDocumentDate = formattedStringData;
////							    documentRetrieve.updateJobStatus(1, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, organization, false, jobId, "DOC_FETCH","No");
//							    
//							    UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//								List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, organization.getTenantId(), 1, latestDocumentDate);
//				
////					input JSONObject creation
//								JSONObject input = new JSONObject();
//						        
//								input.put("tokenKey", uuidAsString);
//								ObjectMapper mapper = new ObjectMapper();
//								String writeValueAsString =
//										null;
//								try {
//									writeValueAsString = mapper.writeValueAsString(document);
//								} catch (JsonProcessingException e) {
//									LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", document, e);
////									return new JSONObject();
//								}
//								JSONObject documentOJson = new JSONObject(writeValueAsString);
//								for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigListLocal) {
//									String sigmaField = sigmaAPIDocConfig.getSigmaField();
//									String targetExtField = documentOJson.optString(sigmaField);
//									input.put(sigmaField, targetExtField);
//									}
//								int sizeOfInput = sigmaDocFieldConfigListLocal.size()+1;
//								for(int counter=sizeOfInput; counter<=10;counter++) {
//									input.put("fVar"+counter, "");
//								}
//								input.put("fVar8", document.getFileName());
//								input.put("fVar9", latestDocumentDate);
//								input.put("fVar10", document.getDocChecksum());
//								input.put("fVar11", document.getMd5Checksum());
//								
//								
//								String mintNftUrl = networkById.getSmartContractAccessUrl()+"contracts"+"/"+ networkById.getSmartContractAddress() +
//										"/mintNFT?kld-from="	+ networkById.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
//								
//								PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
//								JSONObject nftInfo = polygonEdgeUtil.mintNftEthereumSigmacompliance(mintNftUrl, networkById.getCreatedByUser(), networkById.getNetworkName(), input,infuraUrl,contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
//								
//								
//						  		document.setNftCreationStatus(1);
//						  		document.setStatus(nftInfo.optString("txhash","ERROR"));
//						  		
//								LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+document.getSigmaId()+
//						  				", uuid => "+nftInfo.optString("uuid","Error"));
//								
//								sigmaDocumentPersistence5.updateImmutableRecord(document, jdbcTemplate);
//
//
//							
//							} // tenant doc source
////							} 
//						}
//						catch (Exception e) {
//							LOGGER.error("Application.populateDocumentData()", e);
//						}
//						return new ResponseEntity<String>("Successfully created, use **/v1/sigmafieldconf/{id} to get the config", HttpStatus.OK);
//					} catch (Exception exception) {
//						LOGGER.error("Error while getting the location risk result.", exception);
//						throw new Exception("Error while getting the location risk result");
//					}
//				}

		@PostMapping(value = "/v1/uploadsigmafieldconfig/{tid}/{mail}")
		public ResponseEntity<String> uploadSigmaFieldConfig(@RequestParam("file") MultipartFile file, @PathVariable("tid") String id,@PathVariable("mail") String id1) throws Exception {
			try {
				InputStream inputStream = file.getInputStream();
				List<String> configs = new BufferedReader(new InputStreamReader(inputStream,
					          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
				SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				Organization organizationInfo = new Organization();
				organizationInfo.setCreatedBy("TEST_ADMIN");
				organizationInfo.setTenantId(id);
				Integer fieldCounter = 0;
				List<SigmaAPIDocConfig> sigmaDocFieldConfigListLocal = new ArrayList<>();
				for(String config : configs) {
					String[] split = config.split(",");
					if(fieldCounter++ == 0)
						continue;
					if (split.length == 0) {
				        // Skip empty or invalid entries
				        continue;
				    }
					String extConfig = split[0];
					SigmaAPIDocConfig inputConfig = new SigmaAPIDocConfig();
					inputConfig.setCreatedBy(organizationInfo.getCreatedBy());
					inputConfig.setSigmaField("fVar"+(fieldCounter-1));//tr v
					inputConfig.setExtField(extConfig);
					inputConfig.setStatus("Y");
					inputConfig.setTenantId(organizationInfo.getTenantId());
					
					sigmaDocFieldConfigListLocal.add(inputConfig);
					
//					int generateDocument = sigmaDocFieldConfigPersistence6.generateDocument(inputConfig, jdbcTemplate);
//					System.out.println(generateDocument);
//					
				}
//				Application app = new Application();
//				app.SinglefileIpfsUpload(file);
				LOGGER.info("for loop executed");
				try {
//					InputStream inputStream = file.getInputStream();
				DocumentRetrieve documentRetrieve = new DocumentRetrieve();
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
				for(Organization organization : organizationList) {	
//					JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//					boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
//					
//					if(!jobcurrentstatus) {
//						LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
//						return;
//					}
//					SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
					List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
							sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
					if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
						LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
						continue;
					}
					String tenantId = organization.getTenantId();
//					TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
//					List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
//					for(TenantDocSource2 source : organizationList2) {
//						if(source.getStatus() != 1)
//							continue;
					//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//					PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//					PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
			
					
						String latestDocumentDate = null;
//						Long jobId = documentRetrieve.updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, organization, true, 0l, "DOC_FETCH","No");
						
						SigmaDocumentPersistence52 sigmaDocumentPersistence5 = new SigmaDocumentPersistence52();
						SigmaDocument2 document = new SigmaDocument2();
						document.setTenantId(tenantId);
						document.setCreatedBy(organization.getCreatedBy());
//						document.setJobId(jobId);
						document.setNftCreationStatus(0);
						document.setMailId(id1);
						
						UUID uuid = UUID.randomUUID();
				        String uuidAsString = uuid.toString();
				        document.setUuid(uuidAsString);
				        
						InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
						JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSsingleFile("1", file.getOriginalFilename(),
								file, ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
						String hash = ipfsInfo.optString("createIRec");
						document.setDocChecksum(hash);		
					
					    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
					    document.setMd5Checksum(md5Checksum);
					    document.setFileName(file.getOriginalFilename());
					    sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
					    
					    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						Date date = new Date();
						String formattedStringData = dateFormat.format(date);
						latestDocumentDate = formattedStringData;
//					    documentRetrieve.updateJobStatus(1, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, organization, false, jobId, "DOC_FETCH","No");
					    
					    UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
						List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, organization.getTenantId(), 1, latestDocumentDate);
		
//			input JSONObject creation
						JSONObject input = new JSONObject();
				        
						input.put("tokenKey", uuidAsString);
						ObjectMapper mapper = new ObjectMapper();
						String writeValueAsString =
								null;
						try {
							writeValueAsString = mapper.writeValueAsString(document);
						} catch (JsonProcessingException e) {
							LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", document, e);
//							return new JSONObject();
						}
						JSONObject documentOJson = new JSONObject(writeValueAsString);
						for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigListLocal) {
							String sigmaField = sigmaAPIDocConfig.getSigmaField();
							String targetExtField = documentOJson.optString(sigmaField);
							input.put(sigmaField, targetExtField);
							}
						int sizeOfInput = sigmaDocFieldConfigListLocal.size()+1;
						for(int counter=sizeOfInput; counter<=10;counter++) {
							input.put("fVar"+counter, "");
						}
						input.put("fVar8", document.getFileName());
						input.put("fVar9", latestDocumentDate);
						input.put("fVar10", document.getDocChecksum());
						input.put("fVar11", document.getMd5Checksum());
						
						
//						String mintNftUrl = networkById.getSmartContractAccessUrl()+"contracts"+"/"+ networkById.getSmartContractAddress() +
//								"/mintNFT?kld-from="	+ networkById.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
						
						PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
						JSONObject nftInfo = polygonEdgeUtil.mintNftEthereumSigmacompliance(input,infuraUrl,contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
						
						
				  		document.setNftCreationStatus(1);
				  		document.setStatus(nftInfo.optString("txhash","ERROR"));
				  		
						LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+document.getSigmaId()+
				  				", uuid => "+nftInfo.optString("uuid","Error"));
						
						sigmaDocumentPersistence5.updateImmutableRecord(document, jdbcTemplate);


					
					} // tenant doc source
//					} 
				}
				catch (Exception e) {
					LOGGER.error("Application.populateDocumentData()", e);
				}
				return new ResponseEntity<String>("Successfully created, use **/v1/sigmafieldconf/{id} to get the config", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}



		

				
				//for sigmacomplaincesuite
				@GetMapping(value = "/v1/sigmadoc2countbytid/{tid}")
				public ResponseEntity<Integer> getSigmaDoc2CountbyTid(@PathVariable("tid") String id) throws Exception {
					try {
						SigmaDocumentPersistence52 sigmaDocumentPersistence52 = new SigmaDocumentPersistence52();

						Integer count = sigmaDocumentPersistence52.getSigmaDoc2CountbyTid(jdbcTemplate, id);
						
							return new ResponseEntity<Integer>(count, HttpStatus.OK);
						
					} catch (Exception exception) {
						LOGGER.error("Error while getting the location risk result.", exception);
						throw new Exception("Error while getting the location risk result");
					}
				}
				//sigmacomplaincesuite
				@GetMapping(value = "/v1/sigmadoc2countbymail/{mail}")
				public ResponseEntity<Integer> getSigmaDoc2CountbyMail(@PathVariable("mail") String id) throws Exception {
					try {
						SigmaDocumentPersistence52 sigmaDocumentPersistence52 = new SigmaDocumentPersistence52();

						Integer count = sigmaDocumentPersistence52.getSigmaDoc2CountbyMail(jdbcTemplate, id);
						
							return new ResponseEntity<Integer>(count, HttpStatus.OK);
						
					} catch (Exception exception) {
						LOGGER.error("Error while getting the location risk result.", exception);
						throw new Exception("Error while getting the location risk result");
					}
				}
				//sigmacomplaincesuite
				@GetMapping(value = "/v1/sigmadoc2bymail/{mail}")
				public ResponseEntity<List<SigmaDocument2>> getSigmadoc2byMail(@PathVariable("mail") String id) throws Exception {
					try {
						SigmaDocumentPersistence52 sigmaDocumentPersistence52 = new SigmaDocumentPersistence52();
						List<SigmaDocument2> generateDocument = sigmaDocumentPersistence52.getDocumentsByMail(jdbcTemplate, id);
						if(generateDocument != null)
							return new ResponseEntity<List<SigmaDocument2>>(generateDocument, HttpStatus.OK);
						else
							return new ResponseEntity<List<SigmaDocument2>>(new ArrayList<SigmaDocument2>(), HttpStatus.OK);
					} catch (Exception exception) {
						LOGGER.error("Error while getting the location risk result.", exception);
						throw new Exception("Error while getting the location risk result");
					}
				}
	    @PostMapping("/upload")
	    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
	        try {
	        	if(true)
	        		return null;
	            // Save the uploaded file to a temporary location
	            File tempFile = File.createTempFile(file.getOriginalFilename(), ".java");
	            file.transferTo(tempFile);

	            // Compile the uploaded file
	            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
	            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(tempFile);
	            compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
	            fileManager.close();

	            // Load the compiled class
	            ClassLoader classLoader = getClass().getClassLoader();
	            Class<?> clazz = classLoader.loadClass(file.getOriginalFilename().replace(".java", ""));

	            // Get the method object and invoke it
	            Method method = clazz.getDeclaredMethod("getMessage");
	            Object result = method.invoke(null);
	            System.out.println(result);
	            return ResponseEntity.ok("File uploaded and method invoked successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	        }	    
	}
		@GetMapping(value = "/v1/sigmadocsummary/{tid}")
		public ResponseEntity<List<PieBean>> getSigmaDocSummary(@PathVariable("tid") String tid) throws Exception {
			try {
				PieUtil pieUtil = new PieUtil();
				List<PieBean> documentSummary = pieUtil.getDocumentSummary(12, jdbcTemplate, tid);
				if(documentSummary != null)
					return new ResponseEntity<List<PieBean>>(documentSummary, HttpStatus.OK);
				else
					return new ResponseEntity<List<PieBean>>(new ArrayList<PieBean>(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/sigmanodesummary/{tid}")
		public ResponseEntity<InfraBean> getSigmaNodeSummary(@PathVariable("tid") String tid) throws Exception {
			try {
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tid);
				PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
				InfraBean nodeDetails = polygonEdgeUtil.getNodeDetails(networkByTenant, pbcToken);
				if(nodeDetails != null)
					return new ResponseEntity<InfraBean>(nodeDetails, HttpStatus.OK);
				else
					return new ResponseEntity<InfraBean>(new InfraBean(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/sigmanodesummary1/{tid}")
		public ResponseEntity<InfraBean> getSigmaNodeSummary1(@PathVariable("tid") String tid) throws Exception {
			try {
				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
				PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tid);
				PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
				InfraBean nodeDetails = polygonEdgeUtil.getNodeDetails(networkByTenant, pbcToken);
				if(nodeDetails != null)
					return new ResponseEntity<InfraBean>(nodeDetails, HttpStatus.OK);
				else
					return new ResponseEntity<InfraBean>(new InfraBean(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		//new code
		
		@PostMapping(value = "/v1/userdetail")
		public ResponseEntity<String> generateUserInfoDetails(@RequestBody UserInfo userInfo) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				int insertCount = userinfoPersistence.createUserInfo(userInfo, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PutMapping(value = "/v1/userdetail")
		public ResponseEntity<String> updateUserdetails(@RequestBody UserInfo userInfo) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				int insertCount = userinfoPersistence.updateUser(userInfo, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/userdetail/{id}")
		public ResponseEntity<List<UserInfo>> getUserDetailbyEmail(@PathVariable("id") String id) throws Exception {
			try {
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				List<UserInfo> userProfile = userinfoPersistence.getUserInfoByEmail(jdbcTemplate, id);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/userdetailpwdcheck/{id}")
		public ResponseEntity<String> getUserDetailbyEmailCheck(@PathVariable("id") String id) throws Exception {
			try {
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				String userProfilepwd = userinfoPersistence.getUserInfoWithEmailCheck(jdbcTemplate, id);
				return new ResponseEntity<>(userProfilepwd, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/userdetailbytennant/{id}/{id1}")
		public ResponseEntity<List<UserInfo>> getUserDetailbyTennant(@PathVariable("id") String id,@PathVariable("id1") int id1) throws Exception {
			try {
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				List<UserInfo> userProfile = userinfoPersistence.getUserInfoBytennatId(jdbcTemplate, id, id1);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/userdetailbytennantall/{id}")
		public ResponseEntity<List<UserInfo>> getUserDetailbyTennantAll(@PathVariable("id") String id) throws Exception {
			try {
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				List<UserInfo> userProfile = userinfoPersistence.getUserInfoBytennatIdAll(jdbcTemplate, id);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}

		@PostMapping(value = "/v1/userdetailexists")
		public ResponseEntity<String> generateUserdetailCheck(@RequestBody UserInfo userProfile) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				UserInfo userProfileFromDb = userinfoPersistence.getUserProfileCheck(jdbcTemplate, userProfile);
				if (userProfileFromDb != null)
					responseJson.put("result", "Y");
				else
					responseJson.put("result", "N");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/userdetail")
		public ResponseEntity<List<UserInfo>> getUserDetail() throws Exception {
			try {
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				List<UserInfo> userProfile = userinfoPersistence.getUserInfo(jdbcTemplate);
				return new ResponseEntity<>(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@DeleteMapping(value = "/v1/userdetail/{id}")
		public ResponseEntity<String> deleteUserIfoByEmailId(@PathVariable("id") String id) throws Exception {
		    try {
		    	UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
		        boolean isDeleted = userinfoPersistence.deleteUserByEmailId(jdbcTemplate, id);
		        
		        if (isDeleted) {
		            return new ResponseEntity<>("userdetail deleted successfully", HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>("Failed to delete userdetail", HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    } catch (Exception exception) {
		        LOGGER.error("Error while deleting userdetail.", exception);
		        throw new Exception("Error while deleting userdetail");
		    }
		}
		
		@PutMapping(value = "/v1/userPassword/{id}")
		public ResponseEntity<String> updatePassword(@PathVariable("id") String id,@RequestBody UserInfo userInfo) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				int insertCount = userinfoPersistence.updatePassword(userInfo, id, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Password Update Successful !");
				else
					responseJson.put("result", "User Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PutMapping(value = "/v1/userRole/{id}")
		public ResponseEntity<String> updateRoletype(@PathVariable("id") String id,@RequestBody UserInfo userInfo) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
				int insertCount = userinfoPersistence.updateRole(userInfo, id, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Role Update Successful !");
				else
					responseJson.put("result", "User Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		
		
		@PostMapping(value = "/v1/profile")
		public ResponseEntity<String> createUserProfile(@RequestBody ProfilePage userProfile) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				ProfilePagePersistence pofilePagePersistence = new ProfilePagePersistence();
				pofilePagePersistence.setStorageService(storageService);
				int insertCount = pofilePagePersistence.createUserProfile(userProfile, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		

		@GetMapping(value = "/v1/profile/{id}")
		public ResponseEntity<List<ProfilePage>> getUserProfileByEmailId(@PathVariable("id") String id) throws Exception {
			try {
				ProfilePagePersistence pofilePagePersistence = new ProfilePagePersistence();
				pofilePagePersistence.setNftImageRepositoryLocation(nftImageRepositoryLocation);
				pofilePagePersistence.setStorageService(storageService);
//				ProfilePagePersistence pofilePagePersistence = new ProfilePagePersistence();
				ProfilePage userProfile = pofilePagePersistence.getUserProfileByEmail(jdbcTemplate, id);
				return new ResponseEntity(userProfile, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PutMapping(value = "/v1/profile")
		public ResponseEntity<String> updateUserProfile(@RequestBody ProfilePage userProfile) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				ProfilePagePersistence pofilePagePersistence = new ProfilePagePersistence();
				pofilePagePersistence.setNftImageRepositoryLocation(nftImageRepositoryLocation);
				pofilePagePersistence.setStorageService(storageService);
				int insertCount = pofilePagePersistence.updateUser(userProfile, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PostMapping(value = "/v1/session")
		public ResponseEntity<String> generatesession(@RequestBody Session session) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				SessionPersistence sessionpersistence = new SessionPersistence();
				int insertCount = sessionpersistence.createSession(session, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/session/{id}/{id1}")
		public ResponseEntity<List<Session>> getSessionInfo(@PathVariable("id") String id,@PathVariable("id1") int id1) throws Exception {
			try {
				SessionPersistence sessionPersistence6 = new SessionPersistence();
				List<Session> session = sessionPersistence6.getMailsession(jdbcTemplate, id, id1);
				return new ResponseEntity<>(session, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/session/{id}")
		public ResponseEntity<List<Session>> getAllSessionInfo(@PathVariable("id") int id) throws Exception {
			try {
				SessionPersistence sessionPersistence6 = new SessionPersistence();
				List<Session> session = sessionPersistence6.getAllSessionManage(jdbcTemplate, id);
				return new ResponseEntity<>(session, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PutMapping(value = "/v1/session")
		public ResponseEntity<String> updateSessionInfo(@RequestBody Session session) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				SessionPersistence sessionpersistence = new SessionPersistence();
				int insertCount = sessionpersistence.updateSession(session, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PostMapping(value = "/v1/favourite")
		public ResponseEntity<String> generateFavourite(@RequestBody Favourite favourite) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				FavouritePersistence favouritePersistence = new FavouritePersistence();
				int insertCount = favouritePersistence.createFavourite(favourite, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/favourite/{id}/{id1}")
		public ResponseEntity<List<Favourite>> getFavouriteByEmailId(@PathVariable("id") String id,@PathVariable("id1") int id1) throws Exception {
			try {
				FavouritePersistence favouritePersistence = new FavouritePersistence();
				List<Favourite> session = favouritePersistence.getFavouriteByEmailId(jdbcTemplate, id, id1);
				return new ResponseEntity<>(session, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@DeleteMapping(value = "/v1/favourite/{id}/{id1}")
		public ResponseEntity<String> deleteFavouriteByEmailId(@PathVariable("id") String id, @PathVariable("id1") String id1) throws Exception {
		    try {
		        FavouritePersistence favouritePersistence = new FavouritePersistence();
		        boolean isDeleted = favouritePersistence.deleteFavouriteByEmailId(jdbcTemplate, id, id1);
		        
		        if (isDeleted) {
		            return new ResponseEntity<>("Favourite deleted successfully", HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>("Failed to delete favourite", HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    } catch (Exception exception) {
		        LOGGER.error("Error while deleting favourite.", exception);
		        throw new Exception("Error while deleting favourite");
		    }
		}
		
		@PostMapping(value = "/v1/userdetails/{id}")
		public ResponseEntity<String> getresetpasswordbyEmail(@PathVariable("id") String id) throws Exception {
		    try {
		        UserInfoPersistence userinfoPersistence = new UserInfoPersistence();
		        boolean otp = userinfoPersistence.resetUserpassword(jdbcTemplate, id);
		        if (otp) {
//		            OTPResponse otpResponse = new OTPResponse(otp);
		            return new ResponseEntity<>("Mailed successfully", HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    } catch (Exception exception) {
		        LOGGER.error("Error while getting the location risk result.", exception);
		        throw new Exception("Error while getting the location risk result");
		    }
		}
		
		@PostMapping(value = "/v1/notification")
		public ResponseEntity<String> generatenotification(@RequestBody Notification notification) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				NotificationPersistence notificationpersistence = new NotificationPersistence();
				int insertCount = notificationpersistence.createSession(notification, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/notification/{id}")
		public ResponseEntity<List<Notification>> getNotificationInfo(@PathVariable("id") String id) throws Exception {
			try {
				NotificationPersistence notificationPersistence = new NotificationPersistence();
				List<Notification> notification = notificationPersistence.getMailNotification(jdbcTemplate, id);
				return new ResponseEntity<>(notification, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/notification/{id}/{id1}")
		public ResponseEntity<List<Notification>> getNotificationInfoByStatus(@PathVariable("id") String id, @PathVariable("id1") String id1) throws Exception {
			try {
				NotificationPersistence notificationPersistence = new NotificationPersistence();
				List<Notification> notification = notificationPersistence.getMailNotificationByStatus(jdbcTemplate, id, id1);
				return new ResponseEntity<>(notification, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PutMapping(value = "/v1/notification")
		public ResponseEntity<String> updateNotificationInfo(@RequestBody Notification notification) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				NotificationPersistence notificationpersistence = new NotificationPersistence();
				int insertCount = notificationpersistence.updateNotification(notification, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PutMapping(value = "/v1/notificationbyid")
		public ResponseEntity<String> updateNotificationInfoById(@RequestBody Notification notification) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				NotificationPersistence notificationpersistence = new NotificationPersistence();
				int insertCount = notificationpersistence.updateNotificationById(notification, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PostMapping(value = "/v1/helpandsupport")
		public ResponseEntity<String> generateHelpandsupport(@RequestBody HelpandSupport helpandsupport) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				HelpandsupportPersistence helpandsupportpersistence = new HelpandsupportPersistence();
				int insertCount = helpandsupportpersistence.createSession(helpandsupport, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/helpandsupport/{id}")
		public ResponseEntity<List<HelpandSupport>> getHelpandsupportInfo(@PathVariable("id") int id) throws Exception {
			try {
				HelpandsupportPersistence helpandsupportpersistence = new HelpandsupportPersistence();
				List<HelpandSupport> helpandsupport = helpandsupportpersistence.getMailHelpandSupport(jdbcTemplate, id);
				return new ResponseEntity<>(helpandsupport, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/helpandsupportstatus/{id}/{id1}")
		public ResponseEntity<List<HelpandSupport>> getHelpandsupportInfoByStatus(@PathVariable("id") String id, @PathVariable("id1") String id1) throws Exception {
			try {
				HelpandsupportPersistence helpandsupportpersistence = new HelpandsupportPersistence();
				List<HelpandSupport> helpandsupport = helpandsupportpersistence.getMailHelpandSupportByStatus(jdbcTemplate, id, id1);
				return new ResponseEntity<>(helpandsupport, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		
		@PutMapping(value = "/v1/helpandsupportbyid")
		public ResponseEntity<String> updateHelpandsupportInfoById(@RequestBody HelpandSupport helpandsupport) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				HelpandsupportPersistence helpandsupportpersistence = new HelpandsupportPersistence();
				int insertCount = helpandsupportpersistence.updateHelpandsupportById(helpandsupport, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PutMapping(value = "/v1/helpandsupportassigneebyid")
		public ResponseEntity<String> updateAssigneeHelpandsupportInfoById(@RequestBody HelpandSupport helpandsupport) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				HelpandsupportPersistence helpandsupportpersistence = new HelpandsupportPersistence();
				int insertCount = helpandsupportpersistence.updateAssigneeHelpandsupportById(helpandsupport, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "Helpandsupportassigneebyid / Update Successful !");
				else
					responseJson.put("result", "Helpandsupportassigneebyid / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		// blocks tx fetch from block provider
		@PostMapping(value = "/v1/blockstx")
		public ResponseEntity<String> getTransactionsBlocks(@RequestBody PageRequestBean bean) throws Exception {
			try {
				PolygonEdgeUtil peu123 = new PolygonEdgeUtil();
				String transactionsByPage = peu123.getTransactionsBlocksByPage(pbcToken, bean, jdbcTemplate);
				return new ResponseEntity<String>(transactionsByPage, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		// NFT token details fetch from txn provider
		@PostMapping(value = "/v1/nftdetails/{id}")
		public ResponseEntity<String> getNFTDetails(@RequestBody PageRequestBean bean,@PathVariable("id") String id) throws Exception {
			try {
				PolygonEdgeUtil peu123 = new PolygonEdgeUtil();
				String transactionsByPage = peu123.getNFTDeatils(pbcToken, bean, id, jdbcTemplate);
				return new ResponseEntity<String>(transactionsByPage, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@GetMapping(value = "/v1/jobname/{id}")
		public ResponseEntity<Integer> getMakeIrecCount(@PathVariable("id") String id) {
		    try {
		    	JobOPersistence5 jobOPersistence5 = new JobOPersistence5();
		        int makeIrecCount = jobOPersistence5.getMakeIrecCount(jdbcTemplate, id);
		        return new ResponseEntity<>(makeIrecCount, HttpStatus.OK);
		    } catch (Exception exception) {
		        LOGGER.error("Error while getting the MAKE_IREC count.", exception);
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

		@GetMapping(value = "/v1/latestjobdate")
		public ResponseEntity<String> getLastUpdatedDate() {
		    try {
		    	JobOPersistence5 jobOPersistence5 = new JobOPersistence5();
		        String lastUpdatedDate = jobOPersistence5.getLastUpdatedDate(jdbcTemplate);
		        return new ResponseEntity<>(lastUpdatedDate, HttpStatus.OK);
		    } catch (Exception exception) {
		        LOGGER.error("Error while getting the last updated date.", exception);
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

		@PostMapping(value = "/v1/jobnotifyemail")
		public ResponseEntity<String> generateJobNotifyemail(@RequestBody JobNotifyEmail jobnotify) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				JobNotifyEmailPersistence jobnotifypersistence = new JobNotifyEmailPersistence();
				int insertCount = jobnotifypersistence.createSession(jobnotify, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/jobnotifyemail/{id}")
		public ResponseEntity<List<JobNotifyEmail>> getJobNotifyByTennnat(@PathVariable("id") String id) throws Exception {
			try {
				JobNotifyEmailPersistence jobnotifypersistence = new JobNotifyEmailPersistence();
				List<JobNotifyEmail> jobnotify = jobnotifypersistence.getMailJobNotifyEmail(jdbcTemplate, id);
				return new ResponseEntity<>(jobnotify, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PutMapping(value = "/v1/jobnotifyemail")
		public ResponseEntity<String> updateJobNotifyById(@RequestBody JobNotifyEmail jobnotify) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				JobNotifyEmailPersistence jobnotifypersistence = new JobNotifyEmailPersistence();
				int insertCount = jobnotifypersistence.updateJobNotifyEmail(jobnotify, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Create / Update Successful !");
				else
					responseJson.put("result", "User Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@DeleteMapping(value = "/v1/jobnotifyemail/{id}")
		public ResponseEntity<String> deleteJobNotifyByEmailId(@PathVariable("id") String id) throws Exception {
		    try {
		    	JobNotifyEmailPersistence jobnotifypersistence = new JobNotifyEmailPersistence();
		        boolean isDeleted = jobnotifypersistence.deleteUserByNotifyEmailId(jdbcTemplate, id);
		        
		        if (isDeleted) {
		            return new ResponseEntity<>("userdetail deleted successfully", HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>("Failed to delete userdetail", HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    } catch (Exception exception) {
		        LOGGER.error("Error while deleting userdetail.", exception);
		        throw new Exception("Error while deleting userdetail");
		    }
		}
		 

		@PostMapping(value = "/v1/jobmanage")
		public ResponseEntity<String> generatejobmanage(@RequestBody JobManagement jobmanage) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				JobManagementPersistence jobmanangepersistence = new JobManagementPersistence();
				int insertCount = jobmanangepersistence.createSession(jobmanage, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/jobmanage")
		public ResponseEntity<List<JobManagement>> getJobManageInfo() throws Exception {
			try {
				JobManagementPersistence jobmanangepersistence = new JobManagementPersistence();
				List<JobManagement> jobmanage = jobmanangepersistence.getAllJobManage(jdbcTemplate);
				return new ResponseEntity<>(jobmanage, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/jobmanagetime")
		public ResponseEntity<List<JobManagement>> getJobManageInfoTime() throws Exception {
			try {
				JobManagementPersistence jobmanangepersistence = new JobManagementPersistence();
				List<JobManagement> jobmanage = jobmanangepersistence.getAllJobManageTime(jdbcTemplate);
				return new ResponseEntity<>(jobmanage, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/jobmanage/{id}")
		public ResponseEntity<List<JobManagement>> getJobMangewithTennatInfo(@PathVariable("id") String id) throws Exception {
			try {
				JobManagementPersistence jobmanangepersistence = new JobManagementPersistence();
				List<JobManagement> jobmanage = jobmanangepersistence.getJobManageWithTennat(jdbcTemplate, id);
				return new ResponseEntity<>(jobmanage, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@PostMapping(value = "/v1/sigmadocbytid/rest/download")
		public ResponseEntity<org.springframework.core.io.Resource> getSigmaDocByHashResources(@RequestBody SigmaProps props) throws Exception {
		    try {
		        String tenantId = props.getTenantId();
		        PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
		        PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);
		        if (networkByTenant == null) {
		            byte[] defaultValue = null;
		            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		        }
		        CrateIPFS crateIPFS = new CrateIPFS();
		        String getUrl = networkByTenant.getIpfsUrl() + "cat/" + props.getIpfsHash();
		        String encoded = Base64.getEncoder().encodeToString((networkByTenant.getCreatedByUser() + ":"
		                + networkByTenant.getNetworkName()).getBytes());
		        byte[] ipfsFile = crateIPFS.getIpfsFilenew(getUrl, encoded);
		        //System.out.println("ipfsFile contents: " + new String(ipfsFile, StandardCharsets.UTF_8));
		       //byte[] decodedIpfsFile = Base64.getDecoder().decode(ipfsFile);
//		        org.springframework.core.io.Resource retValue = new ByteArrayResource(decodedIpfsFile);
		        // Create HttpHeaders to set the file name and content type
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentDispositionFormData("attachment", "filename.pdf");
		        headers.setContentType(MediaType.APPLICATION_PDF); // Replace with appropriate content type if not PDF

		        // Create a ByteArrayResource with the file content
		        org.springframework.core.io.Resource retValue = new ByteArrayResource(ipfsFile);

		        // Return the ResponseEntity with the resource and headers
		        return new ResponseEntity<>(retValue, headers, HttpStatus.OK);
		    } catch (Exception e) {
		        // Handle exceptions and return an appropriate ResponseEntity if needed
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
//		@PostMapping(value = "/v1/sigmadocbytid/rest/download")
//		public ResponseEntity<org.springframework.core.io.InputStreamResource> getSigmaDocByHashResources(@RequestBody SigmaProps props) {
//		    try {
//		        String tenantId = props.getTenantId();
//		        PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//		        PrivateNetwork2 networkByTenant = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, tenantId);
//		        if (networkByTenant == null) {
//		            byte[] defaultValue = null;
//		            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		        }
//		        CrateIPFS crateIPFS = new CrateIPFS();
//		        String getUrl = networkByTenant.getIpfsUrl() + "cat/" + props.getIpfsHash();
//		        String encoded = Base64.getEncoder().encodeToString((networkByTenant.getCreatedByUser() + ":"
//		                + networkByTenant.getNetworkName()).getBytes());
//		        FileInputStream inputStream = crateIPFS.getIpfsFileStream(getUrl, encoded);
//		        
//		        if (inputStream == null) {
//		            // Return appropriate response if the InputStream is null
//		            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		        }
//
//		        // Create HttpHeaders to set the file name and content type
//		        HttpHeaders headers = new HttpHeaders();
//		        headers.setContentDispositionFormData("attachment", "filename.pdf");
//		        headers.setContentType(MediaType.APPLICATION_PDF); // Replace with appropriate content type if not PDF
//
//		        // Wrap the InputStream in InputStreamResource to use it as the response body
//		        org.springframework.core.io.InputStreamResource inputStreamResource = new org.springframework.core.io.InputStreamResource(inputStream);
//
//		        // Return the ResponseEntity with the InputStreamResource and headers
//		        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
//		    } catch (Exception e) {
//		        // Handle exceptions and return an appropriate ResponseEntity if needed
//		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		    }
//		}
		
		@PostMapping(value = "/v1/jobtrigger")
		public ResponseEntity<String> generateJobTrigger(@RequestBody JobTrigger jobtrigger) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
				int insertCount = jobtriggerpersistence.createSession(jobtrigger, jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "resource Create / Update Successful !");
				else
					responseJson.put("result", "resource Create / Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		@PutMapping(value = "/v1/jobtrigger/{id}/{id1}/{id2}")
		public ResponseEntity<String> updateJobstatus(@PathVariable("id") String id,@PathVariable("id1") String id1,@PathVariable("id2") Integer id2) throws Exception {
			try {
				JSONObject responseJson = new JSONObject();
				JobTriggerPersistence jobtriggerPersistence = new JobTriggerPersistence();
				int insertCount = jobtriggerPersistence.UpdateJobTrigger(id, id1,id2,jdbcTemplate);
				if (insertCount == 1)
					responseJson.put("result", "User Password Update Successful !");
				else
					responseJson.put("result", "User Update Failed !");
				return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		@GetMapping(value = "/v1/jobstatusall")
		public ResponseEntity<List<JobTrigger>> getJobInfo() throws Exception {
			try {
				JobTriggerPersistence jobtriggerPersistence = new JobTriggerPersistence();
				List<JobTrigger> jobtrigger = jobtriggerPersistence.getAllJobManage(jdbcTemplate);
				return new ResponseEntity<>(jobtrigger, HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		private String calculateMD5Checksum(InputStream inputStream) {
		    try {
		        MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] buffer = new byte[4096];
		        int bytesRead;
		        
		        while ((bytesRead = inputStream.read(buffer)) != -1) {
		            md.update(buffer, 0, bytesRead);
		        }
		        
		        byte[] hashBytes = md.digest();

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
		@PostMapping(value = "/v1/getSessionId/{tenantid}/{docid}")
		public ResponseEntity<String> getSessionId(@PathVariable("tenantid") String tenantId,
		                                           @PathVariable("docid") String docId,
		                                           @RequestBody String id) throws Exception {
		    try {
		        TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
		        TenantDocSource2 source = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, id);

		        if (source.getStatus() != 1) {
		            throw new Exception("Invalid tenant source configuration !");
		        }

		        SigmaProps props = new SigmaProps(source.getExtUrl() + "/api/v22.3/auth",
		                source.getExtUrl() + "/api/v22.3/query",
		                source.getExtUserName(), source.getExtPassword(), tenantId, true,
		                source.getExtUrl() + "/api/v22.3/objects/documents/");

		        GenericArtefactRetriever retriever = new VeevaImplementor();
		        String sessionId = retriever.postAuthenticationToken(props, "sessionId");

		        String apiUrl = source.getExtUrl() + "/api/v22.3/query";
		        String query = "select id, global_id__sys, filename__v,id,version_id,source_owner__v,source_document_number__v,application__v,name__v,type__v,title__v,manufacturer__v,source_vault_id__v,created_by__v,region__v,status__v,document_date__c,binder_last_autofiled_date__v,archived_date__sys,file_modified_date__v,file_created_date__v,document_creation_date__v FROM documents WHERE id='" + docId + "'";
		        HttpHeaders headers = new HttpHeaders();
		        headers.set("Authorization", "Bearer " + sessionId);
		        headers.set("X-VaultAPI-DescribeQuery", "true");
		        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		        HttpEntity<String> requestEntity = new HttpEntity<>("q=" + query, headers);

		        RestTemplate restTemplate = new RestTemplate();
		        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

		        if (responseEntity.getStatusCode() == HttpStatus.OK) {
		            String responseBody = responseEntity.getBody();
		            JSONObject responseJson = new JSONObject(responseBody);
		            JSONArray data = responseJson.getJSONArray("data"); 
		            JSONObject documentInfo = data.getJSONObject(0); // Get the first document object
		            
		            String docname = documentInfo.optString("name__v");
		            System.out.println(responseBody);
		        	new HttpConnector(null).skipTrustCertificates(); 
//		          URL obj = new URL(props.getExtFileUrl() +"/"+ docId + "/file"); // v
		      	URL obj = new URL(props.getExtFileUrl() + docId + "/file"); // v
		          HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		          con.setRequestMethod("GET");
		          con.setRequestProperty("Authorization", sessionId);
		          con.setRequestProperty("Accept", "application/json");
		          int responseCode = con.getResponseCode();

		          JSONObject result = new JSONObject();
		          InputStream inputStream = null;
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
		        	  // Calculate the MD5 checksum of the document content

		      	    String md5Checksum = calculateMD5Checksum(inputStream);
		      	    
//		            result.put("md5Checksum", md5Checksum);
		            responseJson.put("md5Checksum", md5Checksum);
		            HttpHeaders responseHeaders = new HttpHeaders();
		            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		            ResponseEntity<String> responseWithChecksum = new ResponseEntity<>(responseJson.toString(), responseHeaders, HttpStatus.OK);
		            return responseWithChecksum;
		        	 }  
		      	 else {
		      		throw new Exception ("Error response code from web3 responseCode {}" + responseCode);
//		        	 inputStream.close();
		      	 }
					
		        } else {
		            System.err.println("Request failed with status code: " + responseEntity.getStatusCode());
		        }
		       

		        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
		    } catch (Exception exception) {
		        LOGGER.error("Error while processing the request.", exception);
		        throw new Exception("Error while processing the request");
		    }
		}
		
		@PostMapping(value = "/v1/getdocCount/{tenantid}")
		public ResponseEntity<String> getDocumentCount(@PathVariable("tenantid") String tenantId,
		                                           @RequestBody String id) throws Exception {
		    try {
		        TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
		        TenantDocSource2 source = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, id);

		        if (source.getStatus() != 1) {
		            throw new Exception("Invalid tenant source configuration !");
		        }

		        SigmaProps props = new SigmaProps(source.getExtUrl() + "/api/v22.3/auth",
		                source.getExtUrl() + "/api/v22.3/query",
		                source.getExtUserName(), source.getExtPassword(), tenantId, true,
		                source.getExtUrl() + "/api/v22.3/objects/documents/");

		        GenericArtefactRetriever retriever = new VeevaImplementor();
		        String sessionId = retriever.postAuthenticationToken(props, "sessionId");
		        JobOPersistence5 jobOPersistence5 = new JobOPersistence5();
		        String runstartDate = jobOPersistence5.getRunStartTime(jdbcTemplate);
		        System.out.println(runstartDate);
		        // Define the input and output formats
		        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		        
		        // Parse the input string
		        LocalDateTime dateTime = LocalDateTime.parse(runstartDate, inputFormatter);
		        
		        // Format the parsed date time to ISO 8601 format
		        String iso8601Formatted = dateTime.format(outputFormatter);
		        System.out.println(iso8601Formatted);
		        String apiUrl = source.getExtUrl() + "/api/v22.3/query";
		        //String query = "select id FROM documents where document_creation_date__v >= '" +iso8601Formatted+"'";
		        String query = "select id FROM documents where file_created_date__v >= '" +iso8601Formatted+"'";
		        HttpHeaders headers = new HttpHeaders();
		        headers.set("Authorization", "Bearer " + sessionId);
		        headers.set("X-VaultAPI-DescribeQuery", "true");
		        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		        HttpEntity<String> requestEntity = new HttpEntity<>("q=" + query, headers);

		        RestTemplate restTemplate = new RestTemplate();
		        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
		        JSONObject countdata = new JSONObject();
		        if (responseEntity.getStatusCode() == HttpStatus.OK) {
		            String responseBody = responseEntity.getBody();
		            JSONObject responseJson = new JSONObject(responseBody);
		            JSONObject responseDetails = responseJson.getJSONObject("responseDetails");
		            int total = responseDetails.getInt("total");
		            System.out.println(total);
		            countdata.put("totalcount", total);
		            HttpHeaders responseHeaders = new HttpHeaders();
		            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		            ResponseEntity<String> responseWithCount = new ResponseEntity<>(countdata.toString(), responseHeaders, HttpStatus.OK);
		            return responseWithCount;
		          
		     
		        } else {
		            System.err.println("Request failed with status code: " + responseEntity.getStatusCode());
		          
		        }
		       
		        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
		       
		    } catch (Exception exception) {
		        LOGGER.error("Error while processing the request.", exception);
		        throw new Exception("Error while processing the request");
		    }
		}

		@GetMapping(value = "/v1/blockstxavalanche/{id}", produces = "application/json")
		@ResponseBody
		public String getTransactionsBlocksAvalanche(@PathVariable("id") String id) throws Exception {
			try {
				 RestTemplate restTemplate = new RestTemplate();
			        
			        // Define the URL
			        String url = "https://"+transactiondataUrl+"/api?module=proxy&action=eth_getBlockByNumber&tag="+id+"&boolean=true&apikey=7591ca9e4ccc415faf028b9dff4c7ce2;";
			        
			        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
			        
			        if (responseEntity.getStatusCode().is2xxSuccessful()) {
			            return responseEntity.getBody();
			        } else {
			            throw new RuntimeException("Failed to fetch data from the API");
			        }
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		
		// blocks tx fetch from Avalanche
		@CrossOrigin(origins = "http://localhost:3000")
		@GetMapping(value = "/v1/txpolygon/{id}", produces = "application/json")
		@ResponseBody
		public String getTransactionsPolygon(@PathVariable("id") String id ) throws Exception {
			try {
				 RestTemplate restTemplate = new RestTemplate();
			        
			        // Define the URL
			        String url = "https://"+transactiondataUrl+"/api?module=account&action=tokennfttx&contractaddress="+contractAddress+"&address="+account+"&page="+id+"&offset=10&startblock=0&endblock=99999999&sort=desc&apikey=7591ca9e4ccc415faf028b9dff4c7ce2";
			        
			        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
			        
			        if (responseEntity.getStatusCode().is2xxSuccessful()) {
			            return responseEntity.getBody();
			        } else {
			            throw new RuntimeException("Failed to fetch data from the API");
			        }
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
		// blocks tx fetch from Avalanche
		@GetMapping(value = "/v1/txInputpolygon/{id}", produces = "application/json")
		@ResponseBody
		public String getTransactionInputPolygon(@PathVariable("id") String id) throws Exception {
			try {
				 RestTemplate restTemplate = new RestTemplate();
			        
			        // Define the URL
			        String url = "https://"+transactiondataUrl+"/api?module=proxy&action=eth_getTransactionByHash&txhash="+id+"&apikey=7591ca9e4ccc415faf028b9dff4c7ce2";
			        
			        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
			        
			        if (responseEntity.getStatusCode().is2xxSuccessful()) {
			            return responseEntity.getBody();
			        } else {
			            throw new RuntimeException("Failed to fetch data from the API");
			        }
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}


		@GetMapping(value = "/v1/txInputpolygondecode/{id}", produces = "application/json")
	    @ResponseBody
	    public String[] getTransactionInputPolygondecode(@PathVariable("id") String id) throws Exception {
	        try {
	            RestTemplate restTemplate = new RestTemplate();

	            // Define the URL to get the transaction data
	            String url = "https://"+transactiondataUrl+"/api?module=proxy&action=eth_getTransactionByHash&txhash=" + id + "&apikey=7591ca9e4ccc415faf028b9dff4c7ce2";

	            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

	            if (responseEntity.getStatusCode().is2xxSuccessful()) {
	                String responseData = responseEntity.getBody();

	                // Parse the JSON response to extract the transaction input data (hexadecimal string)
	                JSONObject jsonResponse = new JSONObject(responseData);
	                String inputHex = jsonResponse.getJSONObject("result").getString("input");

	                // Remove the "0x" prefix, if present
	                if (inputHex.startsWith("0x")) {
	                    inputHex = inputHex.substring(2);
	                }
	                String asciiString = new String(DatatypeConverter.parseHexBinary(inputHex), "UTF-8");
	                //String unwantedCharactersRegex = "[^\\w\\s-:,.]"; // Matches any character that is not a word character, '-', ':', ',', or '.'
//	                String unwantedCharactersRegex = "[^\\w\\s:.,-]";
	                String unwantedCharactersRegex = "[^\\w-:,. ]";

	                // Replace unwanted characters with commas
	                asciiString = asciiString.replaceAll(unwantedCharactersRegex, ",");
	                asciiString = asciiString.replaceAll(",+", ",");
	   
	                String[] values = asciiString.split(",");
	                List<String> resultList = new ArrayList<>();
	                for (String value : values) {
	                    if (!value.trim().isEmpty()) {
	                        resultList.add(value.trim());
	                    }
	                }

	                return resultList.toArray(new String[0]);
	            } else {
	                throw new RuntimeException("Failed to fetch data from the API");
	            }
	        } catch (Exception exception) {
	            // Handle exceptions
	            throw exception;
	        }
	        
		}
		
		
//		@Value("${ipfsUrl}")
//	    private String ipfsUrl;
//		@PostMapping(value = "/v1/docfetchprivate/{tenantid}")
//		public ResponseEntity<String> triggerDocumentFetchPrivate(@PathVariable("tenantid") String tenantId,
//				@RequestBody String id) throws Exception {
//			try {
//				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,tenantId,"DOC_FETCH");
//				
//				if(!jobcurrentstatus) {
//					LOGGER.info("Exiting the docfetch as it is disabled by flag runDocumentFetchJob {" + jobcurrentstatus + "}");
//					return new ResponseEntity<>("Exiting the docfetch as it is disabled by flag runDocumentFetchJob { !" + jobcurrentstatus + "}", HttpStatus.OK);
//				}
////				if(!runDocumentFetchJob)
////				{
////					LOGGER.info("Exiting the docfetch as it is disabled by flag runDocumentFetchJob {" + runDocumentFetchJob + "}");
////					return new ResponseEntity<>("Exiting the docfetch as it is disabled by flag runDocumentFetchJob { !" + runDocumentFetchJob + "}", HttpStatus.OK);
////				}
//				ExecutorService executor = Executors.newFixedThreadPool(10);
//				executor.submit(new Runnable() {					
//					@Override
//					public void run() {		
//				LOGGER.info("populateDocumentData start()");
//				DocumentRetrieve documentRetrieve = new DocumentRetrieve();
//				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//				Organization organizationInfo = organizationPersistence6.getOrganizationInfo(jdbcTemplate, tenantId);
//				
//				TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
//				TenantDocSource2 source = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, id);
//			//	List<TenantDocSource2> sources = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
//				//TenantDocSource2 tenantSourceInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//			//	for(TenantDocSource2 source : sources) {
//					if(source.getStatus() != 1)
//					try{
//						throw new Exception("Invalid tenant source configuration !");						
//					}catch(Exception exception) {
//						LOGGER.error("Invalid config ", exception);
//					}
//					SigmaProps props = new SigmaProps(source.getExtUrl()+"/api/v22.3/auth", source.getExtUrl()+"/api/v22.3/query", 
//							source.getExtUserName() , source.getExtPassword(), tenantId, true, source.getExtUrl()+"/api/v22.3/objects/documents/");
//					Organization org = new Organization();
//					org.setTenantId(tenantId);
//					SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//					List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//							sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, org.getTenantId());
//					try {
//							PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//							PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organizationInfo.getTenantId());
//							documentRetrieve.findLatestDocumentsPrivate(props, jdbcTemplate, organizationInfo, sigmaDocFieldConfigList, networkById,"Yes", ipfsUrl);
//					}catch (Exception e) {
//							LOGGER.error("PlatformResource.triggerDocumentFetch()", e);
//					}
//			//	} // doc source for loop
//					LOGGER.info("populateDocumentData completed ...");
//				} // run method
//					
//				});				
//				executor.shutdown();
//				return new ResponseEntity<>("Successfully started doc fetch !", HttpStatus.OK);
//			} catch (Exception exception) {
//				LOGGER.error("Error while getting the location risk result.", exception);
//				throw new Exception("Error while getting the location risk result");
//			}
//		}



@Value("${ipfsUrl}")
	    private String ipfsUrl;
		@PostMapping(value = "/v1/docfetchprivate/{tenantid}")
		public ResponseEntity<String> triggerDocumentFetchPrivate(@PathVariable("tenantid") String tenantId,
				@RequestBody String id) throws Exception {
			try {
				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,tenantId,"DOC_FETCH");
				
				if(!jobcurrentstatus) {
					LOGGER.info("Exiting the docfetch as it is disabled by flag runDocumentFetchJob {" + jobcurrentstatus + "}");
					return new ResponseEntity<>("Exiting the docfetch as it is disabled by flag runDocumentFetchJob { !" + jobcurrentstatus + "}", HttpStatus.OK);
				}
//				if(!runDocumentFetchJob)
//				{
//					LOGGER.info("Exiting the docfetch as it is disabled by flag runDocumentFetchJob {" + runDocumentFetchJob + "}");
//					return new ResponseEntity<>("Exiting the docfetch as it is disabled by flag runDocumentFetchJob { !" + runDocumentFetchJob + "}", HttpStatus.OK);
//				}
				ExecutorService executor = Executors.newFixedThreadPool(10);
				executor.submit(new Runnable() {					
					@Override
					public void run() {		
				LOGGER.info("populateDocumentData start()");
				DocumentRetrieve documentRetrieve = new DocumentRetrieve();
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				Organization organizationInfo = organizationPersistence6.getOrganizationInfo(jdbcTemplate, tenantId);
				
				TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
				TenantDocSource2 source = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, id);
			//	List<TenantDocSource2> sources = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
				//TenantDocSource2 tenantSourceInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
			//	for(TenantDocSource2 source : sources) {
					if(source.getStatus() != 1)
					try{
						throw new Exception("Invalid tenant source configuration !");						
					}catch(Exception exception) {
						LOGGER.error("Invalid config ", exception);
					}
					SigmaProps props = new SigmaProps(source.getExtUrl()+"/api/v22.3/auth", source.getExtUrl()+"/api/v22.3/query", 
							source.getExtUserName() , source.getExtPassword(), tenantId, true, source.getExtUrl()+"/api/v22.3/objects/documents/");
					Organization org = new Organization();
					org.setTenantId(tenantId);
					SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
					List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
							sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, org.getTenantId());
					try {
//							PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//							PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organizationInfo.getTenantId());
							documentRetrieve.findLatestDocumentsPrivate(props, jdbcTemplate, organizationInfo, sigmaDocFieldConfigList, "Yes", ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
					}catch (Exception e) {
							LOGGER.error("PlatformResource.triggerDocumentFetch()", e);
					}
			//	} // doc source for loop
					LOGGER.info("populateDocumentData completed ...");
				} // run method
					
				});				
				executor.shutdown();
				return new ResponseEntity<>("Successfully started doc fetch !", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}


//@Value("${ipfsUrl}")
//			    private String ipfsUrl1;
//				@PostMapping(value = "/v1/singlefileupload/{tid}/{mail}/{filename}")
//				public ResponseEntity<String> singleFileUpload(@RequestBody byte[] binaryData, @PathVariable("tid") String id,@PathVariable("mail") String id1,@PathVariable("filename") String filename) throws Exception {
//					try {
//						String tennantId = null;
//						InputStream inputStream = new ByteArrayInputStream(binaryData);
//						System.out.println("bin"+binaryData);
//						List<String> configs = new BufferedReader(new InputStreamReader(inputStream,
//							          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
//						SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//						Organization organizationInfo = new Organization();
//						organizationInfo.setCreatedBy("TEST_ADMIN");
//						organizationInfo.setTenantId(id);
//						Integer fieldCounter = 0;
//						List<SigmaAPIDocConfig> sigmaDocFieldConfigListLocal = new ArrayList<>();
//						for(String config : configs) {
//							String[] split = config.split(",");
//							if(fieldCounter++ == 0)
//								continue;
//							if (split.length == 0) {
//						        // Skip empty or invalid entries
//						        continue;
//						    }
//							String extConfig = split[0];
//							SigmaAPIDocConfig inputConfig = new SigmaAPIDocConfig();
//							inputConfig.setCreatedBy(organizationInfo.getCreatedBy());
//							inputConfig.setSigmaField("fVar"+(fieldCounter-1));//tr v
//							inputConfig.setExtField(extConfig);
//							inputConfig.setStatus("Y");
//							inputConfig.setTenantId(organizationInfo.getTenantId());
//							
//							sigmaDocFieldConfigListLocal.add(inputConfig);
//							
////							int generateDocument = sigmaDocFieldConfigPersistence6.generateDocument(inputConfig, jdbcTemplate);
////							System.out.println(generateDocument);
//							
//						}
////						Application app = new Application();
////						app.SinglefileIpfsUpload(file);
//						LOGGER.info("for loop executed");
//						try {
////							InputStream inputStream = file.getInputStream();
//						DocumentRetrieve documentRetrieve = new DocumentRetrieve();
//						OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//						List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//						for(Organization organization : organizationList) {	
////							JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
////							boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
////							
////							if(!jobcurrentstatus) {
////								LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
////								return;
////							}
////							SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
////							List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
////									sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
////							if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
////								LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
////								continue;
////							}
//							if(organization.getTenantId().equals(id)) {
//								tennantId = organization.getTenantId();
//							}else {
//								continue;
//							}
//							String tenantId = organization.getTenantId();
////							TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
////							List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
////							for(TenantDocSource2 source : organizationList2) {
////								if(source.getStatus() != 1)
////									continue;
//							//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//							PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//							PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//					
//							
//								String latestDocumentDate = null;
////								Long jobId = documentRetrieve.updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, organization, true, 0l, "DOC_FETCH","No");
//								
//								SigmaDocumentPersistence52 sigmaDocumentPersistence5 = new SigmaDocumentPersistence52();
//								SigmaDocument2 document = new SigmaDocument2();
//								document.setTenantId(tenantId);
//								document.setCreatedBy(organization.getCreatedBy());
////								document.setJobId(jobId);
//								document.setNftCreationStatus(0);
//								document.setMailId(id1);
//								
//								UUID uuid = UUID.randomUUID();
//						        String uuidAsString = uuid.toString();
//						        document.setUuid(uuidAsString);
//						        
//								InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
//								JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSsingleFilePrivate("1", filename,
//										networkById, binaryData, ipfsUrl1);
//								String hash = ipfsInfo.optString("createIRec");
//								document.setDocChecksum(hash);		
//							
//							    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
//							    document.setMd5Checksum(md5Checksum);
//							    document.setFileName(filename);
//							    sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
//							    
//							    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//								Date date = new Date();
//								String formattedStringData = dateFormat.format(date);
//								latestDocumentDate = formattedStringData;
////							    documentRetrieve.updateJobStatus(1, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, organization, false, jobId, "DOC_FETCH","No");
//							    
//							    UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//								List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, organization.getTenantId(), 1, latestDocumentDate);
//				
////					input JSONObject creation
//								JSONObject input = new JSONObject();
//						        
//								input.put("tokenKey", uuidAsString);
////								input.put("uuid", uuidAsString);
//								ObjectMapper mapper = new ObjectMapper();
//								String writeValueAsString =
//										null;
//								try {
//									writeValueAsString = mapper.writeValueAsString(document);
//								} catch (JsonProcessingException e) {
//									LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", document, e);
////									return new JSONObject();
//								}
//								JSONObject documentOJson = new JSONObject(writeValueAsString);
//								for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigListLocal) {
//									String sigmaField = sigmaAPIDocConfig.getSigmaField();
//									String targetExtField = documentOJson.optString(sigmaField);
//									input.put(sigmaField, targetExtField);
//									}
//								int sizeOfInput = sigmaDocFieldConfigListLocal.size()+1;
//								for(int counter=sizeOfInput; counter<=10;counter++) {
//									input.put("fVar"+counter, "");
//								}
//								input.put("fVar8", document.getFileName());
//								input.put("fVar9", latestDocumentDate);
//								input.put("fVar10", document.getDocChecksum());
//								input.put("fVar11", document.getMd5Checksum());
//								
//								String mintNftUrl = networkById.getSmartContractAccessUrl()+"contracts"+"/"+ networkById.getSmartContractAddress() +
//										"/mintNFT?kld-from="	+ networkById.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
//								
//								PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
//								JSONObject nftInfo = polygonEdgeUtil.mintNftEthereumSigmacompliance(mintNftUrl, networkById.getCreatedByUser(), networkById.getNetworkName(), input, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
//								
//								
//						  		document.setNftCreationStatus(1);
//						  		document.setStatus(nftInfo.optString("txhash","ERROR"));
//						  		
//								LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+document.getSigmaId()+
//						  				", uuid => "+nftInfo.optString("uuid","Error"));
//								
//								sigmaDocumentPersistence5.updateImmutableRecord(document, jdbcTemplate);
//
//
//							
//							} // tenant doc source
////							} 
//						}
//						catch (Exception e) {
//							LOGGER.error("Application.populateDocumentData()", e);
//						}
//						return new ResponseEntity<String>("Successfully created, use **/v1/sigmafieldconf/{id} to get the config", HttpStatus.OK);
//					} catch (Exception exception) {
//						LOGGER.error("Error while getting the location risk result.", exception);
//						throw new Exception("Error while getting the location risk result");
//					}
//				}
		
		@Value("${ipfsUrl}")
	    private String ipfsUrl1;
		@PostMapping(value = "/v1/singlefileupload/{tid}/{mail}/{filename}")
		public ResponseEntity<String> singleFileUpload(@RequestBody byte[] binaryData, @PathVariable("tid") String id,@PathVariable("mail") String id1,@PathVariable("filename") String filename) throws Exception {
			try {
				String tennantId = null;
				InputStream inputStream = new ByteArrayInputStream(binaryData);
				System.out.println("bin"+binaryData);
				List<String> configs = new BufferedReader(new InputStreamReader(inputStream,
					          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
				SigmaDocFieldConfigPersistence6 sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				Organization organizationInfo = new Organization();
				organizationInfo.setCreatedBy("TEST_ADMIN");
				organizationInfo.setTenantId(id);
				Integer fieldCounter = 0;
				List<SigmaAPIDocConfig> sigmaDocFieldConfigListLocal = new ArrayList<>();
				for(String config : configs) {
					String[] split = config.split(",");
					if(fieldCounter++ == 0)
						continue;
					if (split.length == 0) {
				        // Skip empty or invalid entries
				        continue;
				    }
					String extConfig = split[0];
					SigmaAPIDocConfig inputConfig = new SigmaAPIDocConfig();
					inputConfig.setCreatedBy(organizationInfo.getCreatedBy());
					inputConfig.setSigmaField("fVar"+(fieldCounter-1));//tr v
					inputConfig.setExtField(extConfig);
					inputConfig.setStatus("Y");
					inputConfig.setTenantId(organizationInfo.getTenantId());
					
					sigmaDocFieldConfigListLocal.add(inputConfig);
					
//					int generateDocument = sigmaDocFieldConfigPersistence6.generateDocument(inputConfig, jdbcTemplate);
//					System.out.println(generateDocument);
					
				}
//				Application app = new Application();
//				app.SinglefileIpfsUpload(file);
				LOGGER.info("for loop executed");
				try {
//					InputStream inputStream = file.getInputStream();
				DocumentRetrieve documentRetrieve = new DocumentRetrieve();
				OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
				List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
				for(Organization organization : organizationList) {	
//					JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//					boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
//					
//					if(!jobcurrentstatus) {
//						LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
//						return;
//					}
//					SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//					List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//							sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//					if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//						LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
//						continue;
//					}
					if(organization.getTenantId().equals(id)) {
						tennantId = organization.getTenantId();
					}else {
						continue;
					}
					String tenantId = organization.getTenantId();
//					TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
//					List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
//					for(TenantDocSource2 source : organizationList2) {
//						if(source.getStatus() != 1)
//							continue;
					//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//					PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//					PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
			
					
						String latestDocumentDate = null;
//						Long jobId = documentRetrieve.updateJobStatus(0, "P",  "", "Started the job !", jdbcTemplate, organization, true, 0l, "DOC_FETCH","No");
						
						SigmaDocumentPersistence52 sigmaDocumentPersistence5 = new SigmaDocumentPersistence52();
						SigmaDocument2 document = new SigmaDocument2();
						document.setTenantId(tenantId);
						document.setCreatedBy(organization.getCreatedBy());
//						document.setJobId(jobId);
						document.setNftCreationStatus(0);
						document.setMailId(id1);
						
						UUID uuid = UUID.randomUUID();
				        String uuidAsString = uuid.toString();
				        document.setUuid(uuidAsString);
				        
						InterPlanetaryAssist interPlanetaryAssist = new InterPlanetaryAssist();
						JSONObject ipfsInfo = interPlanetaryAssist.getAndPersistIPFSsingleFilePrivate("1", filename,
								binaryData, ipfsUrl1,ec2IP1, ec2IP2, ec2IP3);
						String hash = ipfsInfo.optString("createIRec");
						document.setDocChecksum(hash);		
					
					    String md5Checksum = ipfsInfo.optString("md5Checksum"); // Get the MD5 checksum
					    document.setMd5Checksum(md5Checksum);
					    document.setFileName(filename);
					    sigmaDocumentPersistence5.generateDocument(document, jdbcTemplate);
					    
					    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						Date date = new Date();
						String formattedStringData = dateFormat.format(date);
						latestDocumentDate = formattedStringData;
//					    documentRetrieve.updateJobStatus(1, "Y",  latestDocumentDate, "No Errors", jdbcTemplate, organization, false, jobId, "DOC_FETCH","No");
					    
					    UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
						List<String> emailIds = userInfoPersistence.getEmailIdsByTennantId(jdbcTemplate, organization.getTenantId(), 1, latestDocumentDate);
		
//			input JSONObject creation
						JSONObject input = new JSONObject();
				        
						input.put("tokenKey", uuidAsString);
//						input.put("uuid", uuidAsString);
						ObjectMapper mapper = new ObjectMapper();
						String writeValueAsString =
								null;
						try {
							writeValueAsString = mapper.writeValueAsString(document);
						} catch (JsonProcessingException e) {
							LOGGER.error("PolygonEdgeUtil.mintNft() error converting SigmaDocument to json documentO{}", document, e);
//							return new JSONObject();
						}
						JSONObject documentOJson = new JSONObject(writeValueAsString);
						for(SigmaAPIDocConfig sigmaAPIDocConfig : sigmaDocFieldConfigListLocal) {
							String sigmaField = sigmaAPIDocConfig.getSigmaField();
							String targetExtField = documentOJson.optString(sigmaField);
							input.put(sigmaField, targetExtField);
							}
						int sizeOfInput = sigmaDocFieldConfigListLocal.size()+1;
						for(int counter=sizeOfInput; counter<=10;counter++) {
							input.put("fVar"+counter, "");
						}
						input.put("fVar8", document.getFileName());
						input.put("fVar9", latestDocumentDate);
						input.put("fVar10", document.getDocChecksum());
						input.put("fVar11", document.getMd5Checksum());
						
//						String mintNftUrl = networkById.getSmartContractAccessUrl()+"contracts"+"/"+ networkById.getSmartContractAddress() +
//								"/mintNFT?kld-from="	+ networkById.getSmartContractDefaultWalletAddress() +"&kld-sync=true";
						
						PolygonEdgeUtil polygonEdgeUtil = new PolygonEdgeUtil();
						JSONObject nftInfo = polygonEdgeUtil.mintNftEthereumSigmacompliance(input, infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
						
						
				  		document.setNftCreationStatus(1);
				  		document.setStatus(nftInfo.optString("txhash","ERROR"));
				  		
						LOGGER.info("Thread {"+ Thread.currentThread()+"} created NFT for doc id =>  "+document.getSigmaId()+
				  				", uuid => "+nftInfo.optString("uuid","Error"));
						
						sigmaDocumentPersistence5.updateImmutableRecord(document, jdbcTemplate);


					
					} // tenant doc source
//					} 
				}
				catch (Exception e) {
					LOGGER.error("Application.populateDocumentData()", e);
				}
				return new ResponseEntity<String>("Successfully created, use **/v1/sigmafieldconf/{id} to get the config", HttpStatus.OK);
			} catch (Exception exception) {
				LOGGER.error("Error while getting the location risk result.", exception);
				throw new Exception("Error while getting the location risk result");
			}
		}
				//sigmacomplaincesuite
				@GetMapping(value = "/v1/sigmadoc2bytid/{tid}/{limit}")
				public ResponseEntity<List<SigmaDocument2>> getSigmadoc2byTid(@PathVariable("tid") String id, @PathVariable("limit") String id2) throws Exception {
					try {
						SigmaDocumentPersistence52 sigmaDocumentPersistence52 = new SigmaDocumentPersistence52();
						List<SigmaDocument2> generateDocument = sigmaDocumentPersistence52.getDocumentsByTenant2(jdbcTemplate, id, id2);
						if(generateDocument != null)
							return new ResponseEntity<List<SigmaDocument2>>(generateDocument, HttpStatus.OK);
						else
							return new ResponseEntity<List<SigmaDocument2>>(new ArrayList<SigmaDocument2>(), HttpStatus.OK);
					} catch (Exception exception) {
						LOGGER.error("Error while getting the location risk result.", exception);
						throw new Exception("Error while getting the location risk result");
					}
				}
	    }
		