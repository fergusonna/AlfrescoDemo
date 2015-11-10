package com.nick.model;

import org.alfresco.service.namespace.QName;

public interface MyCoModel {
	
	//Types
	public static final String NAMESPACE_MYCO_CONTENT_MODEL = "http://www.mycompany.com/model/content/1.0";
	public static final String TYPE_MY_DOC = "doc";
	public static final String TYPE_MY_MARKETING_DOC = "marketingDoc";
	public static final String TYPE_MY_WHITEPAPER = "whitepaper";
	public static final String TYPE_MY_EXPENSE_REPORT = "expenseReport";
	public static final QName EXPENSE_REPORT_QNAME = QName.createQName("http://www.mycompany.com/model/content/1.0", "expenseReport");
	
	//Aspects
	public static final String ASPECT_MY_PUBLISH_TO_WEB = "publishToWeb";
	public static final String ASPECT_MY_CLIENT_RELATED = "clientRelated";
	public static final String ASPECT_MY_PRODUCT_RELATED = "productRelated";
	
	//Properties
	public static final String PROP_MY_PRODUCT = "product";
	public static final String PROP_MY_CLIENT_NAME = "clientName";
	public static final String PROP_MY_PROJECT_NAME = "projectName";
	public static final String PROP_MY_EXPENSE_TYPE = "expenseType";
	public static final String PROP_MY_TOTAL_AMOUNT = "totalAmount";
	public static final QName TOTAL_AMOUNT_QNAME = QName.createQName("http://www.mycompany.com/model/content/1.0", PROP_MY_TOTAL_AMOUNT);
	public static final QName EXPENSE_TYPE_QNAME = QName.createQName("http://www.mycompany.com/model/content/1.0", PROP_MY_EXPENSE_TYPE);
}
