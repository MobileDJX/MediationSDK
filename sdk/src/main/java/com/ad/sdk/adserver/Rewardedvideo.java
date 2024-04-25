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

import com.ad.sdk.adserver.Listener.RewardedAdListener;
import com.ad.sdk.mtrack.Device_settings;
import com.ad.sdk.utils.LoadData;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyZone;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Mediation;
import com.chartboost.sdk.ads.Rewarded;
import com.chartboost.sdk.callbacks.RewardedCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.RewardEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InitializationListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

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

public class Rewardedvideo {

    RewardedAd rewardedAd;

    String rewardedItem;
    int rewardAmount;

    int adRefreshCount = 0;

    JSONArray zoneList;
    int zonelistSize;

    String ad_network_type, ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneId;
    String click, imp, response, request;
    int loopCount = 0;

    public static final String TAG = "RewardedVideo";
    public static RewardedAdListener rewardadListener = null;


    //ChartBoost
    private Rewarded chartboostRewarded = null;


    public RewardedAdListener getRewardadListen() {
        return rewardadListener;
    }

    public void setRewardadListen(RewardedAdListener rewardadListen) {
        this.rewardadListener = rewardadListen;
    }

    public Rewardedvideo() {
    }

    public void loadRewardedAd(Context context, RewardedAdListener listener) {
        setRewardadListen(listener);
        try {
            Activity activity = (Activity) context;

            //Get Zone list
            SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
            String zoneList_String = sharedPreferences.getString("rewardedVideoZoneList", "");


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
                    loadAdMobAd(context, activity, ad_unit, imp, click);
                }

                if (ad_network_type.equalsIgnoreCase("Adcolony")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "Adcolony Started", Toast.LENGTH_SHORT).show();

                    loadAdColony(context, app_id, zoneId, imp, click, response);
                }

                if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "ChartBoost Started", Toast.LENGTH_SHORT).show();


                    loadChartBoost(context, app_id, app_signature, imp, click, response);
                }

                if (ad_network_type.equalsIgnoreCase("IronSource")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "IronSource Started", Toast.LENGTH_SHORT).show();


                    loadIronSource(context, app_id, imp, click, response);
                }

                if (ad_network_type.equalsIgnoreCase("Unity")) {
                    adRefreshCount = 0;
                    MediationTracking(context, request);
                    adRefreshCount = 1;

                    Toast.makeText(context, "Unity Started", Toast.LENGTH_SHORT).show();

                    loadUnity(context, gameID, placementId, testMode, imp, click, response);
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
                        new Rewardedvideo().loadRewardedAd(context, (RewardedAdListener) context);
                    }
                }, 4000);


            }

            //Internal
            else {
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);
                String ad_url = sharedPreferences1.getString("RewardedVideo_URL", "");

                System.out.println("@@ RewardedVideo_URL ad_url " + ad_url);
                if (ad_url.length() > 0) {
                    Intent i = new Intent(context, RewardedLoadActivity.class);
                    context.startActivity(i);
                } else {
                    Log.d("SDK", "No Ads:");
                }
            }


        } catch (Exception e) {
            Log.d("SDK", "Rewardedvideo Ad Exception:" + e);
            return;
        }
    }


    private void getAdDatas(Context adViewContext) throws JSONException {
        SharedPreferences sharedPreferences = adViewContext.getSharedPreferences("ZoneList", MODE_PRIVATE);
        String zoneList_String = sharedPreferences.getString("rewardedVideoZoneList", "");

        JSONArray zoneList = new JSONArray(zoneList_String);

        Log.e("Banner : ", "zoneList :" + zoneList);
        Log.e("Banner : ", "zoneListSize :" + zoneList.length());

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
    private void loadAdMobAd(Context context, Activity activity, String AD_UNIT_ID, String imp, String click) {

        Log.e("adMobIMP", imp);
        Log.e("adMobCLICK", click);
        Log.e("adMobREQ", request);
        Log.e("adMobRES", response);

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "Initialize Completed.");

            }
        });

        if (rewardedAd == null) {

            AdRequest adRequest = new AdRequest.Builder().build();

            RewardedAd.load((Activity) context, AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAds) {


                    adRefreshCount = 0;
                    MediationTracking(context, response);

                    Rewardedvideo.this.rewardedAd = rewardedAds;

                    Log.d(TAG, "onAdLoaded");

                    rewardedAds.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Log.e("Rewarded Item :", " " + rewardItem.getType());
                            Log.e("Rewarded Amount :", " " + rewardItem.getAmount());

                            rewardedItem = rewardItem.getType();
                            rewardAmount = rewardItem.getAmount();

                            Rewardedvideo.rewardadListener.Rewarded(rewardedItem, rewardAmount);

                            SharedPreferences sharedPreferences = context.getSharedPreferences("RewardPoint", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("Point", rewardItem.getAmount());
                            editor.apply();

                        }
                    });


                    rewardedAds.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            Log.e(TAG, "Status :" + "Ad Clicked...");
                            Log.e(TAG, "Click URL  :" + click);

                            adRefreshCount = 0;
                            MediationTracking(context, click);

                            Toast.makeText(context, "AdMob Clicked", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                            adRefreshCount = 0;
                            MediationTracking(context, imp);
                            Toast.makeText(context, "AdMob Impression Triggered", Toast.LENGTH_SHORT).show();

                        }
                    });


                    if (rewardAmount != 0) {
                        Toast.makeText(context, "Reward Amount : " + rewardAmount, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);

                    Toast.makeText(context, "AdMob Failure", Toast.LENGTH_SHORT).show();


                    loopCount++;
                    Log.d(TAG, loadAdError.getMessage());

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
                                SharedPreferences sharedPreferences1 = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);
                                String ad_url = sharedPreferences1.getString("RewardedVideo_URL", "");

                                System.out.println("@@ RewardedVideo_URL ad_url " + ad_url);
                                if (ad_url.length() > 0) {
                                    Intent i = new Intent(context, RewardedLoadActivity.class);
                                    context.startActivity(i);
                                } else {
                                    Log.d("SDK", "No Ads:");
                                }

                            }
                        }, 3000);

                    } else {

                        loadRewardedAd(context, (RewardedAdListener) context);

                    }


                }
            });


        }
    }


    //AdColony
    public void loadAdColony(Context context, String APP_ID, String Rewarded_Zone_ID, String imp, String click, String response) {


        Log.e("adColIMP", imp);
        Log.e("adColCLICK", click);
        Log.e("adColREQ", request);
        Log.e("adColRES", response);

        AdColonyAppOptions appOptions = new AdColonyAppOptions().setUserID("userid");

        AdColony.configure((Activity) context, appOptions, APP_ID);


//        AdColony.configure((Activity) context, APP_ID, Rewarded_Zone_ID);

        AdColonyInterstitial[] ad = new AdColonyInterstitial[2];

        AdColonyRewardListener listenerRew = new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                Log.e("AdColonyRewards", "RewardItem : " + reward.getRewardAmount());
            }
        };

        AdColony.setRewardListener(listenerRew);


        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {

                Log.d(TAG, "Adcolony Request Status :" + "Filled");


                ad[1] = adColonyInterstitial;
                ad[1].show();

                adRefreshCount = 0;
                MediationTracking(context, response);


                adRefreshCount = 0;
                MediationTracking(context, imp);

                Toast.makeText(context, "AdColony Impression Triggered", Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);

                Toast.makeText(context, "AdColony Failure", Toast.LENGTH_SHORT).show();


                loopCount++;
                Log.d(TAG, "Adcolony Request Status :" + "Not Filled");

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
                            SharedPreferences sharedPreferences1 = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences1.getString("RewardedVideo_URL", "");

                            System.out.println("@@ RewardedVideo_URL ad_url " + ad_url);
                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, RewardedLoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads:");
                            }

                        }
                    }, 3000);

                } else {

                    loadRewardedAd(context, (RewardedAdListener) context);

                }

            }


            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
                adRefreshCount = 0;
                MediationTracking(context, click);
                Toast.makeText(context, "AdColony Clicked", Toast.LENGTH_SHORT).show();

            }
        };


        AdColonyAdOptions options = new AdColonyAdOptions()
                .enableConfirmationDialog(false)
                .enableResultsDialog(false);

        AdColony.requestInterstitial(Rewarded_Zone_ID, listener, options);

    }


    //IronSource
    public void loadIronSource(Context context, String APP_ID, String imp, String click, String response) {

        Log.e("IronIMP", imp);
        Log.e("IronCLICK", click);
        Log.e("IronREQ", request);
        Log.e("IronRES", response);

        IronSource.init((Activity) context, APP_ID, new InitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.e(TAG, "Iron Source : " + " Initialize Status : " + "Successfully..");
            }
        }, IronSource.AD_UNIT.REWARDED_VIDEO);


        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
            @Override
            public void onRewardedVideoAdOpened() {

                adRefreshCount = 0;
                MediationTracking(context, response);

                Log.d(TAG, "IronSource Video Status :" + "Opened");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Log.d(TAG, "IronSource Video Status :" + "Closed");
            }

            @Override
            public void onRewardedVideoAvailabilityChanged(boolean b) {

                Log.d(TAG, "IronSource Video Availability :" + b);
                if (b) {

                    IronSource.loadRewardedVideo();

                } else {
                    Log.d(TAG, "IronSource Video Available :" + "Not Available");
                }

            }

            @Override
            public void onRewardedVideoAdStarted() {
                adRefreshCount = 0;
                MediationTracking(context, imp);

                Toast.makeText(context, "IronSouce Impression Triggered", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "IronSource Video Status :" + "Started");
            }

            @Override
            public void onRewardedVideoAdEnded() {
                Log.d(TAG, "IronSource Video Status :" + "Ended");
            }

            @Override
            public void onRewardedVideoAdRewarded(Placement placement) {

                Log.e("IronSource", "Reward Item : " + placement.getRewardAmount());

            }

            @Override
            public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {

                Toast.makeText(context, "IronSource Failure", Toast.LENGTH_SHORT).show();

                loopCount++;
                Log.d(TAG, "IronSource Video Status :" + "Load Failed : " + "Error : " + ironSourceError.getErrorMessage());

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
                            SharedPreferences sharedPreferences1 = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences1.getString("RewardedVideo_URL", "");

                            System.out.println("@@ RewardedVideo_URL ad_url " + ad_url);
                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, RewardedLoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads:");
                            }

                        }
                    }, 3000);

                } else {

                    loadRewardedAd(context, (RewardedAdListener) context);

                }

            }

            @Override
            public void onRewardedVideoAdClicked(Placement placement) {
                adRefreshCount = 0;
                MediationTracking(context, click);
                Log.d(TAG, "IronSource Video Status :" + "Clicked");

                Toast.makeText(context, "IronSource Clicked", Toast.LENGTH_SHORT).show();

            }


        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IronSource.showRewardedVideo();
            }
        }, 5000);


    }


    //Unity
    public void loadUnity(Context context, String unityGameID, String adUnitId, String testMode, String imp, String click, String response) {


        Log.e("unityIMP", imp);
        Log.e("unityCLICK", click);
        Log.e("unityREQ", request);
        Log.e("unityRES", response);


        IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {

                UnityAds.show((Activity) context, adUnitId, new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {

                        Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);


                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {


                        adRefreshCount = 0;
                        MediationTracking(context, response);
                        adRefreshCount = 0;
                        MediationTracking(context, imp);
                        Log.e("Unity Ads", "Video :" + "Started");

                        Toast.makeText(context, "Unity Impression Triggered", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {

                        adRefreshCount = 0;
                        MediationTracking(context, click);
                        Log.e("Unity Ads", "Video :" + "Clicked");
                        Toast.makeText(context, "Unity Clicked", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {

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
                            SharedPreferences sharedPreferences1 = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences1.getString("RewardedVideo_URL", "");

                            System.out.println("@@ RewardedVideo_URL ad_url " + ad_url);
                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, RewardedLoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads:");
                            }

                        }
                    }, 3000);

                } else {

                    loadRewardedAd(context, (RewardedAdListener) context);

                }
            }
        };
        UnityAds.initialize(context, unityGameID, Boolean.parseBoolean(testMode), new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.e("UnityAdsExample", "Unity Ads initialization Successfully");

            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [" + error + "] " + message);

            }
        });


        UnityAds.load(adUnitId, loadListener);

    }


    //ChartBoost
    public void loadChartBoost(Context context, String appID, String appSignature, String imp, String click, String response) {


        Log.e("chartIMP", imp);
        Log.e("chartCLICK", click);
        Log.e("chartREQ", request);
        Log.e("chartRES", response);


        Chartboost.startWithAppId(context, appID, appSignature, startError -> {
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
                            SharedPreferences sharedPreferences1 = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);
                            String ad_url = sharedPreferences1.getString("RewardedVideo_URL", "");

                            System.out.println("@@ RewardedVideo_URL ad_url " + ad_url);
                            if (ad_url.length() > 0) {
                                Intent i = new Intent(context, RewardedLoadActivity.class);
                                context.startActivity(i);
                            } else {
                                Log.d("SDK", "No Ads:");
                            }

                        }
                    }, 3000);

                } else {

                    loadRewardedAd(context, (RewardedAdListener) context);

                }

            }
        });
        Mediation mediation = new Mediation("Mediation", "1.0.0", "1.0.0.1");

        RewardedCallback rewardedCallback = new RewardedCallback() {
            @Override
            public void onRewardEarned(@NonNull RewardEvent rewardEvent) {

                Log.e("Rewarded onAdDismiss:", rewardEvent.getAd().getLocation() + " reward: " + rewardEvent.getReward());
            }

            @Override
            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Dismissed");

            }

            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                Log.e("ChartBoost CacheEvent", "cacheEvent : " + cacheEvent.getAd());
                Log.e("ChartBoost CacheEvent", "cacheError : " + cacheError);
                chartboostRewarded.show();

                if (chartboostRewarded.isCached()) {
                    adRefreshCount = 0;
                    MediationTracking(context, response);
                }


            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Request to Show");

                adRefreshCount = 0;
                MediationTracking(context, imp);

                Toast.makeText(context, "ChartBoost Impression Triggered", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

                Log.i("ChartBoost Status", "ChartBoost Ad Shown...");

//                adRefreshCount = 0;
//                MediationTracking(context, imp);
//
//                Toast.makeText(context, "ChartBoost Impression Triggered", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {
                Log.i("ChartBoost Status", "ChartBoost Ad Clicked");
                adRefreshCount = 0;
                MediationTracking(context, click);
                Toast.makeText(context, "ChartBoost Clicked", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Impression Recorded");


            }
        };

        chartboostRewarded = new Rewarded("start", rewardedCallback, mediation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chartboostRewarded.cache();
            }
        }, 1200);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if (chartboostRewarded.isCached()) {
//                    chartboostRewarded.show();
//                }
//            }
//        }, 3000);


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



