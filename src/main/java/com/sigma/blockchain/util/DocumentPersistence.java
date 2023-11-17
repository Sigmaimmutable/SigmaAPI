package com.sigma.blockchain.util;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.DocumentO;
import com.sigma.model.db.DocumentOPersistence4;

public class DocumentPersistence {
	private JdbcTemplate template;
	
	public DocumentPersistence(JdbcTemplate template) {
		super();
		this.template = template;
	}
	public void getDocuments() {
		
	}
	public void createImmutableRecords() {
		DocumentOPersistence4 persistence = new DocumentOPersistence4();
		List<DocumentO> documentList = persistence.getPaymentList(template, null);
		for(DocumentO documentO : documentList) {
			
		}

		
	}
}
