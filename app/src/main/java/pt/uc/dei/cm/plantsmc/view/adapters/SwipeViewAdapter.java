package pt.uc.dei.cm.plantsmc.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.view.plant.PlantsFragment;
import pt.uc.dei.cm.plantsmc.view.sensors.SensorsFragment;

public class SwipeViewAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2; // Change this to the number of fragments you want
    private String greenhouseID;
    public SwipeViewAdapter(Fragment fragment, String greenhouseID) {

        super(fragment);
        this.greenhouseID=greenhouseID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int).
        Fragment fragment;
        if (position==0){
            fragment = SensorsFragment.newInstance(greenhouseID, SensorHolderType.GREENHOUSE);
        }
        else {
            fragment = PlantsFragment.newInstance(greenhouseID);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}