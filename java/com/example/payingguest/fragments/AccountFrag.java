package com.example.payingguest.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payingguest.Aboutus;
import com.example.payingguest.Editprofile;
import com.example.payingguest.Login;
import com.example.payingguest.MyPg;
import com.example.payingguest.R;

import com.example.payingguest.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFrag extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    private static final String USERS="users";
    String email;
    Intent intent;
    StorageReference storage;
    TextView mypg;
    TextView log;
    TextView ed;
    String username;
    TextView usermail, cuname,about;
    CircleImageView profile;
    //  ShapeableImageView profile;
    private Uri uriImage;
    ProgressDialog p;
    //private String username,email;
    FirebaseUser user;
    String usernamev, uid;
    DataSnapshot dt;
   // DatabaseReference reference;

    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mypg = view.findViewById(R.id.mypg);
        log = view.findViewById(R.id.logout);
        ed = view.findViewById(R.id.editprofile);
        usermail = view.findViewById(R.id.umail);
        profile = view.findViewById(R.id.userprofile);
        about=view.findViewById(R.id.aboutus);
        //cuname = view.findViewById(R.id.uname);
        user = FirebaseAuth.getInstance().getCurrentUser();
        usermail.setText(user.getEmail());
        database=FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        usermail.setText(user.getEmail());
        mAuth = FirebaseAuth.getInstance();
        uid = user.getUid();
        p = new ProgressDialog(getActivity());
        mypg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyPg.class);
                getActivity().startActivity(i);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k=new Intent(getActivity(), Aboutus.class);
                getActivity().startActivity(k);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setMessage("Logging out");
                p.setCanceledOnTouchOutside(false);
                p.show();
                mAuth.signOut();
                p.dismiss();
                Intent j = new Intent(getActivity(), Login.class);
                getActivity().startActivity(j);
            }
        });
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getActivity(), Editprofile.class);
                getActivity().startActivity(k);

            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "User details not availabe", Toast.LENGTH_LONG).show();
        } else {
            showUserProfile(firebaseUser);
        }
        return view;



    }
    public void showUserProfile (FirebaseUser firebaseUser){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference("DisplayPics");
        Uri uri = firebaseUser.getPhotoUrl();
        //set user current DP in imageView(if uploades already)
        Picasso.get().load(uri).into(profile);

       /* userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        DataSnapshot ds = task.getResult();
                        String cunames = String.valueOf(ds.child("name").getValue());
                        cuname.setText(cunames);
                    }
                }
            }
        });*/
    }



}
