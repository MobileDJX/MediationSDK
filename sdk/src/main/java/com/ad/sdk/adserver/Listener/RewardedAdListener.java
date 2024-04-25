package com.ad.sdk.adserver.Listener;

import java.util.Set;

public interface RewardedAdListener {
    void Rewarded(String rewardItem, int rewardvalue);

    void AdLoaded();

    void AdFailed();

    void Adclosed();

    void Adclicked();

    void Adshown();

}
