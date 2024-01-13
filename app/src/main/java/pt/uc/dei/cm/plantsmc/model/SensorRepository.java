package pt.uc.dei.cm.plantsmc.model;

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
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SensorData> sensorDataList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                SensorData sensorData = new SensorData(
                                        documentSnapshot.getId(),
                                        Double.valueOf(
                                                Objects.requireNonNull(documentSnapshot.getString("temperature"))),
                                        Double.valueOf(
                                                Objects.requireNonNull(documentSnapshot.getString("humidity"))),
                                        documentSnapshot.getString("parentId"),
                                        parentType,
                                        documentSnapshot.getString("timestamp"),
                                        userId);
                                sensorDataList.add(sensorData);
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
