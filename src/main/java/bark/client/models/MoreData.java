package bark.client.models;

import bark.client.util.JSONObjectDeserializer;
import bark.client.util.JSONObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.JSONObject;

public class MoreData {
    @JsonSerialize(using = JSONObjectSerializer.class)
    @JsonDeserialize(using = JSONObjectDeserializer.class)
    @JsonProperty("data")
    JSONObject jsonObject = new JSONObject();
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
