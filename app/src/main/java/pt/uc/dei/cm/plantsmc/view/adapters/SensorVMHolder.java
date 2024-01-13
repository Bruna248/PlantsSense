package pt.uc.dei.cm.plantsmc.view.adapters;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorDataObject;
import pt.uc.dei.cm.plantsmc.model.SensorType;

public interface SensorVMHolder {

    LiveData<List<SensorDataObject>> getSensorsObjectByType(String parentId, SensorType sensorType);

    void addSensor(SensorData sensorData);

    LiveData<SensorDataObject> getTemperatureData(String parentId);

    LiveData<SensorDataObject> getHumidityData(String parentId);
}
