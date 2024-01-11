package pt.uc.dei.cm.plantsmc.view.plant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseHolder;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantsHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditPlantFragment extends Fragment {


    private static final String ARG_PLANT = "arg_plant";
    private Plant plant;
    private PlantsHolder parent;
    private EditText plantNameEditText;
    private EditText plantSpecieEditText;
    private UserViewModel userViewModel;

    public EditPlantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        /*assert getParentFragment() != null;
        Fragment parentFragment=getParentFragment();*/

        assert getActivity().getSupportFragmentManager().findFragmentByTag("GreenhouseDetail")!=null;
        Fragment parentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("GreenhouseDetail");


        if (parentFragment instanceof PlantsHolder) {
            parent = (PlantsHolder) parentFragment;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PlantsHolder");
        }
    }

    public static EditPlantFragment newInstance(@Nullable Plant plant) {
        EditPlantFragment fragment = new EditPlantFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLANT, plant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plant = (Plant) getArguments().getSerializable(ARG_PLANT);
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up views
        Button addPlantButton = view.findViewById(R.id.savePlantButton);
        plantNameEditText = view.findViewById(R.id.plantNameEditText);
        plantSpecieEditText = view.findViewById(R.id.plantSpecieEditText);

        if (plant != null) {
            plantNameEditText.setText(plant.getName());
        }

        // Set up listeners
        addPlantButton.setOnClickListener(v -> {
            String plantName = plantNameEditText.getText().toString();
            if (plantName.isEmpty()) {
                plantNameEditText.setError("Please enter a name for the plant");
                return;
            }

            String plantSpecie = plantSpecieEditText.getText().toString();
            if (plantSpecie.isEmpty()) {
                plantSpecieEditText.setError("Please enter a specie for the plant");
                return;
            }

            // Create plant object
            this.plant = new Plant(
                    plantName,
                    plantSpecie);

            parent.savePlant(plant);

            // Pop the current fragment
            getParentFragmentManager().popBackStack();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_plant, container, false);
    }

    public void setParent(PlantsHolder parent) {
        this.parent = parent;
    }
}