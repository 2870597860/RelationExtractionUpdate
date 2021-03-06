package com.ner.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import cn.ner.readwrite.WriteContent;

public class KNNClasMain {
	//将每个公司的所有实体对应的分类结果存储起来
	private HashMap<String, HashMap<String, String>> classifacResults=new HashMap<>();
	
	public HashMap<String, HashMap<String, String>> getClassifacResults() {
		return classifacResults;
	}

	public void testVectorDatas(){
		//训练数据路径
		String datafilePath = new File("").getAbsolutePath() + File.separator + "seeds"+File.separator+"seeddatafile";
		//获取训练数据
		List<List<Double>> trainDatas = new ArrayList<List<Double>>();
		readDatas(trainDatas, datafilePath); 
		//测试数据路径
		String datafiletestPath = new File("").getAbsolutePath() + File.separator + "testcorpus"+File.separator+"datafiletest";
		HashMap<String, List<String>> testDatas = new HashMap<>();
		readDatas(testDatas, datafiletestPath);
		Set<String> copmpanys=testDatas.keySet();
		WriteContent wr=new WriteContent();	
		StringBuffer parseResult=new StringBuffer();
		
		KNN knn = new KNN();
		for (String company : copmpanys) {
			List<String> vectors=testDatas.get(company);
			
			/*HashMap<List<Double>,String> testMapVector=new HashMap<>();
			List<List<Double>> testDatasList = new ArrayList<List<Double>>();*/
			HashMap<String, String> entityResults=new HashMap<>();
			
			for (String entityVector : vectors) {
				String[] arr=entityVector.split("~");
				String[] vector=arr[1].split(" ");
				List<Double> test=new ArrayList<Double>();
				for (int i = 0; i < vector.length; i++) {
					test.add(Double.parseDouble(vector[i]));
				}
				/*if (arr[0].equals("长春佛吉亚排气系统有限公司")) {
					System.out.println("=================================");
				}*/
				/*testMapVector.put( test,arr[0]);  
				testDatasList.add(test);//获取测试数据
*/				
				System.out.print("测试元组 ("+arr[0]+"):"); //arr[0]为实体
				while (test.size()<8) {
					test.add(0.0);
				}
				for (int j = 0; j < test.size(); j++) {  
					System.out.print(test.get(j) + " ");  
				}  
				System.out.print("类别为: ");  
				int leiBie=Math.round(Float.parseFloat((knn.knn(trainDatas, test, 1))));
				addSeedVector(trainDatas, test, leiBie);
				String re=leiBie(leiBie);
				System.out.println( re);
				entityResults.put(arr[0], re);
				parseResult.append("测试元组 ("+arr[0]+"):"+"类别为: "+re+"\n");
				//relaAtr
			}
			classifacResults.put(company, entityResults);			
			wr.writeCon(company+":\n"+parseResult.toString(), "./seedPattern/parseResult");
			parseResult.setLength(0);
			System.out.println("====================================================");
		}
	}
	//将合格的特征向量加入种子
	public void addSeedVector(List<List<Double>> trainDatas,List<Double> test,int leiBie){
		test.add((double)leiBie);
		
		boolean contain=true;
		for (List<Double> list : trainDatas) {
			if (CollectionUtils.isEqualCollection(list, test)) {
				contain=false;
				break;
			}
		}
		if (contain) {
			trainDatas.add(test);
		}
	}
	public String leiBie(int al){
		String lei="无";
		switch (al) {
		case 0:
			lei="客户关系";
			break;
		case 1:
			lei="供应商关系";
			break;
		case 2:
			lei="研发关系";
			break;
		case 3:
			lei="附属关系";
			break;

		default:
			break;
		}
		return lei;
	}
	private void readDatas(List<List<Double>> datas, String path){
		try {  
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));  
			String data = br.readLine();  
			List<Double> l = null;  
			while (data != null) {  
				String t[] = data.split(" ");  
				l = new ArrayList<Double>();  
				for (int i = 0; i < t.length; i++) {  
					l.add(Double.parseDouble(t[i]));  
				}  
				datas.add(l);  
				data = br.readLine();  
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
	private void readDatas(HashMap<String, List<String>> datas, String path){
		try {  
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));  
			String data = br.readLine();
			String company="";
			List<String> list=null;
			while (data!=null) {
				if (data.contains("(:)")) {
					if (list!=null && list.size()>0) {
						datas.put(company, list);
					}
					list=new ArrayList<>();
					company=data.split("(:)")[0];
				}else {
					list.add(data);
				}
				data = br.readLine();
			}
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
}
