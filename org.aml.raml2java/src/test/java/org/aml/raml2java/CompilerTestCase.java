package org.aml.raml2java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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

import org.raml.v2.internal.utils.StreamUtils;

import com.sun.codemodel.JCodeModel;

import junit.framework.TestCase;

public abstract class CompilerTestCase extends TestCase {

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

	@SuppressWarnings({ "unused", "deprecation" })
	public HashMap<String, Class<?>> compileAndTest(JCodeModel mdl, String... names) {
		String path=BasicTests.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		File fl=new File(path,"generated");
		recDelete(fl);
		fl.mkdirs();
		
		
		try {
			mdl.build(fl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
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
		classes.mkdirs();
		output.add(classes);
		try {
			standardFileManager.setLocation(ll,output);
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
	}

	private void recDelete(File fl) {
		try {
			Files.walkFileTree(Paths.get(fl.toURI()),new SimpleFileVisitor<java.nio.file.Path>(){
				@Override
				public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs)
						throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult postVisitDirectory(java.nio.file.Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
				
			});
		} catch (IOException e) {
			//throw new IllegalStateException();
		}
	}

}