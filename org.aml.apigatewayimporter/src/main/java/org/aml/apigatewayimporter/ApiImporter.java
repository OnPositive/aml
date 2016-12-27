package org.aml.apigatewayimporter;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.aml.apimodel.Api;
import org.aml.swagger.writer.SwaggerWriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.amazonaws.services.apigateway.model.ImportRestApiRequest;
import com.amazonaws.services.apigateway.model.ImportRestApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Swagger;

public class ApiImporter {

	private AWSCredentialsProvider credentialsProvider;
	private AmazonApiGatewayClient amazonApiGatewayClient;

	@SuppressWarnings("deprecation")
	public ApiImporter(String profile, String accessKey, String secret) {
		if (accessKey != null && secret != null) {
			credentialsProvider=new AWSCredentialsProvider() {
				
				@Override
				public void refresh() {
				}
				
				@Override
				public AWSCredentials getCredentials() {
					return new AWSCredentials() {
						
						@Override
						public String getAWSSecretKey() {
							return secret;
						}
						
						@Override
						public String getAWSAccessKeyId() {
							return accessKey;
						}
					};
				}
			};
		} else {
			credentialsProvider = new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
					new SystemPropertiesCredentialsProvider(), new ProfileCredentialsProvider(profile),
					new InstanceProfileCredentialsProvider());
		}
		amazonApiGatewayClient = new AmazonApiGatewayClient(credentialsProvider);
	}

	public ImportRestApiResult doImport(Api api) {
		ImportRestApiRequest importRestApiRequest = new ImportRestApiRequest();
		SwaggerWriter swaggerWriter = new SwaggerWriter();
		swaggerWriter.setDumpExamples(false);
		String store = swaggerWriter.store(api);
		Swagger swaggerObject = swaggerWriter.toSwaggerObject(api);
		try {
			String writeValueAsString = new ObjectMapper().writeValueAsString(swaggerObject);
			importRestApiRequest.setBody(ByteBuffer.wrap(store.getBytes("UTF-8")));
			importRestApiRequest.setFailOnWarnings(false);
			System.out.println("Hello");
			ImportRestApiResult importRestApi = amazonApiGatewayClient.importRestApi(importRestApiRequest);
			return importRestApi;
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
}
