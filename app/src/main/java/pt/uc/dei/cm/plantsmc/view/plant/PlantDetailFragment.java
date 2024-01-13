package pt.uc.dei.cm.plantsmc.view.plant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.Serializable;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsViewHolder;
import pt.uc.dei.cm.plantsmc.view.sensors.SensorDetailFragment;
import pt.uc.dei.cm.plantsmc.view.sensors.SensorsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlantDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantDetailFragment extends Fragment implements SensorsViewHolder {

    private static final String ARG_PLANT = "arg_plant";
    private Plant plant;

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
        if (getArguments() != null) {
            plant = (Plant) getArguments().getSerializable(ARG_PLANT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        TextView plantNameTextView = view.findViewById(R.id.plantNameTextView);
        plantNameTextView.setText(plant.getName());

        TextView plantFieldTextView = view.findViewById(R.id.fieldTextView);
        plantFieldTextView.setText(plant.getGreenhousename());

        TextView plantSpecieTextView = view.findViewById(R.id.specieTextView);
        plantSpecieTextView.setText(plant.getSpecie());

        ShapeableImageView imageView=view.findViewById(R.id.field_image_plant_detail);
        imageView.setImageResource(R.drawable.field1);

        SensorsFragment sensorsFragment = SensorsFragment.newInstance(plant.getId(), SensorHolderType.PLANT);
        getChildFragmentManager().beginTransaction().replace(R.id.sensorFragmentContainerView, sensorsFragment)
                .commit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSensorClick(String parentId, SensorType sensorType) {
        SensorDetailFragment sensorPDetailFragment = SensorDetailFragment.newInstance(parentId, SensorHolderType.PLANT, sensorType);
        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, sensorPDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}