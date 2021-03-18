package com.hexa.pecheur_du_dimanche.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.hexa.pecheur_du_dimanche.R;
import com.hexa.pecheur_du_dimanche.api.fishState.WaterFishStateApi;
import com.hexa.pecheur_du_dimanche.api.hydrometry.WaterHydrometryApi;
import com.hexa.pecheur_du_dimanche.api.waterQuality.WaterQualityApi;
import com.hexa.pecheur_du_dimanche.api.waterTemp.WaterTempApi;
import com.hexa.pecheur_du_dimanche.models.Station;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button buttonGo = findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(value ->
                startActivity(new Intent(MainActivity.this, MapActivity.class))
        );
    }
}