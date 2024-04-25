package com.ad.sdk.adserver;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ad.sdk.mtrack.Device_settings;
import com.ad.sdk.utils.LoadData;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyZone;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ads.Banner;
import com.chartboost.sdk.callbacks.BannerCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

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

public class BannerAds {

    IronSourceBannerLayout ironImageBanner;

    Banner chartboostBanner;

    String adMob_click, adMob_imp, adMob_response, adMob_request;
    String adColony_click, adColony_imp, adColony_response, adColony_request;
    String unity_click, unity_imp, unity_response, unity_request;
    String iron_click, iron_imp, iron_response, iron_request;
    String chart_click, chart_imp, chart_response, chart_request;

    String ad_network_type, ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneId;

    int adRefreshCount = 0;

    JSONArray zoneList;
    int zonelistSize;

    int loopCount = 0;


    public void loadBannerAd(Context adViewContext, WebView webView) {
        try {


            //Get Zone List
            SharedPreferences sharedPreferencesZoneList = adViewContext.getSharedPreferences("ZoneList", MODE_PRIVATE);
            String zoneList_String = sharedPreferencesZoneList.getString("bannerZoneList", "");

            //Get Internal Error
            SharedPreferences sharedPreferencesError = adViewContext.getSharedPreferences("InternalError", MODE_PRIVATE);
            String error = sharedPreferencesError.getString("status", "");


            if (!(zoneList_String.isEmpty())) {
                zoneList = new JSONArray(zoneList_String);
                zonelistSize = zoneList.length();
            }


            String getAdtype = new LoadData().getMediationNetworkStatus(adViewContext);
            Log.e("adTypeInBanner", " : " + getAdtype);

            if (new LoadData().getMediationNetworkStatus(adViewContext).equalsIgnoreCase("mediation")) {
                getAdDatas(adViewContext);

                if (ad_network_type.equalsIgnoreCase("Admob")) {

                    adRefreshCount = 0;
                    MediationTracking(adViewContext, adMob_request);
                    adRefreshCount = 1;

                    Toast.makeText(adViewContext, "AdMob Started", Toast.LENGTH_SHORT).show();
                    loadAdMobBanner(adViewContext, webView, ad_unit);

                } else if (ad_network_type.equalsIgnoreCase("Unity")) {
                    adRefreshCount = 0;
                    MediationTracking(adViewContext, unity_request);
                    adRefreshCount = 1;
                    Toast.makeText(adViewContext, "Unity Started", Toast.LENGTH_SHORT).show();
                    loadUnityAd(adViewContext, testMode, gameID, placementId, webView);

                } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
                    adRefreshCount = 0;
                    MediationTracking(adViewContext, iron_request);
                    adRefreshCount = 1;
                    Toast.makeText(adViewContext, "IronSource Started", Toast.LENGTH_SHORT).show();
                    loadironSource(adViewContext, app_id, webView);

                } else if (ad_network_type.equalsIgnoreCase("Adcolony")) {
                    adRefreshCount = 0;
                    MediationTracking(adViewContext, adColony_request);
                    adRefreshCount = 1;
                    Toast.makeText(adViewContext, "AdColony Started", Toast.LENGTH_SHORT).show();
                    loadAdColony(adViewContext, app_id, zoneId, webView);

                } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
                    adRefreshCount = 0;
                    MediationTracking(adViewContext, chart_request);
                    adRefreshCount = 1;
                    Toast.makeText(adViewContext, "ChartBoost Started", Toast.LENGTH_SHORT).show();
                    loadChartBoost(adViewContext, app_id, app_signature, webView);
                }

            } else if (!(error.isEmpty())) {

                SharedPreferences sharedPreferences = adViewContext.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                String zone_id = sharedPreferences.getString("Zone_ID", "");
                Device_settings.getSettings(adViewContext).mediation = "2";

                com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(adViewContext);
                adView.setZoneid(zone_id);
                adView.LoadAd();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()   {
                        loadBannerAd(adViewContext, webView);
                    }
                }, 4000);


            } else {
                new BannerInterAds().loadBannerAd(adViewContext, webView);
            }
        } catch (Exception e) {
            Log.d("SDK", "Banner Ad Exception:" + e);
        }
    }

    private void getAdDatas(Context adViewContext) throws JSONException {
        SharedPreferences sharedPreferences = adViewContext.getSharedPreferences("ZoneList", MODE_PRIVATE);
        String zoneList_String = sharedPreferences.getString("bannerZoneList", "");

        JSONArray zoneList = new JSONArray(zoneList_String);

        Log.e("Banner : ", "zoneList :" + zoneList);
        Log.e("Banner : ", "zoneListSize :" + zoneList.length());

        zonelistSize = zoneList.length();

        for (int i = 0; i < zoneList.length(); i++) {

            JSONObject adObject = zoneList.getJSONObject(loopCount);
            ad_network_type = adObject.getString("ad_network_type");

            JSONObject ad_tag = adObject.getJSONObject("ad_tag");

            if (ad_network_type.equalsIgnoreCase("Admob")) {
                ad_unit = ad_tag.getString("adunit").trim();

                //Tracking
                adMob_click = adObject.getString("click_url");
                adMob_imp = adObject.getString("imp_url");
                adMob_request = adObject.getString("request_url");
                adMob_response = adObject.getString("response_url");


            } else if (ad_network_type.equalsIgnoreCase("Unity")) {
                gameID = ad_tag.getString("game_id").trim();
                placementId = ad_tag.getString("placement_id").trim();
                testMode = ad_tag.getString("testmode").trim();


                //Tracking
                unity_click = adObject.getString("click_url");
                unity_imp = adObject.getString("imp_url");
                unity_request = adObject.getString("request_url");
                unity_response = adObject.getString("response_url");


            } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
                app_id = ad_tag.getString("app_id").trim();
                app_signature = ad_tag.getString("app_signature").trim();

                //Tracking
                chart_click = adObject.getString("click_url");
                chart_imp = adObject.getString("imp_url");
                chart_request = adObject.getString("request_url");
                chart_response = adObject.getString("response_url");


            } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
                app_id = ad_tag.getString("app_id").trim();

                //Tracking
                iron_click = adObject.getString("click_url");
                iron_imp = adObject.getString("imp_url");
                iron_request = adObject.getString("request_url");
                iron_response = adObject.getString("response_url");


            } else if (ad_network_type.equalsIgnoreCase("Adcolony")) {
                app_id = ad_tag.getString("app_id").trim();
                zoneId = ad_tag.getString("zone_id").trim();

                //Tracking
                adColony_click = adObject.getString("click_url");
                adColony_imp = adObject.getString("imp_url");
                adColony_request = adObject.getString("request_url");
                adColony_response = adObject.getString("response_url");
            }
        }
    }


    //AdMob
    public void loadAdMobBanner(Context context, WebView webView, String AD_UNIT_ID) {

        String TAG = "BannerAdMob";

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "Initialize Completed.");
            }
        });

        AdView adView = new AdView(context);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                Toast.makeText(context, "AdMob Failed", Toast.LENGTH_SHORT).show();


                Log.e("Admob", "Failed " + "");
                Log.e("Admob", "Error " + loadAdError.getMessage());

                loopCount = loopCount + 1;


                if (zonelistSize == loopCount) {


                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new BannerInterAds().loadBannerAd(context, webView);

                        }
                    }, 4000);

                } else {
                    loadBannerAd(context, webView);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                adRefreshCount = 0;
                MediationTracking(context, adMob_response);
                adRefreshCount = 1;
                Log.e("Admob", "Loaded " + "Successfully");
            }


            @Override
            public void onAdImpression() {
                super.onAdImpression();

                adRefreshCount = 0;
                MediationTracking(context, adMob_imp);
                adRefreshCount = 1;
                Log.e("Admob", "Impression " + "Triggered");
                Toast.makeText(context, "AdMob Impression Triggered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();


                adRefreshCount = 0;
                MediationTracking(context, adMob_click);
                adRefreshCount = 1;
                Log.e("Admob", "AdClicked " + "Triggered");
            }

        });


        Log.e("getAdListener()", " result : " + adView.getAdListener());

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        webView.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

//        loadmediationAdShow(context, adView, webView, AD_UNIT_ID);
    }

    //AdMob Load AD
    public void loadmediationAdShow(Context context, AdView mAdView, WebView view, String adunit) {

        if (view != null) {
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(adunit);
            view.addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }

    }
    //
    //
    //
    //
    //
    //
    //


    //Unity
    private void loadUnityAd(Context adContext, String testMode, String UnityGameID, String PlacementID, WebView view) {

        final BannerView[] bannerView = {null};
        RelativeLayout bannerLayout = new RelativeLayout(adContext);

        UnityAds.initialize(adContext, UnityGameID, Boolean.parseBoolean(testMode));

        bannerView[0] = new BannerView((Activity) adContext, PlacementID, new UnityBannerSize(320, 50));
        final BannerView[] finalBannerView = {bannerView[0]};


        finalBannerView[0].setListener(new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                // Called when the banner is loaded.
                Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.getPlacementId());


                adRefreshCount = 0;
                MediationTracking(adContext, unity_response);
                adRefreshCount = 1;

                if (bannerAdView.isShown()) {
                    adRefreshCount = 0;
                    MediationTracking(adContext, unity_imp);
                    adRefreshCount = 1;
                    Toast.makeText(adContext, "Unity Impression Triggered", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                Log.e("UnityAdsExample", "Unity Ads failed to load banner for " + bannerAdView.getPlacementId() + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage);

                Toast.makeText(adContext, "Unity Failure", Toast.LENGTH_SHORT).show();

                loopCount = loopCount + 1;


                if (zonelistSize == loopCount) {

                    SharedPreferences sharedPreferences = adContext.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(adContext).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(adContext);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new BannerInterAds().loadBannerAd(adContext, view);
                        }
                    }, 4000);
                } else {
                    loadBannerAd(adContext, view);
                }
            }

            @Override
            public void onBannerClick(BannerView bannerAdView) {
                // Called when a banner is clicked.
                Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.getPlacementId());


                adRefreshCount = 0;
                MediationTracking(adContext, unity_click);
                adRefreshCount = 1;
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerAdView) {
                // Called when the banner links out of the application.
                Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.getPlacementId());
            }

        });
        bannerView[0].load();

        bannerLayout.addView(bannerView[0]);

        view.addView(bannerLayout);

//        loadMediationUnityAds(adContext, view);

    }

    //Unity ADS Loader
    public void loadMediationUnityAds(Context context, WebView view) {
        if (view != null) {
            AdView mAdView = new AdView(context);
            view.addView(mAdView);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);
        }
    }
    //
    //
    //
    //
    //
    //
    //

    //IronSource
    private void loadironSource(Context adContext, String AppID, WebView view) {
        FrameLayout ironContainer = new FrameLayout(adContext);

        IronSource.init((Activity) adContext, AppID);

//        IronSourceBannerLayout ironImageBanner = IronSource.createBanner((Activity) adContext, ISBannerSize.BANNER);
        ironImageBanner = IronSource.createBanner((Activity) adContext, ISBannerSize.BANNER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);


        Log.i("IronSource Status", "IronSource SDK is initialized Successfully");

        ironImageBanner.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                Toast.makeText(adContext, "IronSource Ad Loaded", Toast.LENGTH_SHORT).show();


                adRefreshCount = 0;
                MediationTracking(adContext, iron_response);
                adRefreshCount = 1;

                if (ironImageBanner.isShown()) {


                    adRefreshCount = 0;
                    MediationTracking(adContext, iron_imp);
                    adRefreshCount = 1;

                    Toast.makeText(adContext, "IronSource Impression Triggered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                Log.i("IronSource Status", "IronSource Banner Failed" + ironSourceError.getErrorMessage());
                Toast.makeText(adContext, "IronSource Failed", Toast.LENGTH_SHORT).show();


                loopCount = loopCount + 1;

                if (zonelistSize == loopCount) {

                    SharedPreferences sharedPreferences = adContext.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(adContext).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(adContext);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new BannerInterAds().loadBannerAd(adContext, view);
                        }
                    }, 4000);
                } else {
                    loadBannerAd(adContext, view);

                }


            }

            @Override
            public void onBannerAdClicked() {

                adRefreshCount = 0;
                MediationTracking(adContext, iron_click);
                adRefreshCount = 1;
            }

            @Override
            public void onBannerAdScreenPresented() {

            }

            @Override
            public void onBannerAdScreenDismissed() {
                Log.i("IronSource Status", "IronSource Banner AdScreenDismissed");
                IronSource.destroyBanner(ironImageBanner);
            }

            @Override
            public void onBannerAdLeftApplication() {
                Log.i("IronSource Status", "IronSource Banner AdLeftApplication");
                IronSource.destroyBanner(ironImageBanner);
            }
        });


        ironContainer.addView(ironImageBanner, 0, layoutParams);

        IronSource.loadBanner(ironImageBanner);

        view.addView(ironContainer);

//        loadMediationIronSource(adContext, view);

    }

    //IronSource Load AD
    public void loadMediationIronSource(Context context, WebView view) {
        if (view != null) {
            AdView mAdView = new AdView(context);
            view.addView(mAdView);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);
        }
    }
    //
    //
    //
    //
    //
    //
    //


    //AdColony
    private void loadAdColony(Context adViewContext, String AppID, String ZoneID, WebView view) {

        Log.e("adCo_Imp", adColony_imp);
        Log.e("adCo_Click", adColony_click);
        Log.e("adCo_req", adColony_request);
        Log.e("adCo_response", adColony_response);

        AdColonyAdViewListener listener;
        AdColonyAdOptions adOptions;
        final String TAG = "AdColonyBannerDemo";

        RelativeLayout adContainer = new RelativeLayout(adViewContext);

        adOptions = new AdColonyAdOptions();

        AdColonyAppOptions appOptions = new AdColonyAppOptions();
        AdColony.configure((Activity) adViewContext, appOptions, AppID);

        listener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {
                Log.d(TAG, "onRequestFilled");
                adContainer.addView(adColonyAdView);
                view.addView(adContainer);


                adRefreshCount = 0;
                MediationTracking(adViewContext, adColony_response);
                adRefreshCount = 1;

                if (adColonyAdView.isShown()) {


                    adRefreshCount = 0;
                    MediationTracking(adViewContext, adColony_imp);
                    adRefreshCount = 1;
                    Toast.makeText(adViewContext, "AdColony Impression Triggered", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
                Log.d(TAG, "onRequestNotFilled");
                Toast.makeText(adViewContext, "AdColony Failed", Toast.LENGTH_SHORT).show();


                loopCount = loopCount + 1;

                if (zonelistSize == loopCount) {

                    SharedPreferences sharedPreferences = adViewContext.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(adViewContext).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(adViewContext);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new BannerInterAds().loadBannerAd(adViewContext, view);
                        }
                    }, 4000);
                } else {

                    loadBannerAd(adViewContext, view);

                }


            }

            @Override
            public void onOpened(AdColonyAdView ad) {
                super.onOpened(ad);
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onClosed(AdColonyAdView ad) {
                super.onClosed(ad);
                Log.d(TAG, "onClosed");
            }

            @Override
            public void onClicked(AdColonyAdView ad) {
                super.onClicked(ad);
                Log.d(TAG, "onClicked");

                adRefreshCount = 0;
                MediationTracking(adViewContext, adColony_click);
                adRefreshCount = 1;

            }

            @Override
            public void onLeftApplication(AdColonyAdView ad) {
                super.onLeftApplication(ad);
                Log.d(TAG, "onLeftApplication");
            }
        };

        AdColony.requestAdView(ZoneID, listener, AdColonyAdSize.BANNER, adOptions);

//        view.addView(adContainer);

//        loadmediationAdShow_adcolony(adViewContext, view);


    }


    //Adcolony Load AD
    public void loadmediationAdShow_adcolony(Context context, WebView view) {
        if (view != null) {
            AdView mAdView = new AdView(context);
            view.addView(mAdView);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);

        }
    }
    //
    //
    //
    //
    //
    //
    //

    //ChartBoost
    private void loadChartBoost(Context adContext, String AppID, String AppSignature, WebView view) {


        RelativeLayout adContainer = new RelativeLayout(adContext);

        chartboostBanner = new Banner(adContext, "start", Banner.BannerSize.STANDARD, new BannerCallback() {
            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {


                chartboostBanner.show();

                if (chartboostBanner.isCached()) {
                    adRefreshCount = 0;
                    MediationTracking(adContext, chart_response);
                    adRefreshCount = 1;
                }

            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

                adRefreshCount = 0;
                MediationTracking(adContext, chart_click);
                adRefreshCount = 1;
            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

                adRefreshCount = 0;
                MediationTracking(adContext, chart_imp);
                adRefreshCount = 1;
                Toast.makeText(adContext, "ChartBoost Impression Triggered", Toast.LENGTH_SHORT).show();


            }
        }, null);
        Chartboost.startWithAppId(adContext, AppID, AppSignature, startError -> {
            if (startError == null) {
                Log.i("ChartBoost Status", "ChartBoost SDK is initialized Successfully");

            } else {
                Log.i("ChartBoost Status", "SDK initialized with error:" + startError.getCode().name());
                Toast.makeText(adContext, "ChartBoost Failure", Toast.LENGTH_SHORT).show();


                loopCount = loopCount + 1;

                if (zonelistSize == loopCount) {

                    SharedPreferences sharedPreferences = adContext.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(adContext).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(adContext);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new BannerInterAds().loadBannerAd(adContext, view);
                        }
                    }, 4000);
                } else {
                    loadBannerAd(adContext, view);
                }


            }
        });


        adContainer.addView(chartboostBanner);
        view.addView(adContainer);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chartboostBanner.cache();
            }
        }, 1000);


//        loadmediationChartBoost(adContext, view);


    }


    //ChartBoost Load AD
    public void loadmediationChartBoost(Context context, WebView view) {
        if (view != null) {
            AdView mAdView = new AdView(context);
            view.addView(mAdView);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);

        }
    }


    //
    //
    //
    //
    //
    //
    //


    //DB Tracking
    public void MediationTracking(Context context, String URL) {

        Log.i("TrackURL", " : " + URL);

        if (adRefreshCount == 0) {

            CronetEngine.Builder myBuilder = new CronetEngine.Builder(context).enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, 10 * 1024 * 1024);
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
//            request.cancel();
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.clear();
            request.read(byteBuffer);
//            request.cancel();
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
            request.cancel();
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {

        }
    }
}






