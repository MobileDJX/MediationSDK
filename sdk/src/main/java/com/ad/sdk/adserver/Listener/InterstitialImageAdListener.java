package com.ad.sdk.adserver.Listener;

public interface InterstitialImageAdListener {

    void onInterstitialAdLoaded();

    void onInterstitialAdFailed();

    void onInterstitialAdShown();

    void onInterstitialAdClicked();

    void onInterstitialAdDismissed();
}
