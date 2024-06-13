package com.example.payingguest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payingguest.HomeDetails;
import com.example.payingguest.R;
import com.example.payingguest.interfaces.WishlistListener;
import com.example.payingguest.model.homesInFeedModel;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
    Context context;
    ArrayList<homesInFeedModel> list;
    private List<homesInFeedModel> wishList;
    private WishlistListener wishlistListener;
//    private List<String> bookedPids;


    public HomeAdapter(Context context, ArrayList<homesInFeedModel> list) {

        this.context = context;
        this.list = list;
        this.wishlistListener = (WishlistListener) context;
        this.wishList = new ArrayList<>();
//        this.bookedPids = new ArrayList<>();
    }
//public HomeAdapter(Context context, ArrayList<homesInFeedModel> list, WishlistListener listener) {
//    this.context = context;
//    this.list = list;
//    this.listener = listener;
//}


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi= LayoutInflater.from(context).inflate(R.layout.home_in_feed_design,parent,false);
        return new MyViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        homesInFeedModel home = list.get(position);
        holder.pgrent.setText(home.getMonthlyRent());
        holder.pgtype.setText(home.getRoomType());
        holder.pgloc.setText(home.getLocality());
        holder.pgcity.setText(home.getCity());
        Picasso.get().load(home.getImage()).into(holder.pgpic);
//        boolean isBooked = bookedPids.contains(home.getpId());
//        holder.itemView.setEnabled(!isBooked);
//        holder.itemView.setAlpha(isBooked ? 0.5f : 1.0f);
//
//        boolean isBooked = model.isBooked();
//        if (isBooked) {
//            holder.itemView.setEnabled(false);
//            // You can also change the appearance of the itemView to indicate that it is disabled
//        } else {
//            holder.itemView.setEnabled(true);
//            // Set the normal appearance of the itemView
//        }
        if (home.isAvailable()) {
            holder.itemView.setEnabled(true); // Enable the card view
            holder.itemView.setAlpha(1f); // Optionally, reset the opacity of the card view
            holder.pgavailable.setVisibility(View.GONE); // Hide the "unavailable" text

        } else {
            holder.itemView.setEnabled(false); // Disable the card view
            holder.itemView.setAlpha(0.5f); // Optionally, reduce the opacity of the card view
            holder.pgavailable.setVisibility(View.VISIBLE); // Show the "unavailable" text
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
        TextView pgrent,pgtype,pgloc,pgcity,pgavailable;
        ImageView pgpic;
        CheckBox wishadd;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pgrent=itemView.findViewById(R.id.txtrent);
           pgtype =itemView.findViewById(R.id.txttype);
           pgloc=itemView.findViewById(R.id.txtloc);
           pgcity=itemView.findViewById(R.id.txtcity);
           pgpic=itemView.findViewById(R.id.homePic);
           wishadd=itemView.findViewById(R.id.cbwish);
           pgavailable=itemView.findViewById(R.id.avail);
            wishadd.setOnCheckedChangeListener(this);
           itemView.setOnClickListener(this);

//

        }


        @Override
        public void onClick(View v) {
           int position=getAdapterPosition();
            Intent intent=new Intent(context, HomeDetails.class);
                      intent.putExtra("pId",list.get(position).getpId());
                       context.startActivity(intent);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getAdapterPosition();
            homesInFeedModel home = list.get(position);

            if (isChecked) {
//
                wishlistListener.onItemAddedToWishlist(home);
                wishList.add(home);
            } else {
//
                wishlistListener.onItemRemovedFromWishlist(home);
                wishList.remove(home);
            }
        }
    }
    public void updateData(List<homesInFeedModel> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }
    public interface WishlistListener {
        void onItemAddedToWishlist(homesInFeedModel item);
        void onItemRemovedFromWishlist(homesInFeedModel item);
    }
}
