package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class Editprofile extends AppCompatActivity {
    private View rootview;
    FirebaseAuth authprofile;
    ImageView profile;
    ShapeableImageView  imagebox;
    StorageReference storage;
    private AppCompatButton cmail, cpass, cprofile;
    private FirebaseUser firebaseUser;
    EditText currentpassword, newpassword;
    boolean passwordvisible, passvisible;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
    ProgressDialog pd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit profile");
        setContentView(R.layout.activity_editprofile);
        cmail = findViewById(R.id.changeusermail);
        cpass = findViewById(R.id.changepass);
        cprofile = findViewById(R.id.changeprofilepic);
        profile=findViewById(R.id.userprofile);
        pd=new ProgressDialog(this);
        cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Editprofile.this);
                //View dialogpass=getLayoutInflater().inflate(R.layout.changepassword,null);;
                // EditText emailbox=dialogView.findViewById(R.id.emailbox);
                Dialog d = new Dialog(Editprofile.this);
                d.setContentView(R.layout.changepassword);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
                EditText currentpass = d.findViewById(R.id.currentpassbox);
                EditText newpass = d.findViewById(R.id.newpassbox);
                ConstraintLayout c=d.findViewById(R.id.lnewp);
                c.setVisibility(View.INVISIBLE);

                authprofile = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = authprofile.getCurrentUser();

                currentpass.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        final int End = 2;
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= currentpass.getRight() - currentpass.getCompoundDrawables()[End].getBounds().width()) {
                                int selection = currentpass.getSelectionEnd();
                                if (passwordvisible) {
                                    currentpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_visibility_off_24, 0);
                                    //hide password
                                    currentpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    passwordvisible = false;
                                } else {
                                    currentpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_visibility_24, 0);
                                    //show password
                                    currentpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                    passwordvisible = true;
                                }
                                currentpass.setSelection(selection);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                newpass.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        final int End = 2;
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= newpass.getRight() - newpass.getCompoundDrawables()[End].getBounds().width()) {
                                int selection = newpass.getSelectionEnd();
                                if (passvisible) {
                                    newpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_visibility_off_24, 0);
                                    //hide password
                                    newpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    passvisible = false;
                                } else {
                                    newpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_visibility_24, 0);
                                    //show password
                                    newpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                    passvisible = true;
                                }
                                newpass.setSelection(selection);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                d.findViewById(R.id.btncancelpass).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                d.findViewById(R.id.btnchange).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
                        String passnew = newpass.getText().toString();
                        String passcurrent = currentpass.getText().toString();
                        if (currentpass.getText().toString().matches(newpass.getText().toString())) {
                            newpass.setError(" New Pasword cannot be same as old password");
                            newpass.requestFocus();
                        }else if (!passnew.matches(passwordPattern)) {
                            newpass.setError("Password should contain atleast 6 characters with 1 digit,1 lowercase letter,1 uppercase letter,one special character");
                        }
                        else if (TextUtils.isEmpty(passnew)) {
                            newpass.setError("Enter the Password");
                            newpass.requestFocus();

                        } else if (TextUtils.isEmpty(passcurrent)) {
                            currentpass.setError("Enter the Current Password and Authenicate ");
                            currentpass.requestFocus();
                        } else {
                            firebaseUser.updatePassword(passnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Editprofile.this, "Password Changed", Toast.LENGTH_LONG).show();
                                        d.dismiss();

                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Toast.makeText(Editprofile.this, "Error has happended", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }
                            });

                        }
                    }
                });
                d.findViewById(R.id.btnauth).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passcurrent = currentpass.getText().toString();
                        //String passnew=newpass.getText().toString();

                        if (firebaseUser.equals("")) {
                            Toast.makeText(Editprofile.this, "User details not availabe", Toast.LENGTH_LONG).show();
                        }

                        if (TextUtils.isEmpty(passcurrent)) {
                            currentpass.setError("Enter the current password");
                            currentpass.requestFocus();
                        } else {

                            AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), passcurrent);
                            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Editprofile.this, "Password has been verified", Toast.LENGTH_LONG).show();
                                        c.setVisibility(View.VISIBLE);


                                    } else {
                                        try {
                                            throw task.getException();

                                        } catch (Exception e) {
                                            currentpass.setError("Invalid Passoword");
                                            currentpass.requestFocus();
                                            Toast.makeText(Editprofile.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

       cmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(Editprofile.this,Update_email.class);
               startActivity(i);
           }
       });
        cprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //AlertDialog.Builder builder = new AlertDialog.Builder(userprofile.this);
                //View dialogView = getLayoutInflater().inflate(R.layout.upload_pic_dialog, null);
                Dialog dialogView = new Dialog(Editprofile.this);
                dialogView.setContentView(R.layout.upload_pic_dialog);
                dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.show();
                imagebox = dialogView.findViewById(R.id.imagebox);
                authprofile = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = authprofile.getCurrentUser();
                storage = FirebaseStorage.getInstance().getReference("DisplayPics");
                Uri uri = firebaseUser.getPhotoUrl();
                //set user current DP in imageView(if uploades already)
                Picasso.get().load(uri).into(imagebox);
                imagebox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFilechooser();
                    }
                });
                dialogView.findViewById(R.id.btnupload).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UploadPic();
                        dialogView.dismiss();

                    }
                });

                dialogView.findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogView.dismiss();
                    }
                });
            }
        });




    }


    private void UploadPic() {
        pd.setMessage("uploading");
        pd.show();
        if (uriImage != null) {
            StorageReference file = storage.child(authprofile.getCurrentUser().getUid() + "." + getFileExtension(uriImage));
            file.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Uri downloaduri = uri;
                            FirebaseUser firebaseUser = authprofile.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloaduri).build();
                            firebaseUser.updateProfile(profileUpdates);
                            pd.dismiss();
                            Toast.makeText(Editprofile.this, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(Editprofile.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });
        } else {
            Toast.makeText(Editprofile.this, "No file selected", Toast.LENGTH_LONG).show();

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void openFilechooser() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            imagebox.setImageURI(uriImage);
        }
    }


}









