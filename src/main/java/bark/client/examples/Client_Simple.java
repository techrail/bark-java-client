package bark.client.examples;


import bark.client.constants.Constants;
import bark.client.models.BarkLog;
import bark.client.models.Config;
import bark.client.models.MoreData;
import bark.client.util.CustomLogFormatter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.FileHandler;

import static bark.client.models.Config.BarkClient;

public class Client_Simple {

    public static void main(String[] args) throws IOException {
        Config logConf = BarkClient("http://localhost:8080/", Constants.Info, "testSvc", "testSess", true,true);
        FileHandler fileHandler = new FileHandler("output_logger.log");
        fileHandler.setFormatter(new CustomLogFormatter());
        logConf.clearHandlers(); // If this method is not called, it'll only append FileHandler to logger along with default ConsoleHandler i.e. Output will be on both file and console.
        logConf.setLoggerHandler(fileHandler);
        //simpleTest(logConf);
        //rawLogMoreDataTest(logConf);
        disableBulkSend();
        //disableLogger();
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
        BarkLog rawLog = new BarkLog(Constants.DefaultLogLevel, "testSvc", "testSess","00000", "Hello Raw");
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

    public static void disableLogger() throws IOException {
        Config logConf = BarkClient("http://localhost:8080/", Constants.Info, "testSvc", "testSess", false,false);

        logConf.Info("Hello Server. Not you Logger!");
        logConf.Debug("Hello Server. Not you Logger!");
        logConf.Panic("Hello Server. Not you Logger!");
        logConf.Alert("Hello Server. Not you Logger!");
        logConf.Warn("Hello Server. Not you Logger!");
        logConf.Error("Hello Server. Not you Logger!");
        logConf.Notice("Hello Server. Not you Logger!");
    }

    public static void disableBulkSend() throws IOException {
        Config logger =  BarkClient("http://localhost:8080/", "INFO", "testSvc",
                "svc2",false, false);
        logger.Error("Disabled bulk send");
    }
}
