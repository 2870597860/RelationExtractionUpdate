package com.ner.tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class TFIDF {
	public static ArrayList<Map.Entry<String,Double>> list =null;
	public static Map<String, Double> getTFIDF(List<String> wordsLists,List<String> sentences){
		Map<String, Double> wordsCount=getTF(wordsLists);
		Map<String, Double> wordsIDF=getIDF(sentences, wordsCount);
		for (String word : wordsCount.keySet()) {
			wordsIDF.put(word, (wordsCount.get(word))*(wordsIDF.get(word)));
		}
		return wordsIDF;
	}
	public static Map<String, Double> getTF(List<String> wordsLists){
		Map<String, Double> wordsCount=new TreeMap<>();
		if (wordsLists!=null) {
			for (String li : wordsLists) {
				if (wordsCount.get(li)!=null) {
					wordsCount.put(li, wordsCount.get(li)+1.0);
				}else {
					wordsCount.put(li, 1.0);
				}
			}
		}
		return wordsCount;
	}
	public static Map<String, Double> getIDF(List<String> sentences,Map<String, Double> wordsCount){
		//公式IDF＝log(|D|/(1+|Dt|))，其中|D|表示文档总数，|Dt|表示包含关键词t的文档数量。
		Map<String, Double> wordsIDF=new TreeMap<>();
		double Dt=1;
		double D=sentences.size();
		Set<String> wordsSet=wordsCount.keySet();
		for (String word : wordsSet) {
			Dt=1;
			for (String sentence : sentences) {
				if (sentence.contains(word)) {
					Dt+=1;
				}
			}
			wordsIDF.put(word, log(D/(1 + Dt), 10.0));
		}
		return wordsIDF;
	}
	public static Double gettfidf(List<String> sentences,String wordType,Double tf,HashMap<String, TreeMap<String,String>> companys_entitys_types){
		//公式IDF＝log(|D|/(1+|Dt|))，其中|D|表示文档总数，|Dt|表示包含关键词t的文档数量。
		double tfidf=0.0;
		double Dt=1;
		double D=sentences.size();
		Set<String> companys=companys_entitys_types.keySet();
		for (String company : companys) {
			 TreeMap<String,String> entitysType=companys_entitys_types.get(company);
			 Set<String> entitys=entitysType.keySet();
				for (String entity : entitys) {
					if (entitysType.get(entity).equals(wordType)) {
						for (String sentence : sentences) {
							if (sentence.contains(entity)) {
								Dt+=1;//Dt表示含有此类型实体的句子数
							}
						}
					}
				}
		}
		
		tfidf=log(D/(1 + Dt), 10.0);
		return tfidf;
	}

	private static Double log(Double value, Double base) {
		return (Double) (Math.log(value) / Math.log(base));
	}
	public static void SortMap(Map<String,Double> oldmap){    

		list = new ArrayList<Map.Entry<String,Double>>(oldmap.entrySet());    

		Collections.sort(list,new Comparator<Map.Entry<String,Double>>(){    
			@Override    
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {    
				return o2.getValue().compareTo(o1.getValue());  //降序    
			}    
		}); 
		/*
		for(int i = 0; i<list.size(); i++){    
			System.out.println(list.get(i).getKey()+ ": " +list.get(i).getValue());    
		} */ 
	} 
}
