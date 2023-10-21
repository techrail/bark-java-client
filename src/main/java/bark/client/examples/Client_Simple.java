package bark.client.examples;


import bark.client.constants.Global;
import bark.client.models.Config;

import java.io.IOException;

import static bark.client.models.Config.BarkClient;

public class Client_Simple {

    public static void main(String[] args) throws IOException {
        Config logConf = BarkClient("http://localhost:8080/", Global.Info, "testSvc", "testSess");
        simpleTest(logConf);
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
}
