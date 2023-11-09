package bark.client.constants;

public class Constants {
    // Server Endpoints
    public static final String  singleInsertUrl = "insertSingle";
    public static final String multipleInsertUrl  = "insertMultiple";

    // Supported logging levels
    public static final String Panic  = "PANIC";
    public static final String Alert  = "ALERT";
    public static final String Error  = "ERROR";
    public static final String Warning  = "WARN";
    public static final String Notice  = "NOTICE";
    public static final String Info = "INFO";
    public static final String Debug = "DEBUG";
    public static final String DefaultLogLevel = "INFO";

    // Configurations
    public static final int MaxLogCodeLength = 16; // DB constraint
    public static final int ClientServerUsageModeDisabled = 0;
    public static final int ClientServerUsageModeRemoteServer = 1;
    public static final String DisabledServerUrl = "http://0.0.0.0/";

    // Batch sizes
    public static final int LogBatchSizeSmall = 10;
    public static final int LogBatchSizeMedium = 100;
    public static final int LogBatchSizeLarge = 500;

}
