package org.aml.java2ramlMaven;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aml.java2raml.Config;
import org.aml.java2raml.Config.DefaultAnnotationBehavior;
import org.aml.java2raml.Config.JavaOptionalityMode;
import org.aml.java2raml.Config.MemberProcessingMode;
import org.aml.java2raml.Java2Raml;
import org.aml.typesystem.java.IConfiguarionExtension;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.dependency.fromDependencies.AbstractDependencyFilterMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactFilterException;
import org.apache.maven.shared.artifact.filter.collection.ArtifactsFilter;

@Mojo(name = "generateRaml", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class Java2RamlMojo extends AbstractDependencyFilterMojo{

	
	
	@Parameter(property = "outputFile", defaultValue = "${project.build.directory}/generated-sources/raml/types.raml")
    private File outputFile;
	
	@Parameter
	protected MemberProcessingMode memberMode=MemberProcessingMode.FIELDS;
	
	@Parameter
	protected JavaOptionalityMode optionalityMode=JavaOptionalityMode.EVERYTHING_IS_REQUIRED;
	
	@Parameter
	protected DefaultAnnotationBehavior annotationsBehavior=DefaultAnnotationBehavior.IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES;
	
	@Parameter
	protected List<String> annotationPackages;
	
	@Parameter(required=true)
	List<String> packagesToProcess;
	
	@Parameter
	protected List<String> classMasksToIgnore;
	
	@Parameter
	protected boolean ignoreUnreferencedAnnotationDeclarations=true;
	
	//Annotation profiles related settings
	@Parameter
	protected List<String> annotationProfiles;
	
	@Parameter
	boolean ignoreDefaultAnnotationProfiles=false;
	//settings!!!
	
	@Parameter(property = "extensions")
	private List<String> extensions;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try{
		Set<Artifact> resolvedDependencies = getResolvedDependencies(true);
		ArrayList<URL>urls=new ArrayList<URL>();
		for (Artifact f:resolvedDependencies){
			urls.add(f.getFile().toURL());		
		}
		String outputDirectory=project.getModel().getBuild().getOutputDirectory();
		urls.add(new File(outputDirectory).toURL());
		URLClassLoader cl=new URLClassLoader(urls.toArray(new URL[urls.size()]));
		Config cfg=new Config();
		cfg.setMemberMode(this.memberMode);
		cfg.setOptinalityMode(this.optionalityMode);
		cfg.setAnnotationsBehavior(this.annotationsBehavior);
		if (this.annotationPackages!=null){
			cfg.setAnnotationPackages(new ArrayList<>(this.annotationPackages));	
		}
		cfg.getPackageNamesToLook().addAll(this.packagesToProcess);
		if (this.classMasksToIgnore!=null){
			cfg.getClassMasksToIgnore().addAll(this.classMasksToIgnore);	
		}
		cfg.setIgnoreUnreferencedAnnotationDeclarations(this.ignoreUnreferencedAnnotationDeclarations);
		if (this.annotationProfiles!=null){
			cfg.getAnnotationProfiles().addAll(this.annotationProfiles);	
		}
		if (extensions != null) {
			for (String className : extensions) {
				try{
					Class c = Class.forName(className);
					if (c == null) {
						throw new MojoExecutionException("generatorExtensionClass " + className
								+ " cannot be loaded."
								+ "Have you installed the correct dependency in the plugin configuration?");
					}
					Object newInstance = c.newInstance();
					cfg.addExtension((IConfiguarionExtension) newInstance);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		cfg.setPathToLookForClasses(project.getModel().getBuild().getOutputDirectory());
		String processConfigToString = new Java2Raml().processConfigToString(cl, cfg);
		outputFile.getParentFile().mkdirs();
		FileWriter fs=new FileWriter(outputFile);
		fs.write(processConfigToString);
		fs.close();
		}catch (Exception e){
			throw new MojoFailureException(e.getMessage(), e);
		}
		
	}

	@Override
	protected ArtifactsFilter getMarkedArtifactFilter() {
		return new ArtifactsFilter() {
			
			public boolean isArtifactIncluded(Artifact artifact) throws ArtifactFilterException {
				return true;
			}
			
			@SuppressWarnings("rawtypes")
			public Set filter(Set artifacts) throws ArtifactFilterException {
				return artifacts;
			}
		};
	}
}
