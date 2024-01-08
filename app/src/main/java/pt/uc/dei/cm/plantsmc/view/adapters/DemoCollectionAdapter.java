package pt.uc.dei.cm.plantsmc.view.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pt.uc.dei.cm.plantsmc.view.greenhouse.DemoObjectFragment;
import pt.uc.dei.cm.plantsmc.view.greenhouse.fPlantsObjectFragment;
import pt.uc.dei.cm.plantsmc.view.plant.PlantsFragment;

public class DemoCollectionAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2; // Change this to the number of fragments you want
    private String greenhouseID;
    public DemoCollectionAdapter(Fragment fragment, String greenhouseID) {

        super(fragment);
        this.greenhouseID=greenhouseID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int).
        Fragment fragment = null;
        if (position==0){
            fragment = new DemoObjectFragment();
        Bundle args = new Bundle();
        // The object is just an integer.
        args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);}
        else if (position==1){
            //PlantsFragment plantsFragment = PlantsFragment.newInstance(greenhouse.getId());
            fragment = PlantsFragment.newInstance(greenhouseID);
            //Bundle args = new Bundle();
            // The object is just an integer.
            //args.putInt(fPlantsObjectFragment.ARG_OBJECT, position + 1);
            //fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}