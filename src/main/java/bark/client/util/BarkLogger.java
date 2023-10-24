package bark.client.util;

import bark.client.constants.Global;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BarkLogger {
    public static final Level LvlPanic = new Level(Global.Panic, Level.INFO.intValue()+500){};
    public static final Level LvlAlert = new Level(Global.Alert, Level.INFO.intValue()+400){};
    public static final Level LvlError = new Level(Global.Error, Level.INFO.intValue()+300){};
    public static final Level LvlWarn = new Level(Global.Warning, Level.INFO.intValue()+200){};
    public static final Level LvlNotice = new Level(Global.Notice, Level.INFO.intValue()+100){};
    public static final Level LvlInfo = Level.INFO;
    public static final Level LvlDebug = new Level(Global.Debug, Level.INFO.intValue()-100){};

    public static Logger newLogger(String loggerName, Handler handler){
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        return logger;
    }

    public static boolean isValidLogLevel(String level){
        switch (level) {
            case Global.Panic:
            case Global.Notice:
            case Global.Alert:
            case Global.Error:
            case Global.Warning:
            case Global.Info:
            case Global.Debug:
                return true;
            default:
                return false;
        }
    }
}
