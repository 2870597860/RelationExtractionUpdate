package com.ner.demo;

public class Testdemo {
	public static void main(String[] args) {
		Test test1=Test.getInst();
		Test test2=Test.getInst();
		System.out.println(test1.ld==test2.ld);
		System.out.println(test1.list==test2.list);
		System.out.println(test1.list.equals(test2.list));
	}
}
