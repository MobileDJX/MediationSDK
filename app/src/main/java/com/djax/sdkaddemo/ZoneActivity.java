package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.sdk.adserver.AdView;



public class ZoneActivity extends AppCompatActivity {


    /*private StyledPlayerView playerView;
    private ExoPlayer player;
    private ImaAdsLoader adsLoader;*/
    String zone_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

      /*  playerView = findViewById(R.id.player_view);
        playerView.setControllerAutoShow(false);
        // Create an AdsLoader.
        adsLoader = new ImaAdsLoader.Builder(this).build();
*/

        //((TextView)findViewById(R.id.header)).setText(AppApplication.sharedPreferences.getString("Ad_type",""));
        ((TextView)findViewById(R.id.header)).setText("Zone");

        ((LinearLayout)findViewById(R.id.lay_zone)).setVisibility(View.VISIBLE);
        ((LinearLayout)findViewById(R.id.lay_response)).setVisibility(View.GONE);


        ((LinearLayout)findViewById(R.id.zone_btn_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LinearLayout)findViewById(R.id.lay_zone)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.lay_response)).setVisibility(View.GONE);

            }
        });



        ((LinearLayout)findViewById(R.id.res_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LinearLayout)findViewById(R.id.lay_zone)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.lay_response)).setVisibility(View.VISIBLE);
            }
        });


        ((Button)findViewById(R.id.load_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                zone_id=((EditText)findViewById(R.id.ed_zone_id)).getText().toString();

                AdView adv1 = new AdView(ZoneActivity.this);
                adv1.setZoneid(zone_id); //Place Your Zone id
                adv1.LoadAd(adv1);

                AppApplication.editor.putString("Zone_ID",((EditText)findViewById(R.id.ed_zone_id)).getText().toString());
                AppApplication.editor.commit();
                startActivity(new Intent(ZoneActivity.this,HomeActivity.class));
                finish();



               /* String ad_type= AppApplication.sharedPreferences.getString("Ad_type","");

                if(ad_type.equalsIgnoreCase("Banner"))
                {
                    startActivity(new Intent(ZoneActivity.this,BannerActivity.class));
                    finish();

                }
                else if(ad_type.equalsIgnoreCase("Interstitial"))
                {
                    AdView ad1 = new AdView(ZoneActivity.this, ZoneActivity.this);
                    ad1.setZoneid(((EditText)findViewById(R.id.ed_zone_id)).getText().toString()); //Place Your Zone id
                    ad1.LoadAd();

                }
                else if(ad_type.equalsIgnoreCase("Native"))
                {
                    startActivity(new Intent(ZoneActivity.this,NativeActivity.class));
                    finish();
                }
                else if(ad_type.equalsIgnoreCase("Interstitial_Video"))
                {
                    //new Interstitial_video().loadInterstitalVideo(ZoneActivity.this);
                    //new LoadActivity();
                    new Banner_image().loadAdShow(ZoneActivity.this);
                }
                else if(ad_type.equalsIgnoreCase("Rewarded"))
                {
                    new Rewardedvideo().loadRewardedAd(ZoneActivity.this);
                }
                else if(ad_type.equalsIgnoreCase("Native_video"))
                {
                    startActivity(new Intent(ZoneActivity.this,NativeVideoActivity.class));
                    finish();
                }
                else if(ad_type.equalsIgnoreCase("Video_slider"))
                {
                    startActivity(new Intent(ZoneActivity.this,VideoSlideAdActivity.class));
                    finish();
                }
                else if(ad_type.equalsIgnoreCase("Radio_ad"))
                {
                    startActivity(new Intent(ZoneActivity.this,RadioAdActivity.class));
                    finish();
                }
                else if(ad_type.equalsIgnoreCase("Inplayer_ad"))
                {
                    startActivity(new Intent(ZoneActivity.this,InPlayerActivity.class));
                    finish();
                }*/

            }
        });




    }

    /*private void releasePlayer() {
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
        player.release();
        player = null;
    }

    private void initializePlayer() {
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);

        MediaSourceFactory mediaSourceFactory =
                new DefaultMediaSourceFactory(dataSourceFactory)
                        .setAdsLoaderProvider(unusedAdTagUri -> adsLoader)
                        .setAdViewProvider(playerView);

        // Create an ExoPlayer and set it as the player for content and ads.
        player = new ExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();
        playerView.setPlayer(player);
        adsLoader.setPlayer(player);

        // Create the MediaItem to play, specifying the content URI and ad tag URI.
        //Uri contentUri = Uri.parse(getString(R.string.content_url));
        Uri contentUri = Uri.parse("");
        Uri adTagUri = Uri.parse(getString(R.string.ad_tag_url2));
        MediaItem mediaItem =
                new MediaItem.Builder()
                        .setUri(contentUri)
                        .setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(adTagUri).build())
                        .build();

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player.setMediaItem(mediaItem);
        player.prepare();

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        //
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adsLoader.release();
    }*/


    @Override
    public void onBackPressed() {
        finish();
    }


}