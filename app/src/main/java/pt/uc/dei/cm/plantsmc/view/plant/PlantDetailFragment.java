package pt.uc.dei.cm.plantsmc.view.plant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.io.Serializable;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.SensorG;
import pt.uc.dei.cm.plantsmc.model.SensorP;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantsHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorGreenhouseAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsPlantsAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsPlantsHolder;
import pt.uc.dei.cm.plantsmc.view.greenhouse.GreenhouseDetailFragment;
import pt.uc.dei.cm.plantsmc.view.sensors.EditSensorGFragment;
import pt.uc.dei.cm.plantsmc.view.sensors.EditSensorPFragment;
import pt.uc.dei.cm.plantsmc.view.sensors.SensorsGDetailFragment;
import pt.uc.dei.cm.plantsmc.view.sensors.SensorsPDetailFragment;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsGViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsPViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlantDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantDetailFragment extends Fragment implements SensorsPlantsHolder {

    private static final String ARG_PLANT = "arg_plant";
    private Plant plant;
    private PlantViewModel plantViewModel;
    private SensorsPlantsAdapter adapter;
    private SensorsPViewModel sensorsViewModel;
    private UserViewModel userViewModel;

    public PlantDetailFragment() {
        // Required empty public constructor
    }

    public static PlantDetailFragment newInstance(Plant plant) {
        PlantDetailFragment fragment = new PlantDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLANT, (Serializable) plant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your PlantViewModel
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sensorsViewModel = new ViewModelProvider(this).get(SensorsPViewModel.class);
        if (getArguments() != null) {
            plant = (Plant) getArguments().getSerializable(ARG_PLANT);
        }
        sensorsViewModel.setSensorsByPlant(plant.getGreenhouseId(),plant.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        TextView plantNameTextView = view.findViewById(R.id.plantNameTextView);
        plantNameTextView.setText(plant.getName());

        TextView plantFieldTextView = view.findViewById(R.id.field);
        plantFieldTextView.setText(plant.getGreenhousename());

        ShapeableImageView imageView=view.findViewById(R.id.field_image_plant_detail);
        imageView.setImageResource(R.drawable.field1);

        RecyclerView recyclerView = view.findViewById(R.id.SensorsPlantsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new SensorsPlantsAdapter(this);
        recyclerView.setAdapter(adapter);


        setup_sensors_button(view);

        return view;
    }

    private void setup_sensors_button(View view) {
        Button addPlantButton = view.findViewById(R.id.addSensorPlantButton);
        addPlantButton.setOnClickListener(v -> {
            this.onAddSensorP();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorsViewModel.getSensorsByPlant().observe(getViewLifecycleOwner(), sensors -> {
            // Update UI with the list of plants
            adapter.setSensors(sensors);
            adapter.notifyDataSetChanged();
        });
    }
    @Override
    public void onAddSensorP() {
        EditSensorPFragment editsensorPFragment = EditSensorPFragment.newInstance(null);
        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, editsensorPFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onSensorClickP(SensorP sensorData) {
        SensorsPDetailFragment sensorPDetailFragment = SensorsPDetailFragment.newInstance(sensorData);
        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, sensorPDetailFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void saveSensorP(SensorP sensorData) {

        sensorData.setGreenhouseId(plant.getGreenhouseId());
        sensorData.setPlantId(plant.getId());
        sensorData.setUserId(userViewModel.getCurrentUser().getValue().getUid());

        sensorsViewModel.addSensorP(sensorData);

        sensorsViewModel.getSensorsByPlant().observe(this, sensors -> {
            if (sensors != null) {
                PlantDetailFragment newFragment = PlantDetailFragment.newInstance(plant);
                //PlantsFragment newFragment = PlantsFragment.newInstance(greenhouse.getId());
                getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, newFragment)
                        .commit();
            }
        });
    }
}