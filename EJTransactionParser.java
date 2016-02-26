package com.homedepot.myproject;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class EJTransactionParser {

	private static final String ROOT_ELEM = "TRANS";

	public static EJTransKey parseKey(String transXML) throws XMLStreamException {
	    XMLInputFactory factory = XMLInputFactory.newInstance();
	    XMLStreamReader reader = 
	        factory.createXMLStreamReader(new StringReader(transXML));
	    while(reader.hasNext()){
	      int event = reader.next();
	      switch(event){
	        case XMLStreamConstants.START_ELEMENT: 
	          if (ROOT_ELEM.equals(reader.getLocalName())){
	            return EJTransKey.create(
	            		getRequiredNonEmptyAttributeValue(reader, "STR_NBR")
	            		,parseSalesDateFromSalesTimestamp(reader)
	            		,getRequiredAttributeValueAsInt(reader, "RGSTR_NBR")
	            		,getRequiredAttributeValueAsInt(reader, "POS_TRANS_ID")
	            		);
	          }
	          else {
	        	  throw new XMLStreamValidationException("Root element was not the expected " + ROOT_ELEM + ". Root=" + reader.getLocalName(), (Throwable)null); 
	          }
	      }
	    }
	    throw new XMLStreamValidationException("Root element " + ROOT_ELEM + " was not found.", (Throwable)null);
	}
	
	private static String getRequiredNonEmptyAttributeValue(
			XMLStreamReader reader, String attrLocalName) throws XMLStreamValidationException {
		String value = reader.getAttributeValue((String)null, attrLocalName);
		if (value==null
			|| value.trim().length()==0) {
			throw new XMLStreamValidationException("Invalid value for attrLocalName=" + attrLocalName + ".  [Value]=[" + value + "]");
		}
		return value;
	}

	private static int getRequiredAttributeValueAsInt(XMLStreamReader reader, String attrLocalName) throws XMLStreamValidationException {
		String attributeValue = getRequiredNonEmptyAttributeValue(reader, attrLocalName);
		try {
			return Integer.parseInt(attributeValue);
		} catch (NumberFormatException e) {
			throw new XMLStreamValidationException("Invalid int value for attrLocalName=" + attrLocalName + ". [Value]=["+attributeValue+"]", e);
		}
	}

	private static LocalDate parseSalesDateFromSalesTimestamp(XMLStreamReader reader) throws XMLStreamValidationException {
		final String salesTimpestampAttrName = "SLS_TS";
		String salesTimestampAttrValue = getRequiredNonEmptyAttributeValue(reader, salesTimpestampAttrName);
		/* Strip off the time. 
		 * Expected format: SALES_DT + ' ' + TIME 
		 * Ex: '2016-01-28 20:22:33.414' */ 
		String[] parts = salesTimestampAttrValue.split(" ");
		if ( parts.length != 2 ) {
			throw new XMLStreamValidationException("Invalid Sales Date in attribute " + salesTimpestampAttrName + ". [Value]=[" + salesTimestampAttrValue + "]");
		}
		try {
			return LocalDate.parse(parts[0]);
		} catch (DateTimeParseException e) {
			throw new XMLStreamValidationException("Invalid Sales Date in attribute " + salesTimpestampAttrName + ". [Value]=[" + salesTimestampAttrValue + "]", e);
		}
	}

	/**
	 * Custom exception for xml validation errors.
	 * @author nwh02
	 *
	 */
	public static class XMLStreamValidationException extends XMLStreamException {
		private static final long serialVersionUID = 1L;

		public XMLStreamValidationException(String msg, Throwable thr) {
			super(msg,thr);
		}

		public XMLStreamValidationException(String msg) {
			super(msg);
		}
	}
}
