package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText usermail, userpass;
    TextView txt,forgot;
    AppCompatButton btn;
    private FirebaseAuth mAuth;
    ProgressDialog progress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usermail = findViewById(R.id.mail);
        userpass = findViewById(R.id.pass);
        btn = findViewById(R.id.loginbtn);
        mAuth=FirebaseAuth.getInstance();
        txt = findViewById(R.id.no_acc);
        forgot=findViewById(R.id.forgottext);
        progress=new ProgressDialog(this);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
                finish();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Forget_activity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!usermail.getText().toString().trim().isEmpty()  &&emailChecker(usermail.getText().toString().trim())) {
                   if(!usermail.getText().toString().isEmpty()) {
                       progress.setTitle("Login");
                       progress.setMessage("Logging in");
                       progress.setCanceledOnTouchOutside(false);
                       progress.show();
                       loginUser(usermail.getText().toString().trim(),userpass.getText().toString().trim());
                   }
                   else{
                       Toast.makeText(Login.this,"Enter valid password",Toast.LENGTH_SHORT).show();
                   }
                }else{
                    Toast.makeText(Login.this,"Enter valid email",Toast.LENGTH_SHORT).show();
                    }



            }
        });

    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
    void loginUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progress.dismiss();
                    Toast.makeText(Login.this,"Login successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,HomeActivity.class));
                    finish();
                }
                else{
                    progress.dismiss();
                    Toast.makeText(Login.this,"Failed",Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(Login.this,"Fail..",Toast.LENGTH_SHORT).show();
            }
        });
    }
}