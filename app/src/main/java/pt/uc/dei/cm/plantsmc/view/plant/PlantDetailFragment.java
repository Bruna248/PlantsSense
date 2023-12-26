package pt.uc.dei.cm.plantsmc.view.plant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.io.Serializable;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.view.greenhouse.GreenhouseDetailFragment;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlantDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantDetailFragment extends Fragment {

    private static final String ARG_PLANT = "arg_plant";
    private Plant plant;

    private PlantViewModel plantViewModel;
    private View temperatureView;
    private View humidityView;

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
        plantViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);

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

        TextView plantIdTextView = view.findViewById(R.id.plantIdTextView);
        plantIdTextView.setText("@" + plant.getId());

        TextView specieNameTextView = view.findViewById(R.id.specieNameTextView);
        specieNameTextView.setText("Specie - " + plant.getSpecie());

        LinearLayout sensorContainer = view.findViewById(R.id.sensorContainerLayout);

        // Initialize temperature and humidity views
        temperatureView = createSensorView("Temperature", "--", false);
        humidityView = createSensorView("Humidity", "--", false);

        sensorContainer.addView(temperatureView);
        sensorContainer.addView(humidityView);

        plantViewModel.getTemperatureDataForPlant(plant.getId()).observe(getViewLifecycleOwner(), temperature -> {
            TextView temperatureSensorDataTextView = temperatureView.findViewById(R.id.sensorValueTextView);
            temperatureSensorDataTextView.setText(String.format("%s º", temperature));
        });

        plantViewModel.getHumidityDataForPlant(plant.getId()).observe(getViewLifecycleOwner(), humidity -> {
            TextView humiditySensorDataTextView = humidityView.findViewById(R.id.sensorValueTextView);
            humiditySensorDataTextView.setText(String.format("%s %%", humidity));
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private View createSensorView(String sensorName, String sensorValue, boolean isActuator) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout sensorLayout = (LinearLayout) inflater.inflate(R.layout.sensor_item, null);

        TextView sensorNameTextView = sensorLayout.findViewById(R.id.sensorNameTextView);
        sensorNameTextView.setText(sensorName);

        TextView sensorValueTextView = sensorLayout.findViewById(R.id.sensorValueTextView);
        sensorValueTextView.setText(sensorValue);

        SwitchCompat actuatorSwitch = sensorLayout.findViewById(R.id.actuatorSwitch);
        if (isActuator) {
            actuatorSwitch.setVisibility(View.VISIBLE);
            // set up actuator switch if needed
        }

        return sensorLayout;
    }
}