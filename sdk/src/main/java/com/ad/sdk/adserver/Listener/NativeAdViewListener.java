package com.ad.sdk.adserver.Listener;

import com.ad.sdk.adserver.NativeDatavalue;

import java.util.ArrayList;

public interface NativeAdViewListener {
    void onAdLoad(ArrayList<NativeDatavalue> nativeDatavalues);

    void onAdFail();
}
