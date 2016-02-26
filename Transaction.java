package com.homedepot.myproject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "TRANS", strict = false)
public class Transaction {
	@Attribute(name = "STR_NBR")
	String storeNumber;
	
	@Attribute(name = "RGSTR_NBR")
	String registerNumber;
	
	@Attribute(name = "POS_TRANS_ID")
	String posTransId;
	
	@Attribute(name = "SLS_TS")
	String salesDate;
	
	public Transaction()
	{}
	
	public String getStoreNumber()
	{
		return storeNumber;
	}
	
	public String getRegisterNumber()
	{
		return registerNumber;
	}
	
	public String getPosTransId()
	{
		return posTransId;
	}
	
	public String getSalesDate()
	{
		return salesDate;
	}
}
