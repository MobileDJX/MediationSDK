package com.ad.sdk.adserver;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ad.sdk.adserver.Listener.InterstitialImageAdListener;
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
import com.google.android.gms.ads.AdError;
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

public class Interstitial_image {

    //AdColony
    AdColonyInterstitial adColonyInterstitial;
    boolean isInterstitialLoaded;
    AdColonyInterstitialListener adColony_listener;
    AdColonyAdOptions adColony_adOptions;
    int adRefreshCount = 0;


    //ChartBoost
    private Interstitial chartboostInterstitial = null;
    PopupWindow pop;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "Interstitial_image";

    JSONArray zoneList;
    int zonelistSize;

    String ad_network_type, ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneId;
    String adMob_click, adMob_imp, adMob_response, adMob_request;
    String adColony_click, adColony_imp, adColony_response, adColony_request;
    String unity_click, unity_imp, unity_response, unity_request;
    String iron_click, iron_imp, iron_response, iron_request;
    String chart_click, chart_imp, chart_response, chart_request;
    int loopCount = 0;


    public Interstitial_image() {
    }

    public void loadInterstital(Context context, InterstitialImageAdListener listener) {
        try {
            Activity activity = (Activity) context;

            //Get Zone list
            SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
            String zoneList_String = sharedPreferences.getString("interstitialZoneList", "");

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
                    MediationTracking(context, adMob_request);
                    adRefreshCount = 1;
                    Toast.makeText(context, "AdMob Started", Toast.LENGTH_SHORT).show();

                    loadInterstitialImageMediationAdMob(context, activity, ad_unit, adMob_imp, adMob_click, adMob_response);

                } else if (ad_network_type.equalsIgnoreCase("Adcolony")) {
//                    MediationTracking(context, adColony_request);
//
//
//                    loadInterstitialImageMediation_AdColony(context, app_id, zoneId, adColony_imp, adColony_click, adColony_response);


                } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
//                    MediationTracking(context, iron_request);
//
//
//                    loadInterstitial_IronSource(context, app_id, iron_imp, iron_click, iron_response);


                } else if (ad_network_type.equalsIgnoreCase("Unity")) {
//                    MediationTracking(context, unity_request);
//
//
//                    loadUnityInterstitial(context, gameID, placementId, testMode, unity_imp, unity_click, unity_response);


                } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
//                    MediationTracking(context, chart_request);

//                    loadChartBoost(context, app_id, app_signature, chart_imp, chart_click, chart_response);

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
                        new Interstitial_image().loadInterstital(context, (InterstitialImageAdListener) context);
                    }
                }, 4000);


            } else {
                new Interstitial_InterAds().loadInt(context, (InterstitialImageAdListener) context);

            }
        } catch (Exception e) {
            Log.d("SDK", "Interstital Image Ad Exception:" + e);
        }


    }

    private void getAdDatas(Context adViewContext) throws JSONException {
        SharedPreferences sharedPreferences = adViewContext.getSharedPreferences("ZoneList", MODE_PRIVATE);
        String zoneList_String = sharedPreferences.getString("interstitialZoneList", "");

        JSONArray zoneList = new JSONArray(zoneList_String);

        zonelistSize = zoneList.length();

        Log.e("Interstitial : ", "zoneList :" + zoneList);
        Log.e("Interstitial : ", "zoneListSize :" + zoneList.length());


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
    public void loadInterstitialImageMediationAdMob(Context context, Activity activity, String adunit, String impression, String click, String response) {

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        com.google.android.gms.ads.AdRequest adRequest = new AdRequest.Builder().build();


        mInterstitialAd.load(context, adunit, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

                adRefreshCount = 0;
                MediationTracking(context, response);
                adRefreshCount = 1;

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Log.d(TAG, "Get Ad Impression");
                        adRefreshCount = 0;
                        MediationTracking(context, impression);
                        adRefreshCount = 1;

                        Toast.makeText(context, "AdMob Impression Triggered", Toast.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Log.d(TAG, "Failure : " + "Error : " + adError.getMessage());

                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();

                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.");
                        adRefreshCount = 0;
                        MediationTracking(context, click);
                        adRefreshCount = 1;

                        Toast.makeText(context, "AdMob Clicked", Toast.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Log.d(TAG, "Ad was Dismissed.");

                    }
                });

                Log.i(TAG, "onAdLoaded");

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(activity);

                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                }
            }


            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
                Toast.makeText(context, "AdMob Failure", Toast.LENGTH_SHORT).show();

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
                            new Interstitial_InterAds().loadInt(context, (InterstitialImageAdListener) activity);
                        }
                    }, 4000);
                } else {
                    loadInterstital(context, (InterstitialImageAdListener) activity);
                }
            }
        });
    }


    //AdColony
    public void loadInterstitialImageMediation_AdColony(Context context, String APP_ID, String INTERSTITIAL_ZONE_ID, String imp, String click, String response) {


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
                super.onRequestNotFilled(zone);
                Toast.makeText(context, "AdColony Failed", Toast.LENGTH_SHORT).show();


                Log.e("Adcolony Ad Status", "Interstitial Ad Is Not Loaded Yet or Request Not Filled");

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
                            new Interstitial_InterAds().loadInt(context, (InterstitialImageAdListener) context);
                        }
                    }, 4000);
                } else {
                    loopCount++;
                    loadInterstital(context, (InterstitialImageAdListener) context);
                }
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
                adRefreshCount = 0;
                MediationTracking(context, imp);
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
                adRefreshCount = 1;
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
                    SharedPreferences sharedPreferences = context.getSharedPreferences("Djaxdemo", MODE_PRIVATE);
                    String zone_id = sharedPreferences.getString("Zone_ID", "");
                    Device_settings.getSettings(context).mediation = "1";

                    com.ad.sdk.adserver.AdView adView = new com.ad.sdk.adserver.AdView(context);
                    adView.setZoneid(zone_id);
                    adView.LoadAd();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Interstitial_InterAds().loadInt(context, (InterstitialImageAdListener) context);

                        }
                    }, 3000);

                } else {

                    loadInterstital(context, (InterstitialImageAdListener) context);

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
                adRefreshCount = 1;

            }

            @Override
            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onInterstitialAdClicked() {

                adRefreshCount = 0;
                MediationTracking(context, click);
                adRefreshCount = 1;

            }
        };

        IronSource.setInterstitialListener(interstitialListener);


    }


    //Unity
    void loadUnityInterstitial(Context context, String unityGameID, String adUnitID, String testMode, String imp, String click, String response) {

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
                        adRefreshCount = 0;
                        MediationTracking(context, response);
                        MediationTracking(context, imp);

                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {
                        Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
                        adRefreshCount = 0;
                        MediationTracking(context, click);

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

                Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                loopCount++;
                Log.e("UnityAdsExample", "Loop Count : " + loopCount);

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
                            new Interstitial_InterAds().loadInt(context, (InterstitialImageAdListener) context);
                        }
                    }, 4000);
                } else {

                    loadInterstital(context, (InterstitialImageAdListener) context);
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
                Toast.makeText(context, "ChartBoost Failure", Toast.LENGTH_SHORT).show();


                Log.i("ChartBoost Status", "SDK initialized with error:" + startError.getCode().name());

                loopCount++;

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
                            new Interstitial_InterAds().loadInt(context, (InterstitialImageAdListener) context);
                        }
                    }, 4000);
                } else {
                    loadInterstital(context, (InterstitialImageAdListener) context);
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
                chartboostInterstitial.show();
                adRefreshCount = 0;
                MediationTracking(context, response);
            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Request to Show");
            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {
                Log.i("ChartBoost Status", "ChartBoost Ad Shown...");
            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {
                adRefreshCount = 0;
                MediationTracking(context, click);
            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {
                Log.i("ChartBoost Status", "ChartBoost Ad Impression Recorded");
                adRefreshCount = 0;
                MediationTracking(context, imp);
            }

        };

        chartboostInterstitial = new Interstitial("start", callback, mediation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chartboostInterstitial.cache();
            }
        }, 1200);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (chartboostInterstitial.isCached()) {
//                    chartboostInterstitial.show();
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

    class MyUrlRequestCallback extends UrlRequest.Callback {
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
