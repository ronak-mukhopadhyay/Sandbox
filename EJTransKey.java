package com.homedepot.myproject;

import java.io.Serializable;
import java.time.LocalDate;

public class EJTransKey implements Serializable {
	private String storeNumber;
	private LocalDate salesDate; 
	private int posTransID;
	private int registerNumber;
	
	private EJTransKey() {}
	
	public static EJTransKey create(String storeNumber,
			LocalDate salesDate, int registerNumber,
			int posTransID) {
		EJTransKey key = new EJTransKey();
		key.storeNumber = storeNumber;
		key.salesDate = salesDate;
		key.registerNumber = registerNumber;
		key.posTransID = posTransID;
		return key;
	}


	public String getStoreNumber() {
		return storeNumber;
	}
	
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public LocalDate getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(LocalDate salesDate) {
		this.salesDate = salesDate;
	}

	public int getPosTransID() {
		return posTransID;
	}

	public void setPosTransID(int posTransID) {
		this.posTransID = posTransID;
	}

	public int getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(int registerNumber) {
		this.registerNumber = registerNumber;
	}
	
	
	
}
