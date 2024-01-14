package pt.uc.dei.cm.plantsmc.view.greenhouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.viewmodel.ImageViewModel;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditGreenhouseFragment extends Fragment {


    private static final String ARG_GREENHOUSE = "arg_greenhouse";
    private Greenhouse greenhouse;
    private GreenhouseViewHolder parent;

    private ImageViewModel imageViewModel;

    private Button saveGreenhouseButton;
    private EditText greenhouseNameEditText;
    private EditText greenhouseLocationEditText;
    private ShapeableImageView greenhouseImage;
    private Uri imageUri;

    private ActivityResultLauncher<Intent> galleryLauncher;

    public EditGreenhouseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GreenhouseViewHolder) {
            parent = (GreenhouseViewHolder) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GreenhouseHolder");
        }
    }

    public static EditGreenhouseFragment newInstance(Greenhouse greenhouse) {
        EditGreenhouseFragment fragment = new EditGreenhouseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GREENHOUSE, greenhouse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            greenhouse = (Greenhouse) getArguments().getSerializable(ARG_GREENHOUSE);
        }

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = handleGalleryResult(result.getData());
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up listeners
        greenhouseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        saveGreenhouseButton.setOnClickListener(v -> {
            String greenhouseName = greenhouseNameEditText.getText().toString();
            String greenhouseLocation = greenhouseLocationEditText.getText().toString();

            if (greenhouseName.isEmpty()) {
                greenhouseNameEditText.setError("Please enter a name for the greenhouse");
            } else {
                Double latitude = null;
                Double longitude = null;

                if (!greenhouseLocation.isEmpty()) {
                    // Extract latitude and longitude
                    String[] coordinates = greenhouseLocation.split(",");
                    if (coordinates.length != 2) {
                        greenhouseLocationEditText.setError("Invalid location coordinates format");
                    } else {
                        try {
                            latitude = Double.parseDouble(coordinates[0].trim());
                            longitude = Double.parseDouble(coordinates[1].trim());
                        } catch (NumberFormatException e) {
                            greenhouseLocationEditText.setError("Invalid latitude or longitude format");
                        }
                    }
                }

                // Create greenhouse object
                if (this.greenhouse == null) {
                    this.greenhouse = new Greenhouse(greenhouseName, latitude, longitude);
                } else {
                    this.greenhouse.setName(greenhouseName);
                    this.greenhouse.setLatitude(latitude);
                    this.greenhouse.setLongitude(longitude);
                }

                if (imageUri != null) {
                    imageViewModel.addGreenhouseDisplayPhoto(imageUri, greenhouse)
                            .addOnSuccessListener(downloadUrl -> {
                                Log.d("Firestore", "Download URL After: " + downloadUrl);
                                this.greenhouse.setImageURL(downloadUrl);
                                Log.d("GREENHOUSE 1", this.greenhouse.getImageURL());

                                parent.saveGreenhouse(this.greenhouse);

                                // Pop the current fragment
                                getParentFragmentManager().popBackStack();
                            })
                            .addOnFailureListener(exception -> {
                                Log.e("Firestore", "Error adding gallery photo", exception);
                            });
                }
                else {
                    parent.saveGreenhouse(this.greenhouse);

                    // Pop the current fragment
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_greenhouse, container, false);

        // Set up views
        saveGreenhouseButton = view.findViewById(R.id.saveGreenhouseButton);
        greenhouseNameEditText = view.findViewById(R.id.greenhouseNameEditText);
        greenhouseLocationEditText = view.findViewById(R.id.greenhouseLocationEditText);
        greenhouseImage = view.findViewById(R.id.field_image);
        //Log.d("CREATEVIEW", greenhouse.getImageURL());

        if (greenhouse != null) {
            greenhouseNameEditText.setText(greenhouse.getName());
            if (greenhouse.getLatitude()!=null && greenhouse.getLongitude()!=null)
                greenhouseLocationEditText.setText(greenhouse.getLatitude() + "," + greenhouse.getLongitude());

            if (greenhouse.getImageURL()!=null) {
                Log.d("URL CREATE", greenhouse.getImageURL());
                Glide.with(this)
                        .load(greenhouse.getImageURL())
                        .into(greenhouseImage);
            }
            else {
                greenhouseImage.setImageResource(android.R.drawable.ic_input_add);
            }
        }
        else {
            greenhouseImage.setImageResource(android.R.drawable.ic_input_add);
        }

        return view;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private Uri handleGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        Log.d("Image URI", String.valueOf(selectedImageUri));
        return selectedImageUri;
    }
}