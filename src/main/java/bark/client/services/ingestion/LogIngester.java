package bark.client.services.ingestion;

import bark.client.models.BarkLog;
import bark.client.requestchannel.ClientChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogIngester {
    public static void SendToClientChannel(BarkLog barkLog) {
        if(ClientChannel.ClientQueue.size()  < ClientChannel.ClientChannelCapacity - 1) {
            ClientChannel.ClientQueue.add(barkLog);
        }
        else{
            System.out.println("E#1LMAEG - Queue full, can not push more logs. Log received.");
        }
    }
}
