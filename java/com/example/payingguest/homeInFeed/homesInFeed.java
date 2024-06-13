package com.example.payingguest.homeInFeed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.payingguest.R;
import com.example.payingguest.interfaces.homesInFeedInterface;

public class homesInFeed extends RecyclerView.ViewHolder implements View.OnClickListener{
public TextView txtrent,txtroomtype,txtloc,txtcity;
    public homesInFeedInterface listener;
public ImageView HIFpic;
    public homesInFeed(@NonNull View itemView) {
        super(itemView);
        txtrent=(TextView) itemView.findViewById(R.id.txtrent);
        txtroomtype=(TextView) itemView.findViewById(R.id.txttype);
        txtloc=(TextView) itemView.findViewById(R.id.txtloc);
        txtcity=(TextView) itemView.findViewById(R.id.txtcity);
        HIFpic=(ImageView) itemView.findViewById(R.id.homePic);
    }
    public void setItemClickListener(homesInFeedInterface listener)
    {
        this.listener=  listener;

    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(), false);
    }
}
