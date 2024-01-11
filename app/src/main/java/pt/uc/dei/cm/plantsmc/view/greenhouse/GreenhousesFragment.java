package pt.uc.dei.cm.plantsmc.view.greenhouse;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;

public class GreenhousesFragment extends Fragment {

    private GreenhouseViewModel viewModel;
    private GreenhouseHolder parent;
    private GreenhouseAdapter adapter;

    public GreenhousesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GreenhouseHolder) {
            parent = (GreenhouseHolder) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GreenhouseHolder");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_greenhouses, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.greenhouseRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,LinearLayoutManager.VERTICAL,false));
        adapter = new GreenhouseAdapter(parent);
        recyclerView.setAdapter(adapter);

        // Add greenhouse button
        setup_add_greenhouse(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getGreenhouses().observe(getViewLifecycleOwner(), greenhouses -> {
            // Update UI with the list of greenhouses
            adapter.setGreenhouses(greenhouses);
            adapter.notifyDataSetChanged();
        });
    }

    private void setup_add_greenhouse(View view) {
        Button addGreenhouseButton = view.findViewById(R.id.addGreenhouseButton);
        addGreenhouseButton.setOnClickListener(v -> {

            parent.onAddGreenhouse();
        });
    }
}
