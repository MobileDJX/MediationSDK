package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.sdk.adserver.InterstitialVideo;
import com.ad.sdk.adserver.Listener.InterstitialImageAdListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

public class HomeActivity extends AppCompatActivity implements InterstitialImageAdListener, IUnityAdsInitializationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String zone_id = AppApplication.sharedPreferences.getString("Zone_ID", "");
        System.out.println("@ zone_id " + zone_id);


        ((LinearLayout) findViewById(R.id.lay_basic)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, BannerActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_interstitial_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, InterstitialActivity.class));
                finish();

            }
        });


        ((LinearLayout) findViewById(R.id.lay_native)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, NativeActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_gif)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, GIFPopUpActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_interstitial_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, InterstitialActivity.class));
                finish();

            }
        });

        ((LinearLayout) findViewById(R.id.lay_interstitial_video)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InterstitialVideo().loadAdShow(HomeActivity.this);
            }
        });


        ((LinearLayout) findViewById(R.id.lay_rewarded)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, RewardActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_inarticle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, InarticleActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_native_video)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, NativeVideoActivity.class));
                finish();
            }
        });


        ((LinearLayout) findViewById(R.id.lay_video_slider)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, VideoSlideAdActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_radio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, RadioAdActivity.class));
                finish();
            }
        });

        ((LinearLayout) findViewById(R.id.lay_inplayer_ad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, InPlayerActivity.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HomeActivity.this, ZoneActivity.class));
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

    @Override
    public void onInitializationComplete() {

    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {

    }
}