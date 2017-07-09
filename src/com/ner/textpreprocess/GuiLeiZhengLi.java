package com.ner.textpreprocess;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import cn.ner.readwrite.WriteContent;

public class GuiLeiZhengLi {
	public void getEntitySentenceMap(){
		/*EntitysTextsMap et=new EntitysTextsMap();
		et.getCompanys_entitys_types();

		TableTextSeparateUpdate tts=new TableTextSeparateUpdate();
		tts.companys_entitys_types=et.companys_entitys_types;
		tts.companys_texts=et.companys_texts;
		tts.getEntitySentence();
		//获取实体和句子
		HashMap<String, HashMap<String, List<String>>> entitySentenceMap=tts.entitySentenceMap;
		System.out.println("获取实体和句子结束");
		//归类整理
		gz.guiLeiWrite(et.companys_entitys_types, entitySentenceMap);*/
		ObjectAndDataCollection.getEntitysTextsMapObject();
		//获取实体和句子
		HashMap<String, HashMap<String, List<String>>> entitySentenceMap=ObjectAndDataCollection.entitySentenceMap;
		guiLeiWrite(ObjectAndDataCollection.companys_entitys_types, entitySentenceMap);
	}
	/*
	 * 将获取得到的实体，实体类型，以及句子以公司为单位进行划分整理；写到companys文件夹中
	 */
	public void guiLeiWrite(HashMap<String, TreeMap<String,String>> companys_entitys_types,
			HashMap<String, HashMap<String, List<String>>> entitySentenceMap){
		Set<String> companys=entitySentenceMap.keySet();
		StringBuilder sb=new StringBuilder();
		WriteContent wc=new WriteContent();
		for (String company : companys) {
			HashMap<String, List<String>> flagSentences=entitySentenceMap.get(company);
			Set<String> setFlags=flagSentences.keySet();
			for (String flag : setFlags) {
				sb.append(flag+"->"+"\n");
				List<String> sentences=flagSentences.get(flag);
				for (String sentence : sentences) {
					String[] arr=sentence.split("~");
					if (arr.length>1) {
						String entity=arr[0];
						String sen=arr[1].replace("['   ']+"," ").replace("\n", " ");
						String type="";
						if (companys_entitys_types.containsKey(company)) {
							type=companys_entitys_types.get(company).get(entity);
						}
						sb.append(entity+" : "+type+" : "+sen);
						sb.append("\n");
					}
				}
			}
			wc.writeCon(sb.toString(), "./data/companys/"+company);
			sb.setLength(0);
		}
	}


}
