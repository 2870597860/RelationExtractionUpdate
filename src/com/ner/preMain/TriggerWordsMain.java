package com.ner.preMain;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ner.nlp.WordSegmentation;
import com.ner.textpreprocess.ObjectAndDataCollection;
import com.ner.textpreprocess.OnlySentencesList;
import com.ner.textpreprocess.WordsFilter;
import com.ner.tfidf.TFIDF;

import cn.ner.readwrite.WriteContent;
import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.nlp.corpus.StopWords;

public class TriggerWordsMain {
	public void getTriggerWord(List<String> sentences){
		//对句子进行分词，去除停用词处理；仅仅获取动词，名词，动名词
		List<String> candidateWords=ChineseWordSegmentation(sentences);
		//对分词结果进行过滤，去掉数字以及一个字的词
		WordsFilter wf=new WordsFilter();
		wf.wordsFilter(candidateWords);
		Map<String, Double> wordsIDF=TFIDF.getTFIDF(candidateWords,sentences);
		TFIDF.SortMap(wordsIDF);
		ArrayList<Map.Entry<String,Double>> lists=TFIDF.list;
		System.out.println("触发词====================");
		WriteContent wc=new WriteContent();
		OutputStreamWriter osw1=wc.writeConAppend("./triggetwordsDemo.txt");
		OutputStreamWriter osw2=wc.writeConAppend("./triggetwords.txt");
		for (Map.Entry<String, Double> entry : lists) {
			System.out.println(entry.getKey()+":"+entry.getValue() );
			try {
				if (entry.getValue()>110) {
					osw2.write(entry.getKey()+"\n");
				}
				osw1.write(entry.getKey()+":"+entry.getValue()+"\n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			osw1.close();
			osw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public List<String>  ChineseWordSegmentation(List<String> sentences){
		List<String> candidateTriggers=new ArrayList<>();
		WordSegmentation ws=WordSegmentation.getInstance();
		System.out.println("开始分词。。。");
		int count=0;
		for (String sentence : sentences) {
			count++;
			System.out.println("第"+count+"个句子");
			String s=ws.SegWord(sentence, true);
			String[] words=s.split("\\s+");
			for (int i = 0; i < words.length; i++) {
				String w=words[i];
				if (w.contains("名词") || w.contains("动词") || w.contains("动名词")) {
					String wordStr=w.split("/")[0].replaceAll("\t|\r|\n|\r\n", "");
					if (wordStr.length()>1) {
						if (wordStr.getBytes().length!=wordStr.length()) {
							candidateTriggers.add(wordStr);
						}
					}
					
					//System.out.println(w.split("/")[0]);
				}
			}
		}
		System.out.println("分词结束。。。，成功获取候选触发词！");
		return candidateTriggers;
	}	
	public static void main(String[] args) {
		/*
		 * 整理句子集合
		 */
		OnlySentencesList sl=new OnlySentencesList();
		ObjectAndDataCollection.getEntitysTextsMapObject();
		List<String> sentencesLists=sl.getSentencesList(ObjectAndDataCollection.entitySentenceMap);
		System.out.println("触发词所需句子集合获取完毕！");
		//开始对句子集合进行处理
		System.out.println("开始对句子集合进行处理。。。");
		TriggerWordsMain tw=new TriggerWordsMain();
		tw.getTriggerWord(sentencesLists);

	}
}
