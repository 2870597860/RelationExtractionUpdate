package com.ner.relationpattern;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;  
import java.net.URL;  
import java.net.URLEncoder;

  
public class LTPAPI_dp {
	public static StringBuffer sentenceDP(String text)throws IOException{  
		String api_key = "z8z4W3E6zCgisEkzqf7dAdgYleZrSPbWiVFGXMUy";//api_key,申请账号后生成，这个账户每月有19G流量  
		String pattern = "dp";//ws表示只分词，除此还有pos词性标注、ner命名实体识别、dp依存句法分词、srl语义角色标注、all全部  
		String format  = "xml";//指定结果格式类型，plain表示简洁文本格式  
		String result = "";  
		
		text = URLEncoder.encode(text, "utf-8");  
		URL url = new URL("http://api.ltp-cloud.com/analysis/?"  
				+ "api_key=" + api_key + "&"  
				+ "text="    + text    + "&"  
				+ "format="  + format  + "&"  
				+ "pattern=" + pattern);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.connect();  
		InputStreamReader isr=new InputStreamReader(conn.getInputStream(), "utf-8");
		BufferedReader innet = new BufferedReader(isr);
		StringBuffer sb=new StringBuffer();
		String line;  
		while ((line = innet.readLine())!=null){
			sb.append(line+"\n");
			//System.out.println(line);  
		}  
		//System.out.println("xml:"+sb.toString());
		innet.close();
		return sb;  
	} 
	//语义角色标注
	public static StringBuffer sentenceDPsrl(String text)throws IOException{  
		String api_key = "z8z4W3E6zCgisEkzqf7dAdgYleZrSPbWiVFGXMUy";//api_key,申请账号后生成，这个账户每月有19G流量  
		String pattern = "srl";//ws表示只分词，除此还有pos词性标注、ner命名实体识别、dp依存句法分词、srl语义角色标注、all全部  
		String format  = "xml";//指定结果格式类型，plain表示简洁文本格式  
		String result = "";  
		
		text = URLEncoder.encode(text, "utf-8");  
		URL url = new URL("http://api.ltp-cloud.com/analysis/?"  
				+ "api_key=" + api_key + "&"  
				+ "text="    + text    + "&"  
				+ "format="  + format  + "&"  
				+ "pattern=" + pattern);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.connect();  
		InputStreamReader isr=new InputStreamReader(conn.getInputStream(), "utf-8");
		BufferedReader innet = new BufferedReader(isr);
		StringBuffer sb=new StringBuffer();
		String line;  
		while ((line = innet.readLine())!=null){
			sb.append(line+"\n");
			//System.out.println(line);  
		}  
		System.out.println("xml:"+sb.toString());
		innet.close();
		return sb;  
	} 
	//test
	public static void main(String[] args) {
		String text = "重庆长安有限公司。Meta System S.p.A.，公司注册资本为1800万欧元，报告期内公司完成收购并间接持有其60%的股权，深圳华麟电路技术有限公司，注册资本6500万元，报告期公司直接间接持有其100%股权，纳入合并报表范围。深圳得润精密零组件有限公司，注册资本为2000万元，报告期公司直接持有其100%股权，纳入合并报表范围。科世得润在长春设立分公司并新建工厂，故增加投资972.92万元。科世得润汽车部件有限公司，公司注册资本为1900万欧元，报告期公司直接持有其45%的股权，不纳入合并报表范围。科世得润获得一汽大众新的订单，为更好的为客户服务，科世得润在长春设立分公司并新建工厂，故增加投资972.92万元。绵阳虹润电子有限公司，注册资本为2000万元，公司直接持有其100%的股份，纳入合并报表范围。青岛得润电子有限公司，注册资本1000万元，公司直接持有其100%的股份，纳入合并报表范围。青岛海润电子有限公司，注册资本为1000万元，公司直接间接持有其100%的股份，纳入合并报表范围。";
		try {
			//sentenceDP(text);
			StringBuffer sbb=sentenceDP(text);
			System.out.println(sbb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(text+"分析失败");
		}
	}
}
