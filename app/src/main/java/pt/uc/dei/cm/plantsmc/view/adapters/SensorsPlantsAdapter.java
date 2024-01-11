package pt.uc.dei.cm.plantsmc.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.SensorP;

public class SensorsPlantsAdapter extends RecyclerView.Adapter<SensorsPlantsAdapter.ViewHolder> {

    private List<SensorP> sensorgreenhouseList;
    private SensorsPlantsHolder parent;

    public SensorsPlantsAdapter(SensorsPlantsHolder parent) {
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
        SensorP sensorData = sensorgreenhouseList.get(position);
        holder.SensorNameTextView.setText(sensorData.getType());

        if (sensorData.getType().equals("Temperature"))
            holder.image_view.setBackgroundResource(R.drawable.thermometer_temperature_svgrepo_com);
        else if (sensorData.getType().equals("Humidity"))
            holder.image_view.setBackgroundResource(R.drawable.baseline_water_24);
        if (parent!=null){
            Log.d("parent not null","not null");
        }
        else
            Log.d("parent null","null");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onSensorClickP(sensorData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sensorgreenhouseList.size();
    }

    public void setSensors(List<SensorP> sensors) {
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