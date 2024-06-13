package com.example.payingguest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payingguest.HomeDetails;
import com.example.payingguest.R;
import com.example.payingguest.model.homesInFeedModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {
    Context context;
    ArrayList<homesInFeedModel> list;
    private OnDeleteClickListener onDeleteClickListener;


    public WishlistAdapter(Context context,ArrayList<homesInFeedModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.wishlist_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        homesInFeedModel wish=list.get(position);
        holder.wrent.setText(wish.getMonthlyRent());
        holder.wtype.setText(wish.getRoomType());
        holder.wloc.setText(wish.getLocality());
        holder.wcity.setText(wish.getCity());
        Picasso.get().load(wish.getImage()).into(holder.wpic);
        if (wish.isAvailable()) {
            holder.itemView.setEnabled(true); // Enable the card view
            holder.itemView.setAlpha(1f);
            holder.pgavailable.setVisibility(View.GONE); // Hide the "unavailable" text

        } else {
            holder.itemView.setEnabled(false); // Disable the card view
            holder.itemView.setAlpha(0.5f);
            holder.pgavailable.setVisibility(View.VISIBLE); // Show the "unavailable" text
            holder.delete.setEnabled(true);
            holder.delete.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView wrent,wtype,wloc,wcity,pgavailable;
        ImageView wpic;
        ImageButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            wrent=itemView.findViewById(R.id.wishrent);
            wtype=itemView.findViewById(R.id.wishtype);
            wloc=itemView.findViewById(R.id.wishloc);
            wcity=itemView.findViewById(R.id.wishcity);
            wpic=itemView.findViewById(R.id.wishPic);
            delete=itemView.findViewById(R.id.delete);
            pgavailable=itemView.findViewById(R.id.wavail);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (v.getId() == R.id.delete) {
                    // Delete button clicked
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                } else {
                    // Item view clicked
                    Intent intent = new Intent(context, HomeDetails.class);
                    intent.putExtra("pId", list.get(position).getpId());
                    context.startActivity(intent);
                }
            }
        }
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    // Method to set the delete button click listener
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

}
