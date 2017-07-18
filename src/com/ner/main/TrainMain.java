package com.ner.main;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.ner.featurevector.FeatureVector;
import com.ner.relationpattern.DepParsingMain;
import com.ner.textpreprocess.ObjectAndDataCollection;
import com.ner.textpreprocess.OnlySentencesList;

import cn.ner.readwrite.ReadFiles;
import cn.ner.readwrite.WriteContent;

public class TrainMain {
	
	public static void main(String[] args) {
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\";
		ObjectAndDataCollection.getEntitysTextsMapObject( entityPath, textPath);
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
			fileLists = ReadFiles.readDirs("data/traincorpus/companys/");
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
						if (bgArrArr[2].contains("子公司")||bgArrArr[2].contains("参股公司")) {
							trainvector.add(cacheVector.toString()+" "+3 );
						}
						if (bgArrArr[2].contains("客户")) {
							trainvector.add(cacheVector.toString()+" "+0 );
						}
						if (bgArrArr[2].contains("供应商")) {
							trainvector.add(cacheVector.toString()+" "+1 );
						}
						if (bgArrArr[2].contains("研发")||bgArrArr[2].contains("注册")) {
							trainvector.add(cacheVector.toString()+" "+2 );
						}						
						osw.write(bgArrArr[0]+"~"+cacheVector.toString()+"\n");						
						osw.flush();
					}					
				}
				
								
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
	
}
