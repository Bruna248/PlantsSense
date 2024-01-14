package pt.uc.dei.cm.plantsmc.view.greenhouse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SwipeViewAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseViewHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantsViewHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsViewHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.GalleryAdapter;
import pt.uc.dei.cm.plantsmc.view.plant.EditPlantFragment;
import pt.uc.dei.cm.plantsmc.view.plant.PlantDetailFragment;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.view.sensors.SensorDetailFragment;
import pt.uc.dei.cm.plantsmc.viewmodel.ImageViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GreenhouseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GreenhouseDetailFragment extends Fragment implements PlantsViewHolder, SensorsViewHolder, OnMapReadyCallback {

    private static final String ARG_GREENHOUSE = "arg_greenhouse";
    private Greenhouse greenhouse;

    private GreenhouseViewModel greenhouseViewModel;
    private PlantViewModel plantViewModel;
    private ImageViewModel imageViewModel;
    private UserViewModel userViewModel;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private GreenhouseViewHolder parent;
    SwipeViewAdapter swipeViewAdapter;
    ViewPager2 viewPager;
    MapView mapView;
    GalleryAdapter galleryAdapter;

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

    // This method is used to create new instances of the fragment with a greenhouse object
    public static GreenhouseDetailFragment newInstance(Greenhouse greenhouse) {
        GreenhouseDetailFragment fragment = new GreenhouseDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GREENHOUSE, (Serializable) greenhouse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            greenhouse = (Greenhouse) getArguments().getSerializable(ARG_GREENHOUSE);
        }

        greenhouseViewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        imageViewModel.setImagesByGreenhouse(greenhouse.getId());

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        handleGalleryResult(result.getData());
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_greenhouse_details, container, false);

        // Set up the data in the views
        if (greenhouse != null) {
            TextView textViewName = view.findViewById(R.id.greenhouseNameTextView);
            textViewName.setText(greenhouse.getName());

            ShapeableImageView imageView= view.findViewById(R.id.field_image);
            Glide.with(this)
                    .load(greenhouse.getImageURL())
                    .into(imageView);
        }

        RecyclerView galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL,false));
        galleryAdapter = new GalleryAdapter(new ArrayList<>());
        galleryRecyclerView.setAdapter(galleryAdapter);

        // Edit greenhouse button
        setup_edit_greenhouse(view);

        Button addPhotoButton = view.findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(v -> openGallery());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeViewAdapter = new SwipeViewAdapter(this, greenhouse.getId());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(swipeViewAdapter);
        viewPager.setOffscreenPageLimit(2);


        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        // Use the existing tabs from the XML layout
                        tab.setText("Sensors");
                    } else if (position == 1) {
                        tab.setText("Plants");
                    } else {
                        // Generate tabs for positions beyond the existing tabs
                        tab.setText("OBJECT " + (position + 1));
                    }
                }
        ).attach();

        imageViewModel.getImagesByGreenhouse().observe(getViewLifecycleOwner(), images -> {
            // Update UI with the image gallery
            galleryAdapter.setImages(images);
            galleryAdapter.notifyDataSetChanged();
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void handleGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            imageViewModel.addGalleryPhoto(selectedImageUri, greenhouse);

            imageViewModel.getImagesByGreenhouse().observe(this, images -> {
                if (images != null) {
                    galleryAdapter.setImages(images);
                    galleryAdapter.notifyDataSetChanged();
                    //parent.onEditGreenhouse(greenhouse);
                }
            });
        }
    }


    @Override
    public void onAddPlant() {
        EditPlantFragment editPlantFragment = EditPlantFragment.newInstance(null);
        editPlantFragment.setParent(this);

        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, editPlantFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPlantClick(Plant plant) {
        plant.setGreenhousename(greenhouse.getName());
        PlantDetailFragment plantDetailFragment = PlantDetailFragment.newInstance(plant);
        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, plantDetailFragment, "plant_selected_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSensorClick(String parentId, SensorType sensorType) {
        SensorDetailFragment sensorDetailFragment = SensorDetailFragment.newInstance(parentId, SensorHolderType.GREENHOUSE, sensorType);
        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, sensorDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void savePlant(Plant plant) {

        plant.setGreenhouseId(greenhouse.getId());
        plant.setUserId(userViewModel.getCurrentUser().getValue().getUid());
        plant.setGreenhousename(greenhouse.getName());

        plantViewModel.addPlant(plant);

        plantViewModel.getPlantsByGreenhouse().observe(this, plants -> {
            if (plants != null) {
                GreenhouseDetailFragment newFragment = GreenhouseDetailFragment.newInstance(greenhouse);

                //PlantsFragment newFragment = PlantsFragment.newInstance(greenhouse.getId());
                getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, newFragment)
                        .commit();
            }


        });
    }

    private void setup_edit_greenhouse(View view) {
        Button editGreenhouseButton = view.findViewById(R.id.editGreenhouseButton);
        editGreenhouseButton.setOnClickListener(v -> {

            parent.onEditGreenhouse(greenhouse);

        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (greenhouse.getLatitude() != null && greenhouse.getLongitude() != null) {
            mapView.setVisibility(View.VISIBLE);
            Log.d("COORD", ""+greenhouse.getLatitude()+" "+greenhouse.getLongitude());
            LatLng location = new LatLng(greenhouse.getLatitude(), greenhouse.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location).title(greenhouse.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
        } else {
            mapView.setVisibility(View.GONE);
            LatLng defaultLocation = new LatLng(0, 0); // Set a default location
            googleMap.addMarker(new MarkerOptions().position(defaultLocation));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12.0f));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}