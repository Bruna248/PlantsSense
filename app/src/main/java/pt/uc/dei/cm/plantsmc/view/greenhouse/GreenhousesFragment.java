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
import android.widget.TextView;

import java.util.Objects;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

public class GreenhousesFragment extends Fragment {

    private GreenhouseViewModel greenhouseViewModel;
    private UserViewModel userViewModel;

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
        greenhouseViewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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

        setup_fragment_title(view);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        greenhouseViewModel.getGreenhouses().observe(getViewLifecycleOwner(), greenhouses -> {
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

    private void setup_fragment_title(View view) {
        TextView titleTextView = view.findViewById(R.id.textViewHelloUser);

        String[] parsedEmail = Objects.requireNonNull(userViewModel.getCurrentUser()
                .getValue())
                .getEmail()
                .split("@");

        String title = String.format("Hello, %s!", parsedEmail[0]);
        titleTextView.setText(title);
    }
}
