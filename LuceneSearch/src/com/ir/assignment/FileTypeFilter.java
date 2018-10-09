package com.ir.assignment;

import java.io.File;
import java.io.FileFilter;

public class FileTypeFilter implements FileFilter {
	public boolean accept(File pathname) {
		//Returns true for txt or html files only
		if(pathname.getName().toLowerCase().endsWith(".txt") || pathname.getName().toLowerCase().endsWith(".htm") || pathname.getName().toLowerCase().endsWith(".html")){
			return true;
		}else {
			return false;
		}
	      
	   }
}
