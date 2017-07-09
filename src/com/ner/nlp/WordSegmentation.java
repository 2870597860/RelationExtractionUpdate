package com.ner.nlp;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.nlp.corpus.StopWords;

public class WordSegmentation {
	/**
	 * 
	 * @param sentence 要分词的句子
	 * @param flag 是否带词性
	 * @return
	 */
	private static WordSegmentation ws=new WordSegmentation();
	public LoadModels lm=null;
/*	StopWords sw=null;
	CWSTagger cwst=null;
	POSTagger tag=null;*/
	private WordSegmentation(){
		lm=LoadModels.getInstance();
	}	
	public static WordSegmentation getInstance(){
		return ws;
	}
/*	private void modelInit(){
		try {
			sw= new StopWords("models/stopwords");//使用词库停用词等
			cwst = new CWSTagger("./models/seg.m",new Dictionary("./models/dict_ambiguity.txt",true));
			tag = new POSTagger(cwst,"models/pos.m");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("初始模型加载失败");
		}
	}*/
	/**
	 * 
	 * @param sentence 要分词的句子
	 * @param flag 是否进行词性标注
	 * @return
	 */
	public String SegWord(String sentence,boolean flag){
		String s=null;
		if (flag) {
			//tag.SetTagType("en");//设置标注类型是是中文标注还是英文标注
			if (null==lm.posTag) {
				lm.posModelInitWithSegModel();//加载词性模型
			}
			s=lm.posTag.tag(sentence);
		}else {
			if (null==lm.cwst) {
				lm.segModelInit();//加载分词模型				
			}
			s=lm.cwst.tag(sentence);
		} 
		return s;
	}
	public static void main(String[] args) {
		WordSegmentation ws=WordSegmentation.getInstance();
		String sentence="徽商银行为本公司的参股公司,全资子公司,前五名";
		String s=ws.SegWord(sentence, true);
		System.out.println(s);
	}
}
