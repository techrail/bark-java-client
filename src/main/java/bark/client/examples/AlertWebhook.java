package bark.client.examples;

import bark.client.constants.Global;
import bark.client.models.BarkLog;
import bark.client.models.Config;
import bark.client.models.Webhook;

import java.io.IOException;

import static bark.client.models.Config.BarkClient;

public class AlertWebhook {
    public static void main(String[] args) throws IOException {
        Config logConf = BarkClient("http://localhost:8080/", Global.Info, "AlertWebhookSvc", "AlertWebhookSess");
        Webhook webhook = new Webhook() {
            @Override
            public void processWebhook(BarkLog barkLog) {
                System.out.println(barkLog.toString());
            }
        };

        logConf.setAlertWebhook(webhook);
        logConf.Alert("Hello Alert Webhook");
    }
}
