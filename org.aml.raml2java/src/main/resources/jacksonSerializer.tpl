package {packageName};


import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class {typeName}Serializer extends JsonSerializer<{typeName}>{

	@Override
	public void serialize({typeName} value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
{writeCode}		
	}

}
