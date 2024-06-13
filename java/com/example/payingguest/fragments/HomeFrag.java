package com.example.payingguest.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.payingguest.HomePage;
import com.example.payingguest.MyPg;
import com.example.payingguest.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class HomeFrag extends Fragment {
    ImageButton h,a;
    RadioButton male,female;
    TextView h1,a1;
    AppCompatButton search;
    ImageSlider slider;

    boolean checkman,ifwoman,ishome,isapart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        male=view.findViewById(R.id.radiomale);
        female=view.findViewById(R.id.radiofemale);
        slider=view.findViewById(R.id.slider);
        h=view.findViewById(R.id.house);
        a=view.findViewById(R.id.apart);
        h1=view.findViewById(R.id.housetxt);
        a1=view.findViewById(R.id.aparttxt);
        search=view.findViewById(R.id.searchbtn);
        ArrayList<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.searchbg, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.h4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.h5, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.h6, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.c1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.c3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.c4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.c5, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.c6, ScaleTypes.FIT));
        slider.setImageList(slideModels,ScaleTypes.FIT);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ishome) {
                    ishome=true;
                    h.setImageDrawable(getResources().getDrawable(R.drawable.bhome80));
                    h1.setTextColor(Color.parseColor("#00babe"));
                } else {
                    ishome=false;
                    h.setImageDrawable(getResources().getDrawable(R.drawable.home80));
                    h1.setTextColor(Color.parseColor("#FF000000"));
                }
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isapart) {
                    isapart=true;
                    a.setImageDrawable(getResources().getDrawable(R.drawable.bapart70));
                    a1.setTextColor(Color.parseColor("#00babe"));
                } else {
                    isapart=false;
                    a.setImageDrawable(getResources().getDrawable(R.drawable.apart70));
                    a1.setTextColor(Color.parseColor("#FF000000"));
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isapart==false&&ishome==false) {
                    Toast.makeText(getActivity(), "Please choose atleast one type", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(getActivity(), HomePage.class);
                    i.putExtra("male", male.isChecked());
                    i.putExtra("female", female.isChecked());
                    i.putExtra("home", ishome);
                    i.putExtra("apart", isapart);
                    getActivity().startActivity(i);
                }
            }
        });
        return view;

    }

}
