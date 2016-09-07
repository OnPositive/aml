package org.aml.java2ramlMaven;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.dependency.fromDependencies.AbstractDependencyFilterMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.shared.artifact.filter.collection.ArtifactFilterException;
import org.apache.maven.shared.artifact.filter.collection.ArtifactsFilter;

@Mojo(name = "generateRaml", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class Java2RamlMojo extends AbstractDependencyFilterMojo{

	//settings!!!
	
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		Set<Artifact> resolvedDependencies = getResolvedDependencies(true);
		for (Artifact f:resolvedDependencies){
			f.getFile().getAbsolutePath();
		}
		String outputDirectory=project.getModel().getBuild().getOutputDirectory();
		System.out.println(outputDirectory);
	}

	@Override
	protected ArtifactsFilter getMarkedArtifactFilter() {
		return new ArtifactsFilter() {
			
			public boolean isArtifactIncluded(Artifact artifact) throws ArtifactFilterException {
				return true;
			}
			
			public Set filter(Set artifacts) throws ArtifactFilterException {
				return artifacts;
			}
		};
	}
}
