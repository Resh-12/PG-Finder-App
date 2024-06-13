package com.example.payingguest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.payingguest.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private EditText name, mail, pass, cpass;
    private TextView msg;
    private AppCompatButton b;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private  String uname,upass,ucpass,umail;
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.repass);
        b = findViewById(R.id.signbtn);
        msg = findViewById(R.id.have_acc);
        mAuth = FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();
        dialog=new ProgressDialog(this);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this, Login.class);
                startActivity(i);
                finish();

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname=name.getText().toString();
                umail=mail.getText().toString();
                upass=pass.getText().toString();
                ucpass=cpass.getText().toString();
                if (uname.isEmpty()) {
                   name.setError("Enter name");
                } else if (umail.isEmpty()) {
                    mail.setError("Enter valid email");
                } else if (!upass.matches(passwordPattern)) {
                    pass.setError("Password should contain atleast 6 characters with 1 digit,1 lowercase letter,1 uppercase letter");
                } else if (!upass.equals(ucpass)) {
                    cpass.setError("Password does not match");
                } else {
                    if (emailChecker(umail)) {
                        dialog.setTitle("Sign Up");
                        dialog.setMessage("Signing up");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        createUser(umail,upass,uname);
                    }else{
                        mail.setError("Enter valid email");
                    }

                }
            }
        });

    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    void createUser(String email, String pword,String name) {
        mAuth.createUserWithEmailAndPassword(email,pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                User user=new User(name,email);
               if(task.isSuccessful()){
                   mRef.child("users").push().setValue(user);
                   dialog.dismiss();
                   startActivity(new Intent(Signup.this,HomeActivity.class));
                   finish();
                   Toast.makeText(Signup.this,"User created",Toast.LENGTH_SHORT).show();
               }else{
                   dialog.dismiss();
                   Toast.makeText(Signup.this,""+task.getException(),Toast.LENGTH_SHORT).show();
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
             Toast.makeText(Signup.this,"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}