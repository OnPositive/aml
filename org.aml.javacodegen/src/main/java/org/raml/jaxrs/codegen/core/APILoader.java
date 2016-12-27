package org.raml.jaxrs.codegen.core;

import static org.apache.commons.lang.StringUtils.join;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.apache.commons.io.IOUtils;
import org.raml.v2.api.loader.ClassPathResourceLoader;
import org.raml.v2.api.loader.CompositeResourceLoader;
import org.raml.v2.api.loader.FileResourceLoader;
import org.raml.v2.api.loader.ResourceLoader;
import org.raml.v2.api.loader.UrlResourceLoader;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.internal.impl.commons.model.RamlValidationResult;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class APILoader {
	
	protected static String toDetailedString(RamlValidationResult item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(item.getMessage());
//		stringBuilder.append("\t");
//		stringBuilder.append(item.getLevel());
//		stringBuilder.append(" ");
//		stringBuilder.append(item.getMessage());
//		if (item.getLine() != ValidationResult.UNKNOWN) {
//			stringBuilder.append(" (line ");
//			stringBuilder.append(item.getLine());
//			if (item.getStartColumn() != ValidationResult.UNKNOWN) {
//				stringBuilder.append(", col ");
//				stringBuilder.append(item.getStartColumn());
//				if (item.getEndColumn() != item.getStartColumn()) {
//					stringBuilder.append(" to ");
//					stringBuilder.append(item.getEndColumn());
//				}
//			}
//			stringBuilder.append(")");
//		}
		return stringBuilder.toString();
	}
	
	public TopLevelModel load(Reader ramlReader,Configuration configuration,String readerLocation,boolean ignoreErrors) throws IOException{
		final String ramlBuffer = IOUtils.toString(ramlReader);
		String folder=new File(readerLocation).getParent();
		ResourceLoader[] loaderArray = prepareResourceLoaders(configuration,folder);
		TopLevelRamlModelBuilder bld=new TopLevelRamlModelBuilder();
		TopLevelRamlImpl build = bld.build(ramlBuffer, new CompositeResourceLoader(loaderArray), readerLocation);
		if(ignoreErrors||(build.isOk())){
			
			return build;
		}			
		 else {			 
			final List<String> validationErrors = Lists.transform(build.validationResults(),
					new Function<ValidationResult, String>() {

						public String apply(final ValidationResult vr) {
							return toDetailedString((RamlValidationResult) vr);
						}
					});

			throw new IllegalArgumentException("Invalid RAML definition:\n"
					+ join(validationErrors, "\n"));
		}
	}

	private ResourceLoader[] prepareResourceLoaders(
			final Configuration configuration,final String location) {
		File sourceDirectory = configuration.getSourceDirectory();
		ArrayList<ResourceLoader> loaderList = new ArrayList<ResourceLoader>(
				Arrays.asList(new UrlResourceLoader(),
						new ClassPathResourceLoader()));
		if (sourceDirectory != null) {
			String sourceDirAbsPath = sourceDirectory.getAbsolutePath();
			loaderList.add(new FileResourceLoader(sourceDirAbsPath));
		}
		//Supporting all  options that occured in real life at the moment
		//TODO make loading more consistent (we should drop some options)
		if (location!=null&&location.length()>0){
			String sourceDirAbsPath = sourceDirectory.getAbsolutePath();
			String fl=new File(location).getParent();
			if (sourceDirAbsPath.endsWith(fl)){
				sourceDirAbsPath=sourceDirAbsPath.substring(0,sourceDirAbsPath.length()-fl.length());
				loaderList.add(new FileResourceLoader(sourceDirAbsPath));
				loaderList.add(new FileResourceLoader(sourceDirectory){
					 	
					 	@Override
					    public InputStream fetchResource(String resourceName)
					    {						
					        File includedFile = new File(resourceName);
					        FileInputStream inputStream = null;
//					        if (logger.isDebugEnabled())FIXME
//					        {
//					            logger.debug(String.format("Looking for resource: %s on directory: %s...", resourceName));
//					        }
					        try
					        {
					            return new FileInputStream(includedFile);
					        }
					        catch (FileNotFoundException e)
					        {
					            //ignore
					        }
					        return inputStream;
					    }
				});
			}
			else{
				loaderList.add(new FileResourceLoader(location));
				loaderList.add(new FileResourceLoader(""));
			}
		}
		ResourceLoader[] loaderArray = loaderList
				.toArray(new ResourceLoader[loaderList.size()]);
		return loaderArray;
	}
}
