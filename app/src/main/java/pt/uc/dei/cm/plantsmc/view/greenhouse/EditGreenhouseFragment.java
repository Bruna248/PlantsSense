package pt.uc.dei.cm.plantsmc.view.greenhouse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditGreenhouseFragment extends Fragment {


    private static final String ARG_GREENHOUSE = "arg_greenhouse";
    private Greenhouse greenhouse;
    private GreenhouseViewHolder parent;

    private Button saveGreenhouseButton;
    private EditText greenhouseNameEditText;
    private EditText greenhouseLocationEditText;

    public EditGreenhouseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GreenhouseViewHolder) {
            parent = (GreenhouseViewHolder) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GreenhouseHolder");
        }
    }

    public static EditGreenhouseFragment newInstance(Greenhouse greenhouse) {
        EditGreenhouseFragment fragment = new EditGreenhouseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GREENHOUSE, greenhouse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            greenhouse = (Greenhouse) getArguments().getSerializable(ARG_GREENHOUSE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up views
        saveGreenhouseButton = view.findViewById(R.id.saveGreenhouseButton);
        greenhouseNameEditText = view.findViewById(R.id.greenhouseNameEditText);
        greenhouseLocationEditText = view.findViewById(R.id.greenhouseLocationEditText);

        if (greenhouse != null) {
            greenhouseNameEditText.setText(greenhouse.getName());
            greenhouseLocationEditText.setText(greenhouse.getLatitude() + "," + greenhouse.getLongitude());
        }

        // Set up listeners
        saveGreenhouseButton.setOnClickListener(v -> {
            String greenhouseName = greenhouseNameEditText.getText().toString();
            String greenhouseLocation = greenhouseLocationEditText.getText().toString();

            if (greenhouseName.isEmpty()) {
                greenhouseNameEditText.setError("Please enter a name for the greenhouse");
            } else if (greenhouseLocation.isEmpty()) {
                greenhouseLocationEditText.setError("Please enter location coordinates (latitude,longitude)");
            } else {
                // Extract latitude and longitude
                String[] coordinates = greenhouseLocation.split(",");
                if (coordinates.length != 2) {
                    greenhouseLocationEditText.setError("Invalid location coordinates format");
                } else {
                    try {
                        double latitude = Double.parseDouble(coordinates[0].trim());
                        double longitude = Double.parseDouble(coordinates[1].trim());

                        // Create greenhouse object
                        if (this.greenhouse == null) {
                            this.greenhouse = new Greenhouse(greenhouseName, latitude, longitude);
                        } else {
                            this.greenhouse.setName(greenhouseName);
                            this.greenhouse.setLatitude(latitude);
                            this.greenhouse.setLongitude(longitude);
                        }

                        parent.saveGreenhouse(greenhouse);

                        // Pop the current fragment
                        getParentFragmentManager().popBackStack();
                    } catch (NumberFormatException e) {
                        greenhouseLocationEditText.setError("Invalid latitude or longitude format");
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_greenhouse, container, false);
    }
}