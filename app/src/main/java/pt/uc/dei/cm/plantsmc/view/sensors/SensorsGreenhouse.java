package pt.uc.dei.cm.plantsmc.view.sensors;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantsHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorGreenhouseAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorsGreenhouseHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.SensorsGViewModel;

public class SensorsGreenhouse extends Fragment {

    private static final String ARG_GREENHOUSE_ID = "arg_greenhouse_id";
    private SensorsGViewModel viewModel;
    private SensorsGreenhouseHolder parent;
    private SensorGreenhouseAdapter adapter;
    private String greenhouseId;

    public SensorsGreenhouse() {
        // Required empty public constructor
    }

    public static SensorsGreenhouse newInstance(String greenhouseId) {
        SensorsGreenhouse fragment = new SensorsGreenhouse();
        Bundle args = new Bundle();
        args.putString(ARG_GREENHOUSE_ID, greenhouseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PlantsHolder) {
            parent = (SensorsGreenhouseHolder) parentFragment;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SensorsGreenhouseHolder");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            greenhouseId = getArguments().getString(ARG_GREENHOUSE_ID);
        }
        viewModel = new ViewModelProvider(this).get(SensorsGViewModel.class);
        viewModel.setsensorsByGreenhouse(greenhouseId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.sensorgreenhouseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new SensorGreenhouseAdapter(parent);
        recyclerView.setAdapter(adapter);

        setup_sensors_button(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getSensorsByGreenhouse().observe(getViewLifecycleOwner(), sensors -> {
            // Update UI with the list of plants
            adapter.setSensors(sensors);
            adapter.notifyDataSetChanged();
        });
    }

    private void setup_sensors_button(View view) {
        Button addSensorGButton = view.findViewById(R.id.addSensorGButton);
        addSensorGButton.setOnClickListener(v -> {
            parent.onAddSensor();
        });
    }
}
