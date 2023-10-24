package bark.client.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.json.JSONObject;

import java.io.IOException;

public class JSONObjectDeserializer extends JsonDeserializer<JSONObject> {
    @Override
    public JSONObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String jsonString = jsonParser.getValueAsString();
        return new JSONObject(jsonString);
    }
}
