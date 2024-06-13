package com.example.payingguest.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payingguest.HomeActivity;
import com.example.payingguest.R;
import com.example.payingguest.adapter.WishlistAdapter;
import com.example.payingguest.model.BookModel;
import com.example.payingguest.model.homesInFeedModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class WishFrag extends Fragment implements WishlistAdapter.OnDeleteClickListener{

RecyclerView recyclerwish;
DatabaseReference data;
WishlistAdapter wishlistAdapter;
ArrayList<homesInFeedModel> list;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView t;

    public WishFrag() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wish, container, false);
        recyclerwish=view.findViewById(R.id.wishrecyler);
        t=view.findViewById(R.id.l);
        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        data= FirebaseDatabase.getInstance().getReference().child("wish");
        recyclerwish.setHasFixedSize(true);
        recyclerwish.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=new ArrayList<>();
        wishlistAdapter =new WishlistAdapter(getActivity(),list);
        wishlistAdapter.setOnDeleteClickListener(this);
        recyclerwish.setAdapter(wishlistAdapter);
        String userid=getCurrentUser();
        data.orderByChild("user").equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    homesInFeedModel pg=dataSnapshot.getValue(homesInFeedModel.class);
                    list.add(pg);
                    t.setVisibility(view.INVISIBLE);
                }
                wishlistAdapter.notifyDataSetChanged();
//                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    public String getCurrentUser() {

        user=auth.getCurrentUser();
        if(user!=null){
            return user.getEmail();
        }else{
            return null;
        }

    }


    @Override
    public void onDeleteClick(int position) {

            homesInFeedModel item = list.get(position);
            String itemId = item.getpId();

            // Remove the item from Firebase
            DatabaseReference itemRef = data.child(itemId);
            list.remove(item);
            itemRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "removed from wishlist", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    });

    }
}