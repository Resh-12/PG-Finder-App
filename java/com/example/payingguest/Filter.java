package com.example.payingguest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.slider.RangeSlider;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Filter extends AppCompatActivity {
    RangeSlider pricerange;
    CheckBox udupi,kundapur,karkal,bhr;
    AppCompatButton apply;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        pricerange=findViewById(R.id.range);
        udupi=findViewById(R.id.cbudupi);
        kundapur=findViewById(R.id.cbkun);
        karkal=findViewById(R.id.cbkarkal);
        bhr=findViewById(R.id.cbkun);
        apply=findViewById(R.id.apply);


        // Set the minimum and maximum values for the range slider
        float minValue = 2000f; // Minimum value in rupees
        float maxValue = 10000f; // Maximum value in rupees
        pricerange.setValueFrom(minValue);
        pricerange.setValueTo(maxValue);

        // Set the initial minimum and maximum values
        float initialMinValue = 3000f; // Initial minimum value in rupees
        float initialMaxValue = 8000f; // Initial maximum value in rupees
        pricerange.setValues(initialMinValue, initialMaxValue);
        float stepSize = 500f; // Step value in rupees
        pricerange.setStepSize(stepSize);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Float> values = pricerange.getValues();
                if (values.size() >= 2) {
                    float minPrice = values.get(0);
                    float maxPrice = values.get(1);

                    ArrayList<String> selectedCities = new ArrayList<>();
                    if (udupi.isChecked()) {
                        selectedCities.add("Udupi");
                    }
                    if (kundapur.isChecked()) {
                        selectedCities.add("Kundapur");
                    }
                    if (karkal.isChecked()) {
                        selectedCities.add("Karkala");
                    }
                    if (bhr.isChecked()) {
                        selectedCities.add("Bhramavar");
                    }
                    Intent intent = new Intent();
                    intent.putExtra("minPrice", minPrice);
                    intent.putExtra("maxPrice", maxPrice);
                    intent.putStringArrayListExtra("selectedCities", selectedCities);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(Filter.this, "Please select a valid price range", Toast.LENGTH_SHORT).show();

                }
            }

        });


    }
}