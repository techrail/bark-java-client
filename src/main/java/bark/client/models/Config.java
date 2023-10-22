package bark.client.models;

import bark.client.constants.Global;
import bark.client.http.Network;
import bark.client.requestchannel.ClientChannel;
import bark.client.services.ingestion.LogIngester;
import bark.client.services.sender.LogSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Config {
    private int serverMode;
    private boolean disableDebugLevelLogging;
    private String baseUrl;
    private String errorLevel;
    private String serviceName;
    private String sessionName;
    private boolean bulkSend;

    public void Panic(String message) throws IOException {
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Panic);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Alert(String message) throws IOException {
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Alert);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Error(String message) throws IOException {
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Error);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Warn(String message) throws IOException {
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Warning);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Notice(String message) throws IOException {
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Notice);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Info(String message) throws IOException {
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Info);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Debug(String message) throws IOException {
        if(disableDebugLevelLogging){
            return;
        }
        System.out.println(message);
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Global.Debug);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }

    public void Default(String message) throws IOException{
        BarkLog barkLog = parseMessage(getConfig(),message);
        if(barkLog.getLogLevel().equals("DEBUG") && disableDebugLevelLogging){
            return;
        }
        barkLog.setLogLevel(errorLevel);
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }

    public void Println(String message) throws IOException{
        BarkLog barkLog = parseMessage(getConfig(),message);
        if(barkLog.getLogLevel().equals("DEBUG") && disableDebugLevelLogging){
            return;
        }
        barkLog.setLogTime(new Date());
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }
    public void Raw(BarkLog barkLog){

    }

    public Config() {}
    private Config(int serverMode, String baseUrl, String errorLevel, String serviceName, String sessionName, boolean bulkSend) {
        this.serverMode = serverMode;
        this.baseUrl = baseUrl;
        this.errorLevel = errorLevel;
        this.serviceName = serviceName;
        this.sessionName = sessionName;
        this.bulkSend = bulkSend;
    }

    public static Config BarkClient(String baseUrl, String errorLevel, String serviceName, String sessionName, boolean enableBulkSend) {
        if(!isValid(errorLevel)){
            System.out.println("L#1MBWF0 - " + errorLevel + " is not an acceptable log level. " + Global.DefaultLogLevel + " will be used as the default log level.");
            errorLevel = Global.DefaultLogLevel;
        }
        if(!enableBulkSend) {
            ClientChannel.createChannel();
            LogSender logSender = new LogSender();
            logSender.startSendingLogs(baseUrl);
        }
        return new Config(Global.ClientServerUsageModeRemoteServer,baseUrl,
                errorLevel,serviceName,sessionName,enableBulkSend);
    }
    private Config getConfig(){
        return new Config(1,baseUrl,errorLevel,serviceName,sessionName,true);
    }

    public static BarkLog parseMessage(Config config, String message) {
        BarkLog barkLog = new BarkLog();
        barkLog.setServiceName(config.serviceName);
        barkLog.setSessionName(config.sessionName);

        if(message.length()<6){
            barkLog.setMessage(message);
            barkLog.setLogLevel(config.errorLevel);
            return barkLog;
        }

        int positionOfDash = message.indexOf("-");
        if(positionOfDash<1){
            barkLog.setMessage(message);
            barkLog.setLogLevel(config.errorLevel);
            return barkLog;
        }

        if(positionOfDash>message.length()-3){
            barkLog.setMessage(message);
            barkLog.setLogLevel(config.errorLevel);
            return barkLog;
        }

        String messageParsed = message.substring(positionOfDash+1,message.length()-1).trim();
        barkLog.setMessage(messageParsed);
        String metadata = message.substring(0,positionOfDash).trim();

        String [] metas = metadata.split("#");
        if(metas.length>2){
            barkLog.setMessage(message);
            barkLog.setLogLevel(config.errorLevel);
            return barkLog;
        }

        if(metas.length==1){
            if(metas[0].length()>Global.MaxLogCodeLength){
                barkLog.setMessage(message);
                barkLog.setLogLevel(config.errorLevel);
                return barkLog;
            }
            else{
                barkLog.setMessage(message);
                barkLog.setLogLevel(config.errorLevel);
                barkLog.setCode(metas[0]);
                return barkLog;
            }
        }

        if(metas.length==2){
            String logLevel = metas[0];
            String logCode = metas[1];

            if(logLevel.length()!=1 || logCode.length()>Global.MaxLogCodeLength || logCode.isEmpty()){
                barkLog.setMessage(message);
                barkLog.setLogLevel(config.errorLevel);
                return barkLog;
            }
            else{
                String logLevelFromCharacter = getLogLevelFromCharacter(logLevel);
                if(Objects.equals(logLevelFromCharacter, "X")){
                 barkLog.setLogLevel(config.errorLevel);
                }
                else {
                    barkLog.setLogLevel(logLevelFromCharacter);
                }
                barkLog.setCode(logCode);
            }
        }
        return barkLog;

    }

    private static String getLogLevelFromCharacter(String logLevel) {
        switch (logLevel.toUpperCase()) {
            case "P":
                return Global.Panic;
            case "E":
                return Global.Error;
            case "A":
                return Global.Alert;
            case "W":
                return Global.Warning;
            case "I":
                return Global.Info;
            case "D":
                return Global.Debug;
            case "N":
                return Global.Notice;
            default:
                return "X";
        }

    }

    public static Config BarkClientWithServer(String databaseUrl, String defaultLogLevel, String serviceName, String sessionName){
        if(!isValid(defaultLogLevel)){
            System.out.println("L#1MBML8 - " + defaultLogLevel + " is not an acceptable log level. " + Global.DefaultLogLevel + " will be used as the default log level.");
            defaultLogLevel = Global.DefaultLogLevel;
        }




        return new Config(Global.ClientServerUsageModeEmbedded,
                Global.DisabledServerUrl,
                defaultLogLevel,
                serviceName,
                sessionName,
                false);


    }

    private static boolean isValid(String logLevel) {
        switch(logLevel){
            case "PANIC":
            case "WARNING":
            case "ALERT":
            case "ERROR":
            case "INFO":
            case "DEBUG":
            case "NOTICE":
                return true;
            default:
                return false;

        }
    }

    private void DispatchLogMessage(BarkLog barkLog) throws IOException {
        switch(serverMode){
            case Global.ClientServerUsageModeDisabled:
                return;
            case Global.ClientServerUsageModeRemoteServer:
                if(bulkSend){
                    LogIngester.SendToClientChannel(barkLog);
                } else{
                    Network networkCall = new Network();
                    networkCall.postSingleLog(barkLog,baseUrl+Global.singleInsertUrl);
                }
        }

        LogIngester.SendToClientChannel(barkLog);
    }

    public void EnableDebugLogs() {
        disableDebugLevelLogging = false;
    }

    public void DisableDebugLogs() {
        disableDebugLevelLogging = true;
    }
}
