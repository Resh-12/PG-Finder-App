package com.example.payingguest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.payingguest.R;
import com.example.payingguest.adapter.BookAdapter;
import com.example.payingguest.model.BookModel;
import com.example.payingguest.model.homesInFeedModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookedPg extends Fragment {

    BookAdapter bookAdapter;
    TextView t;
   RecyclerView recyclerView;
   DatabaseReference data;
   ArrayList<BookModel> list;
   FirebaseAuth auth;
   FirebaseUser user;


    public BookedPg() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_booked_pg, container, false);
        recyclerView=v.findViewById(R.id.bookrecycler);
        t=v.findViewById(R.id.txtbooked);

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        data= FirebaseDatabase.getInstance().getReference("Booked_pg");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=new ArrayList<>();
        bookAdapter=new BookAdapter(getActivity(),list);
        recyclerView.setAdapter(bookAdapter);
        String userid=getCurrentUser();
        data.orderByChild("user").equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    BookModel pg=dataSnapshot.getValue(BookModel.class);
                    list.add(pg);
                    t.setVisibility(v.INVISIBLE);
                }
                bookAdapter.notifyDataSetChanged();
//                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  v;
    }

    public String getCurrentUser() {

            user=auth.getCurrentUser();
            if(user!=null){
                return user.getEmail();
            }else{
                return null;
            }

    }
}