package pt.uc.dei.cm.plantsmc.view.greenhouse;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseHolder;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditGreenhouseFragment extends Fragment {


    private static final String ARG_GREENHOUSE = "arg_greenhouse";
    private Greenhouse greenhouse;
    private GreenhouseHolder parent;

    private Button saveGreenhouseButton;
    private EditText greenhouseNameEditText;

    public EditGreenhouseFragment() {
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

        if (greenhouse != null) {
            greenhouseNameEditText.setText(greenhouse.getName());
        }

        // Set up listeners
        saveGreenhouseButton.setOnClickListener(v -> {
            String greenhouseName = greenhouseNameEditText.getText().toString();
            if (greenhouseName.isEmpty()) {
                greenhouseNameEditText.setError("Please enter a name for the greenhouse");
            } else {

                if (this.greenhouse == null) {
                    this.greenhouse = new Greenhouse(greenhouseName);

                } else {
                    this.greenhouse.setName(greenhouseName);
                }
                if (greenhouse!=null)
              Log.d("greenhouse is not null","greenhouse");
                else
                    Log.d("greenhouse is  null","greenhouse");

                if (parent!=null)
                    Log.d("parent is not null","greenhouse");
                else
                    Log.d("parent is  null","greenhouse");

                parent.saveGreenhouse(greenhouse);

                // Pop the current fragment
                getParentFragmentManager().popBackStack();
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