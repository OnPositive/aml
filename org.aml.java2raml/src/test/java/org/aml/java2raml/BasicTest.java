package org.aml.java2raml;

import java.io.IOException;
import java.io.InputStreamReader;

import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.java.AllObjectsAreNullable;
import org.aml.typesystem.java.IAnnotationFilter;
import org.junit.Test;

import junit.framework.Assert;
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
	public void test5(){
		Java2Raml r0=new Java2Raml();
		r0.getTypeBuilderConfig().setAnnotationsFilter(new IAnnotationFilter() {
			
			@Override
			public boolean preserve(IAnnotationModel mdl) {
				return true;
			}
		});
		r0.add(CustomAnnotationTypes.class);		
		compare(r0.flush(), "/t6.raml");		
	}
	public void test6(){
		Java2Raml r0=new Java2Raml();
		r0.getTypeBuilderConfig().setAnnotationsFilter(new IAnnotationFilter() {
			
			@Override
			public boolean preserve(IAnnotationModel mdl) {
				return true;
			}
		});
		r0.add(CustomAnnotationTypes2.class);		
		compare(r0.flush(), "/t7.raml");		
	}
	
	public void test7(){
		Java2Raml r0=new Java2Raml();
		r0.getTypeBuilderConfig().setAnnotationsFilter(new IAnnotationFilter() {
			
			@Override
			public boolean preserve(IAnnotationModel mdl) {
				return true;
			}
		});
		r0.add(CustomAnnotationTypes3.class);		
		compare(r0.flush(), "/t8.raml");		
	}
	
	public void test8(){
		Java2Raml r0=new Java2Raml();
		r0.getTypeBuilderConfig().setAnnotationsFilter(new IAnnotationFilter() {
			
			@Override
			public boolean preserve(IAnnotationModel mdl) {
				return true;
			}
		});
		r0.add(NestedAnnotations.class);		
		compare(r0.flush(), "/t9.raml");		
	}
	
	public void test9(){
		Java2Raml r0=new Java2Raml();
		r0.getTypeBuilderConfig().setAnnotationsFilter(new IAnnotationFilter() {
			
			@Override
			public boolean preserve(IAnnotationModel mdl) {
				return true;
			}
		});
		r0.getTypeBuilderConfig().setCheckNullable(new AllObjectsAreNullable());
		r0.add(ValidationBean.class);		
		compare(r0.flush(), "/t10.raml");		
	}
	
	
	static String normalizeWhiteSpace(String s){
		return s.replace('\r', '\n').replaceAll("\n\n","\n");		
	}

	static void compare(String s,String path){
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
		Assert.assertEquals(normalizeWhiteSpace(bld.toString()), normalizeWhiteSpace(s));
	}
}
