package {packageName};


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class PatternAndAdditionalTypeAdapterFactory implements TypeAdapterFactory{

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		return new PatternAndAdditionalTypeAdapter<T>(gson,(Class<T>) type.getRawType());
	}
	
}
