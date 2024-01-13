package pt.uc.dei.cm.plantsmc.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.PlantRepository;
import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorDataObject;
import pt.uc.dei.cm.plantsmc.model.SensorRepository;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorVMHolder;

public class PlantViewModel extends ViewModel implements SensorVMHolder {

    private final MutableLiveData<List<Plant>> plants;
    private MutableLiveData<List<Plant>> plantsByGreenhouse = new MutableLiveData<>();
    private final MutableLiveData<Plant> selectedPlant = new MutableLiveData<>();
    private final PlantRepository plantRepository;
    private final SensorRepository sensorRepository;
    private final Map<String, MutableLiveData<List<SensorData>>> plantSensorDataMap = new HashMap<>();

    public PlantViewModel() {
        plantRepository = new PlantRepository();
        sensorRepository = new SensorRepository();
        this.plants = plantRepository.getPlants();
    }

    public void setPlantsByGreenhouse(String greenhouseId) {
        this.plantsByGreenhouse = plantRepository.getPlantsByGreenhouse(greenhouseId);
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


    @Override
    public LiveData<List<SensorDataObject>> getSensorsObjectByType(String plantId, SensorType sensorType) {
        MutableLiveData<List<SensorDataObject>> sensorObjectLiveData = new MutableLiveData<>();


        LiveData<List<SensorData>> sensorLiveData = plantSensorDataMap.get(plantId);
        if (sensorLiveData != null) {

            List<SensorDataObject> sensorDataObjects = new ArrayList<>();
            for (SensorData sensorData : Objects.requireNonNull(sensorLiveData.getValue())) {
                sensorDataObjects.add(sensorData.toSensorDataObject(sensorType));
            }
            sensorObjectLiveData.postValue(sensorDataObjects);

            sensorLiveData.observeForever(new Observer<List<SensorData>>() {
                @Override
                public void onChanged(List<SensorData> sensorsData) {
                    if (sensorsData != null) {
                        List<SensorDataObject> sensorDataObjects = new ArrayList<>();
                        for (SensorData sensorData : sensorsData) {
                            sensorDataObjects.add(sensorData.toSensorDataObject(sensorType));
                        }
                        sensorObjectLiveData.postValue(sensorDataObjects);
                    }
                }
            });
        }
        return sensorObjectLiveData;
    }

    @Override
    public LiveData<SensorDataObject> getTemperatureData(String plantId) {
        MutableLiveData<SensorDataObject> temperatureLiveData = new MutableLiveData<>();

        LiveData<List<SensorData>> sensorLiveData = plantSensorDataMap.get(plantId);
        if (sensorLiveData != null) {
            sensorLiveData.observeForever(new Observer<List<SensorData>>() {
                @Override
                public void onChanged(List<SensorData> sensorsData) {
                    if (sensorsData != null) {
                        int lastIndex = sensorsData.size()-1;
                        temperatureLiveData.postValue(sensorsData.get(lastIndex).toSensorDataObject(SensorType.TEMPERATURE));
                    }
                }
            });
        }

        return temperatureLiveData;
    }

    @Override
    public LiveData<SensorDataObject> getHumidityData(String plantId) {
        MutableLiveData<SensorDataObject> humidityLiveData = new MutableLiveData<>();

        LiveData<List<SensorData>> sensorLiveData = plantSensorDataMap.get(plantId);
        if (sensorLiveData != null) {
            sensorLiveData.observeForever(new Observer<List<SensorData>>() {
                @Override
                public void onChanged(List<SensorData> sensorsData) {
                    if (sensorsData != null) {
                        int lastIndex = sensorsData.size()-1;
                        humidityLiveData.postValue(sensorsData.get(lastIndex).toSensorDataObject(SensorType.HUMIDITY));
                    }
                }
            });
        }

        return humidityLiveData;
    }

    public void addPlant(Plant plant) {
        plantRepository.addPlant(plant, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Plant added successfully");
                String newPlantId = task.getResult().getId();
                plant.setId(newPlantId);

                List<Plant> updatedList = plantsByGreenhouse.getValue();
                if (updatedList == null) {
                    updatedList = new ArrayList<>();
                }
                updatedList.add(plant);
                plantsByGreenhouse.postValue(updatedList);
            } else {
                Exception e = task.getException();
                Log.e("Firestore", "Error adding plant", e);
            }
        });
    }


    @Override
    public void addSensor(SensorData sensorData) {
        sensorRepository.addSensor(sensorData, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Sensor added successfully");

                MutableLiveData<List<SensorData>> liveData = plantSensorDataMap.get(sensorData.getParentId());
                if (liveData == null) {
                    liveData = new MutableLiveData<>();
                    plantSensorDataMap.put(sensorData.getParentId(), liveData);
                }

                String newSensorId = task.getResult().getId();
                sensorData.setId(newSensorId);

                List<SensorData> updatedList = liveData.getValue();
                if (updatedList == null) {
                    updatedList = new ArrayList<>();
                }
                updatedList.add(sensorData);
                liveData.postValue(updatedList);
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error adding sensor", e);
                Toast.makeText(null, "Error adding sensor", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
