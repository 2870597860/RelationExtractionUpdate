package com.ner.relationpattern;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ParseXmlResult {
	HashMap<String, List<String>> xmlParseResults;
	public void getTextRelationPattern(){
		DomParse domPar=new DomParse();
		xmlParseResults=domPar.XMLParseStart("");
		Set<String> set=xmlParseResults.keySet();
		for (String string : set) {
			System.out.println("以上获取关系模式的公司为：："+string);
		}
	}
}
