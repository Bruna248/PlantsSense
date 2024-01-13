package pt.uc.dei.cm.plantsmc.view.sensors;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorVMHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsViewHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;

public class SensorsFragment extends Fragment {

    private static final String ARG_PARENT_ID = "arg_parent_id";
    private static final String ARG_PARENT_TYPE = "arg_parent_type";
    private SensorVMHolder sensorViewModel;
    private SensorHolderType parentType;
    private SensorsViewHolder parentView;
    private View temperatureView;
    private View humidityView;
    private String parentId;

    public SensorsFragment() {
        // Required empty public constructor
    }

    public static SensorsFragment newInstance(String parentId, SensorHolderType parentType) {
        SensorsFragment fragment = new SensorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENT_ID, parentId);
        args.putString(ARG_PARENT_TYPE, parentType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SensorsViewHolder) {
            parentView = (SensorsViewHolder) parentFragment;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SensorsGreenhouseHolder");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = getArguments().getString(ARG_PARENT_ID);
            parentType = SensorHolderType.valueOf(getArguments().getString(ARG_PARENT_TYPE));
        }

        if (parentType == SensorHolderType.GREENHOUSE) {
            sensorViewModel = new ViewModelProvider(requireActivity()).get(GreenhouseViewModel.class);
        } else if (parentType == SensorHolderType.PLANT) {
            sensorViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_list, container, false);

        LinearLayout sensorContainer = view.findViewById(R.id.sensorContainerLayout);

        temperatureView = createSensorView("Temperature", "--", false);
        humidityView = createSensorView("Humidity", "--", false);

        sensorViewModel.getTemperatureData(parentId).observe(getViewLifecycleOwner(), temperatureDataObject -> {
            TextView temperatureSensorDataTextView = temperatureView.findViewById(R.id.SensorValue);
            TextView timestampTextView = temperatureView.findViewById(R.id.SensorTimestamp);
            temperatureSensorDataTextView.setText(String.format("%s º", temperatureDataObject.getMeasurement()));
            timestampTextView.setText(temperatureDataObject.getTimestamp());
        });

        sensorViewModel.getHumidityData(parentId).observe(getViewLifecycleOwner(), humidityDataObject -> {
            TextView humiditySensorDataTextView = humidityView.findViewById(R.id.SensorValue);
            TextView timestampTextView = humidityView.findViewById(R.id.SensorTimestamp);
            humiditySensorDataTextView.setText(String.format("%s %%", humidityDataObject.getMeasurement()));
            timestampTextView.setText(humidityDataObject.getTimestamp());
        });

        temperatureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentView.onSensorClick(parentId, SensorType.TEMPERATURE);
            }
        });

        humidityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentView.onSensorClick(parentId, SensorType.HUMIDITY);
            }
        });

        sensorContainer.addView(temperatureView);
        sensorContainer.addView(humidityView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private View createSensorView(String sensorName, String sensorValue, boolean isActuator) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout sensorLayout = (LinearLayout) inflater.inflate(R.layout.sensor_item, null);

        // Set up the layout parameters for the sensor layout with 16 dp margin bottom
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 32);
        sensorLayout.setLayoutParams(layoutParams);

        TextView sensorNameTextView = sensorLayout.findViewById(R.id.SensorName);
        sensorNameTextView.setText(sensorName);

        TextView sensorValueTextView = sensorLayout.findViewById(R.id.SensorValue);
        sensorValueTextView.setText(sensorValue);


        /*SwitchCompat actuatorSwitch = sensorLayout.findViewById(R.id.actuatorSwitch);
        if (isActuator) {
            actuatorSwitch.setVisibility(View.VISIBLE);
            // set up actuator switch if needed
        }*/

        return sensorLayout;
    }
}
