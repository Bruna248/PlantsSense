package pt.uc.dei.cm.plantsmc.utils;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

import java.util.UUID;

public class MQTTHelper {

    private static MQTTHelper instance;
    private final Mqtt5BlockingClient client;
    private final static String TAG = "MQTT";

    private MQTTHelper() {
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString()) // or use a static client identifier if you need to maintain state between connections
                .serverHost("broker.hivemq.com")
                .buildBlocking();
    }

    public static synchronized MQTTHelper getInstance() {
        if (instance == null) {
            instance = new MQTTHelper();
        }
        return instance;
    }

    public void connect() {
        client.connect();
    }

    public void disconnect() {
        client.disconnect();
    }

    public void subscribe(String topic, IMqtt5PublishCallback callback) {
        client.toAsync().subscribeWith()
                .topicFilter(topic)
                .callback(publish -> {
                    // Log or process the message
                    callback.onMessage(publish);
                })
                .send();
    }

    public void unsubscribe(String topic) {
        client.unsubscribeWith()
                .topicFilter(topic)
                .send();
    }

    public interface IMqtt5PublishCallback {
        void onMessage(Mqtt5Publish publish);
    }
}

