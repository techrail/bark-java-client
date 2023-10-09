package bark.client.services.sender;

import bark.client.constants.Global;
import bark.client.http.Network;
import bark.client.models.BarkLog;
import bark.client.requestchannel.ClientChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogSender {

    private String baseUrl = null;
    private final int logBatchSizeStandard = 100;
    Network networkCall = new Network();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    Runnable task = () -> {
            int clientQueueSize = ClientChannel.ClientQueue.size();
            if (clientQueueSize >= logBatchSizeStandard) {
                List<BarkLog> barkLogList = new ArrayList<>();
                for (int i = 0; i < logBatchSizeStandard; i++) {
                    try {
                        barkLogList.add(ClientChannel.ClientQueue.poll());
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                try {
                    networkCall.postMultipleLogs(barkLogList, baseUrl + Global.multipleInsertUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (clientQueueSize > 0 && clientQueueSize < logBatchSizeStandard) {
                BarkLog barkLog = ClientChannel.ClientQueue.poll();
                try {
                    networkCall.postSingleLog(barkLog, baseUrl + Global.singleInsertUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
    };

    public void startSendingLogs(String baseUrl){
        this.baseUrl = baseUrl;
        executorService.scheduleAtFixedRate(task,0,1, TimeUnit.SECONDS);
    }
}
