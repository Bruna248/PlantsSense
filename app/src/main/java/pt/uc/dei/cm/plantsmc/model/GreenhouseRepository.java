package pt.uc.dei.cm.plantsmc.model;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GreenhouseRepository {

    private FirebaseFirestore firestore;

    public GreenhouseRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Greenhouse>> getGreenhouses() {
        MutableLiveData<List<Greenhouse>> liveData = new MutableLiveData<>();

        firestore.collection("greenhouses")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Greenhouse> greenhouseList = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Greenhouse greenhouse = new Greenhouse(documentSnapshot.getId(),
                                documentSnapshot.getString("name"),
                                documentSnapshot.getString("userId"),
                                documentSnapshot.getDouble("latitude"),
                                documentSnapshot.getDouble("longitude")
                                );
                        greenhouseList.add(greenhouse);
                    }
                }
                liveData.setValue(greenhouseList);
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error fetching greenhouses", e);
                liveData.setValue(null); // Set value to null or some error state
            }
        });

        return liveData;
    }

    public void addGreenhouse(Greenhouse greenhouse, OnCompleteListener<DocumentReference> onCompleteListener) {
        firestore.collection("greenhouses")
                .add(greenhouse)
                .addOnCompleteListener(onCompleteListener);
    }

    public void updateGreenhouse(Greenhouse greenhouse, OnCompleteListener<Void> onCompleteListener) {
        firestore.collection("greenhouses")
                .document(greenhouse.getId())
                .set(greenhouse)
                .addOnCompleteListener(onCompleteListener);
    }
}
