package com.homedepot.myproject;

import javax.xml.stream.XMLStreamException;

public class EJTransaction {
	private String transXML;
	/**
	 * Nullable until after parseKey is called.
	 */
	private EJTransKey key;
	
	public EJTransaction(String transXML) {
		this.transXML = transXML;
	}
	
	public void parseKey() throws XMLStreamException {
		key = EJTransactionParser.parseKey(transXML);
	}
	
	public EJTransKey getKey() {
		return key;
	}
}
