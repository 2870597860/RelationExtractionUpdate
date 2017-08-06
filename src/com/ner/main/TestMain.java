package com.ner.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ner.knn.KNNClasMain;
import com.ner.relationpattern.similar.Simhash;

import cn.ner.readwrite.ReadFiles;
import cn.ner.readwrite.WriteContent;

public class TestMain {
	public static HashMap<String, HashMap<String,List<String>>> matchResults=new HashMap<>();
	HashMap<String, List<String>> relPatternSeed=new HashMap<>();
	public static void main(String[] args) {
		//knn分类
		KNNClasMain knnc=new KNNClasMain();
		knnc.testVectorDatas();
		//获取分类结果
		HashMap<String, HashMap<String, String>> classifacResults=knnc.getClassifacResults();

	}
	public void matchRelaPatte(){
		String trainPath="./data/seeds/seedRelationPattern";
		String testPath="./data/testcorpus/RelationPatternAllTest";
		//HashMap<String, List<String>> relPatternSeed=new HashMap<>();
		//训练语料
		List<String> trainRPLists=readRalationPattern(trainPath);
		for (String rptTrainResult : trainRPLists) {
			String[] trainRP=rptTrainResult.replace("<", "").replace(">", "").split(";");
			String[] arr=trainRP[2].split(":");
			if (!relPatternSeed.containsKey(trainRP[0])) {
				List<String> relation=new ArrayList<>();
				relation.add(arr[1]);
			}else {
				relPatternSeed.get(trainRP[0]).add(arr[1]);
			}
		}
		Set<String> seedRelations=relPatternSeed.keySet();
		//测试语料
		List<String> testRPLists=readRalationPattern(testPath);
		for (String rpResult : testRPLists) {
			String[] testRP=rpResult.replace("<", "").replace(">", "").split(";");
			String[] arr=testRP[1].split(":");
			String company=testRP[0];
			Simhash simhash = new Simhash(4, 19);//默认按照8段进行simhash存储和汉明距离的衡量标准\
			Long simhashVal = simhash.calSimhash(arr[1]);
			simhash.store(simhashVal, arr[1]);
			List<String> changeSeed=new ArrayList<>();
			int minHamm=30;//用于比较并存放最小汉明距离
			String minHammRealtion="";
			String entityCompany="";
			//然后再一次和每一个种子关系下的种子关系模式匹配，汉明距离最小的则判定语料关系模式归为此关系。
			for (String relation : seedRelations) {
				if (!matchResults.get(company).containsKey(relation)) {
					matchResults.get(company).put(relation, new ArrayList<String>());
				}
				List<String> rpArr=relPatternSeed.get(relation);
				for (String seedPattern : rpArr) {
					String seedRP=seedPattern.substring(seedPattern.indexOf(":")+":".length());
					int[] hamm=new int[2];
					hamm[0]=100;
					boolean flag=simhash.isDuplicate(seedRP,hamm);
					//System.out.println(flag);
					if (flag) {
						System.out.println(hamm[0]);
						if (minHamm>hamm[0]) {
							minHamm=hamm[0];
							minHammRealtion=relation;
						}
						if (rpResult.indexOf(":")>0) {
							entityCompany=rpResult.substring(0,rpResult.indexOf(":"));
						}
						//System.out.println("company:"+company);
						//entityCompany=amendEntity(company, entityCompany,gett);
					}
					/*
					 * 将从语料中抽取的hamm距离大于8关系模式加入种子模式
					 */
					// 将匹配的结果放入集合中	
					if (!minHammRealtion.equals("")) {
						if (minHamm>16) {
							relPatternSeed.get(minHammRealtion).add(rpResult);
						}
						if (entityCompany.equals("青岛得润电子有限公司")) {
							System.out.println();
						}
						if (entityCompany!=null) {
							matchResults.get(company).get(minHammRealtion).add(entityCompany);
						}

					}
				}
			}
		}
		//进行统计
		countCorrect();
		//将新添加的关系模式写入文本
		writeRelPatternSeed();
	}
	//对每篇文章关系下的实体进行统计
		public void countCorrect(){
			Set<String> tongji=matchResults.keySet();	
			int countAll=0;
			for (String company : tongji) {			
				System.out.println("\n===================================");
				System.out.println("\n"+company+":::");
				HashMap<String, List<String>> tongjiRelations=matchResults.get(company);
				Set<String> relations=tongjiRelations.keySet();
				for (String relation : relations) {
					System.out.println(relation+":");
					List<String> entitys=tongjiRelations.get(relation);
					int i=0;
					for (String entity : entitys) {
						System.out.print(entity+"          ");
						++i;
						if ((i%4)==0) {
							System.out.println();
						}
					}
					System.out.println("总共有：："+i);
					countAll+=i;
				}
			}
			System.out.println("总共有识别出的实体有：："+countAll);
		}
		//将新添加的关系模式写入文本(包含新添加的关系模式)
		public void writeRelPatternSeed(){
			WriteContent wr=new WriteContent();
			StringBuffer sb=new StringBuffer();
			Set<String> relPatterns=relPatternSeed.keySet();
			for (String relPattern : relPatterns) {
				sb.append(relPattern+":\n");
				List<String> patterns=relPatternSeed.get(relPattern);
				for (String pattern : patterns) {
					sb.append(pattern+"\n");
				}
			}
			wr.writeCon(sb.toString(), "./data/seeds/addRelationPattern.txt");
		}
	private List<String> readRalationPattern(String path){

		List<String> list=new ArrayList<>();
		try {
			String pattern=ReadFiles.readRawData(path);
			String[] arr=pattern.split("\n");
			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}

