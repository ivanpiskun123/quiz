package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class QuizResultActivity extends AppCompatActivity {

    TextView textRight, textWrong;
    Button btnBack;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        textRight = findViewById(R.id.textRight);
        textWrong = findViewById(R.id.textWrong);
        btnBack = findViewById(R.id.btnBack);

        int nRight = Integer.parseInt(getIntent().getStringExtra("RIGHT"));
        textRight.setText(getIntent().getStringExtra("RIGHT"));
        textWrong.setText( String.valueOf(  6 -  nRight ));




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }
}