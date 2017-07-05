package com.ner.featurevector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.ner.nlp.WordSegmentation;
import com.ner.textpreprocess.ObjectAndDataCollection;
import com.ner.textpreprocess.OnlyTriggerWordSentencesList;

public class FeatureVector {
	private static class FeatureVectorHolder{
		private static final FeatureVector fv=new FeatureVector();
	}
	private FeatureVector() {}
	public static FeatureVector getInstance(){
		return FeatureVectorHolder.fv;
	}
	final FeatureCollection fc=new FeatureCollection();
	final OnlyTriggerWordSentencesList sl=new OnlyTriggerWordSentencesList();
	WordSegmentation ws=WordSegmentation.getInstance();
	public void buildFeatureVector(String entityName,String sentence,String company){
		getFeature(entityName, sentence, company);
		
	}
	public void getFeature(String entityName,String sentence,String company){
		List<String> triggerWordFeature=new ArrayList<>();
		Map<String, Integer> keyWordsKeyValue=fc.keyWordsFeature(sentence);
		Set<String>  keyWords=keyWordsKeyValue.keySet();//得到词汇特征
		List<String> words=fc.getTriggerWordFeature();//匹配触发词
		for (String word : words) {
			if (keyWords.contains(word)) {
				triggerWordFeature.add(word);
			}
		}
		//得到实体类型特征
		String entityType=null;
		if (ObjectAndDataCollection.companys_entitys_types.size()<=0) {
			ObjectAndDataCollection.getEntitysTextsMapObject();
		}
		if(null!=ObjectAndDataCollection.companys_entitys_types.get(company)){
			TreeMap<String,String> entity_types=ObjectAndDataCollection.companys_entitys_types.get(company);
			if (null!=entity_types.get(entityName)) {
				entityType=entity_types.get(entityName);
			}
		}
		liangHuaFeature(keyWords, triggerWordFeature, entityType); 
	}
	public void liangHuaFeature(Set<String>  keyWords,List<String> triggerWordFeature,String entityType){
		List<String> sentencesLists=sl.getSentencesListOnlyTriggerWord(ObjectAndDataCollection.entitySentenceMap);
		List<String> allWords=new ArrayList<>();
		
		//用所有文章的所有句子为单位进行tfidf量化
		for (String sentence : sentencesLists) {
			String words=ws.SegWord(sentence, false);
		}
		
	}
}
