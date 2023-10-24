package bark.client.models;

import bark.client.constants.Global;
import bark.client.requestchannel.ClientChannel;
import bark.client.services.ingestion.LogIngester;
import bark.client.services.sender.LogSender;
import bark.client.util.BarkLogger;
import bark.client.util.CustomLogFormatter;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static bark.client.util.BarkLogger.*;

public class Config {
    private String baseUrl;
    private String errorLevel;
    private  String serviceName;
    private String sessionName;
    private Logger logger;

    public void Panic(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Panic, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlPanic, message);
        }
    }
    public void Alert(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Alert, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlAlert, message);
        }
    }
    public void Error(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Error, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlError, message);
        }
    }
    public void Warn(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Warning, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlWarn, message);
        }
    }
    public void Notice(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Notice, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlNotice, message);
        }
    }
    public void Info(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Info, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlInfo, message);
        }
    }
    public void Debug(String message) throws IOException {
        BarkLog log = new BarkLog(Global.Debug, this.serviceName, this.sessionName, Global.DefaultLogCode,message);
        dispatchLogMessage(log);
        if(logger != null){
            logger.log(LvlDebug, message);
        }
    }

    public void Raw(RawLog rawLog) throws IOException {
        BarkLog log = new BarkLog(rawLog.getLogLevel(),rawLog.getServiceName(), rawLog.getSessionName(), rawLog.getCode(), rawLog.getMessage());
        log.setMoreData(rawLog.getMoreData());
        dispatchLogMessage(log);

        switch (log.getLogLevel()) {
            case Global.Panic:
                logger.log(LvlPanic, log.getMessage());
                break;
            case Global.Notice:
                logger.log(LvlNotice, log.getMessage());
                break;
            case Global.Alert:
                logger.log(LvlAlert, log.getMessage());
                break;
            case Global.Error:
                logger.log(LvlError, log.getMessage());
                break;
            case Global.Warning:
                logger.log(LvlWarn, log.getMessage());
                break;
            case Global.Debug:
                logger.log(LvlDebug, log.getMessage());
                break;
            case Global.Info:
            default:
                logger.log(LvlInfo, log.getMessage());
                break;
        }
    }

    public Config() {}
    private Config(String baseUrl, String errorLevel, String serviceName, String sessionName, Logger logger) {
        this.baseUrl = baseUrl;
        this.errorLevel = errorLevel;
        this.serviceName = serviceName;
        this.sessionName = sessionName;
        this.logger = logger;
    }

    public static Config NewLoggerBarkClient(String defaultLogLevel) {
        if(!BarkLogger.isValidLogLevel(defaultLogLevel)){
            System.out.println(String.format("L#1MFJJC - %s is not an acceptable log level. %s will be used as the default log level", defaultLogLevel, Global.DefaultLogLevel));
            defaultLogLevel = Global.DefaultLogLevel;
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomLogFormatter());
        Logger logger = newLogger("OfflineBarkLogger",consoleHandler);
        return new Config(Global.DisabledServerUrl, defaultLogLevel, "","", logger);
    }
    public static Config BarkClient(String baseUrl, String errorLevel, String serviceName, String sessionName, boolean enableLogger) {
        ClientChannel.createChannel();
        LogSender logSender = new LogSender();
        logSender.startSendingLogs(baseUrl);
        Logger logger;
        if(enableLogger){
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CustomLogFormatter());
            logger = newLogger("BarkLogger",consoleHandler);
        } else {
            logger = null;
        }
        return new Config(baseUrl,errorLevel,serviceName,sessionName, logger);

    }

    private void dispatchLogMessage(BarkLog barkLog) throws IOException {
        LogIngester.SendToClientChannel(barkLog);
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
