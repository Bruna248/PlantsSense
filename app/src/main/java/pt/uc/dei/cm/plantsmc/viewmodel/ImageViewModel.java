package pt.uc.dei.cm.plantsmc.viewmodel;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Image;
import pt.uc.dei.cm.plantsmc.model.ImageRepository;

public class ImageViewModel extends ViewModel {

    //private final MutableLiveData<List<Image>> images;
    private MutableLiveData<List<Image>> imagesByGreenhouse = new MutableLiveData<>();
    private final MutableLiveData<Image> selectedImage = new MutableLiveData<>();
    private final ImageRepository repository;

    public ImageViewModel() {
        repository = new ImageRepository();
        //this.images = repository.getImages();
    }

    public void setImagesByGreenhouse(String greenhouseId) {
        this.imagesByGreenhouse = repository.getImagesByGreenhouse(greenhouseId);
    }

    /*public MutableLiveData<List<Image>> getImages() {
        return images;
    }*/

    public MutableLiveData<List<Image>> getImagesByGreenhouse() {
        return imagesByGreenhouse;
    }

    public void addGalleryPhoto(Uri image, Greenhouse greenhouse) {
        repository.addGalleryPhoto(image, greenhouse, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Gallery photo added successfully");
                //Toast.makeText(null, "Gallery photo added successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error adding gallery photo", e);
                Toast.makeText(null, "Error adding gallery photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Task<String> addGreenhouseDisplayPhoto(Uri image, Greenhouse greenhouse) {
        final TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        repository.addGreenhouseDisplayPhoto(image, greenhouse, task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Gallery photo added successfully");

                String downloadUri = task.getResult();

                if (downloadUri != null) {
                    String downloadUrl = downloadUri.toString();
                    taskCompletionSource.setResult(downloadUrl);
                } else {
                    // Handle the case where the download URL is null
                    Log.e("Firestore", "Download URL is null");
                    taskCompletionSource.setException(new Exception("Download URL is null"));
                }
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firestore", "Error adding gallery photo", e);
                taskCompletionSource.setException(e);
            }
        });

        return taskCompletionSource.getTask();
    }
}
