package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.INamedParam.TypeKind;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.restrictions.minmax.MaxProperties;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;
import org.junit.Test;
import org.raml.v2.internal.impl.RamlBuilder;
import org.raml.v2.internal.impl.commons.RamlHeader;
import org.raml.v2.internal.impl.commons.RamlHeader.InvalidHeaderException;
import org.raml.v2.internal.utils.StreamUtils;
import org.raml.yagi.framework.nodes.Node;

import junit.framework.TestCase;

public class BasicTests extends TestCase{

	@Test
	public void test() {
		TopLevelModel raml = parse("/t1.raml");
		AbstractType type = raml.types().getType("Z");
		List<IProperty> allProperties = type.toPropertiesView().allProperties();
		for (IProperty p:allProperties){
			TestCase.assertEquals(p.getDeclaredAt().name(),"Animal");
		}
		TestCase.assertTrue(allProperties.size()==2);
	}
	
	@Test
	public void test2() {
		TopLevelModel raml = parse("/t2.raml");
		AbstractType type = raml.types().getType("Animal");
		
		TestCase.assertTrue(type.oneMeta(MaxProperties.class).value().intValue()==3);
	}

	@Test
	public void test3() {
		TopLevelModel raml = parse("/t1.raml");
		AbstractType type = raml.types().getType("Z");
		AbstractType next = type.superTypes().iterator().next();
		TestCase.assertEquals(next.name(), "Animal");
		TestCase.assertEquals(next.subTypes().size(), 2);
		TestCase.assertEquals(type.superTypes().size(), 1);
		TestCase.assertEquals(next.superTypes().iterator().next(),BuiltIns.OBJECT);
	}
	
	@Test
	public void test4() {
		TopLevelModel raml = parse("/t3.raml");
		AbstractType type = raml.types().getType("Animal");
		TestCase.assertEquals(type.oneMeta(Description.class).value(), "Small nice description");
	}
	
	@Test
	public void test5() {
		TopLevelModel raml = parse("/t4.raml");
		AbstractType type = raml.types().getType("Animal");		
		TestCase.assertEquals(type.oneMeta(FacetDeclaration.class).getName(), "q?");
	}
	@Test
	public void test6() {
		TopLevelModel raml = parse("/t1.raml");
		AbstractType type = raml.annotationTypes().getType("Hello");		
		TestCase.assertEquals(type.oneMeta(Minimum.class).value().intValue(), 5);
	}
	
	@Test
	public void test7() {
		TopLevelModel raml = parse("/t5.raml");
		AbstractType type = raml.types().getType("Z");
		Set<Annotation> meta = type.meta(Annotation.class);
		TestCase.assertEquals(meta.size(), 1);
		for (Annotation a:meta){
			TestCase.assertEquals(a.value(), 7);
			TestCase.assertEquals(a.annotationType(), raml.annotationTypes().getType("Hello"));
		}		
	}
	
	@Test
	public void test8() {
		TopLevelModel raml = parse("/t6.raml");
		AbstractType type = raml.types().getType("Z");
		Set<Annotation> meta = type.meta(Annotation.class);
		TestCase.assertEquals(meta.size(), 1);
		for (Annotation a:meta){
			TestCase.assertEquals(a.value(), 7);
			TestCase.assertEquals(a.annotationType(), raml.uses().get("t5").annotationTypes().getType("Hello"));
		}		
	}
	
	@Test
	public void test9() {
		TopLevelModel raml = parse("/t7.raml");
		AbstractType type = raml.types().getType("Z");
		XMLFacet meta = type.oneMeta(XMLFacet.class);
		TestCase.assertEquals(meta.getName(),"ZZ");	
	}
	
	@Test	
	public void test10() {
		TopLevelModel raml = parse("/t8.raml");
		AbstractType type = raml.types().getType("Z");
		TestCase.assertEquals(type.meta(Annotation.class).size(), 3);
		type.declaredMeta().forEach(x->{
			if (x instanceof Annotation){
				Annotation zz=(Annotation) x;
				if (zz.getName().equals("MM")){
					HashMap<String,Object>val=(HashMap<String, Object>) zz.getValue();
					TestCase.assertEquals(val.get("x"), 3);
				}
				if (zz.getName().equals("VV")){					
					TestCase.assertEquals(zz.getValue(), "ddd");
				}
				if (zz.getName().equals("QQ")){					
					TestCase.assertEquals(zz.getValue().toString(), "[A, B]");
				}
			}
		});	
	}
	
	@Test	
	public void test11() {
		Api raml = (Api) parse("/t9.raml");
		boolean found=false;
		boolean hasGet=false;
		boolean hasC=false;
		for (Resource r:raml.resources()){
			if (r.relativeUri().equals("/z/{e}")){
				List<? extends INamedParam> uriParameters = r.uriParameters();
				TestCase.assertTrue(uriParameters.size()==1);
			}
			if (r.relativeUri().equals("/q")){
				found=true;
				TestCase.assertTrue(r.displayName().equals("QQ"));
				for (Action c:r.methods()){
					if (c.method().equals("get")){
						hasGet=true;
						ArrayList<String> is = c.getIs();
						TestCase.assertTrue(is.size()==1);
						TestCase.assertTrue(c.description().equals("get some stuff"));
						List<INamedParam> queryParameters = (List<INamedParam>) c.queryParameters();
						TestCase.assertEquals(queryParameters.size(),3);
						for (INamedParam p:queryParameters){
							TestCase.assertTrue(p.getTypeKind()==INamedParam.TypeKind.STRING);
							if(p.getKey().equals("c")){
								TestCase.assertFalse(p.isRequired());
								hasC=true;
							}
						}
						List<Response> responses = c.responses();
						TestCase.assertTrue(responses.size()==1);
						Response ar=responses.get(0);
						TestCase.assertTrue(ar.code().equals("200"));
						List<MimeType> body = ar.body();
						TestCase.assertTrue(body.size()==1);
						MimeType t=(MimeType) body.get(0);
						TestCase.assertEquals(t.getType(), "application/json");
						TestCase.assertTrue(t.getTypeModel().isObject());
						TestCase.assertTrue(t.getTypeModel().toPropertiesView().allProperties().size()==2);
					}
					if (c.method().equals("post")){
						hasGet=true;
						List<MimeType> body = c.body();
						TestCase.assertTrue(body.size()==1);
						MimeType t=(MimeType) body.get(0);
						TestCase.assertEquals(t.getType(), "application/json");
						TestCase.assertTrue(t.getTypeModel().isObject());
						TestCase.assertTrue(t.getTypeModel().toPropertiesView().allProperties().size()==3);
					}
				}
			}
		}
		TestCase.assertTrue(hasC);
		TestCase.assertTrue(hasGet);
		TestCase.assertTrue(found);
	}
	

	@Test	
	public void test12() {
		Api raml = (Api) parse("/t10.raml");
		boolean found=false;
		boolean hasGet=false;
		boolean hasC=false;
		for (Resource r:raml.resources()){
			if (r.relativeUri().equals("/z/{e}")){
				List<? extends INamedParam> uriParameters = r.uriParameters();
				TestCase.assertTrue(uriParameters.size()==1);
				INamedParam namedParam = uriParameters.get(0);
				TestCase.assertTrue(namedParam.getTypeKind()==TypeKind.NUMBER);
				TestCase.assertTrue(namedParam.getMinimum().intValue()==10);
			}
			if (r.relativeUri().equals("/q")){
				found=true;
				TestCase.assertTrue(r.displayName().equals("QQ"));
				for (Action c:r.methods()){
					if (c.method().equals("get")){
						hasGet=true;
						ArrayList<String> is = c.getIs();
						TestCase.assertTrue(is.size()==1);
						TestCase.assertTrue(c.description().equals("get some stuff"));
						List<INamedParam> queryParameters = (List<INamedParam>) c.queryParameters();
						TestCase.assertEquals(queryParameters.size(),3);
						for (INamedParam p:queryParameters){
							TestCase.assertTrue(p.getTypeKind()==INamedParam.TypeKind.STRING);
							if(p.getKey().equals("c")){
								TestCase.assertFalse(p.isRequired());
								hasC=true;
							}
						}
						List<Response> responses = c.responses();
						TestCase.assertTrue(responses.size()==1);
						Response ar=responses.get(0);
						TestCase.assertTrue(ar.code().equals("200"));
						List<MimeType> body = ar.body();
						TestCase.assertTrue(body.size()==1);
						MimeType t=(MimeType) body.get(0);
						TestCase.assertEquals(t.getType(), "application/json");
						TestCase.assertTrue(t.getTypeModel().isExternal());
						
					}
					if (c.method().equals("post")){
						hasGet=true;
						List<MimeType> body = c.body();
						TestCase.assertTrue(body.size()==1);
						MimeType t=(MimeType) body.get(0);
						TestCase.assertEquals(t.getType(), "application/json");
						TestCase.assertTrue(t.getTypeModel().isExternal());
					}
				}
			}
		}
		TestCase.assertTrue(hasC);
		TestCase.assertTrue(hasGet);
		TestCase.assertTrue(found);
	}
	
	@Test	
	public void test13() {
		Api raml = (Api) parse("/t11.raml");
		boolean hasPost=false;
		for (Resource r:raml.resources()){
			
			if (r.relativeUri().equals("/q")){
				for (Action c:r.methods()){
					
					if (c.method().equals("post")){
						hasPost=true;
						List<MimeType> body = c.body();
						TestCase.assertTrue(body.size()==1);
						MimeType t=(MimeType) body.get(0);
						//TestCase.assertEquals(t.getType(), "application/json");
						TestCase.assertTrue(t.getTypeModel().toPropertiesView().allProperties().size()==2);
						TestCase.assertTrue(t.getFormParameters().size()==2);
					}
				}
			}
		}
		TestCase.assertTrue(hasPost);
		
	}
	
	@Test	
	public void test14() {
		Api raml = (Api) parse("/t12.raml");
		boolean hasPost=false;
		for (Resource r:raml.resources()){
			
			if (r.relativeUri().equals("/q")){
				for (Action c:r.methods()){
					
					if (c.method().equals("post")){
						hasPost=true;
						List<MimeType> body = c.body();
						TestCase.assertTrue(body.size()==1);
						MimeType t=(MimeType) body.get(0);
						//TestCase.assertEquals(t.getType(), "application/json");
						TestCase.assertTrue(t.getTypeModel().isExternal());
						String externalSchemaContent = t.getTypeModel().getExternalSchemaContent();
						TestCase.assertTrue(externalSchemaContent.equals("{}"));
					}
				}
			}
		}
		TestCase.assertTrue(hasPost);
		
	}
	
	private TopLevelModel parse(String res) {
		String string = StreamUtils.toString(BasicTests.class.getResourceAsStream(res));
		Node build = new RamlBuilder().build(string);
		RamlHeader header=null;
		try {
			header= RamlHeader.parse(string);
		} catch (InvalidHeaderException e) {
			e.printStackTrace();
		}
		TopLevelModel raml = new TopLevelRamlModelBuilder().build(build,header);
		return raml;
	}

}
