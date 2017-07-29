package com.ner.relationpattern;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;


import cn.ner.readwrite.ReadFiles;
import cn.ner.readwrite.WriteContent;


/**
 * 对实体所在的句子进行依存句法分析
 * 获取分析结果以xml形式返回
 * @author Administrator
 *
 */
public class DepParsingMain {
	public  TreeSet<String> failSentence=new TreeSet<>();
	public WriteContent wr=new WriteContent();
	public HashMap<String, String> sentenceDp(){
		List<String> fileLists=null;
		String company="";
		HashMap<String, String> sentenceResults=new HashMap<>();
		StringBuffer sbEntitySentences=new StringBuffer();
		try {
			//返回文件的绝对路径
			fileLists = ReadFiles.readDirs("DPCache");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (fileLists.size()>0) {
			for (String file : fileLists){
				System.out.println(file);
				int len=file.indexOf("\\DPCache\\");
				company=file.substring(len+"\\DPCache\\".length(),
						file.indexOf(".txt"));
				System.out.println("\n========="+company+":");
				File f=new File(file);
				String sentences;
				try {
					sentences = ReadFiles.simpaleReadFiles(file);
					if (sentences.length()<=0) {
						f.delete();
						continue;
					}
					sbEntitySentences.append(sentences);
				}  catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sentenceResults.put(company, sbEntitySentences.toString());
				sbEntitySentences.setLength(0);
				
				//依存分析
				startDP(sentenceResults);
				sentenceResults.clear();
				//f.delete();
			}
		}
		return sentenceResults;
	}
	public void startDP(HashMap<String,String> sentenceResults){
		Set<String> sentenceCompanys=sentenceResults.keySet();
		for (String company : sentenceCompanys) {
			String sentences=sentenceResults.get(company);
			String sentenceDPXml=sentenceDepParsing(company, sentences);
			//将依存分析的xml结果写入文件
			String pathXML="./xml/"+company+".xml";
			wr.writeCon(sentenceDPXml, pathXML);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String sentenceDepParsing(String company,String entitySentence){
		StringBuffer sb=null;
		try {
			sb=LTPAPI_dp.sentenceDP(entitySentence);
			//System.out.println("处理的公司："+company);
			//System.out.println("xml:"+sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			failSentence.add(company);
		}
		if (sb!=null) {
			return sb.toString();
		}
		return "";
	}
	//test
	public static void main(String[] args) {
		DepParsingMain dep=new DepParsingMain();
		HashMap<String, String> sentenceResults=dep.sentenceDp();
		//dep.startDP(sentenceResults);
		if (dep.failSentence.size()>0) {
			System.out.println("未处理的公司：");
			for (String sentence : dep.failSentence) {
				System.out.println(sentence);
			}
		}
	}
}
