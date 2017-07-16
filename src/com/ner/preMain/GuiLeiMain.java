package com.ner.preMain;

import com.ner.textpreprocess.GuiLeiZhengLi;
/*
 * 将获取得到的实体，实体类型，以及句子以公司为单位进行划分整理；写到companys文件夹中
 */
public class GuiLeiMain {
	public static void main(String[] args) {
		String entityPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\entity";
		String textPath="E:\\SES和企业信息\\股票期刊论文\\词频统计和分析\\report\\testdoing\\";
		GuiLeiZhengLi sc=new GuiLeiZhengLi();
		sc.getEntitySentenceMap(entityPath, textPath);;
		System.out.println("归类结束");
	}
}
