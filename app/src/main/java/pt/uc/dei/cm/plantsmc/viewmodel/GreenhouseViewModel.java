package pt.uc.dei.cm.plantsmc.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.GreenhouseRepository;
import pt.uc.dei.cm.plantsmc.model.SensorData;

public class GreenhouseViewModel extends ViewModel {

    private final MutableLiveData<List<Greenhouse>> greenhouses;
    private final MutableLiveData<Greenhouse> selectedGreenhouse = new MutableLiveData<>();
    private Map<String, MutableLiveData<SensorData>> greenhouseSensorDataMap = new HashMap<>();

    private final GreenhouseRepository repository;

    public GreenhouseViewModel() {
        repository = new GreenhouseRepository();
        greenhouses = repository.getGreenhouses();
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

    public LiveData<SensorData> getSpecificGreenhouseSensorData(String greenhouseID) {
        MutableLiveData<SensorData> liveData = greenhouseSensorDataMap.get(greenhouseID);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            greenhouseSensorDataMap.put(greenhouseID, liveData);
        }
        return liveData;
    }

    public void updateGreenhouseSensorData(String greenhouseID, String sensorType, String sensorValue, String timestamp) {
        // Get or create the MutableLiveData for the given greenhouse ID
        MutableLiveData<SensorData> liveData = greenhouseSensorDataMap.get(greenhouseID);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            greenhouseSensorDataMap.put(greenhouseID, liveData);
        }

        SensorData sensorData = liveData.getValue();
        if (sensorData == null) {
            sensorData = new SensorData();
        }
        sensorData.setParentId(greenhouseID);
        sensorData.setParentType(Greenhouse.class.getSimpleName());
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

    public void addGreenhouse(Greenhouse greenhouse) {
        repository.addGreenhouse(greenhouse, task -> {
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
        repository.updateGreenhouse(greenhouse, task -> {
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
}
