package pt.uc.dei.cm.plantsmc.utils;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import pt.uc.dei.cm.plantsmc.model.Measure;
import pt.uc.dei.cm.plantsmc.view.MainActivity;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsGViewModel;

public class MQTTUtils {

    private static Mqtt5AsyncClient client;
    private final static String TAG = "MQTT";


    public static void initializeChannel(Context context, SensorsGViewModel sensorsGViewModel, PlantViewModel plantViewModel) {

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

                    if (topicLevels.length < 3){
                        return;
                    }
                    if (topicLevels[0].equals("greenhouse")) {
                        // Handle greenhouse sensor data
                        String sensorID = topicLevels[2];
                        String greenhouseID = topicLevels[1];
                        Log.d(sensorID,sensorID);
                        Measure measure=new Measure();
                        measure.setMeasure(incomingMessage);
                        measure.setTimestamp(timestamp);
                        sensorsGViewModel.addMeaure(measure,sensorID);
                        //Log.d("N sensors",String.valueOf(sensorsGViewModel.getSensorsByGreenhouse().getValue().size()));
                        //sensorsGViewModel.updateSensorG(greenhouseID, sensorType, incomingMessage, timestamp);

                    } else if (topicLevels[0].equals("plant")) {
                        // Handle plant sensor data
                        String plantID = topicLevels[1];
                        String sensorType = topicLevels[2];
                        plantViewModel.updatePlantSensorData(plantID, sensorType, incomingMessage, timestamp);
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
