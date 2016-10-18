package {packageName};

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.*;
public class {typeName}Adapter extends TypeAdapter<{typeName}>{

	static Gson gson=new Gson();

static class SimpleParameterizedType implements java.lang.reflect.ParameterizedType{

	final Class<?>raw;
	final Type[] arg;
	public SimpleParameterizedType(Class<?> raw, Class<?> arg) {
		super();
		this.raw = raw;
		this.arg = new Type[]{arg};
	}

	
	
	@Override
	public Type[] getActualTypeArguments() {
		return arg;
	}

	@Override
	public Type getRawType() {
		return raw;
	}

	@Override
	public Type getOwnerType() {
		return null;
	}

}

	@Override
	public void write(JsonWriter out, {typeName} value) throws IOException {
{writeCode}		
	}

	@Override
	public {typeName} read(JsonReader in) throws IOException {
		{typeName} union=new {typeName}();
		JsonElement vl = new JsonParser().parse(in);		
{acCode}		
		return union;
	}
}