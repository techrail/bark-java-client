package bark.client.models;

public interface Webhook {
    void processWebhook(BarkLog log);
}
