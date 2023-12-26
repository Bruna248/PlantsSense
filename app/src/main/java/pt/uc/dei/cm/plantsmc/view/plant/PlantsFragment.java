package pt.uc.dei.cm.plantsmc.view.plant;

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
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantsHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;

public class PlantsFragment extends Fragment implements PlantsHolder {

    private static final String ARG_GREENHOUSE_ID = "arg_greenhouse_id";
    private PlantViewModel viewModel;
    private PlantsHolder parent;
    private PlantAdapter adapter;
    private String greenhouseId;

    public PlantsFragment() {
        // Required empty public constructor
    }

    public static PlantsFragment newInstance(String greenhouseId) {
        PlantsFragment fragment = new PlantsFragment();
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
            parent = (PlantsHolder) parentFragment;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PlantsHolder");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            greenhouseId = getArguments().getString(ARG_GREENHOUSE_ID);
        }
        viewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        viewModel.setPlantsByGreenhouse(greenhouseId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plants, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.plantRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlantAdapter(parent);
        recyclerView.setAdapter(adapter);

        setup_plants_button(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getPlantsByGreenhouse().observe(getViewLifecycleOwner(), plants -> {
            // Update UI with the list of plants
            adapter.setPlants(plants);
            adapter.notifyDataSetChanged();
        });
    }

    private void setup_plants_button(View view) {
        Button addPlantButton = view.findViewById(R.id.addPlantButton);
        addPlantButton.setOnClickListener(v -> {

            parent.onAddPlant();
        });
    }

    @Override
    public void onAddPlant() {
        parent.onAddPlant();
    }

    @Override
    public void onPlantClick(Plant plant) {
        parent.onPlantClick(plant);
    }

    @Override
    public void savePlant(Plant plant) {
        parent.savePlant(plant);
    }
}
