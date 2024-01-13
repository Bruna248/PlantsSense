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

import org.w3c.dom.Text;

import java.util.List;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.SensorData;
import pt.uc.dei.cm.plantsmc.model.SensorDataObject;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorVMHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;


public class SensorDetailFragment extends Fragment {

    private static final String ARG_PARENT_ID = "arg_parent_id";
    private static final String ARG_SENSOR_TYPE = "arg_sensor_type";
    private static final String ARG_PARENT_TYPE = "arg_parent_type";
    private SensorHolderType parentType;
    private SensorType sensorType;
    private String parentId;
    private SensorVMHolder sensorViewModel;

    public SensorDetailFragment() {
        // Required empty public constructor
    }

    public static SensorDetailFragment newInstance(String parentId, SensorHolderType parentType, SensorType sensorType) {
        SensorDetailFragment fragment = new SensorDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENT_ID, parentId);
        args.putString(ARG_SENSOR_TYPE, sensorType.toString());
        args.putString(ARG_PARENT_TYPE, parentType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your SensorViewModel
        if (getArguments() != null) {
            parentId = getArguments().getString(ARG_PARENT_ID);
            sensorType = SensorType.valueOf(getArguments().getString(ARG_SENSOR_TYPE));
            parentType = SensorHolderType.valueOf(getArguments().getString(ARG_PARENT_TYPE));
        }
        
        if (parentType == SensorHolderType.GREENHOUSE) {
            sensorViewModel = new ViewModelProvider(requireActivity()).get(GreenhouseViewModel.class);
        } else if (parentType == SensorHolderType.PLANT) {
            sensorViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor_details, container, false);

        TextView textViewSensorTypeDetails = view.findViewById(R.id.textViewSensorTypeDetails);
        TextView textViewSensorValueDetails = view.findViewById(R.id.textViewSensorValueDetails);
        TextView textViewSensorDateDetails = view.findViewById(R.id.textViewSensorDateDetails);

        textViewSensorTypeDetails.setText(sensorType.toString());

        sensorViewModel.getSensorsObjectByType(parentId, sensorType).observe(getViewLifecycleOwner(), sensorDataObjects -> {
            int lastSensorDataIndex = sensorDataObjects.size() - 1;
            SensorDataObject lastSensor = sensorDataObjects.get(lastSensorDataIndex);

            textViewSensorValueDetails.setText(String.format("%s", lastSensor.getMeasurement()));
            textViewSensorDateDetails.setText(lastSensor.getTimestamp());
        });


        textViewSensorTypeDetails.setText(sensorType.toString());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}