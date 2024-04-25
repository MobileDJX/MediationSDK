package com.ad.sdk.adserver;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

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

public class VideoSlider implements Player.Listener{

    private ExoPlayer player;
    private ImaAdsLoader adsLoader;
    StyledPlayerView playerView;

    public VideoSlider() {
    }

    public void loadVideoSliderAds(StyledPlayerView playerView, Context context)
    {
        MultiDex.install(context);
        //playerView.setControllerAutoShow(false);
        // Create an AdsLoader.
        adsLoader = new ImaAdsLoader.Builder(context).build();
        initializePlayer(context,playerView);
    }

    private void initializePlayer(Context context, StyledPlayerView playerView) {
        this.playerView=playerView;
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



    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        if (isPlaying) {
            // Active playback.
            playerView.setVisibility(View.VISIBLE);
        } else {
            // Not playing because playback is paused, ended, suppressed, or the player
            // is buffering, stopped or failed. Check player.getPlayWhenReady,
            // player.getPlaybackState, player.getPlaybackSuppressionReason and
            // player.getPlaybackError for details.
            playerView.setVisibility(View.GONE);
        }
    }

}

