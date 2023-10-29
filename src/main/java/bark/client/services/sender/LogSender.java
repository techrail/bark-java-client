package bark.client.services.sender;

import bark.client.constants.Constants;
import bark.client.http.Network;
import bark.client.models.BarkLog;
import bark.client.requestchannel.ClientChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogSender {

    private String baseUrl = null;
    Network networkCall = new Network();
    List<BarkLog> barkLogList = new ArrayList<>();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    Runnable task = () -> {
            int clientQueueSize = ClientChannel.ClientQueue.size();
            if (clientQueueSize >= Constants.LogBatchSizeLarge) {
                for (int i = 0; i < Constants.LogBatchSizeLarge; i++) {
                    try {
                        barkLogList.add(ClientChannel.ClientQueue.poll());
                    } catch (Exception e){
                        System.out.println("E#1MJWWM - Error occurred while retrieving logs from queue " + e.getMessage());
                    }
                }
                try {
                    networkCall.postMultipleLogs(barkLogList, baseUrl + Constants.multipleInsertUrl);
                } catch (IOException e) {
                    System.out.println("E#1MJWVP - Error occurred while sending batch of size large " + e.getMessage());;
                }
            } else if (clientQueueSize >= Constants.LogBatchSizeMedium && clientQueueSize < Constants.LogBatchSizeLarge) {
                for (int i = 0; i < Constants.LogBatchSizeMedium; i++) {
                    try {
                        barkLogList.add(ClientChannel.ClientQueue.poll());
                    } catch (Exception e){
                        System.out.println("E#1MJWSM - Error occurred while retrieving logs from queue " + e.getMessage());
                    }
                }
                try {
                    networkCall.postMultipleLogs(barkLogList, baseUrl + Constants.multipleInsertUrl);
                } catch (IOException e) {
                    System.out.println("E#1MJWQV - Error occurred while sending batch of size medium " + e.getMessage());
                }
            } else if (clientQueueSize >= Constants.LogBatchSizeSmall && clientQueueSize < Constants.LogBatchSizeMedium) {
                for (int i = 0; i < Constants.LogBatchSizeSmall; i++) {
                    try {
                        barkLogList.add(ClientChannel.ClientQueue.poll());
                    } catch (Exception e){
                        System.out.println("E#1MJWP5 - Error occurred while retrieving logs from queue " + e.getMessage());
                    }
                }
                try {
                    networkCall.postMultipleLogs(barkLogList, baseUrl + Constants.multipleInsertUrl);
                } catch (IOException e) {
                    System.out.println("E#1MJWN2 - Error occurred while sending batch of size small " + e.getMessage());
                }
            } else if (clientQueueSize > 0 && clientQueueSize < Constants.LogBatchSizeSmall) {
                BarkLog barkLog = ClientChannel.ClientQueue.poll();
                try {
                    networkCall.postSingleLog(barkLog, baseUrl + Constants.singleInsertUrl);
                } catch (IOException e) {
                    System.out.println("E#1MJWM3 - Error occurred while sending single log " + e.getMessage());
                }
            }
            else {
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
