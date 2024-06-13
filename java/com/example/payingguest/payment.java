package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payingguest.model.Renters;
import com.example.payingguest.model.homesInFeedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class payment extends AppCompatActivity implements PaymentResultListener {
    AppCompatButton pay;
    String pgId="",amount;
    String rent="",type="",amountpaid="",img="";
    DatabaseReference d;
    DatabaseReference bookpg;
    FirebaseAuth auth;
    FirebaseUser fuser;
    String saveCurrentDate,s;
    ProgressDialog p;

    //String payamt;
    TextView amt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Payment");
        pay=findViewById(R.id.pay);
        p=new ProgressDialog(this);
        d= FirebaseDatabase.getInstance().getReference().child("Rent_posts");
        bookpg=FirebaseDatabase.getInstance().getReference().child("Booked_pg");
        auth=FirebaseAuth.getInstance();
        pgId=getIntent().getStringExtra("pId");
        rent=getIntent().getStringExtra("rent");
        type=getIntent().getStringExtra("type");
        amountpaid=getIntent().getStringExtra("amount");
        img=getIntent().getStringExtra("bookimage");
        amt=findViewById(R.id.amount);
        getHomeDetails(pgId);
        Checkout.preload(getApplicationContext());
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = amt.getText().toString();
                PaymentNow(amount);
            }
        });
    }

    private void getHomeDetails(String pgId) {
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.child("pId").getValue()!=null){
                        if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(pgId)) {
                            homesInFeedModel rent = dataSnapshot.getValue(homesInFeedModel.class);
                             s=rent.getpId();

                            amt.setText(rent.getSecurityDeposit());

                        }}}


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void PaymentNow(String amount) {
        final Activity activity=this;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_jnYiB1HcYw5vmU");
        checkout.setImage(R.mipmap.icon_round);
        double finalAmount=Float.parseFloat(amount)*100;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "PG Finder");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#0D9FDE");
            options.put("currency", "INR");
            options.put("amount", finalAmount+"");//pass amount in currency subunits
            options.put("prefill.email", "");
            options.put("prefill.contact","");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
//        startActivity(new Intent(payment.this,PaymentSuccess.class));
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        UpdateDatabase();
    }

    private void UpdateDatabase() {
        p.setMessage("Processing");
        p.show();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate= currentDate.format(calendar.getTime());
        String userId=getCurrentUserId();
        HashMap<String, Object> map= new HashMap<>();
        map.put("Rent",rent);
        map.put("Roomtype",type);
        map.put("BookedDate",saveCurrentDate);
        map.put("user",userId);
        map.put("Amountpaid",amountpaid);
        map.put("Image",img);
        map.put("pId",s);

        bookpg.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    p.dismiss();
                    updateavail(pgId);
//                    Toast.makeText(payment.this, "success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(payment.this,PaymentSuccess.class));
                }else{
                    p.dismiss();
                    String msg=task.getException().toString();
                    Toast.makeText(payment.this, "Error"+msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void updateavail(String pgId){
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.child("pId").getValue()!=null){
                        if (Objects.requireNonNull(dataSnapshot.child("pId").getValue()).toString().equals(pgId)) {
                            homesInFeedModel rent = dataSnapshot.getValue(homesInFeedModel.class);
                            String key = dataSnapshot.getKey();
                            DatabaseReference availRef = d.child(key).child("avail");
                           availRef.setValue("false")
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){

//                                               Toast.makeText(payment.this, "success hi", Toast.LENGTH_SHORT).show();
                                               startActivity(new Intent(payment.this,PaymentSuccess.class));
                                           }else{

                                               String msg=task.getException().toString();
                                               Toast.makeText(payment.this, "Error"+msg, Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });

                        }}}


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
    public  String getCurrentUserId() {
        fuser=auth.getCurrentUser();
        if(fuser!=null){
            return fuser.getEmail();
        }else{
            return null;
        }
    }
}