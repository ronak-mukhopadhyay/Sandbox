package com.homedepot.myproject;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Object;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.io.TextIO;
import com.google.cloud.dataflow.sdk.io.XmlSource;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;
import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.DoFnWithContext.ProcessContext;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import com.google.cloud.dataflow.sdk.values.PCollection;

public class TestR {
	
	public static void main(String[] args) throws IOException {

		Pipeline p = Pipeline.create(PipelineOptionsFactory.fromArgs(args).
			withValidation().create());
		
		File file2 = new File("Resources/messages-04.xml");
		Serializer serializer = new Persister();
		File output = new File("Resources/output.xml");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file2));
			FileWriter writer = new FileWriter(output);
			String currentLine;
			int lineCount = 0;
			while((currentLine = reader.readLine()) != null) {
				if(!currentLine.contains("<?xml") || lineCount == 0) {
					writer.write(currentLine);
					writer.write(System.getProperty( "line.separator" ));
					if(lineCount == 0) {
						writer.write("<root>");
						writer.write(System.getProperty( "line.separator" ));
					}
				}
				lineCount++;
			}
			writer.write("</root>");
			
			writer.close();
			reader.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TransactionContainer container = null;
		try {
			container = serializer.read(TransactionContainer.class, output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> cloudOutput = new ArrayList<String>();
		
		for(Transaction transaction : container.getTransactions()) {
			String cloudWrite = "StoreNumber: " + transaction.getStoreNumber() + " salesDate " + transaction.getSalesDate()
					+ " registerNumber: " + transaction.getRegisterNumber() + " postTransId " + transaction.getPosTransId();
			
			cloudOutput.add(cloudWrite);
			
			/*System.out.println("StoreNumber: " + transaction.getStoreNumber() + " salesDate " + transaction.getSalesDate()
				+ " registerNumber: " + transaction.getRegisterNumber() + " postTransId " + transaction.getPosTransId());*/
		}

		//System.out.println(cloudOutput.size());
		p.apply(Create.of(cloudOutput)).apply(TextIO.Write.named("WriteMyFile")
                .to("gs://ronaksandboxthd/Transaction Data 4"));
		
		p.run();
		return;
	}
}