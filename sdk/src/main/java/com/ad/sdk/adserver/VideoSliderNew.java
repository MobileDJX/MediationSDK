package com.ad.sdk.adserver;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import com.ad.sdk.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

public class VideoSliderNew implements Player.Listener{

    private ExoPlayer player;
    private ImaAdsLoader adsLoader;
    StyledPlayerView playerView;
    RelativeLayout layout;
    View view;
    Context context;
    Activity activity;
    boolean fullscreen = false;
    public VideoSliderNew(Context context) {
        this.context=context;
    }

    public void loadVideoSliderAds(Context context,RelativeLayout layout)
    {
        MultiDex.install(context);
        this.layout=layout;
        //playerView.setControllerAutoShow(false);
        // Create an AdsLoader.
        adsLoader = new ImaAdsLoader.Builder(context).build();
        initializePlayer();
    }

    private void initializePlayer() {

        activity = (Activity) context;


        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.video_slider, null);
        this.playerView=dialogView.findViewById(R.id.player_view);
        ImageView close = (ImageView) dialogView.findViewById(R.id.img_close_btn);
        ImageView fullscreenButton= (ImageView) dialogView.findViewById(R.id.exo_fullscreen_icon);
        playerView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(context);

        MediaSourceFactory mediaSourceFactory =
                new DefaultMediaSourceFactory(dataSourceFactory)
                        .setAdsLoaderProvider(unusedAdTagUri -> adsLoader)
                        .setAdViewProvider(playerView);

        // Create an ExoPlayer and set it as the player for content and ads.
        player = new ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build();
        playerView.setPlayer(player);
        adsLoader.setPlayer(player);


        loadView(dialogView);



        //loadZoomOutView(dialogView);
        this.view = dialogView;
        // Create the MediaItem to play, specifying the content URI and ad tag URI.
        //Uri contentUri = Uri.parse(getString(R.string.content_url));
        Uri contentUri = Uri.parse("test");
        //Uri adTagUri = Uri.parse(context.getString(R.string.ad_tag_url2));
        Uri adTagUri = Uri.parse("https://demo.reviveadservermod.com/TitanClocks.mp4");
        MediaItem mediaItem =
                new MediaItem.Builder()
                        .setUri(adTagUri)
                        //.setAdsConfiguration(new MediaItem.AdsConfiguration.Builder(adTagUri).build())
                        .build();

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player.setMediaItem(mediaItem);
        player.prepare();

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(true);
        player.addListener(this);



        //player.setVolume(0f);
        dialogView.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerView.onPause();
                dialogView.setVisibility(View.GONE);
                releasePlayer();
            }
        });
        //Activity activity = (Activity) context;
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_open));

                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);



                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = (int) ( 220 * context.getResources().getDisplayMetrics().density);
                    params.height = (int) ( 140 * context.getResources().getDisplayMetrics().density);
                  /*params.width = (int) (225);
                    params.height = (int) (144);*/
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_close));

                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);

                    //new VideoSliderNew(context).loadVideoSliderAds(context,layout);
                    fullscreen = true;
                }
            }
        });

        layout.setFocusableInTouchMode(true);
        layout.requestFocus();
        layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK)
                {
                    releasePlayer();

                }
                    return false;
            }
        });



    }

    private void releasePlayer() {
        if(player!=null)
        {
            adsLoader.setPlayer(null);
            playerView.setPlayer(null);
            player.release();
            player = null;
        }

    }


    public void loadZoomOutView(View view)
    {



        // Create your custom layout
        RelativeLayout relativeLayout = new RelativeLayout(context);
// Create LayoutParams for it // Note 200 200 is width, height in pixels
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
// Align bottom-right, and add bottom-margin
        //params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
       // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //params.bottomMargin = 30;

        relativeLayout.setLayoutParams(params);
        //relativeLayout.setBackgroundColor(Color.BLUE);
        relativeLayout.removeAllViews();
        relativeLayout.removeView(view);
        relativeLayout.addView(view);
// Add the custom layout to your parent layout
        layout.addView(relativeLayout);


    }

    public void loadView(View view)
    {


/*

        // Create your custom layout
        RelativeLayout relativeLayout = new RelativeLayout(context);
// Create LayoutParams for it // Note 200 200 is width, height in pixels
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(550, 350);
// Align bottom-right, and add bottom-margin
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.bottomMargin = 30;

        relativeLayout.setLayoutParams(params);
        //relativeLayout.setBackgroundColor(Color.BLUE);

        relativeLayout.addView(view);
// Add the custom layout to your parent layout
        layout.addView(relativeLayout);
*/


        RelativeLayout relativeLayout = new RelativeLayout(context);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.width = (int) ( 220 * context.getResources().getDisplayMetrics().density);
        params.height = (int) ( 120 * context.getResources().getDisplayMetrics().density);
      /*  params.width = (int) (225);
        params.height = (int) (144);*/
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        playerView.setLayoutParams(params);

        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(relativeLayoutParams);
        /*Button button1 = new Button(context);
        button1.setText("Button1");
        AddButtonLayout(button1, RelativeLayout.ALIGN_PARENT_BOTTOM);*/
        relativeLayout.addView(view);
        //layout.setContentView(relativeLayout, relativeLayoutParams);

        layout.addView(relativeLayout);
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        if (isPlaying) {
            // Active playback.
            view.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.VISIBLE);

        } else {
            // Not playing because playback is paused, ended, suppressed, or the player
            // is buffering, stopped or failed. Check player.getPlayWhenReady,
            // player.getPlaybackState, player.getPlaybackSuppressionReason and
            // player.getPlaybackError for details.
            view.setVisibility(View.GONE);
            playerView.setVisibility(View.GONE);

        }
    }

    private void AddButtonLayout(Button button, int centerInParent)
    {
        LayoutAddButton(button, centerInParent, 0, 0, 0, 0);
    }
    private void LayoutAddButton(Button button, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom)
    {
        RelativeLayout.LayoutParams buttonLayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        buttonLayoutParameters.addRule(centerInParent);
        button.setLayoutParams(buttonLayoutParameters);
    }

  /*  @Override
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
    }

    @Override
    public void onBackPressed() {
        releasePlayer();
    }*/



}

