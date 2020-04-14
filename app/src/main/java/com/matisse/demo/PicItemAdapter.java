package com.matisse.demo;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.matisse.demo.R;

import java.util.List;

public class PicItemAdapter extends RecyclerView.Adapter<PicItemAdapter.PicViewHolder> {
    private List<Uri> mUris;

    public void setData(List<Uri> uris) {
        mUris = uris;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PicViewHolder holder, int position) {
        Glide.with(holder.itemView).load(mUris.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUris == null ? 0 : mUris.size();
    }

    public class PicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public PicViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
