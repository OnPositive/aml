package org.aml.apigatewayimporter;

import java.io.File;
import java.io.FileNotFoundException;

import org.aml.apimodel.Api;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;

public class Main {

	public static void main(String[] args) {
		TopLevelModel build;
		try {
			if (args.length<1){
				System.err.println("Please specify path to api");
				return;
			}
			build = TopLevelRamlModelBuilder.build(new File(args[0]));
			try{
			new ApiImporter(null).doImport((Api) build);
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.err.println("API file not found:" + args[0]);
		}
	}
}
