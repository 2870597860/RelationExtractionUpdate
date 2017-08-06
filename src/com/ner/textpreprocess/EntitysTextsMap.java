package com.ner.textpreprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


import cn.ner.readwrite.ReadFiles;

public class EntitysTextsMap {
	HashMap<String, TreeMap<String,String>> companys_entitys_types=null;
	HashMap<String, String> companys_texts=null;
	
	public void getCompanys_entitys_types(String entityPath,String textPath){
		//String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		if (companys_entitys_types==null) {
			companys_entitys_types=new HashMap<>();
		}
		if (companys_texts==null) {
			companys_texts=new HashMap<>();
		}
		try {
			List<String> fileLists=ReadFiles.readDirs(entityPath);//返回文件的绝对路径
			int count=0;
			for (String file : fileLists) {
				String textPath2=null;
				count++;
				System.out.println("第"+count+"篇");
				String companyName=null;
				int preCom=file.indexOf("entity_");
				int laterCom=file.indexOf(".txt");
				companyName=file.substring(preCom+"entity_".length(), laterCom);
				TreeMap<String,String> entitys_types=getEntitys_types(file);
				companys_entitys_types.put(companyName, entitys_types);
				//获取文本
				textPath2=textPath+companyName+".txt";
				String text=readText(textPath2);
				companys_texts.put(companyName, text);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private TreeMap<String,String> getEntitys_types(String entityPath){
		String entityContent=readText(entityPath);	
		TreeSet<String> treeSetEntityArr=getFilter(entityContent);//只获取公司实体、产品实体、组织实体
		/*if (entityPath.contains("吉林华微电子股份有限公司")) {
			System.out.println("吉林华微电子股份有限公司::");
			for (String string : treeSetEntityArr) {
			System.out.println(string);
		}
		}*/
		
		TreeMap<String,String> Entity_type=new TreeMap<>();
		for (String string : treeSetEntityArr) {
			String[] entityArr=string.split("、");
			if ( entityArr[0].matches("[a-zA-Z]+") && entityArr[0].length()>5) {
				Entity_type.put(entityArr[0], entityArr[1]);
			}else if ( entityArr[0].matches("[\u4E00-\u9FA5]+") && entityArr[0].length()>=3) {
				Entity_type.put(entityArr[0], entityArr[1]);	
			}else if (entityArr[0].length()>5) {
				Entity_type.put(entityArr[0], entityArr[1]);
			}
		}
		return Entity_type;
	}
	public TreeSet<String> getFilter(String content){
		TreeSet<String> set=new TreeSet<>();
		StringBuilder sb=new StringBuilder();
		String[] entity=content.split("\r\n");
		for (String string : entity) {
			if (string.contains("product_name")||string.contains("company_name")||string.contains("org_name")) {
				if (!string.contains(".")) {
					fenGe(sb,string);					
				}else if((string.length()>16)){
					fenGe(sb,string);
				}
				set.add(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		return set;
	}
	public void fenGe(StringBuilder sb,String string){
		String str=string.replaceAll("\\d+", "");//去掉字符串子所有的数字
		if(str.contains("product_name")){
			sb.append(str);
			sb.insert(sb.indexOf("product_name"), "、");
		}
		if(str.contains("company_name")){
			sb.append(str);
			sb.insert(sb.indexOf("company_name"), "、");
		}
		if (str.contains("org_name")) {
			sb.append(str);
			sb.insert(sb.indexOf("org_name"), "、");
		}
	}
	public  String readText(String filePath){		
		String content="";
		StringBuffer sb = new StringBuffer();
		File file=new File(filePath);
		try {
			InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"utf-8");
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line+"\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
