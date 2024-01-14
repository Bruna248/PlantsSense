package pt.uc.dei.cm.plantsmc.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SensorRepository {

    private FirebaseFirestore firestore;
    public SensorRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void addSensor(SensorData sensorData, OnCompleteListener<DocumentReference> onCompleteListener) {
        firestore.collection("sensors").add(sensorData).addOnCompleteListener(onCompleteListener);
    }

    public MutableLiveData<List<SensorData>> getSensorsByParentType(SensorHolderType parentType) {
        MutableLiveData<List<SensorData>> liveData = new MutableLiveData<>();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore.collection("sensors")
                .whereEqualTo("userId", userId)
                .whereEqualTo("parentType", parentType.toString())
                .orderBy("creationDate", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SensorData> sensorDataList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                Map<String, Object> sensorDataMap = documentSnapshot.getData();
                                if (sensorDataMap.containsKey("temperature") &&
                                        sensorDataMap.containsKey("humidity") &&
                                        sensorDataMap.containsKey("parentId") &&
                                        sensorDataMap.containsKey("parentType") &&
                                        sensorDataMap.containsKey("timestamp") &&
                                        sensorDataMap.containsKey("userId")) {
                                    SensorData sensorData = new SensorData(
                                            documentSnapshot.getId(),
                                            (Double) sensorDataMap.get("temperature"),
                                            (Double) sensorDataMap.get("humidity"),
                                            (String) sensorDataMap.get("parentId"),
                                            parentType,
                                            (String) sensorDataMap.get("timestamp"),
                                            (String) sensorDataMap.get("userId"));
                                    sensorDataList.add(sensorData);
                                } else {
                                    Log.e("Firestore", "SensorData document does not contain all required fields");
                                }
                            }
                        }
                        liveData.setValue(sensorDataList);
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
