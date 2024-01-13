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

import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Measure;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.PlantRepository;
import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorG;
import pt.uc.dei.cm.plantsmc.model.SensorRepository;

public class SensorsGViewModel extends ViewModel {

    private MutableLiveData<List<SensorG>> sensorsByGreenhouse;
    private final SensorRepository repository;

    public SensorsGViewModel() {
        repository = new SensorRepository();
        this.sensorsByGreenhouse=new MutableLiveData<>();

        //this.sensorsG = repository.getSensors();
    }
    public void setsensorsByGreenhouse(String greenhouseId) {
        this.sensorsByGreenhouse=repository.getSensorsByGreenhouse(greenhouseId);
    }

    public MutableLiveData<List<SensorG>> getSensorsByGreenhouse() {
        return sensorsByGreenhouse;
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
                else{
                    Log.d("not null", "not null");

                }
                updatedList.add(sensorData);
                sensorsByGreenhouse.postValue(updatedList);
            } else {
                Exception e = task.getException();
                Log.e("Firestore", "Error adding plant", e);
            }
        });
    }

    public void addMeaure(Measure measure,String sensorID) {
        repository.addMeasureSensorG(measure ,sensorID,task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Plant added successfully");
                String newSensorId = task.getResult().getId();

                List<SensorG> updatedList = sensorsByGreenhouse.getValue();
                if (updatedList != null) {
                    Log.d("not null", "not null");
                    for(SensorG sensor : updatedList){
                        if(sensor.getId().equals(sensorID)){
                            sensor.setTimestamp(measure.getTimestamp());
                            sensor.setMeasure(measure.getMeasure());
                        }
                    }
                }
                else
                    Log.d(" null", " null");

                //updatedList.add(sensorData);
                sensorsByGreenhouse.postValue(updatedList);
            } else {
                Exception e = task.getException();
                Log.e("Firestore", "Error adding plant", e);
            }
        });
    }

    public void updateSensorG(String greenhouseID,Measure measure) {
        // Get or create the MutableLiveData for the given greenhouse ID
        Log.d("A funcao updateSensorG foi chamada","A funcao updateSensorG foi chamada");
        List<SensorG> whydoesitnotwork=sensorsByGreenhouse.getValue();
        if(whydoesitnotwork!=null)
             Log.d("list is not null","list is not null");
        else
            Log.d("list is  null","list is  null");


    }

}


