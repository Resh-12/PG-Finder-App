package com.example.payingguest;

import static com.example.payingguest.R.id.enewmail;
import static com.example.payingguest.R.id.vpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Update_email extends AppCompatActivity {
    FirebaseUser user;

    FirebaseAuth mAuth;
    boolean passwordvisible, passvisible;
    TextView oldemail,mailnew,notauth;
    Button btnauth,btnupdate,btncancel;
    EditText password,nmail;
    RelativeLayout r;
    String passcurrent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        oldemail=findViewById(R.id.currentmail);
        user=FirebaseAuth.getInstance().getCurrentUser();
        oldemail.setText(user.getEmail());
        password = findViewById(R.id.vpassword);
        nmail = findViewById(enewmail);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        btnauth=findViewById(R.id.button_authenticate_user);
        btnupdate=findViewById(R.id.button_update_email);
        btncancel=findViewById(R.id.cancel);
        r=findViewById(R.id.RL_verify);
        r.setVisibility(View.INVISIBLE);





        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int End = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= password.getRight() - password.getCompoundDrawables()[End].getBounds().width()) {
                        int selection = password.getSelectionEnd();
                        if (passwordvisible) {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_visibility_off_24, 0);
                            //hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible = false;
                        } else {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_visibility_24, 0);
                            //show password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Update_email.this, Editprofile.class);
                startActivity(i);
                finish();
                
            }
        });


        btnauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcurrent = password.getText().toString();

                if (user.equals("")) {
                    Toast.makeText(Update_email.this, "User details not available", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(passcurrent)) {
                    password.setError("Enter the current password");
                    password.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passcurrent);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Update_email.this, "Password has been verified", Toast.LENGTH_LONG).show();
                                r.setVisibility(View.VISIBLE);

                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    password.setError("Invalid Password");
                                    password.requestFocus();
                                    Toast.makeText(Update_email.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    });
                }
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String om = oldemail.getText().toString();
                String nm = nmail.getText().toString();
                if (oldemail.getText().toString().matches(nmail.getText().toString())) {
                    nmail.setError(" New Email cannot be same as old Email");
                    nmail.requestFocus();
                } else if (TextUtils.isEmpty(nm)) {
                    nmail.setError("Enter the Email");
                    nmail.requestFocus();

                } else if (TextUtils.isEmpty(om)) {
                    oldemail.setError("Enter the Current Email and Authenicate ");
                    oldemail.requestFocus();
                } else {
                    user.updateEmail(nm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Update_email.this, "Email Changed", Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                               // Toast.makeText(Update_email.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(Update_email.this,e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    });

                }
            }
        });



    }
}