package com.homedepot.myproject;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "root", strict = false)
public class TransactionContainer
{
	@ElementList(inline = true)
	List<Transaction> transactions;
	
	public TransactionContainer()
	{}
	
	public List<Transaction> getTransactions()
	{
		return transactions;
	}
}
