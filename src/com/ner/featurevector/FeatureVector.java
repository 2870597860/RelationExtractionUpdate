package com.ner.featurevector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.ner.nlp.WordSegmentation;
import com.ner.textpreprocess.ObjectAndDataCollection;
import com.ner.textpreprocess.OnlySentencesList;
import com.ner.tfidf.TFIDF;

public class FeatureVector {
	private static class FeatureVectorHolder{
		private static final FeatureVector fv=new FeatureVector();
	}
	private FeatureVector() {}
	public static FeatureVector getInstance(){
		return FeatureVectorHolder.fv;
	}
	final FeatureCollection fc=new FeatureCollection();
	
	final OnlySentencesList sl=new OnlySentencesList();
	final WordSegmentation ws=WordSegmentation.getInstance();
	Map<String, Double> triggerVec=new HashMap<>();
	List<String> wordsAll=new ArrayList<>();//所有分词结果
	List<String> words=fc.getTriggerWordFeature();//匹配触发词
	List<String> sentencesLists=sl.getSentencesList(ObjectAndDataCollection.entitySentenceMap);
	HashMap<String, TreeMap<String,String>> companys_entitys_types=ObjectAndDataCollection.companys_entitys_types;
	public List<Double> buildFeatureVector(String entityName,String sentence,String company,String type){
		return getFeature(entityName, sentence, company,type);

	}
	
	public List<Double> getFeature(String entityName,String sentence,String company,String type){
		Set<String> triggerWordFeature=new HashSet<>();
		Map<String, Integer> keyWordsKeyValue=fc.keyWordsFeature(sentence);//得到每个句子词汇特征
		Set<String>  keyWords=keyWordsKeyValue.keySet();
		//List<String> words=fc.getTriggerWordFeature();//匹配触发词
		for (String word : words) {
			if (keyWords.contains(word)) {
				triggerWordFeature.add(word);
			}
		}
		//得到实体类型特征
		String entityType=type;
		/*if (ObjectAndDataCollection.companys_entitys_types.size()<=0) {
			ObjectAndDataCollection.getEntitysTextsMapObject();
		}
		if(null!=ObjectAndDataCollection.companys_entitys_types.get(company)){
			TreeMap<String,String> entity_types=ObjectAndDataCollection.companys_entitys_types.get(company);
			if (null!=entity_types.get(entityName)) {
				entityType=entity_types.get(entityName);
			}
		}*/
		List<Double> vector=liangHuaFeature(keyWordsKeyValue, triggerWordFeature, entityType); 
	return vector;
	}
	private List<Double> liangHuaFeature(Map<String, Integer> keyWordsKeyValue,Set<String> triggerWordFeature,String entityType){
		List<Double> vector=new ArrayList<>();
		/**
		 * 用所有文章的所有句子为单位进行tfidf量化
		 */
		//触发词量化
		List<Double> triggers=new ArrayList<>();
		if (wordsAll.isEmpty()) {
			String reg = "[^\u4e00-\u9fa5]";
			for (String sentence : sentencesLists) {
				String s=ws.SegWord(sentence, false);
				String[] words=s.split("\\s+");
				//去掉停用词
				List<String> wordsList=ws.lm.sw.phraseDel(words);
				for (String word : wordsList) {
					String wordStr=word.replaceAll("\t|\r|\n|\r\n", "");
					if (wordStr.length()>1) {
						if (wordStr.getBytes().length!=wordStr.length()) {
							wordStr = wordStr.replaceAll(reg, "");
							wordsAll.add(wordStr);
						}
					}
				}
			}	
		}
			
		Map<String, Double> wordsTFIDF=TFIDF.getTFIDF(wordsAll, sentencesLists);
		for (String tw : triggerWordFeature) {
			Double d=wordsTFIDF.get(tw);
			if (d!=null) {			
				triggers.add(d/wordsTFIDF.size());
				triggerVec.put(tw, d/wordsTFIDF.size());
			}
		}
		if (triggers.size()>1) {
			Double he=0.0;
			for (Double double1 : triggers) {
				he+=double1*double1;
			}
			double vallue=Math.sqrt(he);
			vector.add(vallue);//添加触发词特征
		}
		//关键词特征量化
		Set<String> setWords=keyWordsKeyValue.keySet();
		Double dou=0.0;
		for (String string : setWords) {
			Integer in=keyWordsKeyValue.get(string);
			dou += Math.pow(in, 2);
		}
		Double douSqr=Math.sqrt(dou);
		for (String string : setWords) {
			Integer in=keyWordsKeyValue.get(string);
			vector.add(Math.sqrt(in/douSqr));
		}
		while (vector.size()<9) {
			vector.add(0.0);
		}
		//实体类型量化
		double etDou=0.0;
		double countAll=0;
		Set<String> companys=companys_entitys_types.keySet();
		for (String string : companys) {
			TreeMap<String, String> treeMap=companys_entitys_types.get(string);
			countAll+=treeMap.size();
			Set<String> entitys=treeMap.keySet();
			for (String string2 : entitys) {
				if (treeMap.get(string2).equals(entityType)) {
					etDou++;
				}
			}
		}
		 //Double tfidf=TFIDF.gettfidf(sentencesLists, entityType, etDou/countAll, companys_entitys_types);
	//	System.out.println(entityType+" "+etDou/countAll);
		vector.add(etDou/countAll);
		 return vector;
	}
}
