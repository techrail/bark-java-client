package bark.client.models;

import bark.client.constants.Global;
import bark.client.http.Network;
import bark.client.requestchannel.ClientChannel;
import bark.client.services.ingestion.LogIngester;
import bark.client.services.sender.LogSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private String baseUrl;
    private String errorLevel;
    private  String serviceName;
    private String sessionName;

    public void Panic(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Panic);
    }
    public void Alert(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Alert);
    }
    public void Error(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Error);
    }
    public void Warn(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Warning);
    }
    public void Notice(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Notice);
    }
    public void Info(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Info);
    }
    public void Debug(String message) throws IOException {
        System.out.println(message);
        SendLogsToServer(message,Global.Debug);
    }

    public Config() {}
    private Config(String baseUrl, String errorLevel, String serviceName, String sessionName) {
        this.baseUrl = baseUrl;
        this.errorLevel = errorLevel;
        this.serviceName = serviceName;
        this.sessionName = sessionName;
    }
    public static Config BarkClient(String baseUrl, String errorLevel, String serviceName, String sessionName) {
        ClientChannel.createChannel();
        LogSender logSender = new LogSender();
        logSender.startSendingLogs(baseUrl);
        return new Config(baseUrl,errorLevel,serviceName,sessionName);

    }

    private void SendLogsToServer(String message, String errorLevel) throws IOException {
//      List<BarkLog> list = new ArrayList<>();
//       for (int i=0; i<10000; i++) {
           BarkLog barkLog = new BarkLog(
                   errorLevel, serviceName, sessionName, "12345", message);
//         list.add(barkLog);
//       }
        LogIngester.SendToClientChannel(barkLog);
    }
}
