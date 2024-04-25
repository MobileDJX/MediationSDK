package com.ad.sdk.adserver;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ad.sdk.mtrack.Device_settings;
import com.ad.sdk.utils.LoadData;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Mediation;
import com.chartboost.sdk.ads.Interstitial;
import com.chartboost.sdk.callbacks.InterstitialCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InitializationListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InterstitialVideo {

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "Interstitial_Video";


    //AdColony
    AdColonyInterstitial adColonyInterstitial;
    boolean isInterstitialLoaded;
    AdColonyInterstitialListener adColony_listener;
    AdColonyAdOptions adColony_adOptions;

    //ChartBoost
    private Interstitial chartboostInterstitial = null;

    int adRefreshCount = 0;
    JSONArray zoneList;
    int zonelistSize;

    String ad_network_type, ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneId;
    String click, imp, response, request;
    int loopCount = 0;

    public InterstitialVideo() {


    }

    public void loadAdShow(Context context) {
        try {


            Activity activity = (Activity) context;

            //Get zone list
            SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
            String zoneList_String = sharedPreferences.getString("interstitialVideoZoneList", "");

            //Get Internal Error
            SharedPreferences sharedPreferencesError = context.getSharedPreferences("InternalError", MODE_PRIVATE);
            String error = sharedPreferencesError.getString("status", "");


            if (!(zoneList_String.isEmpty())) {
                zoneList = new JSONArray(zoneList_String);

                zonelistSize = zoneList.length();
            }

            String getAdtype = new LoadData().getMediationNetworkStatus(context);
            Log.e("Get_AdType", " : " + getAdtype);


            //External
            if (new LoadData().getMediationNetworkStatus(context).equalsIgnoreCase("mediation")) {

                getAdDatas(context);


                if (ad_network_type.equalsIgnoreCase("AdMob")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "AdMob Started", Toast.LENGTH_SHORT).show();

                    loadInterstitialVideoMediationAdMob(context, activity, ad_unit, imp, click, response);


                } else if (ad_network_type.equalsIgnoreCase("Adcolony")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "AdColony Started", Toast.LENGTH_SHORT).show();

                    loadInterstitialImageMediation_AdColony(context, app_id, zoneId, imp, click, response);


                } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "IronSource Started", Toast.LENGTH_SHORT).show();

                    loadInterstitial_IronSource(context, app_id, imp, click, response);


                } else if (ad_network_type.equalsIgnoreCase("Unity")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "Unity Started", Toast.LENGTH_SHORT).show();

                    loadUnityInterstitial(context, gameID, placementId, testMode, imp, click, response);

                } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "ChartBoost Started", Toast.LENGTH_SHORT).show();

                    loadChartBoost(context, app_id, app_signature, imp, click, response);


                }
            } else if (!(error.isEmpty())) {

                SharedPreferences sharedPreferences1 = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                String zone_id = sharedPreferences1.getString("Zone_ID", "");
                Device_settings.getSettings(context).mediation = "2";

                com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                adView.setZoneid(zone_id);
                adView.LoadAd();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new InterstitialVideo().loadAdShow(context);
                    }
                }, 4000);


            }

            //Internal
            else {

                SharedPreferences sharedPreferences2 = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
                String ad_url = sharedPreferences2.getString("InterstitialVideo_URL", "");

                if (ad_url.length() > 0) {
                    Intent i = new Intent(context, LoadActivity.class);
                    context.startActivity(i);
                } else {
                    Log.d("SDK", "No Ads");
                }
            }


        } catch (Exception e) {
            Log.d("SDK", "InterstitialVideo Ad Exception: " + e);
        }


    }

    private void getAdDatas(Context adViewContext) throws JSONException {
        SharedPreferences sharedPreferences = adViewContext.getSharedPreferences("ZoneList", MODE_PRIVATE);
        String zoneList_String = sharedPreferences.getString("interstitialVideoZoneList", "");

        JSONArray zoneList = new JSONArray(zoneList_String);

        Log.e("Interstitial_Video : ", "zoneList :" + zoneList);
        Log.e("Interstitial_Video : ", "zoneListSize :" + zoneList.length());

        zonelistSize = zoneList.length();

        for (int i = 0; i < zoneList.length(); i++) {

            JSONObject adObject = zoneList.getJSONObject(loopCount);
            ad_network_type = adObject.getString("ad_network_type");
            click = adObject.getString("click_url");
            imp = adObject.getString("imp_url");
            request = adObject.getString("request_url");
            response = adObject.getString("response_url");


            JSONObject ad_tag = adObject.getJSONObject("ad_tag");

            if (ad_network_type.equalsIgnoreCase("Admob")) {
                ad_unit = ad_tag.getString("adunit").trim();
            } else if (ad_network_type.equalsIgnoreCase("Unity")) {
                gameID = ad_tag.getString("game_id").trim();
                placementId = ad_tag.getString("placement_id").trim();
                testMode = ad_tag.getString("testmode").trim();
            } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
                app_id = ad_tag.getString("app_id").trim();
                app_signature = ad_tag.getString("app_signature").trim();
            } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
                app_id = ad_tag.getString("app_id").trim();
            } else if (ad_network_type.equalsIgnoreCase("Adcolony")) {
                app_id = ad_tag.getString("app_id").trim();
                zoneId = ad_tag.getString("zone_id").trim();
            }
        }
    }


    //AdMob
    public void loadInterstitialVideoMediationAdMob(Context context, Activity activity, String adunit, String imp, String click, String response) {


        Log.e("AdMobIMP", imp);
        Log.e("AdMobCLICK", click);
        Log.e("AdMobREQ", request);
        Log.e("AdMobRES", response);

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e(TAG, "AdMob Initialize Status :" + "Completed");
                Log.e(TAG, "Impression URL  :" + imp);
                MediationTracking(context, imp);
            }
        });

        com.google.android.gms.ads.AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.load(context, adunit, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;

                adRefreshCount = 0;
                MediationTracking(context, response);
                adRefreshCount = 1;

                Log.i(TAG, "onAdLoaded");
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(activity);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {


                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Log.d(TAG, "Get Ad Impression");
                        adRefreshCount = 0;
                        MediationTracking(context, imp);
                        adRefreshCount = 1;
                        Toast.makeText(context, "AdMob Impression Triggered", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Log.e(TAG, "Status :" + "Ad Clicked...");
                        Log.e(TAG, "Click URL  :" + click);
                        adRefreshCount = 0;
                        MediationTracking(context, click);
                        adRefreshCount = 1;
                        Toast.makeText(context, "AdMob Clicked", Toast.LENGTH_SHORT).show();

                    }

                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Toast.makeText(context, "AdMob Failure", Toast.LENGTH_SHORT).show();
                // Handle the error
                Log.d(TAG, loadAdError.toString());
                mInterstitialAd = null;
                loopCount++;

                if (zonelistSize == loopCount) {

                    Toast.makeText(context, "Internal Ads Called", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences2 = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences2.getString("InterstitialVideo_URL", "");

                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, LoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads");
                            }

                        }
                    }, 3000);

                } else {

                    loadAdShow(context);

                }
            }
        });
    }


    //AdColony
    public void loadInterstitialImageMediation_AdColony(Context context, String APP_ID, String INTERSTITIAL_ZONE_ID, String imp, String click, String response) {


        Log.e("AdColIMP", imp);
        Log.e("AdColCLICK", click);
        Log.e("AdColREQ", request);
        Log.e("AdColRES", response);


        final String[] AD_UNIT_Zone_IDS = new String[]{INTERSTITIAL_ZONE_ID};

        AdColonyAppOptions appOptions = new AdColonyAppOptions().setKeepScreenOn(true);
        AdColony.configure((Activity) context, appOptions, APP_ID, AD_UNIT_Zone_IDS);


        adColony_listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adIn) {

                adColonyInterstitial = adIn;

                adRefreshCount = 0;

                isInterstitialLoaded = true;

                adColonyInterstitial.show();
                MediationTracking(context, response);


            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                Toast.makeText(context, "AdColony Failed", Toast.LENGTH_SHORT).show();

                Log.e("Adcolony Ad Status", "Interstitial Ad Is Not Loaded Yet or Request Not Filled");
                loopCount++;
                if (zonelistSize == loopCount) {

                    Toast.makeText(context, "Internal Ads Called", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences2 = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences2.getString("InterstitialVideo_URL", "");

                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, LoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads");
                            }
                        }
                    }, 3000);
                } else {
                    loadAdShow(context);
                }
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);

                adRefreshCount = 0;
                MediationTracking(context, imp);

                Toast.makeText(context, "AdColony Impression Triggered", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                //request New Interstitial

            }

            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);

                adRefreshCount = 0;
                MediationTracking(context, click);
                Toast.makeText(context, "AdColony Clicked", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };

        adColony_adOptions = new AdColonyAdOptions();
        AdColony.requestInterstitial(INTERSTITIAL_ZONE_ID, adColony_listener, adColony_adOptions);

    }


    //IronSource
    public void loadInterstitial_IronSource(Context context, String AD_ID, String imp, String click, String response) {
        Log.e("IronIMP", imp);
        Log.e("IronCLICK", click);
        Log.e("IronREQ", request);
        Log.e("IronRES", response);


        IronSource.init((Activity) context, AD_ID, new InitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.e(TAG, "Iron Source : " + " Initialize Status : " + "Successfully..");
            }
        }, IronSource.AD_UNIT.INTERSTITIAL);


        IronSource.loadInterstitial();

        InterstitialListener interstitialListener = new InterstitialListener() {

            @Override
            public void onInterstitialAdReady() {
                adRefreshCount = 0;
                MediationTracking(context, response);
                Log.e(TAG, "Iron Source : " + " AD Ready... ");
                IronSource.showInterstitial();

            }

            @Override
            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                Toast.makeText(context, "IronSource Failure", Toast.LENGTH_SHORT).show();
                loopCount++;
                Log.e(TAG, "Iron Source : " + " AD Shown Failed... ");

                if (zonelistSize == loopCount) {
                    Toast.makeText(context, "Internal Ads Called", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences2 = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences2.getString("InterstitialVideo_URL", "");

                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, LoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads");
                            }

                        }
                    }, 3000);

                } else {

                    loadAdShow(context);

                }
            }

            @Override
            public void onInterstitialAdOpened() {
            }

            @Override
            public void onInterstitialAdClosed() {

            }

            @Override
            public void onInterstitialAdShowSucceeded() {
                Toast.makeText(context, "IronSource Impression Triggered", Toast.LENGTH_SHORT).show();

                Log.e(TAG, "Iron Source : " + " AD Shown... ");
                adRefreshCount = 0;
                MediationTracking(context, imp);

            }

            @Override
            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onInterstitialAdClicked() {

                adRefreshCount = 0;
                MediationTracking(context, click);

                Toast.makeText(context, "IronSource Clicked", Toast.LENGTH_SHORT).show();


            }
        };

        IronSource.setInterstitialListener(interstitialListener);


    }


    //Unity
    void loadUnityInterstitial(Context context, String unityGameID, String adUnitID, String testMode, String imp, String click, String response) {


        Log.e("UnityIMP", imp);
        Log.e("UnityCLICK", click);
        Log.e("UnityREQ", request);
        Log.e("UnityRES", response);
        //Unity
        UnityAds.initialize(context, unityGameID, Boolean.parseBoolean(testMode), (IUnityAdsInitializationListener) context);

        //Unity Load Listener
        IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                UnityAds.show((Activity) context, adUnitID, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {
                        Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);

                        Toast.makeText(context, "Unity Impression Triggered", Toast.LENGTH_SHORT).show();

                        adRefreshCount = 0;
                        MediationTracking(context, response);
                        adRefreshCount = 0;
                        MediationTracking(context, imp);
                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {
                        Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
                        adRefreshCount = 0;
                        MediationTracking(context, click);

                        Toast.makeText(context, "Unity Clicked", Toast.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
                    }
                });
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {

                Toast.makeText(context, "Unity Failure", Toast.LENGTH_SHORT).show();
                loopCount++;
                Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                if (zonelistSize == loopCount) {

                    Toast.makeText(context, "Internal Ads Called", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences2 = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences2.getString("InterstitialVideo_URL", "");

                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, LoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads");
                            }

                        }
                    }, 3000);

                } else {

                    loadAdShow(context);

                }

            }
        };

        UnityAds.load(adUnitID, loadListener);
    }


    //ChartBoost
    void loadChartBoost(Context context, String appID, String appSignature, String imp, String click, String response) {

        Chartboost.startWithAppId(context.getApplicationContext(), appID, appSignature, startError -> {
            if (startError == null) {
                Log.i("ChartBoost Status", "ChartBoost SDK is initialized Successfully");
            } else {
                Log.i("ChartBoost Status", "SDK initialized with error:" + startError.getCode().name());

                Toast.makeText(context, "ChartBoost Failure", Toast.LENGTH_SHORT).show();
                loopCount++;
                if (zonelistSize == loopCount) {

                    Toast.makeText(context, "Internal Ads Called", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences2 = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences2.getString("InterstitialVideo_URL", "");

                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, LoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads");
                            }

                        }
                    }, 3000);

                } else {

                    loadAdShow(context);

                }

            }
        });

        Mediation mediation = new Mediation("Mediation", "1.0.0", "1.0.0.1");


        InterstitialCallback callback = new InterstitialCallback() {
            @Override
            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

            }

            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {

                if (chartboostInterstitial.isCached()) {

                    adRefreshCount = 0;
                    MediationTracking(context, response);
                    adRefreshCount = 1;
                }

            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Request to Show");

                Toast.makeText(context, "ChartBoost Impression Triggered", Toast.LENGTH_SHORT).show();

                adRefreshCount = 0;
                MediationTracking(context, imp);
                adRefreshCount = 1;
            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {
//                Toast.makeText(context, "ChartBoost Impression Triggered", Toast.LENGTH_SHORT).show();
//                Log.i("ChartBoost Status", "ChartBoost Ad Shown...");
//                adRefreshCount = 0;
//                MediationTracking(context, imp);
//                adRefreshCount = 1;
            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {
                Toast.makeText(context, "ChartBoost Clicked", Toast.LENGTH_SHORT).show();
                Log.i("ChartBoost Status", "ChartBoost Ad Shown...");

                adRefreshCount = 0;
                MediationTracking(context, click);
                adRefreshCount = 1;
            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Impression Recorded");


            }

        };


        chartboostInterstitial = new Interstitial("start", callback, mediation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chartboostInterstitial.cache();
            }
        }, 1200);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (chartboostInterstitial.isCached()) {
                    chartboostInterstitial.show();
                }
            }
        }, 3000);


    }


    //Tracking
    public void MediationTracking(Context context, String URL) {
        if (adRefreshCount == 0) {
            CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
            CronetEngine cronetEngine = myBuilder.build();


            Executor executor = Executors.newSingleThreadExecutor();

            UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(URL, new MyUrlRequestCallback(), executor);

            UrlRequest request = requestBuilder.build();

            request.start();

            Log.e("Event Track", "Method :" + "Finished");
            adRefreshCount = 1;
        }
    }


    static class MyUrlRequestCallback extends UrlRequest.Callback {
        private static final String TAG = "MyUrlRequestCallback";

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
            Log.i(TAG, "onRedirectReceived method called.");
            // You should call the request.followRedirect() method to continue
            // processing the request.
            request.followRedirect();
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            request.read(ByteBuffer.allocateDirect(102400));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.clear();
            request.read(byteBuffer);
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {

        }
    }

}




