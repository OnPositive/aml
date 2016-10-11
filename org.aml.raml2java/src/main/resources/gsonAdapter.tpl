package {packageName};

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class {typeName}Adapter extends TypeAdapter<{typeName}>{

	static Gson gson=new Gson();

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