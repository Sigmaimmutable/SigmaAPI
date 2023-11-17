package com.sigma.resource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigma.affinity.DocumentRetrieve;
import com.sigma.affinity.HttpURLConnectionUtil;
import com.sigma.affinity.ImmutabilityUtil;
import com.sigma.model.JobTrigger;
import com.sigma.model.Organization;
import com.sigma.model.PrivateNetwork2;
import com.sigma.model.SigmaAPIDocConfig;
import com.sigma.model.TenantDocSource2;
import com.sigma.model.db.JobTriggerPersistence;
import com.sigma.model.db.OrganizationPersistence6;
import com.sigma.model.db.PrivateNetworkPersistence3;
import com.sigma.model.db.SigmaDocFieldConfigPersistence6;
import com.sigma.model.db.SigmaProps;
import com.sigma.model.db.TenantDocSourcePersistence7;
import com.sigma.model.db.UserInfoPersistence;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import com.algo.files.FileSystemStorageService;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

@ComponentScan(basePackages = {"com.sigma.resource", "com.sigma.executors"})
@SpringBootApplication
@PropertySource(value = "classpath:application.properties")
@EnableAsync
@EnableScheduling
@RestController
public class Application  extends SpringBootServletInitializer 
{
	@Value("${web3.default.url:http://3.133.134.36:10002/}")
	private String web3RpcURl;
	@Value("${web3.default.blocks.to.pricess:10}")
	private Integer noOfBlocksToProcess;
	@Value("${web3.persist.blocks:true}")
	private Boolean blocksPersistEnable;	
	@Value("${sigma.default.batch.size.nft.create:30}")
	private Integer nftBatchSize;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Value("${sigma.ipfs.enabled:false}")
	private boolean ipfsEnabled;
	@Value("${nft.image.storage.path:/home/usr}")
	private String rootLocation;
	private static final Logger LOGGER = LoggerFactory.getLogger("com.sigma.resource.Application");

	@Value("${populate.document.delay.ms}")
	private Long populateDocumentDelay;
	private  ConfigurableEnvironment environment;

 	@Value("${blockchain.infuraUrl}")
private String infuraUrl;

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

@Value("${ec2IP1}")
private String ec2IP1;
@Value("${ec2IP2}")
private String ec2IP2;
@Value("${ec2IP3}")
private String ec2IP3;
	
	 @Autowired
	    private TaskScheduler taskScheduler;
	    private ScheduledFuture<?> scheduledTask;
	
	@Autowired
    public void ApplicationRestController(ConfigurableEnvironment environment, @Value("${populate.document.delay.ms}") Long populateDocumentDelay) {
        this.environment = environment;
        this.populateDocumentDelay = populateDocumentDelay;
    }

	@Bean
    public AtomicLong populateDocumentDelayAtomic(Environment environment) {
        String delayString = environment.getProperty("populate.document.delay.ms");
        long delay = Long.parseLong(delayString);
        return new AtomicLong(delay);
    }
	public static void main(String[] args)
	{  
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public FileSystemStorageService getStorageService() {
		return new FileSystemStorageService(rootLocation);
	}
	/*
	//@Scheduled(cron = "${generate.invoice.cron.expression}")
	//@Scheduled(fixedRate = 5000000)
	@Async
	public void generateInvoices() {
		LOGGER.info("Generating the invoices... Started");
		InvoiceGenerator invoiceGenerator =  new InvoiceGenerator();
		boolean shouldGenerateInvoice = invoiceGenerator.shouldGenerateInvoice(jdbcTemplate);
		if(shouldGenerateInvoice) {
			invoiceGenerator.generateInvoiceOutput(jdbcTemplate);
		}
		LOGGER.info("Generating the invoices... Complete");
	}
	*/
	//@Scheduled(fixedDelay = 1000*60*5)
	public void populateBlockData() {
		HttpURLConnectionUtil util = new HttpURLConnectionUtil(web3RpcURl, noOfBlocksToProcess, jdbcTemplate);
		if(blocksPersistEnable)
			util.findLatestData();
		else
			LOGGER.info("Disabled the block data persist");
	}
	
	
	public void populateDocumentData1() {
		LOGGER.info("populateDocumentData Logcheck ...");
	}
//	@Scheduled(fixedDelayString = "#{@populateDocumentDelayAtomic.get()}")
	public void populateDocumentData2() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    Date currentDate = new Date();
	    LOGGER.info("Scheduled execution at: {}", dateFormat.format(currentDate));
	    LOGGER.info("populateDocumentData Logcheck1 ...");
	}
	
	public void scheduledPopulateDocumentData() {
	    LOGGER.info("Scheduled populateDocumentData Logcheck ...");
	    populateDocumentData1(); // Call the actual method here
	}
	
//	public void populateDocumentData() {
//		LOGGER.info("populateDocumentData started ...");
//		
//		try {
//		DocumentRetrieve documentRetrieve = new DocumentRetrieve();
//		OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//		List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//		for(Organization organization : organizationList) {	
//			JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//			boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
//			
//			if(!jobcurrentstatus) {
//				LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
//				return;
//			}
//			SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//			List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//					sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//			if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//				LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
//				continue;
//			}
//			String tenantId = organization.getTenantId();
//			TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
//			List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
//			for(TenantDocSource2 source : organizationList2) {
//				if(source.getStatus() != 1)
//					continue;
//			//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//			PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//			PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//			SigmaProps props = new SigmaProps(source.getExtUrl()+"/api/v22.3/auth", source.getExtUrl()+"/api/v22.3/query", 
//					source.getExtUserName() , source.getExtPassword(), tenantId, true, source.getExtUrl()+"/api/v22.3/objects/documents/");
//				documentRetrieve.findLatestDocuments(props, jdbcTemplate, organization, sigmaDocFieldConfigList, networkById,"No");
//			} // tenant doc source
//			} 
//		}
//		catch (Exception e) {
//			LOGGER.error("Application.populateDocumentData()", e);
//		}		
//		LOGGER.info("populateDocumentData completed ...");
//	}
	@Value("${sigma.immutable.job.thread.count:3}")
	private Integer executorThreadCount;
	
	@Value("${sigma.run.docfetch.job.enabled:false}")
	private boolean runFetchJob;
	
	@Value("${sigma.run.immutable.job.enabled:false}")
	private boolean runImmutableJob;
	
//    @Scheduled(fixedDelay = 1000*60*30)
//	public void generateAndPersistNFT()  throws Exception{
//		LOGGER.info("Generate And Persist NFT started ...");
////		if(!runImmutableJob) {
////			LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob = "+runImmutableJob);
////			return;
////		}
//		OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//		List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//		DocumentRetrieve documentRet = new DocumentRetrieve();
//		for(Organization organization : organizationList) {	
//			JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//			boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"MAKE_IREC");
//			
//			if(!jobcurrentstatus) {
//				LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob ="+jobcurrentstatus);
//				return;
//			}
//			Long jobid = documentRet.updateJobStatus(0, "P", "", "Started the job !", jdbcTemplate, organization,
//					true, 1l, "MAKE_IREC","No");
//			LOGGER.info("Generate And Persist NFT started organization = "+ organization.getTenantId());
//			SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//			List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//					sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//			if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//				LOGGER.info("Generate And Persist NFT exited sigmaDocFieldConfigList is empty or null ");
//				continue;
//			}
//			PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//			PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//			ImmutabilityUtil immutabilityUtil = new ImmutabilityUtil(executorThreadCount);
//			LOGGER.info("Generate And Persist NFT initiated with org id "+ organization.getId());
//			immutabilityUtil.fetchAndCreateImmutabilityRecords(jdbcTemplate, 
//					nftBatchSize, networkById, sigmaDocFieldConfigList, organization.getTenantId());
//			documentRet.updateJobStatus(0, "Y", "", "Job Complete !", jdbcTemplate, organization,
//					false, jobid, "MAKE_IREC","No");
//			LOGGER.info("Generate And Persist NFT completed with org id "+ organization.getId());
//			UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//			Date date = new Date();
//			String formattedStringData = dateFormat.format(date);
//			
//			List<String> emailIds = userInfoPersistence.getEmailIdsJobByTennantId(jdbcTemplate,organization.getTenantId(), 10, formattedStringData);
//
//			}		
//		LOGGER.info("Generate And Persist NFT ended ...");
//		
//	}
	
//	 @Scheduled(fixedDelay = 1000*60*30)
//		public void generateAndPersistNFT()  throws Exception{
//			LOGGER.info("Generate And Persist NFT started ...");
////			if(!runImmutableJob) {
////				LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob = "+runImmutableJob);
////				return;
////			}
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
//				Long jobid = documentRet.updateJobStatus(0, "P", "", "Started the job !", jdbcTemplate, organization,
//						true, 1l, "MAKE_IREC","No");
//				LOGGER.info("Generate And Persist NFT started organization = "+ organization.getTenantId());
//				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//					LOGGER.info("Generate And Persist NFT exited sigmaDocFieldConfigList is empty or null ");
//					continue;
//				}
//				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//				PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//				ImmutabilityUtil immutabilityUtil = new ImmutabilityUtil(executorThreadCount);
//				LOGGER.info("Generate And Persist NFT initiated with org id "+ organization.getId());
//				immutabilityUtil.fetchAndCreateImmutabilityRecords(jdbcTemplate, 
//						nftBatchSize, networkById, sigmaDocFieldConfigList, organization.getTenantId(), infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
//				documentRet.updateJobStatus(0, "Y", "", "Job Complete !", jdbcTemplate, organization,
//						false, jobid, "MAKE_IREC","No");
//				LOGGER.info("Generate And Persist NFT completed with org id "+ organization.getId());
//				UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//				Date date = new Date();
//				String formattedStringData = dateFormat.format(date);
//				
//				List<String> emailIds = userInfoPersistence.getEmailIdsJobByTennantId(jdbcTemplate,organization.getTenantId(), 10, formattedStringData);
//
//				}		
//			LOGGER.info("Generate And Persist NFT ended ...");
//			
//		}
	
	@Scheduled(fixedDelay = 1000*60*30)
	public void generateAndPersistNFT()  throws Exception{
		LOGGER.info("Generate And Persist NFT started ...");
//		if(!runImmutableJob) {
//			LOGGER.info("Generate And Persist NFT exited due to  runImmutableJob = "+runImmutableJob);
//			return;
//		}
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
			Long jobid = documentRet.updateJobStatus(0, "P", "", "Started the job !", jdbcTemplate, organization,
					true, 1l, "MAKE_IREC","No");
			LOGGER.info("Generate And Persist NFT started organization = "+ organization.getTenantId());
			SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
			List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
					sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
			if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
				LOGGER.info("Generate And Persist NFT exited sigmaDocFieldConfigList is empty or null ");
				continue;
			}
//			PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//			PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
			ImmutabilityUtil immutabilityUtil = new ImmutabilityUtil(executorThreadCount);
			LOGGER.info("Generate And Persist NFT initiated with org id "+ organization.getId());
			immutabilityUtil.fetchAndCreateImmutabilityRecords(jdbcTemplate, 
					nftBatchSize, sigmaDocFieldConfigList, organization.getTenantId(), infuraUrl, contractAddress, privateKey, chainId, gasPrice, nonceApiUrl);
			documentRet.updateJobStatus(0, "Y", "", "Job Complete !", jdbcTemplate, organization,
					false, jobid, "MAKE_IREC","No");
			LOGGER.info("Generate And Persist NFT completed with org id "+ organization.getId());
			UserInfoPersistence userInfoPersistence = new UserInfoPersistence();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			Date date = new Date();
			String formattedStringData = dateFormat.format(date);
			
			List<String> emailIds = userInfoPersistence.getEmailIdsJobByTennantId(jdbcTemplate,organization.getTenantId(), 10, formattedStringData);

			}		
		LOGGER.info("Generate And Persist NFT ended ...");
		
	}


//	@PostMapping(value = "/v1/update-delay/{newDelay}")
//    public ResponseEntity<String> updatePopulateDocumentDelay(@PathVariable("newDelay") Long newDelay) {
//        if (newDelay != null) {
//            this.populateDocumentDelay = newDelay;
//            if (scheduledTask != null) {
//                scheduledTask.cancel(false);
//            }
//            scheduledTask = taskScheduler.scheduleWithFixedDelay(this::populateDocumentData, newDelay);
//            return ResponseEntity.ok("Populate Document Delay updated successfully to " + newDelay);
//        } else {
//            return ResponseEntity.badRequest().body("Invalid value for newDelay.");
//        }
//    }
	 
	 @PostMapping(value = "/v1/update-delay/{newDelay}")
	    public ResponseEntity<String> updatePopulateDocumentDelay(@PathVariable("newDelay") Long newDelay) {
	        if (newDelay != null) {
	            this.populateDocumentDelay = newDelay;
	            if (scheduledTask != null) {
	                scheduledTask.cancel(false);
	            }
	            scheduledTask = taskScheduler.scheduleWithFixedDelay(this::populateDocumentDataPrivate, newDelay);
	            return ResponseEntity.ok("Populate Document Delay updated successfully to " + newDelay);
	        } else {
	            return ResponseEntity.badRequest().body("Invalid value for newDelay.");
	        }
	    }
//	@Value("${ipfsUrl}")
//    private String ipfsUrl;
//	public void populateDocumentDataPrivate() {
//		LOGGER.info("populateDocumentData started ...");
//		
//		try {
//		DocumentRetrieve documentRetrieve = new DocumentRetrieve();
//		OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
//		List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
//		for(Organization organization : organizationList) {	
//			JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
//			boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
//			
//			if(!jobcurrentstatus) {
//				LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
//				continue;
//			}
//			SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
//			List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
//					sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
//			if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
//				LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
//				continue;
//			}
//			String tenantId = organization.getTenantId();
//			TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
//			List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
//			for(TenantDocSource2 source : organizationList2) {
//				if(source.getStatus() != 1)
//					continue;
//			//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//			PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//			PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
//			SigmaProps props = new SigmaProps(source.getExtUrl()+"/api/v22.3/auth", source.getExtUrl()+"/api/v22.3/query", 
//					source.getExtUserName() , source.getExtPassword(), tenantId, true, source.getExtUrl()+"/api/v22.3/objects/documents/");
// 				documentRetrieve.findLatestDocumentsPrivate(props, jdbcTemplate, organization, sigmaDocFieldConfigList, networkById,"No",ipfsUrl);
//			} // tenant doc source
//			} 
//		}
//		catch (Exception e) {
//			LOGGER.error("Application.populateDocumentData()", e);
//		}		
//		LOGGER.info("populateDocumentData completed ...");
//	}
	 @Value("${ipfsUrl}")
	    private String ipfsUrl;
		public void populateDocumentDataPrivate() {
			LOGGER.info("populateDocumentData started ...");
			
			try {
			DocumentRetrieve documentRetrieve = new DocumentRetrieve();
			OrganizationPersistence6 organizationPersistence6 = new OrganizationPersistence6();
			List<Organization> organizationList = organizationPersistence6.getOrganizationList(jdbcTemplate);
			for(Organization organization : organizationList) {	
				JobTriggerPersistence jobtriggerpersistence = new JobTriggerPersistence();
				boolean jobcurrentstatus = jobtriggerpersistence.getJobManageWithTennat(jdbcTemplate,organization.getTenantId(),"DOC_FETCH");
				
				if(!jobcurrentstatus) {
					LOGGER.info("populateDocumentData existed due to flag runFetchJob="+jobcurrentstatus);
					continue;
				}
				SigmaDocFieldConfigPersistence6  sigmaDocFieldConfigPersistence6 = new SigmaDocFieldConfigPersistence6();
				List<SigmaAPIDocConfig> sigmaDocFieldConfigList = 
						sigmaDocFieldConfigPersistence6.getSigmaDocFieldConfigList(jdbcTemplate, organization.getTenantId());
				if(sigmaDocFieldConfigList == null || sigmaDocFieldConfigList.isEmpty()) {
					LOGGER.info("populateDocumentData existed due to sigmaDocFieldConfigList is empty / null");
					continue;
				}
				String tenantId = organization.getTenantId();
				TenantDocSourcePersistence7 tenantDocSourcePersistence7 = new TenantDocSourcePersistence7();
				List<TenantDocSource2> organizationList2 = tenantDocSourcePersistence7.getOrganizationList(jdbcTemplate, tenantId);
				for(TenantDocSource2 source : organizationList2) {
					if(source.getStatus() != 1)
						continue;
				//TenantDocSource2 organizationInfo = tenantDocSourcePersistence7.getOrganizationInfo(jdbcTemplate, "1");
//				PrivateNetworkPersistence3 privateNetworkPersistence3 = new PrivateNetworkPersistence3();
//				PrivateNetwork2 networkById = privateNetworkPersistence3.getNetworkByTenant(jdbcTemplate, organization.getTenantId());
				SigmaProps props = new SigmaProps(source.getExtUrl()+"/api/v22.3/auth", source.getExtUrl()+"/api/v22.3/query", 
						source.getExtUserName() , source.getExtPassword(), tenantId, true, source.getExtUrl()+"/api/v22.3/objects/documents/");
	 				documentRetrieve.findLatestDocumentsPrivate(props, jdbcTemplate, organization, sigmaDocFieldConfigList, "No",ipfsUrl,ec2IP1, ec2IP2, ec2IP3);
				} // tenant doc source
				} 
			}
			catch (Exception e) {
				LOGGER.error("Application.populateDocumentData()", e);
			}		
			LOGGER.info("populateDocumentData completed ...");
		}
} 