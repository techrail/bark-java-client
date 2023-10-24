package bark.client.examples;


import bark.client.constants.Global;
import bark.client.models.Config;
import bark.client.models.MoreData;
import bark.client.models.RawLog;
import org.json.JSONObject;

import java.io.IOException;

import static bark.client.models.Config.BarkClient;

public class Client_Simple {

    public static void main(String[] args) throws IOException {
        Config logConf = BarkClient("http://localhost:8080/", Global.Info, "testSvc", "testSess");
        simpleTest(logConf);
        rawLogMoreDataTest(logConf);
    }

    public static void simpleTest(Config logConf) throws IOException {
        logConf.Info("Hello Info");
        logConf.Debug("Hello Debug");
        logConf.Panic("Hello Panic");
        logConf.Alert("Hello Alert");
        logConf.Warn("Hello Warn");
        logConf.Error("Hello Error");
        logConf.Notice("Hello Notice");
    }

    public static void rawLogMoreDataTest(Config logConf) throws IOException {
        RawLog rawLog = new RawLog(Global.DefaultLogLevel, "testSvc", "testSess",Global.DefaultLogCode, "Hello Raw");
        MoreData moreData = new MoreData();

        String json = "{\n" +
                "  \"id\": \"0001\",\n" +
                "  \"batters\": {\n" +
                "    \"batter\": [\n" +
                "      {\n" +
                "        \"id\": \"1001\",\n" +
                "        \"type\": \"Regular\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"1002\",\n" +
                "        \"type\": \"Chocolate\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"1003\",\n" +
                "        \"type\": \"Blueberry\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"1004\",\n" +
                "        \"type\": \"Devil's Food\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);

        moreData.setJsonObject(jsonObject);

        rawLog.setMoreData(moreData);

        logConf.Raw(rawLog);
    }
}
