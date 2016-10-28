package org.aml.registry.operations;

import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Function;

import org.aml.registry.model.Registry;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StoreRegistry implements Function<Registry,String>{

	public String apply(Registry t) {
		try {
			StringWriter w = new StringWriter();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objectMapper.writeValue(w, t);
			return w.toString();
		} catch (JsonGenerationException e) {
			throw new IllegalStateException(e);
		} catch (JsonMappingException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
