package pt.uc.dei.cm.plantsmc.view.adapters;

import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorG;

public interface SensorsGreenhouseHolder {
    void onAddSensor();
    void onSensorClick(SensorG sensorData);

    void saveSensor(SensorG sensorData);
}
