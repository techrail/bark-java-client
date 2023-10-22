package bark.client.models;

import bark.client.constants.Global;
import bark.client.requestchannel.ClientChannel;
import bark.client.services.ingestion.LogIngester;
import bark.client.services.sender.LogSender;

import java.io.IOException;

public class Config {
    private String baseUrl;
    private String errorLevel;
    private  String serviceName;
    private String sessionName;

    public void Panic(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Panic, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }
    public void Alert(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Alert, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }
    public void Error(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Error, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }
    public void Warn(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Warning, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }
    public void Notice(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Notice, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }
    public void Info(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Info, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }
    public void Debug(String message) throws IOException {
        System.out.println(message);
        BarkLog log = new BarkLog(Global.Debug, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
    }

    public void Raw(RawLog rawLog) throws IOException {
        BarkLog log = new BarkLog(rawLog.getLogLevel(),rawLog.getServiceName(), rawLog.getSessionName(), rawLog.getCode(), rawLog.getMessage());
        log.setMoreData(rawLog.getMoreData());
        dispatchLogMessage(log);
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

    private void dispatchLogMessage(BarkLog barkLog) throws IOException {
        LogIngester.SendToClientChannel(barkLog);
    }
}
