package bark.client.models;

import bark.client.constants.Constants;
import bark.client.http.Network;
import bark.client.requestchannel.ClientChannel;
import bark.client.services.ingestion.LogIngester;
import bark.client.services.sender.LogSender;
import bark.client.util.BarkLogger;
import bark.client.util.CustomLogFormatter;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static bark.client.util.BarkLogger.*;

public class Config {
    private int serverMode;
    private boolean disableDebugLevelLogging;
    private String baseUrl;
    private String errorLevel;
    private String serviceName;
    private String serviceInstanceName;
    private boolean bulkSend;
    private Logger logger;

    public void Panic(String message) throws IOException {
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Panic);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlPanic, message);
        }
    }
    public void Alert(String message) throws IOException {
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Alert);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlAlert, message);
        }
    }
    public void Error(String message) throws IOException {
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Error);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlError, message);
        }
    }
    public void Warn(String message) throws IOException {
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Warning);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlWarn, message);
        }
    }
    public void Notice(String message) throws IOException {
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Notice);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlNotice, message);
        }
    }
    public void Info(String message) throws IOException {
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Info);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlInfo, message);
        }
    }
    public void Debug(String message) throws IOException {
        if(disableDebugLevelLogging) {
            return;
        }
        BarkLog barkLog = parseMessage(getConfig(),message);
        barkLog.setLogLevel(Constants.Debug);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
        if(logger != null){
            logger.log(LvlDebug, message);
        }
    }

    public void Default(String message) throws IOException{
        BarkLog barkLog = parseMessage(getConfig(),message);
        if(barkLog.getLogLevel().equals("DEBUG") && disableDebugLevelLogging){
            return;
        }
        barkLog.setLogLevel(errorLevel);
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }

    public void Println(String message) throws IOException{
        BarkLog barkLog = parseMessage(getConfig(),message);
        if(barkLog.getLogLevel().equals("DEBUG") && disableDebugLevelLogging){
            return;
        }
        barkLog.setMoreData(new MoreData());
        DispatchLogMessage(barkLog);
    }

    public void Raw(BarkLog rawLog) throws IOException {
        if(rawLog.getLogLevel().equals("DEBUG") && disableDebugLevelLogging) {
            return;
        }
        BarkLog log = new BarkLog(rawLog.getLogLevel(),rawLog.getServiceName(), rawLog.getServiceInstanceName(), rawLog.getCode(), rawLog.getMessage());
        log.setMoreData(rawLog.getMoreData());
        DispatchLogMessage(log);

        switch (log.getLogLevel()) {
            case Constants.Panic:
                logger.log(LvlPanic, log.getMessage());
                break;
            case Constants.Notice:
                logger.log(LvlNotice, log.getMessage());
                break;
            case Constants.Alert:
                logger.log(LvlAlert, log.getMessage());
                break;
            case Constants.Error:
                logger.log(LvlError, log.getMessage());
                break;
            case Constants.Warning:
                logger.log(LvlWarn, log.getMessage());
                break;
            case Constants.Debug:
                logger.log(LvlDebug, log.getMessage());
                break;
            case Constants.Info:
            default:
                logger.log(LvlInfo, log.getMessage());
                break;
        }
    }

    public static BarkLog parseMessage(Config config, String message) {
        BarkLog barkLog = new BarkLog();
        barkLog.setServiceName(config.serviceName);
        barkLog.setServiceInstanceName(config.serviceInstanceName);

        if(message.length()<6){
            barkLog.setMessage(message);
            return barkLog;
        }

        int positionOfDash = message.indexOf("-");
        if(positionOfDash<1){
            barkLog.setMessage(message);
            return barkLog;
        }

        if(positionOfDash>message.length()-3){
            barkLog.setMessage(message);
            return barkLog;
        }

        String messageParsed = message.substring(positionOfDash+1,message.length()-1).trim();
        barkLog.setMessage(messageParsed);
        String metadata = message.substring(0,positionOfDash).trim();

        String [] metas = metadata.split("#");
        if(metas.length>2){
            barkLog.setMessage(message);
            return barkLog;
        }

        if(metas.length==1){
            if(metas[0].length()> Constants.MaxLogCodeLength){
                barkLog.setMessage(message);
                return barkLog;
            }
            else{
                barkLog.setMessage(message);
                barkLog.setCode(metas[0]);
                return barkLog;
            }
        }

        if(metas.length==2){
            String logLevel = metas[0];
            String logCode = metas[1];

            if(logLevel.length()!=1 || logCode.length()> Constants.MaxLogCodeLength || logCode.isEmpty()){
                barkLog.setMessage(message);
                return barkLog;
            }
            else{
                String logLevelFromCharacter = getLogLevelFromCharacter(logLevel);
                if(!Objects.equals(logLevelFromCharacter, "X")){
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
                return Constants.Panic;
            case "E":
                return Constants.Error;
            case "A":
                return Constants.Alert;
            case "W":
                return Constants.Warning;
            case "I":
                return Constants.Info;
            case "D":
                return Constants.Debug;
            case "N":
                return Constants.Notice;
            default:
                return "X";
        }

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

    public Config() {}
    private Config(int serverMode, String baseUrl, String errorLevel, String serviceName, String serviceInstanceName, Logger logger, boolean bulkSend) {
        this.serverMode =  serverMode;
        this.baseUrl = baseUrl;
        this.errorLevel = errorLevel;
        this.serviceName = serviceName;
        this.serviceInstanceName = serviceInstanceName;
        this.logger = logger;
        this.bulkSend = bulkSend;
    }

    public static Config BarkClient(String baseUrl, String errorLevel, String serviceName, String serviceInstanceName, boolean enableLogger, boolean enableBulkSend) {
        if(!isValid(errorLevel)){
            System.out.println("E#1MBWF0 - " + errorLevel + " is not an acceptable log level. " + Constants.DefaultLogLevel + " will be used as the default log level.");
            errorLevel = Constants.DefaultLogLevel;
        }
        if(enableBulkSend) {
            ClientChannel.createChannel();
            LogSender logSender = new LogSender();
            logSender.startSendingLogs(baseUrl);
        }
        Logger barkLogger = null;
        if(enableLogger){
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CustomLogFormatter());
            barkLogger = newLogger("OfflineBarkLogger",consoleHandler);
        }

        return new Config(Constants.ClientServerUsageModeRemoteServer,baseUrl,
                errorLevel,serviceName,serviceInstanceName,barkLogger,enableBulkSend);
    }
    private Config getConfig(){
        return new Config(this.serverMode,this.baseUrl,this.errorLevel,this.serviceName,this.serviceInstanceName, this.logger,this.bulkSend);
    }

    public static Config NewLoggerBarkClient(String defaultLogLevel) {
        if(!BarkLogger.isValidLogLevel(defaultLogLevel)){
            System.out.println(String.format("E#1MFJJC - %s is not an acceptable log level. %s will be used as the default log level", defaultLogLevel, Constants.DefaultLogLevel));
            defaultLogLevel = Constants.DefaultLogLevel;
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomLogFormatter());
        Logger logger = newLogger("OfflineBarkLogger",consoleHandler);

        return new Config(Constants.ClientServerUsageModeDisabled , Constants.DisabledServerUrl,
                defaultLogLevel, "","", logger,false);
    }

    private void DispatchLogMessage(BarkLog barkLog) throws IOException {
        switch (serverMode) {
            case Constants.ClientServerUsageModeDisabled:
                return;
            case Constants.ClientServerUsageModeRemoteServer:
                if (bulkSend) {
                    LogIngester.SendToClientChannel(barkLog);
                } else {
                    Runnable runnable = () -> {
                        Network networkCall = new Network();
                        try {
                            networkCall.postSingleLog(barkLog, baseUrl + Constants.singleInsertUrl);
                        } catch (IOException e) {
                            System.out.println("E#1MJWF5 - Error occurred while sending single log");
                        }
                    };
                    runnable.run();
                    //Network networkCall = new Network();
                    //networkCall.postSingleLog(barkLog, baseUrl + Constants.singleInsertUrl);
                }
        }
    }

        public void EnableDebugLogs() {
            disableDebugLevelLogging = false;
        }

        public void DisableDebugLogs() {
            disableDebugLevelLogging = true;
        }



    public void clearHandlers(){
        if(logger == null){
            return;
        }

        Handler[] handlers = logger.getHandlers();

        for(Handler handler1: handlers){
            logger.removeHandler(handler1);
        }
    }
    public void setLoggerHandler(Handler handler){
        logger.addHandler(handler);
    }
}
