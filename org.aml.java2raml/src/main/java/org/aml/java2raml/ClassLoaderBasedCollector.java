package org.aml.java2raml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.reflection.ReflectionType;

/**
 * <p>ClassLoaderBasedCollector class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ClassLoaderBasedCollector {

	
	protected ArrayList<String>unableToLoad=new ArrayList<>();
	
	
	private ClassLoader loader;
	
	/**
	 * <p>Constructor for ClassLoaderBasedCollector.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 */
	public ClassLoaderBasedCollector(ClassLoader loader) {
		super();
		this.loader=loader;
	}

	/**
	 * <p>gather.</p>
	 *
	 * @param cfg a {@link org.aml.java2raml.Config} object.
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<ITypeModel> gather(Config cfg){
		ArrayList<Class<?>>classesToProcess=new ArrayList<>();
		File fl=new File(cfg.pathToLookForClasses);
		for (String str:cfg.packageNamesToLook){
			File f=new File(fl,str.replace('.', File.separatorChar));
			if (f.isDirectory()){
				for (File fileToLook:f.listFiles()){
					if (fileToLook.getName().endsWith(".class")){
						String className=fileToLook.getName().substring(0,fileToLook.getName().indexOf('.'));
						String fullClassName=str+"."+className;
						try{
							classesToProcess.add(loader.loadClass(fullClassName));
						}catch (Throwable t){
							unableToLoad.add(fullClassName);
						}
					}
				}
			}
		}
		ArrayList<ITypeModel>mdl=new ArrayList<>();
		l2:for (Class<?>c:classesToProcess){
			if (c.isAnonymousClass()){
				continue;
			}
			if (c.isAnnotation()&&cfg.ignoreUnreferencedAnnotationDeclarations){
				continue;
			}
			for (String s:cfg.classMasksToIgnore){
				if (c.getName().matches(s)){
					continue l2;
				}
			}
			mdl.add(new ReflectionType(c));
		}
		return mdl;
	}
	
	
}
