package bark.client.examples;

import bark.client.constants.Global;
import bark.client.models.Config;
import bark.client.util.CustomLogFormatter;

import java.io.IOException;
import java.util.logging.FileHandler;

import static bark.client.models.Config.NewLoggerBarkClient;

public class Client_ClientLoggerOnly {
    public static void main(String[] args) throws IOException {
        Config logConf = NewLoggerBarkClient(Global.Info);
        FileHandler fileHandler = new FileHandler("output_logger.log", true);
        fileHandler.setFormatter(new CustomLogFormatter());
        logConf.setLoggerHandler(fileHandler);

        logConf.Info("Hello Info Logger Only");
        logConf.Debug("Hello Debug Logger Only");
        logConf.Panic("Hello Panic Logger Only");
        logConf.Alert("Hello Alert Logger Only");
        logConf.Warn("Hello Warn Logger Only");
        logConf.Error("Hello Error Logger Only");
        logConf.Notice("Hello Notice Logger Only");
    }
}
