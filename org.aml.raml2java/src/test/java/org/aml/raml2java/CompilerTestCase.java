package org.aml.raml2java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.junit.rules.TemporaryFolder;
import org.raml.v2.api.loader.ClassPathResourceLoader;
import org.raml.v2.internal.utils.StreamUtils;

import com.sun.codemodel.JCodeModel;

import junit.framework.TestCase;

public abstract class CompilerTestCase extends TestCase {

	ArrayList<TemporaryFolder>folders=new ArrayList<>();
	
	@Override
	protected void tearDown() throws Exception {
		for (TemporaryFolder f:folders){
			f.delete();
		}
	}
	
	protected static class LocalFileObject extends SimpleJavaFileObject{
	
			protected LocalFileObject(URI uri, Kind kind) {
				super(uri, kind);		
			}
			
			 public CharSequence getCharContent(boolean ignoreEncodingErrors){			 
				try {
					return StreamUtils.toString(uri.toURL().openStream());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			 
				return null;
			 }
		}

	public CompilerTestCase() {
		super();
	}

	public CompilerTestCase(String name) {
		super(name);
	}
	public Object newInstance(String path,String typeName){
		try {
			return compileAndLoadClass(path, typeName).newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException();
		} catch (IllegalAccessException e) {
			throw new IllegalStateException();
		}
	}
	public Class<?>compileAndLoadClass(String path,String typeName){
		return compileAndLoadClass(path, typeName, false);
	}
	public Class<?>compileAndLoadClass(String path,String typeName,boolean supportSer){
		TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(BasicTest.class.getResourceAsStream("/"+path),
				new ClassPathResourceLoader(), path);
		JavaWriter wr = new JavaWriter();
		if (supportSer){
			wr.getConfig().setGsonSupport(true);
			wr.getConfig().setJacksonSupport(true);
			wr.getConfig().setJaxbSupport(true);
		}
		wr.setDefaultPackageName("org.aml.test");
		wr.write(build);
		String string = "org.aml.test."+typeName;
		if (typeName.indexOf('.')!=-1){
			string=typeName;
		}
		HashMap<String, Class<?>> compileAndTest = compileAndTest(wr.getModel(), string);
		TestCase.assertEquals(compileAndTest.size(), 1);
		Class<?> class1 = compileAndTest.get(string);
		return class1;
	}

	@SuppressWarnings({ "deprecation" })
	public HashMap<String, Class<?>> compileAndTest(JCodeModel mdl, String... names) {
		TemporaryFolder folder=new TemporaryFolder();
		try {
			folder.create();
			folders.add(folder);
		} catch (IOException e1) {
			TestCase.assertFalse(true);
		}
		try{
		String path=BasicTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		File fl=folder.getRoot();
		//recDelete(fl);
		fl.mkdirs();
		
		
		try {
			mdl.build(fl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
		if (systemJavaCompiler==null){
			throw new IllegalStateException("no default compiler, please run on JDK");
		}
		DiagnosticListener<JavaFileObject> diagnosticListener = new DiagnosticListener<JavaFileObject>() {
	
			@Override
			public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
				System.out.println(diagnostic);
			}
		};
		
		StandardJavaFileManager standardFileManager = systemJavaCompiler.getStandardFileManager(diagnosticListener, Locale.getDefault(), Charset.defaultCharset());
		javax.tools.JavaFileManager.Location ll=StandardLocation.CLASS_OUTPUT;
		
		ArrayList<File> output = new ArrayList<File>();
		File classes = new File(fl,"classes");
		javax.tools.JavaFileManager.Location ls=StandardLocation.SOURCE_PATH;
		classes.mkdirs();
		output.add(classes);
		ArrayList<File>source=new ArrayList<>();
		source.add(fl);
		try {
			standardFileManager.setLocation(ll,output);
			standardFileManager.setLocation(ls, source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterable<String> options=new ArrayList<>();
		Iterable<String> classesList=new ArrayList<>();
		ArrayList<JavaFileObject> compilationUnits=new ArrayList<>();
		for (String className:names){			
			SimpleJavaFileObject so=new LocalFileObject(new File(fl,className.replace('.', '/')+".java").toURI(), Kind.SOURCE);
			compilationUnits.add(so);
		}
		StringWriter out = new StringWriter();
		CompilationTask task = systemJavaCompiler.getTask(out, standardFileManager, diagnosticListener, options, classesList, compilationUnits);
		task.call();
		try {
			URLClassLoader cl=new URLClassLoader(new URL[]{classes.toURL()});
			HashMap<String, Class<?>>result=new HashMap<>();
			for (String s:names){
				try {
					result.put(s, cl.loadClass(s));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		} catch (MalformedURLException e) {
			throw new IllegalStateException();
		}	
		} finally {
			//folder.delete();
		}
		
	}

	

}