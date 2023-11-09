package bark.client.util;

import bark.client.constants.Constants;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BarkLogger {
    public static final Level LvlPanic = new Level(Constants.Panic, Level.INFO.intValue()+500){};
    public static final Level LvlAlert = new Level(Constants.Alert, Level.INFO.intValue()+400){};
    public static final Level LvlError = new Level(Constants.Error, Level.INFO.intValue()+300){};
    public static final Level LvlWarn = new Level(Constants.Warning, Level.INFO.intValue()+200){};
    public static final Level LvlNotice = new Level(Constants.Notice, Level.INFO.intValue()+100){};
    public static final Level LvlInfo = Level.INFO;
    public static final Level LvlDebug = new Level(Constants.Debug, Level.INFO.intValue()-100){};

    public static Logger newLogger(String loggerName, Handler handler){
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        return logger;
    }

    public static boolean isValidLogLevel(String level){
        switch (level) {
            case Constants.Panic:
            case Constants.Notice:
            case Constants.Alert:
            case Constants.Error:
            case Constants.Warning:
            case Constants.Info:
            case Constants.Debug:
                return true;
            default:
                return false;
        }
    }
}
