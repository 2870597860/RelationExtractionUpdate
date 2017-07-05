package com.ner.textpreprocess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordsFilter {
	public void wordsFilter(List<String> wordslist){
		Iterator<String> it=wordslist.iterator();
		while (it.hasNext()) {
			String word = it.next();
			if (word.matches("[a-zA-Z]+") || word.length()<=1 || word.contains("-")) {
				it.remove();
			}
		}
	}
	public static void main(String[] args) {
		WordsFilter wf=new WordsFilter();
		List<String> list=new ArrayList<>();
		list.add("结构助词 参股公司");
		list.add("结");
		list.add("eweqw");
		wf.wordsFilter(list);
		System.out.println(list);
	}
}
