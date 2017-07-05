package com.ner.textpreprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/*
 * 获取触发词所需要分析的句子
 */
public class OnlyTriggerWordSentencesList {
	public List<String> getSentencesListOnlyTriggerWord(HashMap<String, HashMap<String, List<String>>> entitySentenceMap){		
		List<String> sentencesLists=new ArrayList<>();
		Set<String> companys=entitySentenceMap.keySet();
		System.out.println("获取所有句子集合。。。");
		for (String company : companys) {
			HashMap<String, List<String>> sentences=entitySentenceMap.get(company);
			Set<String> sets=sentences.keySet();
			for (String set : sets) {
				List<String> lists=sentences.get(set);
				for (String str : lists) {
					String[] es=str.split("~");
					if (es.length>1) {
						sentencesLists.add(es[1]);
					}
				}
			}
		}
		return sentencesLists;
	} 
	
}
