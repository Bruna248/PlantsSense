package pt.uc.dei.cm.plantsmc.model;

import android.hardware.Sensor;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SensorPRepository {

    private FirebaseFirestore firestore;

    public SensorPRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }


    public void addSensorP(SensorP sensorData, OnCompleteListener<DocumentReference> onCompleteListener) {
        firestore.collection("plants_sensors").add(sensorData).addOnCompleteListener(onCompleteListener);
    }
    public MutableLiveData<List<SensorP>> getSensorsByPlant(String greenhouseId, String plantID) {
        MutableLiveData<List<SensorP>> liveData = new MutableLiveData<>();

        firestore.collection("plants_sensors")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("greenhouseId", greenhouseId)
                .whereEqualTo("plantId",plantID)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SensorP> sensorsPList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                SensorP sensorData = new SensorP(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("type"),
                                        documentSnapshot.getString("description"));
                                //Log.d("SensorP", "ID: " + sensorData.getId() + ", Type: " + sensorData.getType() + ", Description: " + sensorData.getDescription());

                                sensorsPList.add(sensorData);
                            }
                        }
                        liveData.setValue(sensorsPList);
                    } else {
                        // Handle error
                        Exception e = task.getException();
                        Log.e("Firestore", "Error fetching greenhouses", e);
                        liveData.setValue(null); // Set value to null or some error state
                    }
                });

        return liveData;
    }


}

