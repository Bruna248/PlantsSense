package pt.uc.dei.cm.plantsmc.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import pt.uc.dei.cm.plantsmc.model.SensorP;
import pt.uc.dei.cm.plantsmc.model.SensorPRepository;


public class SensorsPViewModel extends ViewModel {

    private MutableLiveData<List<SensorP>> SensorsByPlant = new MutableLiveData<>();

    private final MutableLiveData<SensorP> selectedSensorP = new MutableLiveData<>();
    private final SensorPRepository repository;

    public SensorsPViewModel() {
        repository = new SensorPRepository();
    }

    public MutableLiveData<List<SensorP>> getSensorsByPlant() {
        return SensorsByPlant;
    }

    public MutableLiveData<SensorP> getSelectedSensorP() {
        return selectedSensorP;
    }

    public void setSensorsByPlant(String greenhouseId,String plantID) {
        this.SensorsByPlant = repository.getSensorsByPlant(greenhouseId,plantID);
    }

    public void setSelectedSensor(SensorP sensorData) {
        selectedSensorP.setValue(sensorData);
    }

    public void addSensorP(SensorP sensorData) {
        repository.addSensorP(sensorData, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Plant added successfully");
                String newSensorId = task.getResult().getId();
                sensorData.setId(newSensorId);

                List<SensorP> updatedList = SensorsByPlant.getValue();
                if (updatedList == null) {
                    updatedList = new ArrayList<>();
                }
                updatedList.add(sensorData);
                SensorsByPlant.postValue(updatedList);
            } else {
                Exception e = task.getException();
                Log.e("Firestore", "Error adding plant", e);
            }
        });
    }
}

