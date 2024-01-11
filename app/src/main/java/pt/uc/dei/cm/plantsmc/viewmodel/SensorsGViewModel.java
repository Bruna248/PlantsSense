package pt.uc.dei.cm.plantsmc.viewmodel;

import android.hardware.Sensor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.PlantRepository;
import pt.uc.dei.cm.plantsmc.model.SensorG;
import pt.uc.dei.cm.plantsmc.model.SensorRepository;

public class SensorsGViewModel extends ViewModel {

    private MutableLiveData<List<SensorG>> sensorsByGreenhouse = new MutableLiveData<>();
    private MutableLiveData<List<SensorG>> SensorsByPlant = new MutableLiveData<>();

    private final MutableLiveData<SensorG> selectedSensorG = new MutableLiveData<>();
    private final SensorRepository repository;
    private final Map<String, MutableLiveData<SensorG>> SensorGDataMap = new HashMap<>();

    public SensorsGViewModel() {
        repository = new SensorRepository();
        //this.sensorsG = repository.getSensors();
    }

    public void setsensorsByGreenhouse(String greenhouseId) {
        this.sensorsByGreenhouse = repository.getSensorsByGreenhouse(greenhouseId);
    }


    public MutableLiveData<List<SensorG>> getSensorsByGreenhouse() {
        return sensorsByGreenhouse;
    }
    public MutableLiveData<List<SensorG>> getSensorsByPlant() {
        return SensorsByPlant;
    }


    public MutableLiveData<SensorG> getSelectedSensorG() {
        return selectedSensorG;
    }

    public void setSelectedSensor(SensorG sensorData) {
        selectedSensorG.setValue(sensorData);
    }

    public LiveData<SensorG> getSpecificGreenhouseSensorData(String greenhouseID) {
        MutableLiveData<SensorG> liveData = SensorGDataMap.get(greenhouseID);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            SensorGDataMap.put(greenhouseID, liveData);
        }
        return liveData;
    }

    public void addSensor(SensorG sensorData) {
        repository.addSensorG(sensorData ,task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Plant added successfully");
                String newSensorId = task.getResult().getId();
                sensorData.setId(newSensorId);

                List<SensorG> updatedList = sensorsByGreenhouse.getValue();
                if (updatedList == null) {
                    updatedList = new ArrayList<>();
                }
                updatedList.add(sensorData);
                sensorsByGreenhouse.postValue(updatedList);
            } else {
                Exception e = task.getException();
                Log.e("Firestore", "Error adding plant", e);
            }
        });
    }


}


