package com.sigma.model;

import java.util.Date;

public class DocumentO {

	private String	filename__v;
	private	Long id;					
	private	String version_id;
	private	String	source_owner__v;				
	private	String	primary_author__c;				
	private	String	source_document_number__v;		
	private	String	application__v;					
	private	String	name__v;							
	private	String	type__v;							
	private	String	title__v;						
	private	String	manufacturer__v;					
	private	String	source_vault_id__v;				
	private	String	created_by__v;					
	private	String	applications__v;					
	private	String	region__v;						
	private	String	status__v;	
	private	Date	document_date__c;				
	private	Date	binder_last_autofiled_date__v;	
	private	Date	archived_date__sys;				
	private	Date	file_modified_date__v;			
	private	Date	file_created_date__v;			
	private	Date	document_creation_date__v;
	private String uuid;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	private String doc_Checksum;
	private String nftCreationStatus; 
	public String getDoc_Checksum() {
		return doc_Checksum;
	}
	public void setDoc_Checksum(String doc_Checksum) {
		this.doc_Checksum = doc_Checksum;
	}
	public String getNftCreationStatus() {
		return nftCreationStatus;
	}
	public void setNftCreationStatus(String nftCreationStatus) {
		this.nftCreationStatus = nftCreationStatus;
	}
	public String getFilename__v() {
		return filename__v;
	}
	public void setFilename__v(String filename__v) {
		this.filename__v = filename__v;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSource_owner__v() {
		return source_owner__v;
	}
	public void setSource_owner__v(String source_owner__v) {
		this.source_owner__v = source_owner__v;
	}
	public String getPrimary_author__c() {
		return primary_author__c;
	}
	public void setPrimary_author__c(String primary_author__c) {
		this.primary_author__c = primary_author__c;
	}
	public String getSource_document_number__v() {
		return source_document_number__v;
	}
	public void setSource_document_number__v(String source_document_number__v) {
		this.source_document_number__v = source_document_number__v;
	}
	public String getApplication__v() {
		return application__v;
	}
	public void setApplication__v(String application__v) {
		this.application__v = application__v;
	}
	public String getName__v() {
		return name__v;
	}
	public void setName__v(String name__v) {
		this.name__v = name__v;
	}
	public String getType__v() {
		return type__v;
	}
	public void setType__v(String type__v) {
		this.type__v = type__v;
	}
	public String getTitle__v() {
		return title__v;
	}
	public void setTitle__v(String title__v) {
		this.title__v = title__v;
	}
	public String getManufacturer__v() {
		return manufacturer__v;
	}
	public void setManufacturer__v(String manufacturer__v) {
		this.manufacturer__v = manufacturer__v;
	}
	public String getSource_vault_id__v() {
		return source_vault_id__v;
	}
	public void setSource_vault_id__v(String source_vault_id__v) {
		this.source_vault_id__v = source_vault_id__v;
	}
	public String getCreated_by__v() {
		return created_by__v;
	}
	public void setCreated_by__v(String created_by__v) {
		this.created_by__v = created_by__v;
	}
	public String getApplications__v() {
		return applications__v;
	}
	public void setApplications__v(String applications__v) {
		this.applications__v = applications__v;
	}
	public String getRegion__v() {
		return region__v;
	}
	public void setRegion__v(String region__v) {
		this.region__v = region__v;
	}
	public String getStatus__v() {
		return status__v;
	}
	public void setStatus__v(String status__v) {
		this.status__v = status__v;
	}
	public Date getDocument_date__c() {
		return document_date__c;
	}
	public void setDocument_date__c(Date document_date__c) {
		this.document_date__c = document_date__c;
	}
	public Date getBinder_last_autofiled_date__v() {
		return binder_last_autofiled_date__v;
	}
	public void setBinder_last_autofiled_date__v(Date binder_last_autofiled_date__v) {
		this.binder_last_autofiled_date__v = binder_last_autofiled_date__v;
	}
	public Date getArchived_date__sys() {
		return archived_date__sys;
	}
	public void setArchived_date__sys(Date archived_date__sys) {
		this.archived_date__sys = archived_date__sys;
	}
	public Date getFile_modified_date__v() {
		return file_modified_date__v;
	}
	public void setFile_modified_date__v(Date file_modified_date__v) {
		this.file_modified_date__v = file_modified_date__v;
	}
	public Date getFile_created_date__v() {
		return file_created_date__v;
	}
	public void setFile_created_date__v(Date file_created_date__v) {
		this.file_created_date__v = file_created_date__v;
	}
	public Date getDocument_creation_date__v() {
		return document_creation_date__v;
	}
	public void setDocument_creation_date__v(Date document_creation_date__v) {
		this.document_creation_date__v = document_creation_date__v;
	}
	public String getVersion_id() {
		return version_id;
	}
	public void setVersion_id(String version_id) {
		this.version_id = version_id;
	}
}