package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.sdk.adserver.Interstitial_image;
import com.ad.sdk.adserver.Listener.GIFAdListener;
import com.ad.sdk.adserver.Listener.InterstitialImageAdListener;
import com.ad.sdk.adserver.LoadGIFAdview;

public class GIFPopUpActivity extends AppCompatActivity implements GIFAdListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifpopup);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
               new LoadGIFAdview().loadGIFAd(GIFPopUpActivity.this,GIFPopUpActivity.this);
            }
        }, 3000);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GIFPopUpActivity.this,HomeActivity.class));
        finish();
    }


    @Override
    public void onAdLoaded() {

    }

    @Override
    public void onAdFailed() {

    }

    @Override
    public void onAdShown() {

    }

    @Override
    public void onAdClicked() {

    }

    @Override
    public void onAdDismissed() {

    }
}