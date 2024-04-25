package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.sdk.adserver.Interstitial_image;
import com.ad.sdk.adserver.Listener.InterstitialImageAdListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

public class InterstitialActivity extends AppCompatActivity implements InterstitialImageAdListener, IUnityAdsInitializationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                new Interstitial_image().loadInterstital(InterstitialActivity.this, InterstitialActivity.this);
            }
        }, 3000);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InterstitialActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onInterstitialAdLoaded() {

    }

    @Override
    public void onInterstitialAdFailed() {

    }

    @Override
    public void onInterstitialAdShown() {

    }

    @Override
    public void onInterstitialAdClicked() {

    }

    @Override
    public void onInterstitialAdDismissed() {

    }


    //Unity Ad Listener
    @Override
    public void onInitializationComplete() {
        Log.e("Unity Ad Status : ", "SDK Initialize Successfully");

    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.e("Unity Ad Status :", "SDK Initialize Failed :" + message);

    }
}