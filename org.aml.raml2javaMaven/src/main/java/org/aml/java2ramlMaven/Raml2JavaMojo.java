package org.aml.java2ramlMaven;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.dependency.fromDependencies.AbstractDependencyFilterMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactFilterException;
import org.apache.maven.shared.artifact.filter.collection.ArtifactsFilter;

/**
 * <p>Java2RamlMojo class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
@Mojo(name = "generateJava", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Raml2JavaMojo extends AbstractDependencyFilterMojo{

	
	
	@Parameter(property = "outputFile", defaultValue = "${project.build.directory}/generated-sources/raml/types.raml")
    private File outputFile;
	
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
	
	/** {@inheritDoc} */
	@SuppressWarnings({ })
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		
			
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