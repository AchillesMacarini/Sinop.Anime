package com.teknestige.classes;

/**
 * Created by ravi on 26/09/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {
    private Context context;
    private List<com.teknestige.classes.Item> imageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(com.teknestige.sinop.R.id.name);
            description = view.findViewById(com.teknestige.sinop.R.id.description);
            price = view.findViewById(com.teknestige.sinop.R.id.price);
            thumbnail = view.findViewById(com.teknestige.sinop.R.id.thumbnail);
            viewBackground = view.findViewById(com.teknestige.sinop.R.id.view_background);
            viewForeground = view.findViewById(com.teknestige.sinop.R.id.view_foreground);
        }
    }


    public ImageListAdapter(Context context, List<com.teknestige.classes.Item> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.teknestige.sinop.R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final com.teknestige.classes.Item item = imageList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
//        holder.price.setText("₹" + item.getPrice());

        Glide.with(context)
                .load(item.getThumbnail())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void removeItem(int position) {
        imageList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position) {
        imageList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
