package org.aml.raml2java;

import java.io.IOException;
import java.util.Base64;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ByteArrayToBase64TypeAdapter extends TypeAdapter<byte[]> {


		public void write(JsonWriter out, byte[] value) throws IOException {
			out.value(Base64.getEncoder().encodeToString(value));
		}

		public byte[] read(JsonReader in) throws IOException {
			return Base64.getDecoder().decode(in.nextString());
		}
}