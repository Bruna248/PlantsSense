package pt.uc.dei.cm.plantsmc.utils;

import android.content.Context;
import android.util.Log;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;

public class MQTTUtils {

    private static Mqtt5AsyncClient client;
    private final static String TAG = "MQTT";

    public static void initializeChannel(Context context, GreenhouseViewModel greenhouseViewModel, PlantViewModel plantViewModel) {
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .buildAsync();

        client.connect().whenComplete((connAck, throwable) -> {
            if (throwable != null) {
                // Handle connection error
            } else {
                // We are connected, subscribe to topics
                client.subscribeWith()
                        .topicFilter("greenhouse/+/#")
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .send()
                        .whenComplete((subAck, subThrowable) -> {
                            if (subThrowable != null) {
                                // Handle subscribe error
                            } else {
                                Log.d(TAG, "Subscription to greenhouse topic complete.");
                            }
                        });

                client.subscribeWith()
                        .topicFilter("plant/+/#")
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .send()
                        .whenComplete((subAck, subThrowable) -> {
                            if (subThrowable != null) {
                                // Handle subscribe error
                            } else {
                                Log.d(TAG, "Subscription to plant topic complete.");
                            }
                        });

                client.publishes(MqttGlobalPublishFilter.SUBSCRIBED, publish -> {
                    String incomingMessage = new String(publish.getPayloadAsBytes());
                    Log.d(TAG, "Received message: " + incomingMessage);
                    SimpleDateFormat timestampID = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                    String timestamp = timestampID.format(new Date());

                    // Parse the topic to determine if it's a greenhouse or plant message
                    String[] topicLevels = publish.getTopic().getLevels().toArray(new String[0]);

                    if (topicLevels.length < 2) {
                        return;
                    }

                    // Handle greenhouse sensor data
                    String parentID = topicLevels[1];
                    SensorData sensorData = new SensorData();
                    sensorData.setParentId(parentID);
                    // {"temperature":31.89999962,"humidity":40,"light_state":"OFF","source":"arduino"}
                    incomingMessage = incomingMessage.replace("{", "").replace("}", "").replace("\"", "");
                    String[] sensorDataArray = incomingMessage.split(",");
                    for (String sensorDataString : sensorDataArray) {

                        String[] sensorDataPair = sensorDataString.split(":");
                        if (sensorDataPair.length < 2) {
                            continue;
                        }

                        String sensorType = sensorDataPair[0];
                        String sensorValue = sensorDataPair[1];

                        if (sensorType.equals("source") && !sensorValue.equals("arduino")) {
                            return;
                        }

                        switch (sensorType) {
                            case "temperature":
                                sensorData.setTemperature((double) Math.round(Double.valueOf(sensorValue)));
                                break;
                            case "humidity":
                                sensorData.setHumidity((double) Math.round(Double.valueOf(sensorValue)));
                                break;
                            case "light_state":
                                sensorData.setLight(sensorValue.equals("ON"));
                                break;
                        }
                    }
                    sensorData.setTimestamp(timestamp);

                    if (topicLevels[0].equals("greenhouse")) {
                        sensorData.setParentType(SensorHolderType.GREENHOUSE);
                        greenhouseViewModel.addSensor(sensorData);
                    } else if (topicLevels[0].equals("plant")) {
                        sensorData.setParentType(SensorHolderType.PLANT);
                        plantViewModel.addSensor(sensorData);
                    }
                });
            }
        });
    }

    public static void disconnect() {
        client.disconnect().whenComplete((mqtt5Disconnect, throwable) -> {
            if (throwable != null) {
                // Handle disconnect error
            } else {
                Log.d(TAG, "Disconnected from the broker.");
            }
        });
    }
}
