package com.ner.relationpattern;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ner.textpreprocess.ObjectAndDataCollection;

import cn.ner.readwrite.ReadFiles;


public class GetEnType {
	TreeSet<String> allCompany=new TreeSet<>();
	HashMap<String, TreeMap<String,String>> companys_entitys_types=ObjectAndDataCollection.companys_entitys_types;	

	public  GetEnType(String path){
		getAllCompany(path);
	}
	public  TreeMap<String,String> startRead(String company){
		Set<String> companys=companys_entitys_types.keySet();
		for (String com : companys) {
			if (company.equals(com)) {
				return companys_entitys_types.get(com);
			}
		}
		return null;
	}
	public  void getAllCompany(String xmlPath){
		//String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		try {
			List<String> fileLists=ReadFiles.readDirs(xmlPath);//返回文件的绝对路径
			for (String file : fileLists) {
				String Company=null;
				
				int preCom=file.indexOf("\\xml\\");
				int laterCom=file.indexOf(".xml");
				//System.out.println("\\xml\\:"+preCom+",.xml"+laterCom);
				Company=file.substring(preCom+"\\xml\\".length(), laterCom);
				allCompany.add(Company);
				//System.out.println(treeSet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
