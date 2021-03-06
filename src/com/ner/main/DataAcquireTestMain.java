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
import com.ner.relationpattern.ParseXmlResult;
import com.ner.textpreprocess.ObjectAndDataCollection;
import com.ner.textpreprocess.OnlySentencesList;

import cn.ner.readwrite.ReadFiles;
import cn.ner.readwrite.WriteContent;
/**
 * 对1000篇文章进行分析
 * @author xiaodai
 *
 */
public class DataAcquireTestMain {
	static DepParsingMain dpm=new DepParsingMain();
	public static void main(String[] args) {
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testEntity";
		String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testFiledoing\\";
		ObjectAndDataCollection.getEntitysTextsMapObject( entityPath, textPath);
		for (String string : args) {
			if (string.equals("biaoge")) {
				biaoGe();
			}
			if (string.equals("text")) {
				Text();
			}
		}
	}
	public static void biaoGe(){
		FeatureVector fv=FeatureVector.getInstance();
		List<String> fileLists=null;
		WriteContent wc=new WriteContent();
		OutputStreamWriter osw=wc.writeConAppend("./data/testcorpus/datafiletest");
		try {
			int count=0;
			fileLists = ReadFiles.readDirs("data/testcorpus/testcompanys/");
			Set<String> trainvector=new HashSet<>();
			for (String file : fileLists) {
				String str=ReadFiles.readRawData(file);
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
						osw.write(bgArrArr[0]+"~"+cacheVector.toString()+"\n");
						osw.flush();
					}					
				}
			}
			StringBuilder sbb=new StringBuilder();
			for (String string : trainvector) {
				sbb.append(string+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void Text(){
		WriteContent wc=new WriteContent();
		List<String> fileLists=null;
		OutputStreamWriter oswTxt=wc.writeConAppend("./data/testcorpus/RelationPatternAll");
		try {
			fileLists = ReadFiles.readDirs("data/testcorpus/testcompanys/");
			int count=0;
			for (String file : fileLists) {
				String str=ReadFiles.readRawData(file);
				String company=file.substring(file.lastIndexOf("\\")+"\\".length());
				StringBuilder sbuilderText=new  StringBuilder();
				String text=str.substring(str.indexOf("text->")+"text->\n".length());
				String[] sentenceArr=text.split("\n");

				for (int i = 0; i < sentenceArr.length; i++) {
					String[] senArr=sentenceArr[i].split(" : ");//数组中分别是实体、类型、句子
					if (senArr.length>2) {
						System.out.println("第"+(count++));
						sbuilderText.append(senArr[2]);
					}
				}
				if (company.equals("深圳市得润电子股份有限公司")) {
					System.out.println(company+"==================");
				}
				if (sbuilderText.length()>0) {
					String xml=dpm.sentenceDepParsing(company, sbuilderText.toString());
					if (xml.length()>0) {
						wc.writeCon(xml, "./data/xml/"+company+".xml");
					}
				}
			}

			if (dpm.failSentence.size()>0) {
				System.out.println("未处理的公司：");
				for (String sentence : dpm.failSentence) {
					System.out.println(sentence);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//分析句子依存分析xml文件，获取关系模式
		String xmlPath="data/xml";
		HashMap<String, List<String>> xmlParseResults=new ParseXmlResult().getTextRelationPattern(xmlPath);
		Set<String> set=xmlParseResults.keySet();
		StringBuilder sbRP=new StringBuilder();
		for (String company : set) {
			try {
				oswTxt.write(company+"(:)\n");
				List<String> value=xmlParseResults.get(company);
				for (String rp : value) {
					sbRP.append("<"+company+";"+rp+">\n");
				}
				oswTxt.write(sbRP.toString());
				oswTxt.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
