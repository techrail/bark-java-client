package bark.client.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLogFormatter extends Formatter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public String format(LogRecord record) {
        String formattedDate = dateFormat.format(new Date(record.getMillis()));
        String logLevel = record.getLevel().getName();
        String logMessage = formatMessage(record);

        return formattedDate + " " + logLevel + " " + logMessage + "\n";
    }
}