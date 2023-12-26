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

public class PlantRepository {

    private FirebaseFirestore firestore;

    public PlantRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void addPlant(Plant plant, OnCompleteListener<DocumentReference> onCompleteListener) {
        firestore.collection("plants").add(plant).addOnCompleteListener(onCompleteListener);
    }

    public MutableLiveData<List<Plant>> getPlants() {
        MutableLiveData<List<Plant>> liveData = new MutableLiveData<>();

        firestore.collection("plants")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Plant> plantList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                Plant plant = new Plant(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("name"),
                                        documentSnapshot.getString("greenhouseId"),
                                        documentSnapshot.getString("userId"),
                                        documentSnapshot.getString("specie"));
                                plantList.add(plant);
                            }
                        }
                        liveData.setValue(plantList);
                    } else {
                        // Handle error
                        Exception e = task.getException();
                        Log.e("Firestore", "Error fetching greenhouses", e);
                        liveData.setValue(null); // Set value to null or some error state
                    }
                });

        return liveData;
    }


    public MutableLiveData<List<Plant>> getPlantsByGreenhouse(String greenhouseId) {
        MutableLiveData<List<Plant>> liveData = new MutableLiveData<>();

        firestore.collection("plants")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("greenhouseId", greenhouseId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Plant> plantList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                Plant plant = new Plant(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("name"),
                                        documentSnapshot.getString("greenhouseId"),
                                        documentSnapshot.getString("userId"),
                                        documentSnapshot.getString("specie"));
                                plantList.add(plant);
                            }
                        }
                        liveData.setValue(plantList);
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
