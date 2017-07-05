package com.ner.featurevector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.fnlp.app.keyword.AbstractExtractor;

import com.ner.nlp.LoadModels;
import com.ner.nlp.WordSegmentation;

import cn.ner.readwrite.ReadFiles;

public class FeatureCollection {
	public List<String> getTriggerWordFeature(){
		List<String> lists=new ArrayList<>();
		try {
			String triggerWordLists=ReadFiles.readRawData("./triggetwords.txt");
			Collections.addAll(lists, triggerWordLists.split("\n"));
			//System.out.println(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}
	public Map<String, Integer>  keyWordsFeature(String sentence){
		LoadModels lm=LoadModels.getInstance();
		lm.keyWordsModel();
		AbstractExtractor key=lm.aeKey;
		Map<String, Integer> map=key.extract(sentence, 8);
		return map;
	}
	public String entityTypeFeature(TreeMap<String,String> entity_type,String entity){
		if (entity_type.containsKey(entity)) {
			return entity_type.get(entity);
		}else {
			return null;
		}
	}
	public static void main(String[] args) {
		FeatureCollection fc=new FeatureCollection();
		fc.getTriggerWordFeature();
		String sentence="公司主要供应商情况前五名供应商合计采购金额（元） 179,641,932.66前五名供应商合计采购金额占年度采购总额比例 15.35%公司前 5 名供应商资料 ";
		Map<String, Integer> map=fc.keyWordsFeature(sentence);
		for (Map.Entry<String, Integer> str: map.entrySet()) {
			System.out.println(str.getKey()+":"+str.getValue());
		}		
	}
}
