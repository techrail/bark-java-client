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


    public MoreData getMoreData() {
        return moreData;
    }

    public void setMoreData(MoreData moreData) {
        this.moreData = moreData;
    }


}
