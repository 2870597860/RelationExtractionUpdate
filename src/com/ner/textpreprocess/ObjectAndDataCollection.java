package com.ner.textpreprocess;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ObjectAndDataCollection{
	public static HashMap<String, TreeMap<String,String>> companys_entitys_types=new HashMap<>();
	public static  HashMap<String, String> companys_texts=new HashMap<>();
	public static HashMap<String, HashMap<String, List<String>>> entitySentenceMap=new HashMap<>();
	public static void getEntitysTextsMapObject(){
		EntitysTextsMap et=new EntitysTextsMap();
		et.companys_entitys_types=companys_entitys_types;
		et.companys_texts=companys_texts;
		et.getCompanys_entitys_types();
		
		TableTextSeparateUpdate tts=new TableTextSeparateUpdate();
		tts.entitySentenceMap=entitySentenceMap;
		tts.companys_entitys_types=et.companys_entitys_types;
		tts.companys_texts=et.companys_texts;
		tts.getEntitySentence();
		System.out.println("数据获取完毕！");
	}
	
}
