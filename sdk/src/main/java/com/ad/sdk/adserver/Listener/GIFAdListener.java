package com.ad.sdk.adserver.Listener;

public interface GIFAdListener {

    void onAdLoaded();

    void onAdFailed();

    void onAdShown();

    void onAdClicked();

    void onAdDismissed();
}
