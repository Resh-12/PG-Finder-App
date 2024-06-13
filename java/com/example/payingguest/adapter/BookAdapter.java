package com.example.payingguest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payingguest.R;
import com.example.payingguest.model.BookModel;
import com.example.payingguest.model.homesInFeedModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    Context context;
    ArrayList<BookModel> list;

    public BookAdapter(Context context, ArrayList<BookModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi= LayoutInflater.from(context).inflate(R.layout.booked_design,parent,false);
        return new MyViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookModel pg = list.get(position);
        holder.brent.setText(pg.getRent());
        holder.btype.setText(pg.getRoomtype());
        holder.date.setText(pg.getBookedDate());
        holder.bamount.setText(pg.getAmountpaid());
        Picasso.get().load(pg.getImage()).into(holder.bpic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
TextView brent,btype,date,bamount;
ImageView bpic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            brent=itemView.findViewById(R.id.brent);
            btype=itemView.findViewById(R.id.btype);
            date=itemView.findViewById(R.id.bdate);
            bamount=itemView.findViewById(R.id.bamt);
            bpic=itemView.findViewById(R.id.bookPic);
        }
    }
}
