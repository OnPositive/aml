package {packageName};

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class {typeName}Deserializer extends JsonDeserializer<{typeName}>{

	@Override
	public {typeName} deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec codec=p.getCodec();
		{typeName} union=new {typeName}();
		JsonNode vl = p.readValueAsTree();
{acCode}		
		return union;
	}
}