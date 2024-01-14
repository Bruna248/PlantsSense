package pt.uc.dei.cm.plantsmc.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.utils.MQTTUtils;
import pt.uc.dei.cm.plantsmc.view.adapters.GreenhouseViewHolder;
import pt.uc.dei.cm.plantsmc.view.auth.LoginActivity;
import pt.uc.dei.cm.plantsmc.view.greenhouse.EditGreenhouseFragment;
import pt.uc.dei.cm.plantsmc.view.greenhouse.GreenhouseDetailFragment;
import pt.uc.dei.cm.plantsmc.view.greenhouse.GreenhousesFragment;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity implements GreenhouseViewHolder {

    private UserViewModel userViewModel;
    private GreenhouseViewModel greenhouseViewModel;
    private PlantViewModel plantViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup view models
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        greenhouseViewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);


        // Set initial data
        init_data();

        // Home button
        setup_home_button();
        // Back button
        setup_back_button();
        // Logout button
        setup_logout();

        MQTTUtils.initializeChannel(this, greenhouseViewModel, plantViewModel, userViewModel);

        // Observers for view models
        userViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if(firebaseUser == null){
                // User is not logged in
                Intent loginActivityIntent = new Intent(this, LoginActivity.class);
                startActivity(loginActivityIntent);
                finish();
            }
        });

        if (savedInstanceState == null) {
            GreenhousesFragment greenhousesFragment = new GreenhousesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, greenhousesFragment)
                    .commit();
        }

    }
    private void setup_back_button() {
        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void setup_home_button() {
        ImageView homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
    }

    private void init_data() {
        userViewModel.setUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void setup_logout() {
        Button logoutButton = findViewById(R.id.button);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            userViewModel.setUser(null);
        });
    }

    @Override
    public void onGreenhouseClick(Greenhouse greenhouse) {
        greenhouseViewModel.setSelectedGreenhouse(greenhouse);

        GreenhouseDetailFragment newFragment = GreenhouseDetailFragment.newInstance(greenhouse);
        getSupportFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, newFragment,"GreenhouseDetail")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveGreenhouse(Greenhouse greenhouse) {
        if (greenhouse.getId()!=null && greenhouse.getUserId()!=null) {
            greenhouseViewModel.updateGreenhouse(greenhouse);
        } else {
            greenhouse.setUserId(userViewModel.getCurrentUser().getValue().getUid());
            greenhouseViewModel.addGreenhouse(greenhouse);
        }
        greenhouseViewModel.getGreenhouses().observe(this, greenhouses -> {
            if (greenhouses != null) {
                GreenhousesFragment newFragment = new GreenhousesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, newFragment)
                        .commit();
            }
        });
    }
    public void onEditGreenhouse(Greenhouse greenhouse) {
        // Check if there are any Fragments
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }

        EditGreenhouseFragment newFragment = EditGreenhouseFragment.newInstance(greenhouse);
        getSupportFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAddGreenhouse() {
        // Check if there are any Fragments
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }

        EditGreenhouseFragment newFragment = new EditGreenhouseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.greenhousesFragmentContainer, newFragment)
                .addToBackStack(null)
                .commit();
    }
}