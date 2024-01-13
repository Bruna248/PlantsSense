package pt.uc.dei.cm.plantsmc.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Image;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<Image> images;

    public GalleryAdapter(List<Image> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.greenhouse_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = images.get(position);

        // Load and display the image using Glide or another image loading library
        Glide.with(holder.itemView.getContext())
                .load(image.getDownloadUrl())
                .into(holder.fieldImageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<Image> images) {
        if (images != null) {
            this.images = images;
        } else {
            this.images = new ArrayList<>(); // If the input list is null, initialize to an empty list
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView fieldImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldImageView = itemView.findViewById(R.id.field_image);
        }
    }
}
