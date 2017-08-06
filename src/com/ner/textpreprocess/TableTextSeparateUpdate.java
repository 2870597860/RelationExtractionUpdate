package com.ner.textpreprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  获取每个公司年报中实体所在的句子以"biaoge"和"text"分类，值为ent+"||"+type+"||"+sectionSentence
 * @author Administrator
 */
public class TableTextSeparateUpdate {
	private boolean flag=true;//表格标记
	HashMap<String, TreeMap<String,String>> companys_entitys_types=null;
	HashMap<String, String> companys_texts=null;
	//表格和文本句子分别作为key
	HashMap<String, HashMap<String, List<String>>> entitySentenceMap=null;
	/**
	 * 获取每个公司年报中实体所在的句子以"biaoge"和"text"分类，值为ent+"||"+type+"||"+sectionSentence
	 * @param entityMap HashMap的键是公司年报名称，key是以“、”隔开的实体名和实体类别的字符串
	 */
	//获取每个实体所对应的句子或者段落
	public void getEntitySentence(){
		/*
		 * 将每一个公司，表格中的实体和句子中的实体分别存储HashMap<String, List<String>分别为
		 * biaoge和文本的句子
		 */
		if (entitySentenceMap==null) {
			entitySentenceMap=new HashMap<>();
		}else {
			entitySentenceMap.clear();
		}		
		Set<String> companys=companys_texts.keySet();

		for (String company : companys) {
			String[] filter={company+"2015年年度报告全文",company+" 2015 年年度报告全文","2015 年年度报告全文"};
			HashMap<String, List<String>> sentence=new HashMap<>();
			TreeMap<String,String> entityAndTypes=companys_entitys_types.get(company);
			List<String> biaogeSentenceList=new ArrayList<>();//表格中实体所在句子的集合
			List<String> textSentenceList=new ArrayList<>();//文本中实体所在句子的集合
			Set<String> entitys=entityAndTypes.keySet();
			String companyText=companys_texts.get(company); 
			int indexTen=companyText.indexOf("十、");//过滤最后的调研部分
			if (indexTen>0) {
				companyText=companyText.substring(0, indexTen);
			}
			System.out.println(company);
			for (String entity : entitys) {
				if (entity.equals(company)) {
					continue;
				}
				String entitySentence=null;

				int indexEntity=companyText.indexOf(entity);//目的是找出实体所在的段落，然后计算相关特征
				String cacheText="";
				//while (indexEntity>0) {
				entitySentence=findSentences(companyText,entity);//得到实体所在的句子
				if (entitySentence!=null ) {
					for (int i = 0; i < filter.length; i++) {
						entitySentence=entitySentence.replace(filter[i], "");
					}
					if (flag) {
						biaogeSentenceList.add(entity+"~"+entitySentence);
					}else {
						int xiaoqi=entitySentence.indexOf(entity);
						if (xiaoqi>0) {
							int xiaoqi1=entitySentence.indexOf("该公司", xiaoqi+entity.length());
							if (xiaoqi1>0 && xiaoqi1-xiaoqi>2) {
								entitySentence=entitySentence.replace("该公司", "");
							}
						}
						entitySentence=entitySentence.replace(",", "").replace(" ", "");
						textSentenceList.add(entity+"~"+entitySentence);
					}
				}
				indexEntity=companyText.indexOf(entity,indexEntity+entity.length());
			}
			//}
			sentence.put("biaoge", biaogeSentenceList);
			entitySentenceMap.put(company, sentence);
			sentence.put("text", textSentenceList);

		}
	}
	/**
	 * 
	 * @param text 公司年报文章
	 * @param entityName 实体名
	 * @return 返回是表格的句子还是文本的句子（true是表格，false是文本）
	 */
	private String findSentences(String companyText, String entityName ){
		String sectionSentence=null;
		int indexEntity=companyText.indexOf(entityName);
		if (entityName.equals("北京银达信融资担保有限责任公司")) {
			System.out.println("北京银达信融资担保有限责任公司");
		}
		if (indexEntity>-1 && indexEntity<companyText.indexOf("公司未来发展",indexEntity)) {
			String beforeSpace=companyText.substring(indexEntity+entityName.length(), indexEntity+entityName.length()+1).replaceAll("\t|\r|\n|\r\n", " ");
			String afterSpace=companyText.substring(indexEntity-1, indexEntity).replaceAll("\t|\r|\n|\r\n", " ");
			/*int firstHang=companyText.lastIndexOf("\n", indexEntity);
			String[] select=null;
			if (firstHang>0) {
				int secondHang=companyText.lastIndexOf("\n", firstHang-"\n".length());
				if (secondHang>0 ) {
					select=companyText.substring(secondHang,indexEntity).split(" ");
				}
			}	*/
			if(beforeSpace.equals(" ") && afterSpace.equals(" ")){
				flag=true;//是表格
			}else {
				flag=false;//说明不是表格是文本
			}
			if (flag) {//表格处理
				sectionSentence=tableSentence(companyText,entityName,indexEntity);//找出表格实体所在的段或句子
			}else {//对实体所在文本进行分析
				sectionSentence=findTextSetence(companyText,entityName,indexEntity);
				if (sectionSentence!=null && (!sectionSentence.contains(entityName))) {
					sectionSentence=null;
				}
				if (sectionSentence!=null && (sectionSentence.indexOf("报告期，")>0)) {
					sectionSentence=sectionSentence.substring(sectionSentence.indexOf("报告期，")+"报告期，".length());
				}
			}
		}
		if (sectionSentence!=null) {
			Pattern p = Pattern.compile("\t|\r|\n|\r\n");
			Matcher m = p.matcher(sectionSentence);
			sectionSentence = m.replaceAll("").replaceAll(" +"," ");//将多个空格替换为一个空格必须用replaceAll
		}
		return sectionSentence;
	}
	/**
	 * 
	 * @param text 公司年报文章
	 * @param entityName 实体名
	 * @return 返回是表格的句子还是文本的句子（true是表格，false是文本）
	 */
	private String tableSentence(String reportText,String entity,int indexEntity){
		int xu=reportText.lastIndexOf("序", indexEntity);
		int mc=reportText.lastIndexOf("名称", indexEntity);
		if(entity.equals("甲磺酸左氧氟")){
			System.out.println();
		}
		String sentence=null;
		if (xu>0) {
			sentence=findBiaogeSentence(xu, reportText, entity, indexEntity);
		}else if(mc>0){
			sentence=findBiaogeSentence(mc, reportText, entity, indexEntity);
		}
		if(sentence!=null){
			sentence=sentence.replace("□ 适用 √ 不适用", "");
		}
		return sentence;
	}
	/**
	 * 
	 * @param reportText 公司年报文章
	 * @param entity 实体名
	 * @return 实体在表格中的所在的句子
	 */
	//找出表格实体所在的段或句子(实体在表格中)
	private String findBiaogeSentence(int xmx,String reportText,String entity,int indexEntity){

		//String regEx = "['   ']+";//匹配文章中连续的多个空格
		//String[] regExkg={"（^\\d+$）","^\\d+$、"};
		//int xu=reportText.lastIndexOf("序", indexEntity);
		int danWei=reportText.lastIndexOf("单位：");
		int juhao=reportText.lastIndexOf("。", indexEntity);
		int bushiyong=reportText.lastIndexOf("不适用", indexEntity);
		int yanfa=reportText.lastIndexOf("研发投入", indexEntity);
		int companyMingCheng=reportText.lastIndexOf("公司名称", indexEntity);
		int companyQuanCheng=reportText.lastIndexOf("公司全称", indexEntity);
		int qiYeMingchen=reportText.lastIndexOf("企业名称", indexEntity);
		int mc=reportText.lastIndexOf("名称", indexEntity);
		int yaoPin=reportText.lastIndexOf("药品", indexEntity);
		int xiangMu=reportText.lastIndexOf("项目", indexEntity);
		int pinMing=reportText.lastIndexOf("品名", indexEntity);
		/*int companyName=reportText.lastIndexOf("公司名称", indexEntity);
		int companyName1=reportText.lastIndexOf("企业名称", indexEntity);*/
		String bufenSentence=null;

		if (entity.equals("奥铃发动机项目")) {
			System.out.println("奥铃发动机项目");
		}
		//获取对表格所描述的
		if (xmx>0) {
			if (bushiyong>0) {
				if (xmx>bushiyong) {
					bufenSentence=reportText.substring(bushiyong+"不适用".length(), xmx);
					if (juhao>bushiyong && xmx>(juhao+"。".length())) {
						bufenSentence=reportText.substring(juhao+"。".length(), xmx);
					}
					if (bufenSentence.getBytes().length==bufenSentence.length()||bufenSentence.split("\n").length>5) {
						if(bufenSentence.contains("客户情况")||bufenSentence.contains("供应商情况")){
							int ke=reportText.lastIndexOf("客户情况", indexEntity);
							int gong=reportText.lastIndexOf("供应商情况",indexEntity);
							if (gong>0 &&  ke>gong ) {
								bufenSentence=reportText.substring(ke-6, xmx);
							}
							if (ke>0 && gong>ke ) {
								bufenSentence=reportText.substring(gong-4, xmx);
							}
						}

					}
				}
				if (bufenSentence==null && yanfa>0) {
					int yanju=reportText.indexOf("。", yanfa);
					if (yanfa>bushiyong && xmx>yanfa) {
						bufenSentence=reportText.substring(yanfa,xmx);
						if (yanju>0 && yanju<xmx) {
							bufenSentence=reportText.substring(yanfa, yanju);
						}					
					}else if(mc>bushiyong &&  mc>yanfa){
						bufenSentence=reportText.substring(yanfa,mc);
						if (yanju>0 && yanju<mc) {
							bufenSentence=reportText.substring(yanfa, yanju);
						}					

					}else if(pinMing>bushiyong &&  pinMing>yanfa){
						bufenSentence=reportText.substring(yanfa,pinMing);
						if (yanju>0 && yanju<pinMing) {
							bufenSentence=reportText.substring(yanfa, yanju);
						}					

					}else if(yaoPin>bushiyong &&  yaoPin>yanfa){
						bufenSentence=reportText.substring(yanfa,yaoPin);
						if (yanju>0 && yanju<yaoPin) {
							bufenSentence=reportText.substring(yanfa, yanju);
						}											
					}else if(xiangMu>bushiyong &&  xiangMu>yanfa){
						bufenSentence=reportText.substring(yanfa,xiangMu);
						if (yanju>0 && yanju<xiangMu) {
							bufenSentence=reportText.substring(yanfa, yanju);
						}											
					}
					if (bufenSentence!=null) {
						if (bufenSentence.getBytes().length==bufenSentence.length()||bufenSentence.length()<6||bufenSentence.length()>100||bufenSentence.split("\n").length>5) {
							bufenSentence="研发投入,报告期公司已经进入注册程序的研发产品进展情况如下";//默认值
							if (reportText.indexOf("主要控股参股公司")>0 && reportText.indexOf("主要控股参股公司")<indexEntity) {

							}else {
								return bufenSentence;
							}
						}
					}

				}
				if (companyMingCheng>bushiyong || companyQuanCheng>bushiyong || qiYeMingchen>bushiyong) {
					int index2=reportText.indexOf("主要控股参股公司");
					if (companyMingCheng>bushiyong) {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), companyMingCheng);
					}
					if (companyQuanCheng>bushiyong) {						
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), companyQuanCheng);
					}				
					if (qiYeMingchen>bushiyong) {
						bufenSentence=reportText.substring(bushiyong+"不适用".length(), qiYeMingchen);
					}
					if(index2>0){
						if (companyMingCheng>index2) {
							bufenSentence=reportText.substring(index2, companyMingCheng);
						}else if (companyQuanCheng>index2) {
							bufenSentence=reportText.substring(index2, companyQuanCheng);
						}else if(qiYeMingchen>index2){
							bufenSentence=reportText.substring(index2, qiYeMingchen);
						}
					}
					if(mc>0 && mc>juhao){
						bufenSentence=reportText.substring(juhao+"。".length(), mc);
					}
					if (bufenSentence!=null){
						if (bufenSentence.getBytes().length==bufenSentence.length() || bufenSentence.length()>100 || bufenSentence.split("\n").length>3) {
							bufenSentence="主要子公司及对公司净利润影响达 10%以上的参股公司情况";//默认值
							return bufenSentence;
						}
					}


				}
			}
		}	
		return bufenSentence;
	}
	/**
	 * 
	 * @param text
	 * @param entityName
	 * @return 实体在文本中的所在的句子
	 */
	//找出实体在文中对应的句子(实体不在表格中)
	private  String findTextSetence(String text,String entityName,int indexEntity){
		int indexEnd=text.indexOf("公司未来发展的",indexEntity);
		int index2=text.indexOf("主要控股参股公司");
		int index3=text.indexOf("报告期内取得和处置子公司的情况");
		String sentence=null;
		if (index2>0) {
			indexEntity=text.indexOf(entityName, index2);
		}else if(index3>0){
			indexEntity=text.indexOf(entityName, index3);
		}
		if(!entityName.matches("[a-zA-Z]+")&& entityName.length()>3) {
			if (indexEntity>0) {
				String beforeSpace=text.substring(indexEntity+entityName.length(), indexEntity+entityName.length()+1).replaceAll("\t|\r|\n|\r\n", " ");
				String afterSpace=text.substring(indexEntity-1, indexEntity).replaceAll("\t|\r|\n|\r\n", " ");
				if(beforeSpace.equals(" ") && afterSpace.equals(" ")){
					sentence=tableSentence(text, entityName, indexEntity);
					return sentence;
				}
				if (index2>0) {
					if (index2<indexEntity && indexEntity<indexEnd){
						int startIndex=text.lastIndexOf(" ", indexEntity);
						if (text.substring(startIndex,indexEntity).contains("，")) {
							startIndex=text.lastIndexOf("，", indexEntity)+"，".length();
						}
						int endIndex=text.indexOf("。",indexEntity);
						sentence=text.substring(startIndex, endIndex+"。".length());
						sentence=sentence.replaceAll("（[a-z0-9A-Z]+）", "");
					}
				}else if (index3>0) {
					if (index3<indexEntity && indexEntity<indexEnd) {
						int startIndex=indexEntity;
						int endIndex=text.indexOf("。",indexEntity);
						sentence=text.substring(startIndex, endIndex+"。".length());
					}
				}
			}
		}
		if(sentence!=null){
			int enTndex=sentence.indexOf(entityName);
			if (sentence.indexOf("；", enTndex)>0 ) {
				sentence=sentence.substring(0, sentence.indexOf("；", enTndex))+"。";
			}
			if(sentence.lastIndexOf("，", enTndex)>0){
				sentence=sentence.substring(0,sentence.lastIndexOf("，", enTndex))+"。";
			}
			if(sentence.indexOf("：", enTndex)>0){
				sentence=sentence.substring(0,sentence.indexOf("：", enTndex)-4)+"。";
			}
			sentence=sentence.replace("□ 适用 √ 不适用", "");
		}

		return sentence;
	}
}
