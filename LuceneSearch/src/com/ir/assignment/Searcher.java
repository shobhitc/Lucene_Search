package com.ir.assignment;
/*
 *This class is responsible for the Search functionality, which is implemented with the Lucene Library
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;

public class Searcher {
	IndexSearcher indexSearcher;
	QueryParser queryParser;
	Query query;
	
	public Searcher(String indexDirectoryPath,String model) 
		      throws IOException{
    
		//Initialize the directory which has to be searched and attach the Index directory
		Directory dir = FSDirectory.open(Paths.get(indexDirectoryPath));
	    IndexReader reader = DirectoryReader.open(dir);
	    this.indexSearcher = new IndexSearcher(reader);
	    if(model.equalsIgnoreCase("ok"))
	    	this.indexSearcher.setSimilarity(new BM25Similarity());
	    if(model.equalsIgnoreCase("vs"))
	    	this.indexSearcher.setSimilarity(new ClassicSimilarity());
	    
	    
	}
	
	
	
	public Document getDocument(ScoreDoc scoreDoc) 
		      throws CorruptIndexException, IOException{
		      return indexSearcher.doc(scoreDoc.doc);	
	}
	
	public TopDocs search(String searchQuery) 
		      throws IOException, ParseException{
		//Query the index for the search result and return the ranked results
	    queryParser = new QueryParser(ConstantsData.CONTENTS, new StandardAnalyzer());
	    query = queryParser.parse(searchQuery);
	    return indexSearcher.search(query, ConstantsData.MAX_SEARCH);
	}

}
