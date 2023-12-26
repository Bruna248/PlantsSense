package pt.uc.dei.cm.plantsmc.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.PlantRepository;
import pt.uc.dei.cm.plantsmc.model.SensorData;

public class PlantViewModel extends ViewModel {

    private final MutableLiveData<List<Plant>> plants;
    private MutableLiveData<List<Plant>> plantsByGreenhouse = new MutableLiveData<>();
    private final MutableLiveData<Plant> selectedPlant = new MutableLiveData<>();
    private final PlantRepository repository;
    private Map<String, MutableLiveData<SensorData>> plantSensorDataMap = new HashMap<>();

    public PlantViewModel() {
        repository = new PlantRepository();
        this.plants = repository.getPlants();
    }

    public void setPlantsByGreenhouse(String greenhouseId) {
        this.plantsByGreenhouse = repository.getPlantsByGreenhouse(greenhouseId);
    }

    public MutableLiveData<List<Plant>> getPlants() {
        return plants;
    }

    public MutableLiveData<List<Plant>> getPlantsByGreenhouse() {
        return plantsByGreenhouse;
    }

    public MutableLiveData<Plant> getSelectedPlant() {
        return selectedPlant;
    }

    public void setSelectedPlant(Plant plant) {
        selectedPlant.setValue(plant);
    }

    public LiveData<SensorData> getSpecificGreenhouseSensorData(String greenhouseID) {
        MutableLiveData<SensorData> liveData = plantSensorDataMap.get(greenhouseID);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            plantSensorDataMap.put(greenhouseID, liveData);
        }
        return liveData;
    }

    public void updatePlantSensorData(String plantId, String sensorType, String sensorValue, String timestamp) {
        // Get or create the MutableLiveData for the given greenhouse ID
        MutableLiveData<SensorData> liveData = plantSensorDataMap.get(plantId);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            plantSensorDataMap.put(plantId, liveData);
        }

        SensorData sensorData = liveData.getValue();
        if (sensorData == null) {
            sensorData = new SensorData();
        }
        sensorData.setParentId(plantId);
        sensorData.setParentType(Plant.class.getSimpleName());
        sensorData.setTimestamp(timestamp);

        // Update the sensor data based on sensor type
        switch (sensorType) {
            case "temperature":
                sensorData.setTemperature(Double.valueOf(sensorValue));
                break;
            case "humidity":
                sensorData.setHumidity(Double.valueOf(sensorValue));
                break;
            case "light":
                sensorData.setLight(Boolean.parseBoolean(sensorValue));
                break;
        }

        liveData.postValue(sensorData);
    }

    public LiveData<Double> getTemperatureDataForPlant(String plantId) {
        MutableLiveData<Double> temperatureLiveData = new MutableLiveData<>();

        LiveData<SensorData> sensorLiveData = plantSensorDataMap.get(plantId);
        if (sensorLiveData != null) {
            sensorLiveData.observeForever(new Observer<SensorData>() {
                @Override
                public void onChanged(SensorData sensorData) {
                    if (sensorData != null) {
                        temperatureLiveData.postValue(sensorData.getTemperature());
                    }
                }
            });
        }

        return temperatureLiveData;
    }

    public LiveData<Double> getHumidityDataForPlant(String plantId) {
        MutableLiveData<Double> humidityLiveData = new MutableLiveData<>();

        LiveData<SensorData> sensorLiveData = plantSensorDataMap.get(plantId);
        if (sensorLiveData != null) {
            sensorLiveData.observeForever(new Observer<SensorData>() {
                @Override
                public void onChanged(SensorData sensorData) {
                    if (sensorData != null) {
                        humidityLiveData.postValue(sensorData.getHumidity());
                    }
                }
            });
        }

        return humidityLiveData;
    }

    public void addPlant(Plant plant) {
        repository.addPlant(plant, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Plant added successfully");
            } else {
                Exception e = task.getException();
                Log.e("Firestore", "Error adding plant", e);
            }
        });
    }

}
