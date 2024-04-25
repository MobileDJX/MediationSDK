package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.sdk.adserver.InarticleVideoAds;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;


public class InarticleActivity extends AppCompatActivity {

    public StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inarticle);

        ((TextView)findViewById(R.id.header)).setText(AppApplication.sharedPreferences.getString("Ad_type",""));

        playerView = findViewById(R.id.player_view);

        new InarticleVideoAds().InarticleVideoAds(playerView,InarticleActivity.this,InarticleActivity.this);





    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(InarticleActivity.this,HomeActivity.class));
       finish();
    }


}