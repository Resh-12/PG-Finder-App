package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_activity extends AppCompatActivity {
private EditText em;
private AppCompatButton bt;
private FirebaseAuth auth;
 ProgressDialog dg;

    private String fmail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        em=findViewById(R.id.mail);
        bt=findViewById(R.id.button);
        dg=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fmail=em.getText().toString();
                if(fmail.isEmpty())
                {
                    em.setError("Enter email");

                }
                else{
                    dg.setMessage("Loading");
                    dg.setCanceledOnTouchOutside(false);
                    dg.show();
                    forgetpass();
                }
            }
        });

    }


    private void forgetpass() {
       auth.sendPasswordResetEmail(fmail).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   dg.dismiss();
                   Toast.makeText(Forget_activity.this,"mail has been sent successfully",Toast.LENGTH_SHORT).show();
                   startActivity( new Intent(Forget_activity.this,Login.class));
                   finish();

               }else{
                   dg.dismiss();
                   Toast.makeText(Forget_activity.this,""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
               }

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(Forget_activity.this,"Failed",Toast.LENGTH_SHORT).show();

           }
       });

    }

}