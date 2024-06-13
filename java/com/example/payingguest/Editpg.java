package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.payingguest.fragments.PostFrag;
import com.example.payingguest.model.homesInFeedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Editpg extends AppCompatActivity {
ImageView editpic;
AppCompatButton update;
EditText editrent,editsecurity,editfacility,editdesc;
String pid="";
DatabaseReference d,updated;
ProgressDialog p;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpg);
        editpic=findViewById(R.id.editPic);
        editrent=findViewById(R.id.editrent);
        editsecurity=findViewById(R.id.editsecurity);
        editfacility=findViewById(R.id.editfacility);
        editdesc=findViewById(R.id.editdescription);
        update=findViewById(R.id.update);
        p=new ProgressDialog(this);
        pid= getIntent().getStringExtra("pId");
        getPgDetails(pid);
update.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        updatedata();
    }
});
    }

//    private void updatedata() {
//        p.setMessage("Updating");
//        p.show();
//        String updatedRent = editrent.getText().toString().trim();
//        String updatedSecurity = editsecurity.getText().toString().trim();
//        String updatedFacility = editfacility.getText().toString().trim();
//        String updatedDesc = editdesc.getText().toString().trim();
//        updated=FirebaseDatabase.getInstance().getReference().child("Rent_posts").child("pId");
//        updated.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if (dataSnapshot.child("pId").getValue() != null) {
//                        if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(pid)) {
//                            Toast.makeText(Editpg.this, "hi", Toast.LENGTH_SHORT).show();
//                            updated.child("monthlyRent").setValue(updatedRent);
//                            updated.child("securityDeposit").setValue(updatedSecurity);
//                            updated.child("Amenities").setValue(updatedFacility);
//                            updated.child("description").setValue(updatedDesc)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                p.dismiss();
//                                                Toast.makeText(Editpg.this, "PG details updated successfully", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(Editpg.this, PostFrag.class));
//                                                finish();
//                                            } else {
//                                                p.dismiss();
//                                                Toast.makeText(Editpg.this, "Failed to update PG details", Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//
//                                            Toast.makeText(Editpg.this, "failed", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                            break;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
private void updatedata() {
    p.setMessage("Updating");
    p.show();

    String updatedRent = editrent.getText().toString().trim();
    String updatedSecurity = editsecurity.getText().toString().trim();
    String updatedFacility = editfacility.getText().toString().trim();
    String updatedDesc = editdesc.getText().toString().trim();

    DatabaseReference pgRef = FirebaseDatabase.getInstance().getReference().child("Rent_posts");

    pgRef.orderByChild("pId").equalTo(pid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DatabaseReference pgChildRef = dataSnapshot.getRef();
                    pgChildRef.child("monthlyRent").setValue(updatedRent);
                    pgChildRef.child("securityDeposit").setValue(updatedSecurity);
                    pgChildRef.child("Amenities").setValue(updatedFacility);
                    pgChildRef.child("description").setValue(updatedDesc)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    p.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Editpg.this, "PG details updated successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Editpg.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Editpg.this, "Failed to update PG details", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            } else {
                p.dismiss();
                Toast.makeText(Editpg.this, "Selected PG does not exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            p.dismiss();
            Toast.makeText(Editpg.this, "Failed to update PG details", Toast.LENGTH_SHORT).show();
        }
    });
}


    private void getPgDetails(String pid) {
        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Rent_posts");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("pId").getValue() != null) {
                        if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(pid)) {
                            homesInFeedModel home = dataSnapshot.getValue(homesInFeedModel.class);
                            editrent.setText(home.getMonthlyRent());
                            editsecurity.setText(home.getSecurityDeposit());
                            editfacility.setText(home.getAmenities());
                            editdesc.setText(home.getDescription());
                            Picasso.get().load(home.getImage()).into(editpic);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}