package pt.uc.dei.cm.plantsmc.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Plant;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private List<Plant> plantList;
    private PlantsHolder parent;

    public PlantAdapter(PlantsHolder parent) {
        plantList = new ArrayList<>();
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.plantNameTextView.setText(plant.getName());

        holder.imageView.setImageResource(R.drawable.field1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onPlantClick(plant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void setPlants(List<Plant> plants) {
        if (plants != null) {
            this.plantList = plants;
        } else {
            this.plantList = new ArrayList<>(); // If the input list is null, initialize to an empty list
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView plantNameTextView;
        ShapeableImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            imageView=itemView.findViewById(R.id.field_image_plant);
            // Initialize other views from the item layout if they exist
        }
    }


}
