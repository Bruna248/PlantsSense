package pt.uc.dei.cm.plantsmc.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
                                documentSnapshot.getString("userId"));
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
