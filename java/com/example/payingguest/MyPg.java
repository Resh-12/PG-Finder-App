package com.example.payingguest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.payingguest.fragments.BookedPg;
import com.example.payingguest.fragments.PostedPg;

public class MyPg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pg);
        loadFragment(new PostedPg());

    }
    public void onClick(View v){
        if(v.getId()==R.id.radiopost)
            loadFragment(new PostedPg());
        else
            loadFragment(new BookedPg());

    }
    private void loadFragment(Fragment fragment){
        FragmentManager fmang=getSupportFragmentManager();
        FragmentTransaction ftrans=fmang.beginTransaction();
        ftrans.replace(R.id.pgdetails,fragment);
        ftrans.commit();
    }
}