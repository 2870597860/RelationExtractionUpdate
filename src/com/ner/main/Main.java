package com.ner.main;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.ner.featurevector.FeatureVector;
import com.ner.textpreprocess.ObjectAndDataCollection;
import com.ner.textpreprocess.OnlySentencesList;

import cn.ner.readwrite.ReadFiles;
import cn.ner.readwrite.WriteContent;

public class Main {
	public static void main(String[] args) {
		ObjectAndDataCollection.getEntitysTextsMapObject();
		FeatureVector fv=FeatureVector.getInstance();
		
		/*HashMap<String, TreeMap<String,String>> companys_entitys_types=ObjectAndDataCollection.companys_entitys_types;
		HashMap<String, HashMap<String, List<String>>> entitySentenceMap=ObjectAndDataCollection.entitySentenceMap;
		
		List<String> sentencesLists=new OnlySentencesList().getSentencesList(entitySentenceMap);
		Set<String> companys=companys_entitys_types.keySet();*/
		
		

		List<String> fileLists=null;
		WriteContent wc=new WriteContent();
		OutputStreamWriter osw=wc.writeConAppend("./data/seeds/seeddatafile");
		OutputStreamWriter oswtrain=wc.writeConAppend("./data/seeds/datafile");
		try {
			int count=0;
			fileLists = ReadFiles.readDirs("data/companys");
			Set<String> trainvector=new HashSet<>();
			for (String file : fileLists) {
				String str=ReadFiles.readRawData(file);
				//System.out.println(str);
				String company=file.substring(file.lastIndexOf("\\")+"\\".length());
				osw.write(company+"(:)\n");
				String biaoge=str.substring(str.indexOf("biaoge->")+"biaoge->\n".length(),str.indexOf("text->"));
				String[] bgArr=biaoge.split("\n");
				for (int i = 0; i < bgArr.length; i++) {
					String[] bgArrArr=bgArr[i].split(" : ");//数组中分别是实体、类型、句子
					if (bgArrArr.length>2) {
						System.out.println("第"+(count++));
						List<Double> vector=fv.buildFeatureVector(bgArrArr[0], bgArrArr[2], company,bgArrArr[1]);
						StringBuffer cacheVector=new StringBuffer();
						double countAll=0.0;
						for (Double dou : vector) {
							countAll+=dou;
						}
						for (Double dou : vector) {
							cacheVector.append(Double.parseDouble(String.format("%.2f",(dou/countAll)))+" ");
						}
						trainvector.add(cacheVector.toString());
						osw.write(bgArrArr[0]+"~"+cacheVector.toString()+"\n");						
						osw.flush();
					}					
				}
				
				String text=str.substring(str.indexOf("text->")+"text->\n".length());
				
				
			}
			StringBuilder sbb=new StringBuilder();
			for (String string : trainvector) {
				sbb.append(string+"\n");
			}
			oswtrain.write(sbb.toString());
			oswtrain.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public void textSentenceDepPro(String sentence){
		
	}
}
