package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.payingguest.model.homesInFeedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HomeDetails extends AppCompatActivity {
private TextView addloc,addcity,addgender,addrent,adddepo,addshare,addmeals,addamen,addguests,addfood,adddesc;
   private ImageView HDhomePic;
    String homeID="",imageurl;
    String number;
    DatabaseReference pg;
    FirebaseAuth auth;
    FirebaseUser user;
    AppCompatButton btcall,btbook;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);
        addloc=findViewById(R.id.txtaddloc);
        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        addcity=findViewById(R.id.txtaddcity);
        addgender=findViewById(R.id.txtaddgender);
        addrent=findViewById(R.id.txtaddrent);
        adddepo=findViewById(R.id.txtadddepo);
        addshare=findViewById(R.id.txtaddsharing);
        addmeals=findViewById(R.id.txtaddmeals);
        addamen=findViewById(R.id.txtaddamenities);
        addguests=findViewById(R.id.txtaddguests);
        addfood=findViewById(R.id.txtaddfood);
        adddesc=findViewById(R.id.txtadddesc);
        HDhomePic=findViewById(R.id.HDhomePic);
        btcall=findViewById(R.id.btncall);
        btbook=findViewById(R.id.btnbook);
        homeID=getIntent().getStringExtra("pId");
        getHomeDetails(homeID);
        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            number=btcall.getText().toString();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
            }
        });
        btbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uids=getCurrentUser();
                pg = FirebaseDatabase.getInstance().getReference().child("Rent_posts");
                pg.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("pId").getValue() != null) {
                                if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(homeID)){
                                    homesInFeedModel pgs = dataSnapshot.getValue(homesInFeedModel.class);
                                   if(pgs.getUser().equals(uids)){
                                       btbook.setEnabled(false);
                                       btbook.setAlpha(0.5f);
                                   }else {

                                       Intent i = new Intent(HomeDetails.this, confirm_rent.class);
                                       i.putExtra("pId", pgs.getpId());
//                                    i.putExtra("Rent",pgs.getMonthlyRent());
//                                    i.putExtra("Type",pgs.getRoomType());
                                       startActivity(i);
                                   }
                                    break;
                                }
                            }
                        }
//                        return false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

        private void getHomeDetails (String homeID){


//        Toast.makeText(this, "toast msg", Toast.LENGTH_SHORT).show();
            DatabaseReference homeRef = FirebaseDatabase.getInstance().getReference().child("Rent_posts");
            homeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if(dataSnapshot.child("pId").getValue()!=null){
                            if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(homeID)) {
                                homesInFeedModel home = dataSnapshot.getValue(homesInFeedModel.class);
                                addloc.setText(home.getLocality());
                                addcity.setText(home.getCity());
                                addgender.setText(home.getGender());
                                addrent.append(home.getMonthlyRent());
                                adddepo.append(home.getSecurityDeposit());
                                addshare.setText(home.getSharetype());
                                addmeals.setText(home.getMeals());
                                addamen.setText(home.getAmenities());
                                addguests.setText(home.getGuestsAllowed());
                                addfood.setText(home.getFoodPreference());
                                adddesc.setText(home.getDescription());
                                btcall.setText(home.getContactNo());
                                Picasso.get().load(home.getImage()).into(HDhomePic);
                                break;
                            }
                        }
//                        Toast.makeText(HomeDetails.this, ""+dataSnapshot.child("pId").getValue(), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editpg, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editMenuItem = menu.findItem(R.id.editpg);
        MenuItem removeMenuItem = menu.findItem(R.id.remove);

        // Add your conditions here
        String uids = getCurrentUser();
        pg = FirebaseDatabase.getInstance().getReference().child("Rent_posts");
        pg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("pId").getValue() != null) {
                        if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(homeID)) {
                            homesInFeedModel pgs = dataSnapshot.getValue(homesInFeedModel.class);
                            if (pgs.getUser().equals(uids)) {
                                // Enable or disable the menu items based on your condition
                                editMenuItem.setEnabled(true); // Enable edit menu item
                                removeMenuItem.setEnabled(true); // Disable remove menu item
                            } else {
                                // Enable or disable the menu items based on your condition
                                editMenuItem.setEnabled(false); // Disable edit menu item
                                removeMenuItem.setEnabled(false); // Enable remove menu item
                            }
                           // break; // Assuming you want to apply the conditions only to the first matching item
                        }
                    }
                }
                // Call invalidateOptionsMenu() to trigger a redraw of the options menu
         //       invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if necessary
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editpg:

                Intent j=new Intent(HomeDetails.this,Editpg.class);
                j.putExtra("pId",homeID);
                startActivity(j);
                break;
            case R.id.remove:removePGFromFirebase();

                break;
        }
        return true;
    }
    private void removePGFromFirebase() {
        DatabaseReference pgRef = FirebaseDatabase.getInstance().getReference().child("Rent_posts");

        pgRef.orderByChild("pId").equalTo(homeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Removal successful
                                        Toast.makeText(HomeDetails.this, "PG removed successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(HomeDetails.this,HomeActivity.class));
                                        // Perform any additional actions or navigate to a different activity if needed
                                        finish(); // Finish the activity to remove it from the list
                                    } else {
                                        // Error occurred while removing PG
                                        Toast.makeText(HomeDetails.this, "Failed to remove PG", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                    // Assuming you only want to delete the first matching item
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if necessary
            }
        });
    }





}

