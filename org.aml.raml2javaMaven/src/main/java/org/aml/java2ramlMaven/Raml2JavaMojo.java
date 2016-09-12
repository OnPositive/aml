package org.aml.java2ramlMaven;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
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
				wr.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.MIX_IN);
				wr.setDefaultPackageName(defaultPackageName);
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