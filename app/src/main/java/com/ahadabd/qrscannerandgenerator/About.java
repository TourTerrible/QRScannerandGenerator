package com.ahadabd.qrscannerandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class About extends AppCompatActivity {
    InterstitialAd mInterstitialAd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mInterstitialAd2 = new InterstitialAd(this);

        mInterstitialAd2.setAdUnitId("ca-app-pub-9537028007919916/9155968668");

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd2.loadAd(adRequest);

        mInterstitialAd2.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }
    private void showInterstitial() {
        if (mInterstitialAd2.isLoaded()) {
            mInterstitialAd2.show();
        }
    }
}
