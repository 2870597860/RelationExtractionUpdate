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
 * 获取种子（其中100篇）
 * @author xiaodai
 *
 */
public class DataAcquireSeedMain {
	static DepParsingMain dpm=new DepParsingMain();
	static WriteContent wc=new WriteContent();
	public static void main(String[] args) {
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\";
		ObjectAndDataCollection.getEntitysTextsMapObject( entityPath, textPath);
		for (String string : args) {
			if (string.equals("biaoge")) {
				biaoGe();
			}
			if (string.equals("dp")) { 
				Text();
			}
			if (string.equals("re")) { 
				relationPattern();
			}
		}
	}
	public static void biaoGe(){
		FeatureVector fv=FeatureVector.getInstance();
		List<String> fileLists=null;

		OutputStreamWriter oswtrain=wc.writeConAppend("./data/seeds/seeddatafile");//表格特征
		OutputStreamWriter osw=wc.writeConAppend("./data/seeds/datafileAll");
		try {
			int count=0;
			fileLists = ReadFiles.readDirs("data/traincorpus/companys/");
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
						//osw.write("==="+"\n");	
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
	public static void Text(){
		List<String> fileLists=null;
		try {
			//fileLists = ReadFiles.readDirs("data/traincorpus/companys/");
			fileLists = ReadFiles.readDirs("data/traincorpus/companyundo/");
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
						sbuilderText.append(senArr[2].replace(",", ""));
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
	}
	public static void relationPattern(){
		OutputStreamWriter  oswtrainTxt=wc.writeConAppend("./data/seeds/seedRelationPattern");//文本句子特征
		OutputStreamWriter oswTxt=wc.writeConAppend("./data/seeds/RelationPatternAll");
		//分析句子依存分析xml文件，获取关系模式
		String xmlPath="data/xml";
		HashMap<String, List<String>> xmlParseResults=new ParseXmlResult().getTextRelationPattern(xmlPath);
		HashMap<String, TreeMap<String,String>> companys_entitys_types=ObjectAndDataCollection.companys_entitys_types;
		Set<String> set=xmlParseResults.keySet();
		StringBuilder sbRP=new StringBuilder();
		for (String company : set) {
			try {
				TreeMap<String,String> entitys_types=companys_entitys_types.get(company);
				StringBuilder sp=new StringBuilder();
				oswTxt.write(company+"(:)\n");
				List<String> value=xmlParseResults.get(company);
				for (String rp : value) {
					if (entitys_types.containsKey(rp.split(":")[0])) {
						sbRP.append("<"+company+";"+rp+">\n");
						sp.append("<"+company+";"+rp+">\n");
					}
				}
				oswTxt.write(sp.toString());
				oswTxt.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			oswtrainTxt.write(sbRP.toString());
			oswtrainTxt.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
