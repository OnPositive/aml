package org.aml.java2ramlMaven;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aml.raml2java.IClassCustomizer;
import org.aml.raml2java.IPropertyCustomizer;
import org.aml.raml2java.JavaGenerationConfig.DefaultIntegerFormat;
import org.aml.raml2java.JavaGenerationConfig.DefaultNumberFormat;
import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
import org.aml.raml2java.JavaGenerationConfig.WrappersStrategy;
import org.aml.raml2java.JavaWriter;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.dependency.fromDependencies.AbstractDependencyFilterMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactFilterException;
import org.apache.maven.shared.artifact.filter.collection.ArtifactsFilter;
import org.raml.v2.api.loader.FileResourceLoader;

/**
 * <p>Java2RamlMojo class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
@Mojo(name = "generateJava", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Raml2JavaMojo extends AbstractDependencyFilterMojo{

	
	
	@Parameter(property = "outputFolder", defaultValue = "./generated-sources/main/java")
    private File outputFolder;
	
	@Parameter
	protected List<File> ramlFiles;
	
	@Parameter
	protected String defaultPackageName="org.aml.samples";
	
		
	@Parameter
	protected boolean gsonSupport=false;//done
	@Parameter(defaultValue="true")
	protected boolean jacksonSupport=true;//done
	@Parameter(defaultValue="true")
	protected boolean jaxbSupport=true;//done
	@Parameter
	protected boolean containerStrategyCollection=true;//done
	
	protected ArrayList<IPropertyCustomizer>customizers=new ArrayList<>();
	
	protected ArrayList<IClassCustomizer> classCustomizers=new ArrayList<>();
	
	@Parameter
	protected WrappersStrategy wrappedTypesStrategy=WrappersStrategy.NONE;	//done
	@Parameter
	protected boolean addGenerated=true;//done
	@Parameter
	protected HashSet<String>annotationNamespacesToSkipDefinition=new HashSet<>();//done
	@Parameter
	protected HashSet<String>annotationNamespacesToSkipReference=new HashSet<>();//done
	
	@Parameter
	protected HashSet<String>annotationIdsToSkipDefinition=new HashSet<>();//done
	@Parameter
	protected HashSet<String>annotationIdsToSkipReference=new HashSet<>();//done
	@Parameter
	protected boolean skipAllAnnotationDefinitions;//done
	@Parameter
	protected boolean skipAllAnnotationReferences;//done

	@Parameter
	protected DefaultIntegerFormat integerFormat=DefaultIntegerFormat.INT;//done
	@Parameter
	protected DefaultNumberFormat doubleFormat=DefaultNumberFormat.DOUBLE;//done
	
	@Parameter
	protected boolean hashCodeAndEquals;//done
	@Parameter
	protected boolean implementSerializable;//done
	@Parameter
	protected boolean implementClonable;//done
	@Parameter
	protected boolean generateBuilderMethods=true;//done
	
	/** {@inheritDoc} */
	@SuppressWarnings({ })
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		if (!outputFolder.exists()){
			outputFolder.mkdirs();
		}
		for (File f:ramlFiles){
			try {
				TopLevelRamlImpl build = new TopLevelRamlModelBuilder().build(new BufferedInputStream(new FileInputStream(f)),
						new FileResourceLoader(f.getParentFile()), f.getName());
				JavaWriter wr = new JavaWriter();
				wr.getConfig().setWrapperStrategy(wrappedTypesStrategy);
				wr.getConfig().getAnnotationConfig().setSkipAllDefinitions(skipAllAnnotationDefinitions);
				wr.getConfig().getAnnotationConfig().setSkipAllReferences(skipAllAnnotationReferences);
				if (this.annotationIdsToSkipDefinition!=null){
					for (String s:annotationIdsToSkipDefinition){
						wr.getConfig().getAnnotationConfig().addIdToSkipDefinition(s);
					}
				}
				if (this.annotationIdsToSkipReference!=null){
					for (String s:annotationIdsToSkipReference){
						wr.getConfig().getAnnotationConfig().addIdToSkipReference(s);
					}
				}
				if (this.annotationNamespacesToSkipDefinition!=null){
					for (String s:annotationNamespacesToSkipDefinition){
						wr.getConfig().getAnnotationConfig().addNamespaceToSkipDefinition(s);
					}
				}
				if (this.annotationNamespacesToSkipReference!=null){
					for (String s:annotationNamespacesToSkipReference){
						wr.getConfig().getAnnotationConfig().addNamespaceToSkipReference(s);
					}
				}
				wr.getConfig().setJacksonSupport(jacksonSupport);
				wr.getConfig().setGsonSupport(gsonSupport);
				wr.getConfig().setJaxbSupport(jaxbSupport);
				wr.getConfig().setContainerStrategyCollection(containerStrategyCollection);
				wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
				wr.setDefaultPackageName(defaultPackageName);
				wr.getConfig().setIntegerFormat(integerFormat);
				wr.getConfig().setDoubleFormat(doubleFormat);
				wr.getConfig().setAddGenerated(addGenerated);
				wr.getConfig().setGenerateHashCodeAndEquals(hashCodeAndEquals);
				wr.getConfig().setImplementSerializable(implementSerializable);
				wr.getConfig().setImplementClonable(implementClonable);
				wr.getConfig().setGenerateBuilderMethods(generateBuilderMethods);
				wr.write(build);
				wr.getModel().build(outputFolder);
			} catch (FileNotFoundException e) {
				throw new MojoExecutionException(e.getMessage());
			} catch (IOException e) {
				throw new MojoExecutionException(e.getMessage());
			}
		}			
	}

	/** {@inheritDoc} */
	@Override
	protected ArtifactsFilter getMarkedArtifactFilter() {
		return new ArtifactsFilter() {
			
			@Override
			public boolean isArtifactIncluded(Artifact artifact) throws ArtifactFilterException {
				return true;
			}
			
			@Override
			@SuppressWarnings("rawtypes")
			public Set filter(Set artifacts) throws ArtifactFilterException {
				return artifacts;
			}
		};
	}
}