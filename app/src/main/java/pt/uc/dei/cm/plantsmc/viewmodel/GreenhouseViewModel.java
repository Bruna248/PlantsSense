package pt.uc.dei.cm.plantsmc.viewmodel;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.GreenhouseRepository;
import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorDataObject;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.model.SensorRepository;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorVMHolder;

public class GreenhouseViewModel extends ViewModel implements SensorVMHolder {

    private final MutableLiveData<List<Greenhouse>> greenhouses;
    private final MutableLiveData<Greenhouse> selectedGreenhouse = new MutableLiveData<>();
    private final Map<String, MutableLiveData<List<SensorData>>> greenhouseSensorDataMap = new HashMap<>();

    private final GreenhouseRepository greenhouseRepository;
    private final SensorRepository sensorRepository;

    public GreenhouseViewModel() {
        greenhouseRepository = new GreenhouseRepository();
        greenhouses = greenhouseRepository.getGreenhouses();

        sensorRepository = new SensorRepository();

        get_sensor_data();
    }

    private void get_sensor_data() {
        sensorRepository.getSensorsByParentType(SensorHolderType.GREENHOUSE).observeForever(new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> sensorDataList) {
                if (sensorDataList != null) {
                    // Temporary map to hold all sensor data lists before posting to LiveData
                    Map<String, List<SensorData>> tempMap = new HashMap<>();

                    for (SensorData sensorData : sensorDataList) {
                        List<SensorData> currentList = tempMap.get(sensorData.getParentId());
                        if (currentList == null) {
                            currentList = new ArrayList<>();
                            tempMap.put(sensorData.getParentId(), currentList);
                        }
                        currentList.add(sensorData);
                    }

                    // Now update the LiveData for each greenhouse ID
                    for (Map.Entry<String, List<SensorData>> entry : tempMap.entrySet()) {
                        String parentId = entry.getKey();
                        List<SensorData> aggregatedList = entry.getValue();
                        MutableLiveData<List<SensorData>> liveData = greenhouseSensorDataMap.get(parentId);

                        if (liveData == null) {
                            liveData = new MutableLiveData<>();
                            greenhouseSensorDataMap.put(parentId, liveData);
                        }

                        // Post the aggregated list of sensor data to the corresponding LiveData
                        liveData.postValue(aggregatedList);
                    }
                }
            }
        });
    }

    public LiveData<List<Greenhouse>> getGreenhouses() {
        return greenhouses;
    }

    public LiveData<Greenhouse> getSelectedGreenhouse() {
        return selectedGreenhouse;
    }

    public void setGreenhouses(List<Greenhouse> greenhouseList) {
        greenhouses.setValue(greenhouseList);
    }

    public void setSelectedGreenhouse(Greenhouse greenhouse) {
        selectedGreenhouse.setValue(greenhouse);
    }

    public LiveData<String> getSelectedGreenhouseName() {
        return Transformations.map(selectedGreenhouse, greenhouse -> {
            if (greenhouse != null) {
                return greenhouse.getName();
            } else {
                return null;
            }
        });
    }

    @Override
    public LiveData<List<SensorDataObject>> getSensorsObjectByType(String greenhouseID, SensorType sensorType) {
        MutableLiveData<List<SensorDataObject>> sensorObjectLiveData = new MutableLiveData<>();

        LiveData<List<SensorData>> sensorLiveData = greenhouseSensorDataMap.get(greenhouseID);
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

    public void addGreenhouse(Greenhouse greenhouse) {

        greenhouseRepository.addGreenhouse(greenhouse, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Greenhouse added successfully");
                String newGreenhouseId = task.getResult().getId();
                greenhouse.setId(newGreenhouseId);

                List<Greenhouse> updatedList = greenhouses.getValue();
                if (updatedList == null) {
                    updatedList = new ArrayList<>();
                }
                updatedList.add(greenhouse);
                greenhouses.postValue(updatedList);
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error adding greenhouse", e);
                Toast.makeText(null, "Error adding greenhouse", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateGreenhouse(Greenhouse greenhouse) {
        greenhouseRepository.updateGreenhouse(greenhouse, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Greenhouse updated successfully");

                List<Greenhouse> updatedList = greenhouses.getValue();
                if (updatedList == null) {
                    updatedList = new ArrayList<>();
                }
                int greenhouseIndex = updatedList.indexOf(greenhouse);
                updatedList.set(greenhouseIndex, greenhouse);
                greenhouses.postValue(updatedList);
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error adding greenhouse", e);
                Toast.makeText(null, "Error adding greenhouse", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void addSensor(SensorData sensorData) {
        sensorRepository.addSensor(sensorData, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Sensor added successfully");

                MutableLiveData<List<SensorData>> liveData = greenhouseSensorDataMap.get(sensorData.getParentId());
                if (liveData == null) {
                    liveData = new MutableLiveData<>();
                    greenhouseSensorDataMap.put(sensorData.getParentId(), liveData);
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

    @Override
    public LiveData<SensorDataObject> getTemperatureData(String greenhouseId) {
        MutableLiveData<SensorDataObject> temperatureLiveData = new MutableLiveData<>();

        LiveData<List<SensorData>> sensorLiveData = greenhouseSensorDataMap.get(greenhouseId);
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

        LiveData<List<SensorData>> sensorLiveData = greenhouseSensorDataMap.get(plantId);
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

    public void addGalleryPhoto(Uri image, Greenhouse greenhouse) {
        repository.addGalleryPhoto(image, greenhouse, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Gallery photo added successfully");
                //Toast.makeText(null, "Gallery photo added successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error adding gallery photo", e);
                Toast.makeText(null, "Error adding gallery photo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
