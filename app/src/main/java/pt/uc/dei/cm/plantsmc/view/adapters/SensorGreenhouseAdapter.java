package pt.uc.dei.cm.plantsmc.view.adapters;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Plant;
import pt.uc.dei.cm.plantsmc.model.SensorG;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;

public class SensorGreenhouseAdapter extends RecyclerView.Adapter<SensorGreenhouseAdapter.ViewHolder> {

    private List<SensorG> sensorgreenhouseList;
    private SensorsGreenhouseHolder parent;



    public SensorGreenhouseAdapter(SensorsGreenhouseHolder parent) {
        sensorgreenhouseList = new ArrayList<>();
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.greenhouse_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SensorG sensorData = sensorgreenhouseList.get(position);
        holder.SensorNameTextView.setText(sensorData.getType());

        if (sensorData.getType().equals("Temperature"))
        holder.image_view.setBackgroundResource(R.drawable.thermometer_temperature_svgrepo_com);
        else if (sensorData.getType().equals("Humidity"))
            holder.image_view.setBackgroundResource(R.drawable.baseline_water_24);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parent.onSensorClick(sensorData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sensorgreenhouseList.size();
    }

    public void setSensors(List<SensorG> sensors) {
        if (sensors != null) {
            this.sensorgreenhouseList = sensors;
        } else {
            this.sensorgreenhouseList = new ArrayList<>(); // If the input list is null, initialize to an empty list
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView SensorNameTextView;
        View image_view;

        public ViewHolder(View itemView) {
            super(itemView);
            SensorNameTextView = itemView.findViewById(R.id.SensorGName);
            image_view=itemView.findViewById(R.id.image_sensor);
            // Initialize other views from the item layout if they exist
        }
    }


}
