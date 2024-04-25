package com.djax.sdkaddemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

public class InPlayerActivity extends AppCompatActivity {

    private StyledPlayerView playerView, small;
    private ExoPlayer player,player2;
    private ImaAdsLoader adsLoader;
    ImageView fullscreenButton;
    boolean fullscreen = false;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_player);


        scrollView = findViewById(R.id.scrollView);
        playerView = findViewById(R.id.playerFull);
        adsLoader = new ImaAdsLoader.Builder(this).build();

        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);


        //Mini Player
        small = findViewById(R.id.playerSmall);
        small.setVisibility(View.GONE);


        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(InPlayerActivity.this, R.drawable.ic_fullscreen_open));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);

                    fullscreen = false;
                } else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(InPlayerActivity.this, R.drawable.ic_fullscreen_close));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);

                    fullscreen = true;
                }
            }
        });


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scroll_Y = scrollView.getScrollY();
                Log.e("scroll Y: ", String.valueOf(scroll_Y));

                if (scroll_Y >= 750) {

                    small.setVisibility(View.VISIBLE);

                } else if(scroll_Y<750) {

                    small.setVisibility(View.INVISIBLE);



                }

            }
        });

    }

    private void releasePlayer() {
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
        small.setPlayer(null);

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
        adsLoader.setPlayer(player);

        playerView.setPlayer(player);
        //small.setPlayer(player);

//        small.setPlayer(player);
//        adsLoader.setPlayer(player);


        // Create the MediaItem to play, specifying the content URI and ad tag URI.

        /*Uri contentUri = Uri.parse("test");
        Uri adTagUri = Uri.parse(getString(R.string.ad_tag_url2));*/
        Uri contentUri = Uri.parse("https://demo.reviveadservermod.com/TitanClocks.mp4");
        Uri adTagUri = Uri.parse("");
        //Uri adTagUri = Uri.parse(getString(R.string.ad_tag_url4));
        MediaItem mediaItem =
                new MediaItem.Builder()
                        .setUri(contentUri)
                        .setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(adTagUri).build())
                        .build();
        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player.setMediaItem(mediaItem);
        player.prepare();

        //Enable Repeat Mode
       // player.setRepeatMode(Player.REPEAT_MODE_ALL);


        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(true);


    }


    private void initializePlayer2() {
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);

        MediaSourceFactory mediaSourceFactory =
                new DefaultMediaSourceFactory(dataSourceFactory)
                        .setAdsLoaderProvider(unusedAdTagUri -> adsLoader)
                        .setAdViewProvider(small);


        // Create an ExoPlayer and set it as the player for content and ads.
        player2 = new ExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build();
        adsLoader.setPlayer(player2);

        small.setPlayer(player2);
        //small.setPlayer(player);

//        small.setPlayer(player);
//        adsLoader.setPlayer(player);


        // Create the MediaItem to play, specifying the content URI and ad tag URI.

        /*Uri contentUri = Uri.parse("test");
        Uri adTagUri = Uri.parse(getString(R.string.ad_tag_url2));*/
        Uri contentUri = Uri.parse("https://demo.reviveadservermod.com/TitanClocks.mp4");
        Uri adTagUri = Uri.parse("");
        //Uri adTagUri = Uri.parse(getString(R.string.ad_tag_url4));
        MediaItem mediaItem =
                new MediaItem.Builder()
                        .setUri(contentUri)
                        .setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(adTagUri).build())
                        .build();
        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player2.setMediaItem(mediaItem);
        player2.prepare();

        //Enable Repeat Mode
        // player.setRepeatMode(Player.REPEAT_MODE_ALL);


        // Set PlayWhenReady. If true, content and ads will autoplay.
        player2.setPlayWhenReady(true);

        player2.setVolume(0f);
    }



    @Override
    public void onStart() {
        super.onStart();
        //
        if (Util.SDK_INT > 23) {
            initializePlayer();
            initializePlayer2();
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
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InPlayerActivity.this, HomeActivity.class));
        finish();
    }
}
