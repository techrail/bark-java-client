package bark.client.models;

import org.json.JSONObject;

public class MoreData {
    JSONObject jsonObject = new JSONObject();
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
