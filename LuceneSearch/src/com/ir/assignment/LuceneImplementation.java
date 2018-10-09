/*
 * The program is written for implementing Lucene Functionalities as a part of Information Retrieval Programming Assignment 01
 * Code has been developed by the following Group members
 * Abhisar Bharti
 * Shobhit Chourasiya

 
 This is the main class and from this class, subsequent calls are made to other Libraries for indexing and Searching Purpose 
 */
package com.ir.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.commons.io.FileUtils;
import org.tartarus.snowball.ext.PorterStemmer;

public class LuceneImplementation {
	String indexDir; 
	String dataDir;
	Indexer indexer;
	Searcher searcher;
	
	public LuceneImplementation(){
	}
	
	public void SetIndexPath(String Path)
	{
		indexDir=Path;
	}
	public void SetContentPath(String Path)
	{
		dataDir=Path;
	}
	
	public String getIndexPath()
	{
		return indexDir;
	}
	
	/*
	 This is the main method which performs the operations. Through Arguments,
	  we take the input and accordingly, the data is passed for indexing and searching
	 */
	public static void main(String[] args) throws ParseException, IOException {
		LuceneImplementation app= new LuceneImplementation();
		try {	
	         InputStreamReader r=new InputStreamReader(System.in); 
	         app.SetIndexPath(args[1]);
	         app.SetContentPath(args[0]);
	        System.out.println("Index path:"+app.getIndexPath());
	        purgeDirectory(new File(app.getIndexPath()));
	        app.createIndex(args[0]);	        
	        app.search(args[3]+" "+Indexer.StringPorterStemmer(args[3]),args[2]); 
		}       
	         
		
	        catch(IndexNotFoundException e){
	              	
	        	System.out.println("Index directory found empty. Starting indexing Step.");
	        	app.createIndex(args[1]);
	        	app.search(args[3]+" "+Indexer.StringPorterStemmer(args[3]),args[2]);
	        }
	         
	       
	       catch (IOException e) {
	         e.printStackTrace();
	      }
	      
	}
/*This method searches and returns the output.
 * The argument model specifies Okapi or vector space model for performing search 
 */
	private void search(String searchQuery, String model) throws IOException, ParseException {
		searcher = new Searcher(indexDir,model);
	    long startTime = System.currentTimeMillis();
	    TopDocs hits = searcher.search(searchQuery);
	    long endTime = System.currentTimeMillis();
	    int rank=1;
	    System.out.println(hits.totalHits +
	         " documents found. Time :" + (endTime - startTime));
	    for(ScoreDoc scoreDoc : hits.scoreDocs) {
	        Document doc = searcher.getDocument(scoreDoc);
	        if((doc.get(ConstantsData.FILE_NAME).contains(".html"))||(doc.get(ConstantsData.FILE_NAME).contains(".htm")))
	        	System.out.println("Rank(Score):"+rank+"("+scoreDoc.score+") File Name(title):"+doc.get(ConstantsData.FILE_NAME)+"("+doc.get(ConstantsData.FILE_HTML_TITLE)+") File Path:" + doc.get(ConstantsData.FILE_PATH)+"   Time Stamp:"+doc.get(ConstantsData.FILE_TIMESTAMP));
	        else
	        	System.out.println("Rank(Score):"+rank+"("+scoreDoc.score+") File Name:"+doc.get(ConstantsData.FILE_NAME)+" File Path:" + doc.get(ConstantsData.FILE_PATH)+"   Time Stamp:"+doc.get(ConstantsData.FILE_TIMESTAMP));
	        rank++;
	    }
	}
	static void purgeDirectory(File dir) {
		try{
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) purgeDirectory(file);
	        file.delete();
	    }
	    }
		catch(NullPointerException e)
		{
			System.out.println("Index directory empty. Continue");
		}
		
	}
	static public String PorterStemmerSearch(String QueryString)
	{
		PorterStemmer stemmer = new PorterStemmer();
		
			StringBuffer stemedLine = new StringBuffer();
			String[] words = QueryString.split(" ");
			for(int i=0; i<words.length;i++)
			{
				stemmer.setCurrent(words[i]);
				stemmer.stem();
		        stemedLine.append(stemmer.getCurrent()+" ");
			}
		return stemedLine.toString();
	}
	private void createIndex(String dataDirPath) throws IOException {
		
		indexer = new Indexer(indexDir);
	    int numIndexed;
	    long startTime = System.currentTimeMillis();	
	    numIndexed = indexer.createIndex(dataDirPath, new FileTypeFilter());
	    long endTime = System.currentTimeMillis();
	    indexer.close();	    
	}
}
