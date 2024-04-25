package com.ad.sdk.adserver;

import android.content.Context;
import android.net.Uri;

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

public class NativeVideo implements Player.Listener {

    private ExoPlayer player;
    private ImaAdsLoader adsLoader;

    public NativeVideo() {

    }

    public void loadNativeVideo(StyledPlayerView playerView, Context context)
    {
        MultiDex.install(context);
        playerView.setControllerAutoShow(false);
        // Create an AdsLoader.
        adsLoader = new ImaAdsLoader.Builder(context).build();

        initializePlayer(context,playerView);
    }

    private void initializePlayer(Context context,StyledPlayerView playerView) {
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
        Uri adTagUri = Uri.parse(context.getString(R.string.ad_SDK_tag_url2));
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
        player.addListener(this);

        player.setVolume(0f);
    }
}
