package com.homedepot.myproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.time.LocalDate;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.homedepot.ta.wh.ej.EJTransactionParser.XMLStreamValidationException;

public class EJTransactionParserTest {
	
	@Test(expected=XMLStreamException.class)
	public void testParseKey_Negative_GivenNonWellFormedXML_ExpectXMLStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("");
	}
	
	@Test
	public void testParseKey_Negative_GivenInvalidXML_ExpectXMLValidationStreamException() throws XMLStreamException {
		try {
			EJTransactionParser.parseKey("<bad></bad>");
		} catch (XMLStreamValidationException e) {
			assertThat(e.getMessage(), containsString("Root=bad"));
		}
	}

	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenInvalidPOSTransID_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='' SLS_TS='2016-01-28 20:22:33.414' RGSTR_NBR='73' STR_NBR='9752' />");
	}

	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenInvalidRegisterNumber_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28 20:22:33.414' RGSTR_NBR='Ricky' STR_NBR='9752' />");
	}
	
	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenInvalidSalesDate1_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28' RGSTR_NBR='1' STR_NBR='9752' />");
	}
	
	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenInvalidSalesDate2_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28 ' RGSTR_NBR='1' STR_NBR='9752' />");
	}

	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenInvalidSalesDate3_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='28-16-2016' RGSTR_NBR='1' STR_NBR='9752' />");
	}

	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenMissingPOSTransID_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS SLS_TS='2016-01-28 20:22:33.414' RGSTR_NBR='73' STR_NBR='9752' />");
	}

	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenMissingRegisterNumber_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28 20:22:33.414' STR_NBR='9752' />");
	}
	
	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenMissingSalesDate_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' RGSTR_NBR='1' STR_NBR='9752' />");
	}
	
	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenMissingStrNbr_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28 20:22:33.414' RGSTR_NBR='1'  />");
	}

	@Test(expected=XMLStreamValidationException.class)
	public void testParseKey_Negative_GivenInvalidSStrNbr_ExpectXMLValidationStreamException() throws XMLStreamException {
		EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28 20:22:33.414' RGSTR_NBR='1' STR_NBR='' />");
	}
	
	@Test
	public void testParseKey_GivenValidTransElement_ExpectSuccess() throws XMLStreamException {
		EJTransKey key = EJTransactionParser.parseKey("<TRANS POS_TRANS_ID='1' SLS_TS='2016-01-28 20:22:33.414' RGSTR_NBR='73' STR_NBR='9752' />");
		assertNotNull(key);
		assertEquals("9752", key.getStoreNumber());
		assertEquals(1, key.getPosTransID());
		assertEquals(73, key.getRegisterNumber());
		assertEquals(LocalDate.parse("2016-01-28"), key.getSalesDate());
	}
	
}
