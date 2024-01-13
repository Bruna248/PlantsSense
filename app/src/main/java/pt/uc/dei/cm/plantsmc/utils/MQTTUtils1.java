package pt.uc.dei.cm.plantsmc.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import pt.uc.dei.cm.plantsmc.model.Measure;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsGViewModel;


public class MQTTUtils1 {

    public static void initializeChannel(Context context, SensorsGViewModel sensorsGViewModel, PlantViewModel plantViewModel) {
        MQTTHelper1.init(context, "PlantsMC");

        MQTTHelper1.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                MQTTHelper1.subscribeToTopic("greenhouse/+/#");
                //MQTTHelper1.subscribeToTopic("plant/+/#");

            }

            @Override
            public void connectionLost(Throwable cause) {
                MQTTHelper1.stop();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String incomingMessage = message.toString();
                Log.d("MQTT INC_MSG", incomingMessage);
                SimpleDateFormat timestampID = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                String timestamp = timestampID.format(new Date());
                Handler handler = new Handler(Looper.getMainLooper());

                Log.d("TAG", "Received message: " + incomingMessage);

                // Parse the topic to determine if it's a greenhouse or plant message
                String[] topicLevels =topic.split("/");

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
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MQTTHelper1.connect();
    }
}
