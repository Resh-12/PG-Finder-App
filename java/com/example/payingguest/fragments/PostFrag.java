package com.example.payingguest.fragments;

//import static android.app.Activity.RESULT_OK;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.payingguest.HomeActivity;
import com.example.payingguest.Login;
import com.example.payingguest.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class PostFrag extends Fragment {
    boolean t=true;
RadioGroup rgroomtype,rgsharetype,rgfood,rgguest,rggender;
RadioButton radioroomtype,radioshare,radiofood,radioguest,radiogender;
CheckBox breakf,lnch,dinr,none,bed,pillw,wf,cupbrd,frdg,laund,park,table,keeping,water,tv,stove;
EditText rent,depo,city,loc,description,contact;
String mobilepattern="[0-9]{10}";
String[] cities={"Udupi","Bhramavar","Karkala","Kundapur"};
String spincity;
AppCompatButton post;
ImageView homeImg;
Spinner spin;
    private String downloadUri;
String room="",share="",food="",guest="",gender="",mrent="",sdep="",edcity="",edloc="",eddesc="",edcontact="";
String meals,amenities;
    String saveCurrentDate, saveCurrentTime;
    private  String randomKey;
    private ProgressDialog pd;
    private DatabaseReference postDataRef;
    private FirebaseAuth auth;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private static final int galleryPic = 1;
    //static final int RESULT_OK=1;
    private Uri ImageUri ;
    private StorageReference homepic;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mAuth=FirebaseAuth.getInstance();
       // auth= FirebaseAuth.getInstance();
       // databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
      //  homepic= FirebaseStorage.getInstance().getReference().child("home_pictures");

       // postDataRef = FirebaseDatabase.getInstance().getReference().child("Rent_posts");

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_post, container, false);
        mAuth=FirebaseAuth.getInstance();
        auth= FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        homepic= FirebaseStorage.getInstance().getReference().child("home_pictures");
        postDataRef = FirebaseDatabase.getInstance().getReference().child("Rent_posts");
        rgroomtype=(RadioGroup)view.findViewById(R.id.rgroom);
        rgsharetype=(RadioGroup)view.findViewById(R.id.rgshare);
        rgfood=(RadioGroup)view.findViewById(R.id.rgfood);
        rgguest=(RadioGroup)view.findViewById(R.id.rgguest);
        rggender=(RadioGroup) view.findViewById(R.id.rggender);
        spin=view.findViewById(R.id.spinnercity);
        breakf=view.findViewById(R.id.breakfst);
        lnch=view.findViewById(R.id.lunch);
        dinr=view.findViewById(R.id.dinner);
        none=view.findViewById(R.id.None);
        bed=view.findViewById(R.id.bed);
        pillw=view.findViewById(R.id.Pillow);
        wf=view.findViewById(R.id.Wifi);
        cupbrd=view.findViewById(R.id.Cupbrd);
        frdg=view.findViewById(R.id.Fridge);
        laund=view.findViewById(R.id.Laundry);
        park=view.findViewById(R.id.Parking);
        table=view.findViewById(R.id.table);
        keeping=view.findViewById(R.id.keeping);
        water=view.findViewById(R.id.water);
        tv=view.findViewById(R.id.tv);
        stove=view.findViewById(R.id.Stove);
        loc=view.findViewById(R.id.etloc);
        post=view.findViewById(R.id.post);
        homeImg=view.findViewById(R.id.homeImage);
        rent=view.findViewById(R.id.mrent);
        depo=view.findViewById(R.id.sdepo);
        description=view.findViewById(R.id.etdesc);
        contact=view.findViewById(R.id.etcontact);
        pd = new ProgressDialog(getContext());
        ArrayAdapter adapter=new ArrayAdapter<>(getActivity(), R.layout.spinnerdisplay,R.id.spinnerDisplay,cities);
        spin.setAdapter(adapter);
        homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // CropImage.activity().start(getContext(),this);
                openGallery();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            collectData();
            }
        });
        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, galleryPic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPic && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            homeImg.setImageURI(ImageUri);
        }
    }
    public String getCurrentUser() {

        user=auth.getCurrentUser();
        if(user!=null){
            return user.getEmail();
        }else{
            return null;
        }

    }
    public void collectData(){
        int genderselect=rggender.getCheckedRadioButtonId();
        radiogender= rggender.findViewById(genderselect);
        int roomselected=rgroomtype.getCheckedRadioButtonId();
        radioroomtype=rgroomtype.findViewById(roomselected);
        int shareselect=rgsharetype.getCheckedRadioButtonId();
        radioshare=rgsharetype.findViewById(shareselect);
        int foodselect=rgfood.getCheckedRadioButtonId();
        radiofood=rgfood.findViewById(foodselect);
        int guestselect=rgguest.getCheckedRadioButtonId();
        spincity=spin.getSelectedItem().toString();
        radioguest=rgguest.findViewById(guestselect);
         room= (String) radioroomtype.getText().toString();
         share= (String) radioshare.getText().toString();
         food= (String) radiofood.getText().toString();
         guest= (String) radioguest.getText().toString();
         gender= (String) radiogender.getText().toString();
         mrent= String.valueOf(rent.getText());
         sdep= String.valueOf(depo.getText());

         edloc= String.valueOf(loc.getText());
         eddesc= String.valueOf(description.getText());
         edcontact= String.valueOf(contact.getText());
         StringBuilder mealselected=new StringBuilder();
        StringBuilder facilityselected=new StringBuilder();
         if(breakf.isChecked())
         {
            mealselected.append(breakf.getText()).append(",");
         }
        if(lnch.isChecked())
        {
            mealselected.append(lnch.getText()).append(",");
        }
        if(dinr.isChecked())
        {
            mealselected.append(dinr.getText()).append(",");
        }
        if(none.isChecked())
        {
            mealselected.append(none.getText()).append(",");
        }
        else {
            mealselected.append("");
        }
        meals=mealselected.toString();
        if(bed.isChecked())
        {
            facilityselected.append(bed.getText()).append(",");
        }if(pillw.isChecked())
        {
            facilityselected.append(pillw.getText()).append(",");
        }if(wf.isChecked())
        {
            facilityselected.append(wf.getText()).append(",");
        }if(laund.isChecked())
        {
            facilityselected.append(laund.getText()).append(",");
        }if(cupbrd.isChecked())
        {
            facilityselected.append(cupbrd.getText()).append(",");
        }if(park.isChecked())
        {
            facilityselected.append(park.getText()).append(",");
        }if(keeping.isChecked())
        {
            facilityselected.append(keeping.getText()).append(",");
        }if(tv.isChecked())
        {
            facilityselected.append(tv.getText()).append(",");
        }if(table.isChecked())
        {
            facilityselected.append(table.getText()).append(",");
        }if(frdg.isChecked())
        {
            facilityselected.append(frdg.getText()).append(",");
        }if(water.isChecked())
        {
            facilityselected.append(water.getText()).append(",");
        }if(stove.isChecked())
        {
            facilityselected.append(stove.getText()).append(",");
        }else {
            facilityselected.append("");
        }
        amenities=facilityselected.toString();
        if(ImageUri==null){
            Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
        }
        else if (mrent.isEmpty()) {
            rent.setError("Enter rent");
        } else if (sdep.isEmpty()) {
            depo.setError("Enter deposit");

//        } else if (edcity.isEmpty()) {
//            city.setError("enter a city");
        } else if (edloc.isEmpty()) {
            loc.setError("enter the locality");
        } else if (!edcontact.matches(mobilepattern)) {
            contact.setError("Enter valid phone number");
        } else{
            storeImageData();
        }
    }

    private void storeImageData() {

        pd.setMessage("Posting");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
       Calendar calendar= Calendar.getInstance();
       SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
       saveCurrentDate= currentDate.format(calendar.getTime());
       SimpleDateFormat currentTime= new SimpleDateFormat("HH: mm: ss a");
       saveCurrentTime= currentTime.format(calendar.getTime());
        randomKey= saveCurrentDate+ saveCurrentTime;
        if(randomKey==null){
            saveCurrentDate= currentDate.format(calendar.getTime());
            saveCurrentTime= currentTime.format(calendar.getTime());
            randomKey= saveCurrentDate+ saveCurrentTime;
        }
        // Create a reference to the image file
        final StorageReference file= homepic.child(ImageUri.getLastPathSegment()+ randomKey + ".jpg");
        // Upload the image to Firebase Storage
        final UploadTask uploadTask= file.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getActivity(), "Failed to upload image: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getActivity(), "Image uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadUri = file.getDownloadUrl().toString();
                        return file.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            // Retrieve the download URL
                            downloadUri=task.getResult().toString();
//                            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                            UpdateDatabase();
                        }
                    }
                });
            }
        });

    }

    private void UpdateDatabase() {
        //databaseReference = FirebaseDatabase.getInstance().getReference("Rent_posts");
        String userid=getCurrentUser();
        HashMap<String, Object> map= new HashMap<>();
        map.put("pId",randomKey);
        map.put("date",saveCurrentDate);
        map.put("time",saveCurrentTime);
        map.put("image",downloadUri);
        map.put("roomType",room);
        map.put("Meals",meals);
        map.put("Amenities",amenities);
        map.put("gender",gender);
        map.put("sharetype",share);
        map.put("monthlyRent",mrent);
        map.put("securityDeposit",sdep);
        map.put("city",spincity);
        map.put("locality",edloc);
        map.put("description",eddesc);
        map.put("contactNo",edcontact);
        map.put("foodPreference",food);
        map.put("guestsAllowed",guest);
        map.put("user",userid);
        map.put("avail","true");
        map.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        postDataRef.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    pd.dismiss();
                    Toast.makeText(getActivity(), "Posted", Toast.LENGTH_SHORT).show();
                    Intent j=new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(j);
                }
                else{
                    pd.dismiss();
                    String msg=task.getException().toString();
                    Toast.makeText(getActivity(), "Error"+msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
