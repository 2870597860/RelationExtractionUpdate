package com.ner.relationpattern;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ner.textpreprocess.ObjectAndDataCollection;

import cn.ner.readwrite.ReadFiles;

public class ParseXmlResult {
	
	public HashMap<String, List<String>> getTextRelationPattern(String path){
		DomParse domPar=new DomParse();
		
		
		//HashMap<String, TreeMap<String,String>> companys_entitys_types=ObjectAndDataCollection.companys_entitys_types;
		
		
		HashMap<String, List<String>> xmlParseResults=domPar.XMLParseStart(path);
		return xmlParseResults;
	}
	
}
