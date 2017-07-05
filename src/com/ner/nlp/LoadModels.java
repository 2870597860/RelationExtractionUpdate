package com.ner.nlp;

import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.NERTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.nlp.corpus.StopWords;

public class LoadModels {
	private static class LoadModelsHolder {
		  private static final LoadModels INSTANCE = new LoadModels();  
	}
	private LoadModels(){}
	public static final LoadModels getInstance(){
		return LoadModelsHolder.INSTANCE;
	}
	public StopWords sw=null;
	public CWSTagger cwst=null;
	public POSTagger posTag=null;
	public NERTagger nerTag=null;
	public AbstractExtractor aeKey=null;
	public void segModelInit(){
		try {
			if (null==cwst) {
				sw= new StopWords("models/stopwords");//使用词库停用词等
				cwst = new CWSTagger("./models/seg.m",new Dictionary("./models/dict_ambiguity.txt",true));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void keyWordsModel(){
		if (null==cwst) {
			segModelInit();
		}
		if (aeKey==null) {
			aeKey =new WordExtract(cwst,sw);
		}		
	}
	public void posModelInitWithSegModel(){
		try {
			if (posTag==null) {
				if (cwst==null) {
					segModelInit();
				}
				posTag = new POSTagger(cwst,"models/pos.m");
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void nerModelInitWithPosModel(){
		try {
			if (nerTag==null) {
				if (posTag==null) {
					posModelInitWithSegModel();
				}
				nerTag = new NERTagger(posTag);
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
