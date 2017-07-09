package com.ner.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;


public class Test {
	private static class TestHolder{
		private static final Test test=new Test();
	}
	private Test(){}
	public static Test getInst(){
		return TestHolder.test;
	}
	final ListDemo ld=new ListDemo();
	List<String> list=ld.getList();
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
		String str = "java怎么把，，,,字符串fdfdf中的的汉字取出来";
		String reg = "[^\u4e00-\u9fa5]";
		str = str.replaceAll(reg, "");
		System.out.println(str);
		List<String> listL=new ArrayList<>();
		listL.add("1");
		listL.add("4");
		listL.add("3");
		listL.add("2");
		listL.add("8");
		System.out.println("tpString:"+listL.toString());
		for (int i = 0; i < listL.size(); i++) {
			System.out.println(listL.get(i));
		}
		String wordStr="fdsfd";
		boolean h=wordStr.getBytes().length==wordStr.length();
		System.out.println(h); 
		System.out.println(wordStr.getBytes().length);
		System.out.println(wordStr.length());
		Double d=230.98;
		System.out.println( d/2);
		HashMap<String, Integer> mmap=new HashMap<>();
		mmap.put("qe", 3);
		mmap.put("we", 4);
		mmap.put("hg", 2);
		mmap.put("wf", 5);
		Collection co=mmap.values();
		Iterator<Integer> it=co.iterator();
		while(it.hasNext()){
			
			System.out.println( Math.pow(it.next(), 2));
		}
		Set<String> set=mmap.keySet();
		for (String string : set) {
			System.out.println(string);
			System.out.println(mmap.get(string));
		}
		Properties po=new Properties();
		try {
			po.load(new FileInputStream("test.properties"));
			Enumeration en = po.propertyNames(); //得到配置文件的名字
			while(en.hasMoreElements()){
				 String strKey = (String) en.nextElement();
				 String strValue = po.getProperty(strKey);
				 System.out.println(strKey + "=" + strValue);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			OutputStream out = new FileOutputStream("test.properties");
			po.setProperty("gh", "aaaaa");
			try {
				po.store(out, "Update");
				
				po.setProperty("dsd", "ssssss");
				po.store(out, "Update");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			InputStreamReader isr=new InputStreamReader(new FileInputStream("./安徽安凯汽车股份有限公司"));
			BufferedReader br=new BufferedReader(isr);
			StringBuilder sb=new StringBuilder();
			String line =null;
			try {
				while ((line=br.readLine()) != null) {
					sb.append(line+"\n");
				}
				br.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(sb);
			System.out.println("=====");
			System.out.println(sb.substring(sb.indexOf("biaoge->")+"biaoge->\n".length(),sb.indexOf("text->")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	} 

}
