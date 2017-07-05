package com.ner.attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyClass {
	public List<Entity> entitys=new ArrayList<>();
	public static class Entity{
		private String entityType;
		private String enyitySentence;
		public void entityType(String entityType){
			this.entityType=entityType;
		}
		public void enyitySentence(String enyitySentence){
			this.enyitySentence=enyitySentence;
		}
		public CompanyClass builder(){
			return new CompanyClass(this);
		}
	}
	private CompanyClass(Entity entity){
		entitys.add(entity);
	}
}
