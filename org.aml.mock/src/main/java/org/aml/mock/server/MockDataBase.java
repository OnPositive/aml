package org.aml.mock.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Response;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.Example;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.raml.v2.api.loader.DefaultResourceLoader;

import com.google.gson.Gson;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MockDataBase {

	protected ArrayList<Api>apis=new ArrayList<>();
	
	protected HashMap<String,Object>variables=new HashMap<>();
	
	Random rs=new Random();
	
	void add(String path){
		InputStream resourceAsStream = MockDataBase.class.getResourceAsStream(path);
		Api build = (Api) new TopLevelRamlModelBuilder().build(resourceAsStream, new DefaultResourceLoader(),
				MockDataBase.class.getResource(path).toExternalForm());
		apis.add(build);
	}
	
	void handle(HttpServletRequest request, HttpServletResponse resp){		
		for (Api api:apis){
			for (Action action:api.allMethods()){
				if (action.name().toLowerCase().equals(request.getMethod().toLowerCase())){
					String pathInfo = request.getPathInfo();
					String uri = action.resource().getUri();
					HashMap<String, String> matches = matches(pathInfo,uri);
					if (matches!=null){
						List<Response> responses = action.responses();
						int nextInt = rs.nextInt(responses.size());
						Response response = responses.get(nextInt);
						List<MimeType> body = response.body();
						MimeType mimeType = body.get(0);
						Map<String, Object> example = getExample(mimeType);
						Gson gson = new Gson();												
						for (String s:example.keySet()){
							Object json = process(""+example.get(s));
							example.put(s, json);
						}
						String json = gson.toJson(example);
						try {
							resp.getWriter().println(json);
							resp.getWriter().close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}					
				}
			}
		}
		
	}

	public String process(String json) {
		Configuration cfg=new Configuration();
		cfg.setBooleanFormat("true,false");
		try {
			Template template = new Template("", new StringReader(json), cfg);
			try {
				StringWriter out = new StringWriter();
				template.process(variables, out);
				json=out.toString();
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return json;
	}

	private Map<String,Object> getExample(MimeType mimeType) {
		String example=mimeType.getExample();
		Set<Example> meta = mimeType.getTypeModel().meta(Example.class);
		if (meta.size()==0){
			return new LinkedHashMap<>();
		}
		Example selected=null;
		for (Example e: meta){
			for (Annotation a:e.getAnnotations()){
				if (a.getName().equals("templatedExample")){
					selected=e;
				}
			}
		}
		if (selected==null){
			selected=new ArrayList<>(meta).get(rs.nextInt(meta.size()));
		}
		if (selected!=null){
			return (Map<String, Object>) selected.value();
		}
		return null;
	}

	private HashMap<String, String> matches(String pathInfo, String uri) {
		String[] split = uri.split("/");
		String[] split2 = pathInfo.split("/");		
		if (split.length==split2.length){
			HashMap<String, String>result=new HashMap<>();
			for (int i=0;i<split.length;i++){
				String str=split[i];				
				if (str.length()>0&&str.charAt(0)=='{'){
					String vname=str.substring(1, str.length()-1);
					String value=split2[i];
					result.put(vname, value);
				}
				else{
					if (!str.equals(split2[i])){
						return null;
					}
				}
			}
			return result;
		}
		return null;
	}
}
