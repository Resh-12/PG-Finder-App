package com.example.payingguest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payingguest.HomeDetails;
import com.example.payingguest.R;
import com.example.payingguest.model.BookModel;
import com.example.payingguest.model.homesInFeedModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    ArrayList<homesInFeedModel> list;

    public PostAdapter(Context context, ArrayList<homesInFeedModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi= LayoutInflater.from(context).inflate(R.layout.posted_design,parent,false);
        return new MyViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        homesInFeedModel pg = list.get(position);
        holder.prent.setText(pg.getMonthlyRent());
        holder.ptype.setText(pg.getRoomType());
        holder.pcity.setText(pg.getCity());
        Picasso.get().load(pg.getImage()).into(holder.postpic);
        if (pg.isAvailable()) {
            holder.itemView.setEnabled(true); // Enable the card view
            holder.itemView.setAlpha(1f); // Optionally, reset the opacity of the card view
            holder.pavail.setVisibility(View.GONE); // Hide the "unavailable" text

        } else {
            holder.itemView.setEnabled(true); // Disable the card view
            holder.itemView.setAlpha(0.5f); // Optionally, reduce the opacity of the card view
            holder.pavail.setVisibility(View.VISIBLE); // Show the "unavailable" text
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
TextView prent,ptype,pcity,pavail,puser;
ImageView postpic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prent=itemView.findViewById(R.id.prent);
            ptype=itemView.findViewById(R.id.ptype);
            pcity=itemView.findViewById(R.id.pcity);
            postpic=itemView.findViewById(R.id.postpic);
            pavail=itemView.findViewById(R.id.pavail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, HomeDetails.class);
            intent.putExtra("pId", list.get(position).getpId());
            context.startActivity(intent);
        }
    }
}
