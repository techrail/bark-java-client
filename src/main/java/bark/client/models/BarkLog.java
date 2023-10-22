package bark.client.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BarkLog {
    private int id;
    private Date logTime;
    private String logLevel;
    private String serviceName;
    private String sessionName;
    private String code;
    @JsonProperty("msg")
    private String message;
    private MoreData moreData;

    public BarkLog(String logLevel, String serviceName, String sessionName, String code, String message) {
        this.logLevel = logLevel;
        this.serviceName = serviceName;
        this.sessionName = sessionName;
        this.code = code;
        this.message = message;
    }
    public BarkLog() {}

    public void setLogTime(Date logTime){
        this.logTime = logTime;
    }
    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MoreData getMoreData() {
        return moreData;
    }

    public void setMoreData(MoreData moreData) {
        this.moreData = moreData;
    }
}
