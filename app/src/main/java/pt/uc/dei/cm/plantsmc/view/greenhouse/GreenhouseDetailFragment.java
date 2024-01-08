package pt.uc.dei.cm.plantsmc.view.greenhouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.Serializable;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.view.adapters.DemoCollectionAdapter;
import pt.uc.dei.cm.plantsmc.view.adapters.PlantsHolder;
import pt.uc.dei.cm.plantsmc.view.plant.EditPlantFragment;
import pt.uc.dei.cm.plantsmc.view.plant.PlantDetailFragment;
import pt.uc.dei.cm.plantsmc.view.plant.PlantsFragment;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GreenhouseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GreenhouseDetailFragment extends Fragment implements PlantsHolder {

    private static final String ARG_GREENHOUSE = "arg_greenhouse";
    private Greenhouse greenhouse;
    private PlantViewModel plantViewModel;
    private UserViewModel userViewModel;

    DemoCollectionAdapter demoCollectionAdapter;
    ViewPager2 viewPager;
    // This method is used to create new instances of the fragment with a greenhouse object
    public static GreenhouseDetailFragment newInstance(Greenhouse greenhouse) {
        GreenhouseDetailFragment fragment = new GreenhouseDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GREENHOUSE, (Serializable) greenhouse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            greenhouse = (Greenhouse) getArguments().getSerializable(ARG_GREENHOUSE);
        }

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Set up the default fragment
        /*if (savedInstanceState == null) {
            PlantsFragment plantsFragment = PlantsFragment.newInstance(greenhouse.getId());
            getChildFragmentManager().beginTransaction().replace(R.id.plantsFragmentContainer, plantsFragment)
                    .commit();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_greenhouse_details, container, false);

        // Set up the data in the views
        if (greenhouse != null) {
            TextView textViewName = view.findViewById(R.id.greenhouseNameTextView);
            textViewName.setText(greenhouse.getName());
        }

        ShapeableImageView imageView= view.findViewById(R.id.field_image);
        imageView.setImageResource(R.drawable.field1);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        demoCollectionAdapter = new DemoCollectionAdapter(this,greenhouse.getId());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(demoCollectionAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position ==0) {
                        // Use the existing tabs from the XML layout
                        tab.setText("Sensors");
                    } else if (position==1) {
                        tab.setText("Plants");
                    } else {
                        // Generate tabs for positions beyond the existing tabs
                        tab.setText("OBJECT " + (position + 1));
                    }
                }
        ).attach();
    }


    @Override
    public void onAddPlant() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
        }

        EditPlantFragment editPlantFragment = EditPlantFragment.newInstance(null);
        getChildFragmentManager().beginTransaction().replace(R.id.plantsFragmentContainer, editPlantFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPlantClick(Plant plant) {
        PlantDetailFragment plantDetailFragment = PlantDetailFragment.newInstance(plant);
        getParentFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, plantDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void savePlant(Plant plant) {

        plant.setGreenhouseId(greenhouse.getId());
        plant.setUserId(userViewModel.getCurrentUser().getValue().getUid());

        plantViewModel.addPlant(plant);

        plantViewModel.getPlantsByGreenhouse().observe(this, plants -> {
            if (plants != null) {
                PlantsFragment newFragment = PlantsFragment.newInstance(greenhouse.getId());
                getChildFragmentManager().beginTransaction().replace(R.id.plantsFragmentContainer, newFragment)
                        .commit();
            }
        });
    }
}