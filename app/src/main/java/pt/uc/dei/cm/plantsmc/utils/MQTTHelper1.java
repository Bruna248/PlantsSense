package pt.uc.dei.cm.plantsmc.utils;


import android.content.Context;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTHelper1 {

    private static MQTTHelper1 instance;
    private MqttAndroidClient mqttAndroidClient;
    private final static String server = "tcp://broker.hivemq.com:1883";
    private final static String TAG = "MQTT";

    private MQTTHelper1(){
    }
    private static synchronized MQTTHelper1 getInstance() {
        if (instance == null) {
            instance = new MQTTHelper1();
        }
        return instance;
    }

    public static void init (Context context, String name) {
        instance = MQTTHelper1.getInstance();
        MQTTHelper1.instance.mqttAndroidClient = new MqttAndroidClient(context, server, name);
    }

    public static void setCallback(MqttCallbackExtended callback) {
        MQTTHelper1.instance.mqttAndroidClient.setCallback(callback);
    }

    public static boolean isConnected() {
        return MQTTHelper1.instance.mqttAndroidClient.isConnected();
    }

    public static void connect() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);

        try {

            MQTTHelper1.instance.mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    //Adjusting the set of options that govern the behaviour of Offline (or Disconnected) buffering of messages
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    MQTTHelper1.instance.mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Failed to connect to: " + server + " " + exception.toString());
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public static void publish(String topic, MqttMessage message) throws MqttException {
        MQTTHelper1.instance.mqttAndroidClient.publish(topic, message);
    }

    public static void stop() {
        try {
            MQTTHelper1.instance.mqttAndroidClient.disconnect();
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public static void subscribeToTopic(String topicfilter) {
        try {
            MQTTHelper1.instance.mqttAndroidClient.subscribe(topicfilter, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG, topicfilter+" - Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, topicfilter+" - Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println(topicfilter+" - Exception subscribing");
            ex.printStackTrace();
        }
    }

    public static void unsubscribeToTopic(String topic) {
        try {
            MQTTHelper1.instance.mqttAndroidClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG, topic+" - Unsubscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, topic+" - Unsubscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println(topic+" - Exception unsubscribing");
            ex.printStackTrace();
        }
    }
}
