package pt.uc.dei.cm.plantsmc.view.sensors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.io.Serializable;
import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorG;
import pt.uc.dei.cm.plantsmc.model.SensorP;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsGViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsPViewModel;


public class SensorsPDetailFragment extends Fragment {

    private static final String ARG_PLANT = "arg_plant";
    private SensorP sensor;

    private SensorsPViewModel sensorViewModel;

    public SensorsPDetailFragment() {
        // Required empty public constructor
    }

    public static SensorsPDetailFragment newInstance(SensorP sensorData) {
        SensorsPDetailFragment fragment = new SensorsPDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLANT, (Serializable) sensorData);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your SensorViewModel
        sensorViewModel = new ViewModelProvider(requireActivity()).get(SensorsPViewModel.class);

        if (getArguments() != null) {
            sensor = (SensorP) getArguments().getSerializable(ARG_PLANT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_greenhouse_sensor_details, container, false);

        TextView sensorNameTextView = view.findViewById(R.id.textViewSensorTypeDetails);
        sensorNameTextView.setText(sensor.getType());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}