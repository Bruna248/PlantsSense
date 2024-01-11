package pt.uc.dei.cm.plantsmc.view.adapters;

import pt.uc.dei.cm.plantsmc.model.SensorP;

public interface SensorsPlantsHolder {
    void onAddSensorP();
    void onSensorClickP(SensorP sensorData);
    void saveSensorP(SensorP sensorData);
}