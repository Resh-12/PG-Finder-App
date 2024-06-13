package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.payingguest.model.homesInFeedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Objects;

public class confirm_rent extends AppCompatActivity {
EditText rname,address,contno;
AppCompatButton confirm;
String nam,addrss,numb;
    String mpattern="[0-9]{10}";
    DatabaseReference pgref;
    String pId;
FirebaseAuth auth;
FirebaseUser user;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_rent);
        rname=findViewById(R.id.rname);
        address=findViewById(R.id.raddress);
        contno=findViewById(R.id.rcontact);
        confirm=findViewById(R.id.confirm);
        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        pId=getIntent().getStringExtra("pId");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect();
            }
        });
    }

    private void collect() {
        nam=rname.getText().toString();
        addrss=address.getText().toString();
        numb=contno.getText().toString();
//        boolean isValid = isValidPhoneNumber(numb);
        if(nam.isEmpty()){
            rname.setError("Enter your name");
        } else if (addrss.isEmpty()) {
            address.setError("Please provide your address");
        } else if (!numb.matches(mpattern)) {
            contno.setError("Enter valid phone number");
        } else{
            storedata();
        }
    }

    private void storedata() {
        DatabaseReference rentRef= FirebaseDatabase.getInstance().getReference().child("ConfirmRent");

        HashMap<String,Object> map= new HashMap<>();
        map.put("RenterName",nam);
        map.put("ContactNo",numb);
        map.put("Address",addrss);
        // map.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        rentRef.child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pgref= FirebaseDatabase.getInstance().getReference().child("Rent_posts");
                    pgref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                if (dataSnapshot.child("pId").getValue() != null) {
                                    if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(pId)) {
                                        Intent intent = new Intent(confirm_rent.this, payment.class);
                                        intent.putExtra("pId", pg.getpId());
                                        intent.putExtra("rent",pg.getMonthlyRent());
                                        intent.putExtra("type",pg.getRoomType());
                                        intent.putExtra("amount",pg.getSecurityDeposit());
                                        intent.putExtra("bookimage",pg.getImage());
                                        startActivity(intent);
                                        finish();

//
//                                         pgref.child(pg.getpId()).setValue(pg);
//                                         Intent intent = new Intent(confirm_rent.this, payment.class);
//                                         intent.putExtra("pId", pg.getpId()); // Pass pId as an extra to the intent
//                                         startActivity(intent);
//                                         finish();
                                        break;
                                    }
                                }
                            }
//                            return false;
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(confirm_rent.this, "Error retrieving pg", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(confirm_rent.this, "Error storing data", Toast.LENGTH_SHORT).show();
                }
//                    Intent intent=new Intent(confirm_rent.this, payment.class);
//
//                       startActivity(intent);

            }




        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(confirm_rent.this, "error", Toast.LENGTH_SHORT).show();
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
//    public boolean isValidPhoneNumber(String phoneNumber) {
//        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        try {
//            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "IN");
//            return phoneNumberUtil.isValidNumber(parsedNumber);
//        } catch (Exception e) {
//            return false;
//        }
//    }
}