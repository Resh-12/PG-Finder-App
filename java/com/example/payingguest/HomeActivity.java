package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.payingguest.fragments.AccountFrag;
import com.example.payingguest.fragments.HomeFrag;
import com.example.payingguest.fragments.PostFrag;
import com.example.payingguest.fragments.WishFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private BottomNavigationView nav;
    ProgressDialog d;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        d = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        nav = findViewById(R.id.nav);
        nav.setOnItemSelectedListener(this);
        loadFragment(new HomeFrag());

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.logout, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.logg:
//                d.setTitle("LOg Out");
//                d.setMessage("Logging out");
//                d.setCanceledOnTouchOutside(false);
//                d.show();
//                mAuth.signOut();
//                d.dismiss();
//                startActivity(new Intent(HomeActivity.this, Login.class));
//
//        }
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment=new HomeFrag();
                getSupportActionBar().setTitle("PG Finder");
                break;
            case R.id.post:
                fragment=new PostFrag();
                getSupportActionBar().setTitle("Post pg");
                break;
            case R.id.wish:
                fragment=new WishFrag();
                getSupportActionBar().setTitle("Wishlist");
                break;
            case R.id.acc:
                fragment=new AccountFrag();
                getSupportActionBar().setTitle("My " +
                        "Account");
                break;
            default:fragment=new HomeFrag();
        }
        return loadFragment(fragment);
    }

   boolean loadFragment(Fragment fragment) {

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }




}