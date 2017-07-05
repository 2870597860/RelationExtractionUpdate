package com.ner.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Test {
	int x=3;
	public static void main(String[] args) {
		/*System.out.println("公司主要销售客户情况".length());
		String text="主要子公司及对公司净利润影响达 10%以上的参股公司情况";
		int index1=text.indexOf("情况");
		int index2=text.lastIndexOf("公司",index1);
		int index3=text.lastIndexOf("公司", index2-"公司".length());
		System.out.println(index2+" "+index3);
		String[] regExkg={"（^\\d+$）","^\\d+$、"};
		String st="（8）主要销售客户和主要供应商情况";
		int ss=st.indexOf(regExkg[0]);
		System.out.println(ss);
		
		int flag=1;
		int com=text.indexOf("公司");		
		while(flag>0){
			System.out.println("d");
			com=text.indexOf("公司", com+"公司".length());
			flag=com;
			System.out.println(flag);
		}
		String str="河北北汽福田汽车部件有限公司company_name";
		StringBuilder sb=new StringBuilder();
		if(str.contains("company_name")){
			sb.append(str);
			sb.insert(sb.indexOf("company_name"), "、");
		}
		System.out.println(sb);*/
		HashMap<String, String> map=new HashMap<>();
		TreeMap<String , String> tree=new TreeMap<>();
		tree.put("ss", "sa");
		map.put("", "");
		System.out.println(tree.get("sa"));
		System.out.println(map.get("sa"));
		String str = "java怎么把字符串中的的汉字取出来";
		String reg = "[^\u4e00-\u9fa5]";
		str = str.replaceAll(reg, "");
		System.out.println(str);
		
	}

}
