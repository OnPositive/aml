package org.aml.java2raml;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import junit.framework.TestCase;

public class BasicTest extends TestCase{

	@Test
	public void test0(){
		Java2Raml r0=new Java2Raml();
		r0.add(Manager.class);		
		compare(r0.flush(), "/t1.raml");
	}
	
	@Test
	public void test1(){
		Java2Raml r0=new Java2Raml();
		r0.add(NestedArray.class);		
		compare(r0.flush(), "/t2.raml");
	}
	
	@Test
	public void test2(){
		Java2Raml r0=new Java2Raml();
		r0.add(XMLSerialized.class);		
		compare(r0.flush(), "/t3.raml");		
	}
	
	@Test
	public void test3(){
		Java2Raml r0=new Java2Raml();
		r0.add(XMLSerialized2.class);		
		compare(r0.flush(), "/t4.raml");		
	}
	
	public void test4(){
		Java2Raml r0=new Java2Raml();
		r0.add(EnumTest.class);		
		compare(r0.flush(), "/t5.raml");		
	}
	
	
	
	String normalizeWhiteSpace(String s){
		return s.replace('\r', '\n').replaceAll("\n\n","\n");		
	}

	void compare(String s,String path){
		InputStreamReader inputStreamReader = new InputStreamReader(BasicTest.class.getResourceAsStream(path));
		StringBuilder bld=new StringBuilder();
		while (true){
			int c;
			try {
				c = inputStreamReader.read();
				if (c==-1){
					break;
				}
				bld.append((char)c);
			} catch (IOException e) {
				throw new IllegalStateException();
			}			
		}
		TestCase.assertEquals(normalizeWhiteSpace(bld.toString()), normalizeWhiteSpace(s));
	}
}