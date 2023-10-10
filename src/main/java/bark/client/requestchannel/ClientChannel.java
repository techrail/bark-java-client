package bark.client.requestchannel;

import bark.client.models.BarkLog;

import java.net.HttpURLConnection;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientChannel {
    public static final int ClientChannelCapacity = 10000;
    public static ArrayBlockingQueue<BarkLog> ClientQueue;
    public static void createChannel() {
        ClientQueue = new ArrayBlockingQueue<BarkLog>(ClientChannelCapacity,true);
    }
}
