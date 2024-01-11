package pt.uc.dei.cm.plantsmc.view.sensors;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.SensorG;
import pt.uc.dei.cm.plantsmc.model.SensorP;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsGreenhouseHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsPlantsHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditSensorPFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_SENSOR = "arg_sensor";
    private SensorP sensor;
    private SensorsPlantsHolder parent;
    private EditText sensorNameEditText;
    private UserViewModel userViewModel;

    private String selectedOption;

    public EditSensorPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        assert getActivity().getSupportFragmentManager().findFragmentByTag("plant_selected_fragment")!=null;
        Fragment parentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("plant_selected_fragment");

        if (parentFragment instanceof SensorsPlantsHolder) {
            parent = (SensorsPlantsHolder) parentFragment;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SensorsGreenhouseHolder");
        }
    }

    public static EditSensorPFragment newInstance(@Nullable SensorP sensorData) {
        EditSensorPFragment fragment = new EditSensorPFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SENSOR, sensorData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sensor = (SensorP) getArguments().getSerializable(ARG_SENSOR);
        }
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerSensorType);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sensorPoptions_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button addSensorButton = view.findViewById(R.id.addSensorButton);
        sensorNameEditText = view.findViewById(R.id.editDescriptionSensor);



        if (sensor != null) {
            sensorNameEditText.setText(sensor.getType());
        }

        // Set up listeners
        addSensorButton.setOnClickListener(v -> {
            if (selectedOption.isEmpty()) {
                sensorNameEditText.setError("Please enter a name for the plant");
                return;
            }

            String SensorDescription = sensorNameEditText.getText().toString();

            // Create plant object
            this.sensor = new SensorP(
                    selectedOption,SensorDescription);

            parent.saveSensorP(sensor);

            // Pop the current fragment
            getParentFragmentManager().popBackStack();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_sensor, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedOption = parent.getItemAtPosition(position).toString();
        if (sensor!=null) {
            sensor.setType(selectedOption);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String selectedOption = "";

    }
}
