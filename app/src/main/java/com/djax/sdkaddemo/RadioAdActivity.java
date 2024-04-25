package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.sdk.adserver.AdView;
import com.ad.sdk.adserver.Listener.BannerListener;
import com.ad.sdk.adserver.RadioAds;
import com.google.android.exoplayer2.ui.StyledPlayerView;


public class RadioAdActivity extends AppCompatActivity {

    public StyledPlayerView playerView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        ((TextView)findViewById(R.id.header)).setText(AppApplication.sharedPreferences.getString("Ad_type",""));


        String zone_id=AppApplication.sharedPreferences.getString("Zone_ID","");
        System.out.println("@ zone_id "+zone_id);

        //WebView wb1 = findViewById(R.id.banner1);
        playerView = findViewById(R.id.player_view);
        imageView = findViewById(R.id.image_view);
        new RadioAds().loadRadiosAds(playerView,imageView,RadioAdActivity.this);




    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(RadioAdActivity.this,HomeActivity.class));
       finish();
    }


}