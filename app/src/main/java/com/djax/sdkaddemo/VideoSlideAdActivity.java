package com.djax.sdkaddemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ad.sdk.adserver.AdView;
import com.ad.sdk.adserver.Listener.BannerListener;
import com.ad.sdk.adserver.RadioAds;
import com.ad.sdk.adserver.VideoSlider;
import com.ad.sdk.adserver.VideoSliderNew;
import com.google.android.exoplayer2.ui.StyledPlayerView;


public class VideoSlideAdActivity extends AppCompatActivity implements BannerListener {

    /*public StyledPlayerView playerView;
    ImageView imageView;
    ImageView fullscreenButton;
    boolean fullscreen = false;*/
    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_slider_activity_main);

        layout = (RelativeLayout)findViewById(R.id.layout);

       /* String zone_id=AppApplication.sharedPreferences.getString("Zone_ID","");
        System.out.println("@ zone_id "+zone_id);

        //WebView wb1 = findViewById(R.id.banner1);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
*/

        //new VideoSlider().loadVideoSliderAds(playerView,VideoSlideAdActivity.this);

        //new VideoSliderNew(this).loadView(layout);
        new VideoSliderNew(this).loadVideoSliderAds(this,layout);


      /*  fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(VideoSlideAdActivity.this, R.drawable.ic_fullscreen_open));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }

                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = (int) ( 220 * getApplicationContext().getResources().getDisplayMetrics().density);
                    params.height = (int) ( 140 * getApplicationContext().getResources().getDisplayMetrics().density);
                    *//*params.width = (int) (225);
                    params.height = (int) (144);*//*
                    playerView.setLayoutParams(params);

                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(VideoSlideAdActivity.this, R.drawable.ic_fullscreen_close));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }

                   // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);

                    fullscreen = true;
                }
            }
        });*/
    }

    @Override
    public void onBackPressed() {

       startActivity(new Intent(VideoSlideAdActivity.this,HomeActivity.class));
       finish();
    }


    @Override
    public void AdLoaded() {

    }

    @Override
    public void AdFailed() {

    }
}