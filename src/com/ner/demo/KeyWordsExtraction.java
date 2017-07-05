package com.ner.demo;

import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import edu.fudan.util.exception.LoadModelException;

public class KeyWordsExtraction {
	public static void main(String[] args) {
		StopWords sw= new StopWords("models/stopwords");
		CWSTagger seg=null;
		try {
			seg = new CWSTagger("models/seg.m");
		} catch (LoadModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AbstractExtractor key = new WordExtract(seg,sw);
		
		System.out.println(key.extract("徽商银行为本公司的参股公司,全资子公司,前五名", 20, true));
		
		//处理已经分好词的句子
		sw=null;
		key = new WordExtract(seg,sw);
		System.out.println(key.extract("徽商银行为本公司的参股公司,全资子公司,前五名", 20));
		System.out.println(key.extract("赵嘉亿 是 好人 还是 坏人", 5));
		
		key = new WordExtract();
		System.out.println(key.extract("", 5));
	}
}
