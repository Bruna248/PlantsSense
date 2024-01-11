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

public class SensorRepository {

    private FirebaseFirestore firestore;
    public SensorRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }


    public void addSensorG(SensorG sensorData, OnCompleteListener<DocumentReference> onCompleteListener) {
        firestore.collection("greenhouse_sensors").add(sensorData).addOnCompleteListener(onCompleteListener);
    }
    public MutableLiveData<List<SensorG>> getSensorsByGreenhouse(String greenhouseId) {
        MutableLiveData<List<SensorG>> liveData = new MutableLiveData<>();

        firestore.collection("greenhouse_sensors")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("greenhouseId", greenhouseId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SensorG> sensorsGList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                SensorG sensorData = new SensorG(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("type"),
                                        documentSnapshot.getString("description"));
                                        sensorsGList.add(sensorData);
                            }
                        }
                        liveData.setValue(sensorsGList);
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
