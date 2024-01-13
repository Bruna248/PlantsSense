package pt.uc.dei.cm.plantsmc.model;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ImageRepository {

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public ImageRepository() {
        this.firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }


    public void addGalleryPhoto(Uri imageUri, Greenhouse greenhouse, OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference sRef = storageReference.child("images/" + randomKey);
        sRef.putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    firestore.collection("images").add(new Image(downloadUrl, greenhouse.getId(), false));
                                })
                                .addOnFailureListener(e -> {
                                    // ?
                                });
                        onCompleteListener.onComplete(task);
                    } else {
                        onCompleteListener.onComplete(task);
                    }
                });
    }

    public void addGreenhouseDisplayPhoto(Uri imageUri, Greenhouse greenhouse, OnCompleteListener<String> onCompleteListener) {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference sRef = storageReference.child("images/" + randomKey);
        sRef.putFile(imageUri)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                firestore.collection("images").add(new Image(downloadUrl, greenhouse.getId(), true))
                                        .addOnSuccessListener(documentReference -> onCompleteListener.onComplete(Tasks.forResult(downloadUrl)))
                                        .addOnFailureListener(e -> onCompleteListener.onComplete(Tasks.forResult(null)));
                            })
                            .addOnFailureListener(e -> onCompleteListener.onComplete(Tasks.forException(e)));
                } else {
                    onCompleteListener.onComplete(Tasks.forResult(null));
                }
            });
    }

    /*public MutableLiveData<List<Plant>> getPlants() {
        MutableLiveData<List<Plant>> liveData = new MutableLiveData<>();

        firestore.collection("images")
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
    }*/


    public MutableLiveData<List<Image>> getImagesByGreenhouse(String greenhouseId) {
        MutableLiveData<List<Image>> liveData = new MutableLiveData<>();

        firestore.collection("images")
                .whereEqualTo("greenhouseID", greenhouseId)
                .whereEqualTo("displayPhoto", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Image> galleryItems = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String downloadUrl = document.getString("downloadUrl");
                            String greenhouseID = document.getString("greenhouseID");
                            boolean isDisplayPhoto = Boolean.TRUE.equals(document.getBoolean("isDisplayPhoto"));

                            Image galleryItem = new Image(downloadUrl, greenhouseID, isDisplayPhoto);
                            galleryItems.add(galleryItem);
                        }

                        liveData.setValue(galleryItems);
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });

        return liveData;
    }
}
