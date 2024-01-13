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
import pt.uc.dei.cm.plantsmc.model.Greenhouse;

public class GreenhouseAdapter extends RecyclerView.Adapter<GreenhouseAdapter.ViewHolder> {

    private List<Greenhouse> greenhouseList;
    private GreenhouseViewHolder parent;

    public GreenhouseAdapter(GreenhouseViewHolder parent) {
        greenhouseList = new ArrayList<>();
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.greenhouse_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Greenhouse greenhouse = greenhouseList.get(position);
        holder.greenhouseNameTextView.setText(greenhouse.getName());
        holder.imageView.setImageResource(R.drawable.field1);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onGreenhouseClick(greenhouse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return greenhouseList.size();
    }

    public void setGreenhouses(List<Greenhouse> greenhouses) {
        if (greenhouses != null) {
            this.greenhouseList = greenhouses;
        } else {
            this.greenhouseList = new ArrayList<>(); // If the input list is null, initialize to an empty list
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView greenhouseNameTextView;

        ShapeableImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            greenhouseNameTextView = itemView.findViewById(R.id.field_name);
            imageView=itemView.findViewById(R.id.field_image);
            // Initialize other views from the item layout if they exist
        }
    }


}
