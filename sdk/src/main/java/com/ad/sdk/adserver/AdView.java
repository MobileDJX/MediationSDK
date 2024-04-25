package com.ad.sdk.adserver;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ad.sdk.R;
import com.ad.sdk.adserver.Listener.AdListener;
import com.ad.sdk.adserver.Listener.AdViewListener;
import com.ad.sdk.adserver.Listener.BannerListener;
import com.ad.sdk.adserver.Listener.InterstitialImageAdListener;
import com.ad.sdk.adserver.Listener.NativeAdViewListener;
import com.ad.sdk.mtrack.Device_settings;
import com.ad.sdk.mtrack.Utils;
import com.ad.sdk.utils.Cdlog;
import com.ad.sdk.utils.ConnectionDetecter;
import com.ad.sdk.utils.LoadData;
import com.ad.sdk.utils.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AdView extends FrameLayout {

    private String zoneid = null;
    private int position;
    private String ad_width = null;
    private String ad_height = null;

    private String layer_style = null;
    private String align = null;
    private String padding = null;

    private String ad_tag = null;
    private String ad_type = null;
    WebView webView;
    ArrayList<WebView> multipleViewList = new ArrayList<WebView>();

    private AdViewListener adviewlistener = null;
    private InterstitialImageAdListener interImageadListen = null;
    NativeAdViewListener nativeAdViewListener = null;
    //customprofile
    private String cp_values;


    public void setCustomProfile(String cus_vals) {
        this.cp_values = cus_vals;
    }

    public String getCustomProfile() {
        return cp_values;
    }


    private boolean auto_refresh = false;
    private int auto_refresh_time; // Seconds
    private boolean isInternetPresent = false;
    private FrameLayout ad_container = null;
    private LayoutParams ad_container_params = null;

    private AdListener adListen = null;
    private Context adViewContext = null;

    String find_adtype = "0";


    private static AdFetcher mAdFetcher;

    public AdRequestParam adRequestObj = new AdRequestParam();

    Device_settings settings = null;

    private int measuredWidth;
    private int measuredHeight;
    private boolean measured = false;

    private final Handler handler = new Handler(Looper.getMainLooper());
    //private Displayable lastDisplayable;
    public AdListenerDispatch dispatcher;
    private boolean running;
    private boolean receiversRegistered = false;
    private BroadcastReceiver receiver;

    private BannerListener BanneradListen = null;
    private AdView adv1;

    //In-Article Video Ads values decleare
    private LinearLayout layout;
    private RelativeLayout rlayout;

    private boolean rvvisible = false;
    //In-Article Video Ads values decleare


    private String click_url = null;
    private String imp_url = null;
    private String req_url = null;


    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getImp_url() {
        return imp_url;
    }

    public void setImp_url(String imp_url) {
        this.imp_url = imp_url;
    }

    public String getreq_url() {
        return req_url;
    }

    public void setreq_url(String req_url) {
        this.req_url = req_url;
    }

    public ArrayList<WebView> getMultipleViewList() {
        return multipleViewList;
    }

    public void setMultipleViewList(ArrayList<WebView> multipleViewList) {
        this.multipleViewList = multipleViewList;
    }

    private void setAdListener(AdViewListener listener) {
        Cdlog.d(Cdlog.debugLogTag, "Ad Listener Called..");
        adviewlistener = listener;
    }

    private void setNativeAdViewListener(NativeAdViewListener listener) {
        Cdlog.d(Cdlog.debugLogTag, "Ad Listener Called..");
        nativeAdViewListener = listener;
    }

    public String getFind_adtype() {
        return find_adtype;
    }

    public void setFind_adtype(String find_adtype) {
        this.find_adtype = find_adtype;
    }

    //basic ads
    private void setAdListener(BannerListener listener) {
        Cdlog.d(Cdlog.debugLogTag, "Ad Listener Called..");
        BanneradListen = listener;
    }

    //Interstitial Image ads
    private void setAdListener(InterstitialImageAdListener listener) {
        Cdlog.d(Cdlog.debugLogTag, "Ad Listener Called..");
        interImageadListen = listener;
    }

    //Interstitial image listener
    public InterstitialImageAdListener getInterImageadListen() {
        return interImageadListen;
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    //basic ads
    public AdView(Context context, BannerListener bannerListener) {
        super(context);
        setAdListener(bannerListener);
        setup(context, null);
    }

    public AdView(Context context) {
        super(context);
        setup(context, null);
    }


    //Interstitial Image ads
    public AdView(Context context, InterstitialImageAdListener listener) {
        super(context);
        setAdListener(listener);
        setup(context, null);
    }

    //In-Artical Video Ads Start
    public AdView(Context context, LinearLayout adview, AdViewListener listener) {
        super(context);
        this.adViewContext = context;
        layout = adview;
        setAdListener(listener);

    }

    //Native Ads Start
    public AdView(Context context, NativeAdViewListener nativeAdViewListener) {
        super(context);
        this.adViewContext = context;
        find_adtype = "2";
        setup(context, null);
        setNativeAdViewListener(nativeAdViewListener);
    }


    public AdView(Context context, RelativeLayout adview) {
        super(context);
        this.adViewContext = context;
        rlayout = adview;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    @SuppressLint("Recycle")
    private void setup(Context context, AttributeSet attrs) {
        dispatcher = new AdListenerDispatch(handler);

        mAdFetcher = new AdFetcher(AdView.this);

        this.adViewContext = context;

        // Store self.context in the settings for errors
        Cdlog.error_context = this.getContext();

        Cdlog.d(Cdlog.publicFunctionsLogTag, Cdlog.getString(R.string.new_adview));

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings = Device_settings.getSettings(context);
        if (prefs.getBoolean("msdk_first_launch", true)) {
            // This is the first launch, store a value to remember
            Cdlog.v(Cdlog.baseLogTag,
                    Cdlog.getString(R.string.first_opensdk_launch));
            prefs.edit().putBoolean("msdk_first_launch", false).apply();
        } else {
            // Found the stored value, this is NOT the first launch
            Cdlog.v(Cdlog.baseLogTag,
                    Cdlog.getString(R.string.not_first_opensdk_launch));
        }

        Cdlog.v("mSDK", "Device Info::" + settings.app_id);

        // Load user variables only if attrs isn't null
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable") TypedArray imgAttr = context.obtainStyledAttributes(attrs, R.styleable.ad_param);
            zoneid = imgAttr.getString(R.styleable.ad_param_zone_id);
            ad_width = imgAttr.getString(R.styleable.ad_param_ad_width);
            ad_height = imgAttr.getString(R.styleable.ad_param_ad_height);
            layer_style = imgAttr.getString(R.styleable.ad_param_layer_style);
            setAlign(imgAttr.getString(R.styleable.ad_param_align));
            padding = imgAttr.getString(R.styleable.ad_param_padding);

            if (imgAttr.getInteger(R.styleable.ad_param_auto_refresh_time, 0) > 0) {
                auto_refresh_time = imgAttr.getInteger(R.styleable.ad_param_auto_refresh_time, 30000);
                setAuto_refresh_time(auto_refresh_time);
                Log.d("Inside True Case", "Auto Refresh Time::" + auto_refresh_time);
            } else {
                Log.d("Inside False Case", "Auto Refresh Time::" + auto_refresh_time);
                setAuto_refresh_time(0);

            }
            LoadAd();
        }

        if (getAd_width() == null)
            setAd_width(Utils.convertToString(settings.display_width));

        if (getAd_height() == null)
            setAd_height(Utils.convertToString(settings.display_height));

        // We don't start the ad requesting here, since the view hasn't been
        // sized yet.

        //Set the autorefreshInterval and also autorefresh
//        mAdFetcher.setPeriod(auto_refresh_time);
//        mAdFetcher.setAutoRefresh(isAuto_refresh());
        mAdFetcher.setAdContext(context);
    }

    /**
     * Starts the ad request and fetching the ad
     */

    private void start() {
        Cdlog.d(Cdlog.publicFunctionsLogTag, Cdlog.getString(R.string.start));
        mAdFetcher.start();
        running = true;
    }

    /**
     * Stops the ad request and all other related operations
     */

    private void stop() {
        Cdlog.d(Cdlog.publicFunctionsLogTag, Cdlog.getString(R.string.stop));
        mAdFetcher.stop();
        running = false;
    }


    public void LoadAd(AdView adv1) {

        this.adv1 = adv1;

        Cdlog.i(Cdlog.baseLogTag, "Zone ID::" + zoneid);

        if (this.adViewContext != null) {

            // INTEGRATE AD CONTAINER

            LayoutInflater inflater = (LayoutInflater) this.adViewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            inflater.inflate(R.layout.ad_container, this, true);

            ConnectionDetecter cd = new ConnectionDetecter(adViewContext);

            ad_container = findViewById(R.id.ad_container);

            Cdlog.e(Cdlog.debugLogTag, "Text View Added.");
            isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                // START AD REQUEST TASK
                mAdFetcher.start();
                running = true;

            } else {
                Cdlog.e(Cdlog.debugLogTag, "Internet Connection Failed.");
                dispatcher.internet_connection_failed(AdView.this, true);
            }

        } else {
            Cdlog.e(Cdlog.debugLogTag, "Ad View Not Initialized");
        }
    }

    //In-Article Video Ads End
    public void LoadAd() {


        Cdlog.i(Cdlog.baseLogTag, "Zone ID::" + zoneid);

        if (this.adViewContext != null) {

            // INTEGRATE AD CONTAINER

            LayoutInflater inflater = (LayoutInflater) this.adViewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            inflater.inflate(R.layout.ad_container, this, true);

            ConnectionDetecter cd = new ConnectionDetecter(adViewContext);

            ad_container = findViewById(R.id.ad_container);


            Cdlog.e(Cdlog.debugLogTag, "Text View Added.");

            Log.d("Child Count+", "cc " + ad_container.getChildCount());
            isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {

                // Generate Ad Request Parameters
                //adRequestObj = adRequestObj.GenerateRequestURL(settings, AdView.this);

                // START AD REQUEST TASK
                mAdFetcher.start();
//                running = true;

            } else {
                Cdlog.e(Cdlog.debugLogTag, "Internet Connection Failed.");
                dispatcher.internet_connection_failed(AdView.this, true);
            }

        } else {
            Cdlog.e(Cdlog.debugLogTag, "Ad View Not Initialized");
        }
    }


    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }

    private int getBannerPostion() {
        return position;
    }

    public void setBannerPostion(int position) {
        this.position = position;
    }

    private String getAd_width() {
        return ad_width;
    }

    private void setAd_width(String ad_width) {
        this.ad_width = ad_width;
    }

    private String getAd_height() {
        return ad_height;
    }

    private void setAd_height(String ad_height) {
        this.ad_height = ad_height;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    private void setAd_tag(String ad_tag) {
        this.ad_tag = ad_tag;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    private boolean isAuto_refresh() {
        return auto_refresh;
    }

    private void setAuto_refresh(boolean auto_refresh) {
        Log.d("Inside Set Auto Refresh", "Auto Refresh::" + auto_refresh);
        this.auto_refresh = auto_refresh;

        if (mAdFetcher != null) {
            mAdFetcher.setAutoRefresh(auto_refresh);
            mAdFetcher.clearDurations();
        }
        if (this.auto_refresh && !running && mAdFetcher != null) {
//            start();
        }
    }

    public int getAuto_refresh_time() {
        return auto_refresh_time;
    }

    private void setAuto_refresh_time(int auto_refresh_time) {
        this.auto_refresh_time = Math.max(Settings.getSettings().MIN_REFRESH_MILLISECONDS, auto_refresh_time);

        if (auto_refresh_time > 0) {
            Cdlog.d(Cdlog.baseLogTag, Cdlog.getString(R.string.set_period, auto_refresh_time));
            setAuto_refresh(true);
        } else {
            setAuto_refresh(false);
        }

        Cdlog.d("Auto Refresh Time", "Time :" + this.auto_refresh_time);
        if (mAdFetcher != null)
            mAdFetcher.setPeriod(this.auto_refresh_time);
    }

    /**
     * The view layout
     */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!measured || changed) {

            // Convert to dips
            float density = getContext().getResources().getDisplayMetrics().density;
            measuredWidth = (int) ((right - left) / density + 0.5f);
            measuredHeight = (int) ((bottom - top) / density + 0.5f);
            measured = true;

        }

        // Are we coming back from a screen/user presence change?
        if (running) {
            if (!receiversRegistered) {
                setupBroadcast(getContext());
                receiversRegistered = true;
            }

        }
    }

    private boolean requesting_visible = true;

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            // Register a broadcast receiver to pause and refresh when the phone
            // is
            // locked
            if (!receiversRegistered) {
                setupBroadcast(getContext());
                receiversRegistered = true;
            }
            Cdlog.d(Cdlog.baseLogTag, Cdlog.getString(R.string.unhidden));
            if (mAdFetcher != null
                    && (!requesting_visible || running || auto_refresh) && !rvvisible) {
//                start();

            } else {
                // Were' not displaying the adview, the system is
                requesting_visible = false;
            }

        } else {
            // Unregister the receiver to prevent a leak.
            if (receiversRegistered) {
                dismantleBroadcast();
                receiversRegistered = false;
            }
            Cdlog.d(Cdlog.baseLogTag, Cdlog.getString(R.string.hidden));
            if (mAdFetcher != null && running) {
                stop();
            }
        }
    }

    /**
     * To check the Screen off and on visibility
     *
     * @param context app context
     */

    private void setupBroadcast(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    stop();
                    Cdlog.d(Cdlog.baseLogTag,
                            Cdlog.getString(R.string.screen_off_stop));
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//                    if (auto_refresh)
//                        start();

                    start();
                    Cdlog.d(Cdlog.baseLogTag,
                            Cdlog.getString(R.string.screen_on_start));
                }

            }

        };
        context.registerReceiver(receiver, filter);
    }

    private void dismantleBroadcast() {
        getContext().unregisterReceiver(receiver);
    }


    /**
     * Private class to bridge events from mediation to the user
     * AdListener class.
     */
    class AdListenerDispatch implements AdListener {

        Handler handler;

        AdListenerDispatch(Handler h) {
            handler = h;
        }

        @Override
        public void param_required(final AdView ad, final boolean flag) {
            // TODO Auto-generated method stub
            handler.post(() -> {
                if (adListen != null) {
                    adListen.param_required(ad, flag);
                }
            });
        }

        @Override
        public void internet_connection_failed(final AdView ad, final boolean flag) {
            // TODO Auto-generated method stub
            handler.post(() -> {
                if (adListen != null) {
                    adListen.internet_connection_failed(ad, flag);
                }
            });
        }

        @Override
        public void load_ad_failed(final AdView ad, final boolean flag, final String ecode,
                                   final String edesc) {
            // TODO Auto-generated method stub
            handler.post(() -> {
                if (adListen != null) {
                    Cdlog.d(Cdlog.baseLogTag, "adListen Initialized");
                    adListen.load_ad_failed(ad, flag, ecode, edesc);
                }
            });

        }
    }

    private void setAlign(String align) {
        this.align = align;
    }


    //Native ad
    public void loadNativeImage(AdResponse adResult) {
        new NativeImage().loadNativeImage(adResult.getNativeadsres(), nativeAdViewListener);
    }


    @SuppressLint({"SetJavaScriptEnabled", "ResourceType"})
    public void displayHTMLAd(AdResponse adResult) {
        /*if (BanneradListen != null) {
            Activity activity = (Activity) adViewContext;

            String HtmlCode = adResult.getAdtag();
            //String HtmlCode = Html.fromHtml(adResult.getAdtag()).toString();
            //HtmlCode = "<script type='text/javascript'>function rm364aba82ef(){document.getElementById('image364aba82ef').style.display = 'none';}</script><div id='image364aba82ef' style='display: inline-block; position: relative;' > <a href='https://r3s2.infomo.net/ads/www/delivery/ck.php?oaparams=2__bannerid=220__zoneid=154__OXLCA=1__cb=09d4ee39b9__request_id=NzE3MzI=__bid=MA==__pub=NTA=__admin=NTA=__cid=MTc=__crid=NTU=__ac=MTI=__ar=NjA=__at=MQ==__affid=MTg=__age={age}__gender={gender}__height=50__weight={weight}__oadest=https%3A%2F%2Fwww.google.com' target='_blank' onmouseover=\"self.status='1'; return true;\" onmouseout=\"self.status=''; return true;\"><img src='https://r3s2.infomo.net/ads/www/images/f4dbe40e53aa7b8121c90aeede6fc62d.jpg' width='320' height='50' alt='' title='' border='0' /></a> <div class='cb' aria-hidden='false' style='position: absolute; right: 1px; top: 1px; opacity: 1; height: 15px; width: 15px; z-index: 2147483646; background-color: #ffffff; cursor: pointer;' onclick='rm364aba82ef();'><svg style='position: absolute; top: 0; right: 0; height: 15px; width: 15px; stroke: #00aecd; stroke-width: 1.25;' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' viewBox='0 0 15 15'><path d='M3.25,3.25l8.5,8.5M11.75,3.25l-8.5,8.5'></path></svg></div></div><div id='beacon_09d4ee39b9' style='position: absolute; left: 0px; top: 0px; visibility: hidden;'><img src='https://r3s2.infomo.net/ads/www/delivery/lg.php?bannerid=220&amp;campaignid=55&amp;zoneid=154&amp;OXLIA=1&amp;cb=09d4ee39b9&amp;request_id=NzE3MzI=&amp;bid=MC4wMDAx&amp;pub=NTA=&amp;admin=NTA=&amp;cid=MTc=&amp;crid=NTU=&amp;affid=MTg=&amp;bwidth=320&amp;bheight=50' width='0' height='0' alt='' style='width: 0px; height: 0px;' /></div>";
            if (HtmlCode.length() > 4000) {
                Log.v(TAG, "sb.length = " + HtmlCode.length());
                int chunkCount = HtmlCode.length() / 4000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= HtmlCode.length()) {
                        Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + HtmlCode.substring(4000 * i));
                    } else {
                        Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + HtmlCode.substring(4000 * i, max));
                    }
                }
            }
            Log.d("mSDK Debug", "HTML CODE:" + HtmlCode);
            //WebView htmlAd = new WebView(adViewContext);
            webView.setBackgroundColor(0);
            webView.setPadding(0, 0, 0, 0);
            webView.getSettings().setJavaScriptEnabled(true);
            //String html = "<!DOCTYPE html><html><body style= \"width=\"100%\";height=\"100%\";initial-scale=\"1.0\"; maximum-scale=\"1.0\"; user-scalable=\"no\";\">"+HtmlCode+"</body></html>";
            String html = "<!DOCTYPE html><html><style type='text/css'>html,body {margin: 0;padding: 0;width: 100%;height: 100%;}html {display: table;}body {display: table-cell;vertical-align: middle;text-align: center;}</style><body style= \"width=\"100%\";height=\"100%\";initial-scale=\"1.0\"; maximum-scale=\"1.0\"; user-scalable=\"no\";\">" + HtmlCode + "</body></html>";
            //htmlAd.loadData(html, "text/html", "UTF-8");
            webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
            webView.setClickable(true);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            rvvisible = false;

        } else {
            Log.d("dJAXM", "Invalid ZoneID ");
        }*/

        System.out.println("@@ Response size() = " + adResult.getAdResponseValues().size());
        System.out.println("@@ multiView.size() = " + multipleViewList.size());

        System.out.println("@@ local save data = " + new LoadData().adResponseValues(adViewContext).size());

        try {
            for (int i = 0; i < new LoadData().adResponseValues(adViewContext).size(); i++) {
                if (i < multipleViewList.size()) {
                    for (int k = 0; k < multipleViewList.size(); k++) {
                        if (k < new LoadData().adResponseValues(adViewContext).size()) {
                            Log.d("@@ mulitZone ", "ENCODE HTML CODE:" + new LoadData().adResponseValues(adViewContext).get(k).getAd_tag());
                            //String HtmlCode = Html.fromHtml(adResult.getAdtag()).toString();
                            String HtmlCode = new LoadData().adResponseValues(adViewContext).get(k).getAd_tag();
                            int maxLogSize = 4000;
                            for (int j = 0; j <= HtmlCode.length() / maxLogSize; j++) {
                                int start = j * maxLogSize;
                                int end = (j + 1) * maxLogSize;
                                end = end > HtmlCode.length() ? HtmlCode.length() : end;
                                Log.d("mulitZone", "HTML CODE:" + HtmlCode.substring(start, end));
                            }
                            //WebView htmlAd = new WebView(context);
                            multipleViewList.get(k).setBackgroundColor(0);
                            multipleViewList.get(k).setPadding(0, 0, 0, 0);
                            multipleViewList.get(k).getSettings().setJavaScriptEnabled(true);
                            String html = "<!DOCTYPE html><html>" +
                                    "<style type='text/css'>" +
                                    "html,body {margin: 0;padding: 0;width: 100%;height: 100%;}" +
                                    "html {display: table;}" +
                                    "body {display: table-cell;vertical-align: middle;text-align: center;}" +
                                    "img{display: inline;height: auto;max-width: 100%;}" +
                                    "</style>" +
                                    "<body style= \"width=\"100%\";height=\"100%\";initial-scale=\"1.0\"; maximum-scale=\"1.0\"; user-scalable=\"no\";>" + HtmlCode + "</body></html>";

                            System.out.println("@@ html" + html);
                            multipleViewList.get(k).loadData(html, "text/html", "UTF-8");
                            //ad_container_params = new LayoutParams(-2, -2);
                            //zoneArrayList.get(0).getMultipleView().setLayoutParams(ad_container_params);
                            multipleViewList.get(k).setClickable(true);
                            multipleViewList.get(k).setVerticalScrollBarEnabled(false);
                            multipleViewList.get(k).setHorizontalScrollBarEnabled(false);
                            //ad_container.removeAllViews();
                            //ad_container.addView(htmlAd);
                        }
                    }
                }

            }
        } catch (Exception e) {
            Log.d("SDK", "Exception:" + e);
        }

    }


    @SuppressLint("SetJavaScriptEnabled")
    public void displayVideoAd(AdResponse adResult) {
        Log.d("mSDK Debug", "ENCODE HTML CODE:" + adResult.getAdtag());
        String HtmlCode = Html.fromHtml(adResult.getAdtag()).toString();
        Log.d("mSDK Debug", "HTML CODE:" + HtmlCode);
        WebView htmlAd = new WebView(adViewContext);
        htmlAd.setBackgroundColor(0);
        htmlAd.getSettings().setJavaScriptEnabled(true);
        htmlAd.loadUrl(HtmlCode);
        ad_container_params = new LayoutParams(-2, -2);
        htmlAd.setLayoutParams(ad_container_params);
        htmlAd.setClickable(true);
        htmlAd.setVerticalScrollBarEnabled(false);
        htmlAd.setHorizontalScrollBarEnabled(false);
        ad_container.removeAllViews();
        ad_container.addView(htmlAd);
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void displaycustomsdknetwork(AdResponse adResult) {


        setImp_url(adResult.getImp_url());
        setClick_url(adResult.getClick_url());
        setreq_url(adResult.getReq_url());


        if (adResult.getAd_type().equalsIgnoreCase("Banner")) {
            System.out.println("@@ Mediation Ad Banner : Started");
//            new BannerAds().loadmediationAdShow(adViewContext, webView, adResult.getAdunit());

        } else if (adResult.getAd_type().equalsIgnoreCase("Interstitial")) {
            System.out.println("@@ Mediation Ad Interstitial : Started");
            Activity activity = (Activity) adViewContext;

            SharedPreferences sharedPreferences = activity.getSharedPreferences("Mediation", MODE_PRIVATE);
            String ad_network_type = sharedPreferences.getString("Mediation_ad_network_type", "");

//            if (ad_network_type.equalsIgnoreCase("AdMob")) {
//                new Interstitial_image().loadInterstitialImageMediationAdMob(adViewContext, activity, adResult.getAdunit());
//            } else if (ad_network_type.equalsIgnoreCase("Adcolony")) {
//                new Interstitial_image().loadInterstitialImageMediation_AdColony(adViewContext, "app46ac0830c548470491", "vz53cbd930d4824f42be");
//            } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
//                new Interstitial_image().loadInterstitial_IronSource(adViewContext, "19dcc8f75");
//            } else if (ad_network_type.equalsIgnoreCase("Unity")) {
//                new Interstitial_image().loadUnityInterstitial(adViewContext, "5268614", "android_interstitial");
//            }
        } else if (adResult.getAd_type().equalsIgnoreCase("RewardedVideo")) {
            System.out.println("@@ Mediation Ad RewardedVideo : Started");
            Activity activity = (Activity) adViewContext;

            //  new Rewardedvideo().loadRewardedMediation(adViewContext,activity);
        }

    }


    private void Ad_click(final String click_url, ImageView imgAd) {

        ImageView imgObj = imgAd;

        imgObj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Cdlog.d(Cdlog.debugLogTag, "Image Ad Clicked.");
                Intent internet = new Intent();
                internet.setAction(Intent.ACTION_VIEW);
                internet.addCategory(Intent.CATEGORY_BROWSABLE);
                internet.setData(Uri.parse(click_url));
                adViewContext.startActivity(internet);
            }
        });
    }


    //mediation tecking
    public class MediationEventTracking extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        static final int AdFetchTimeout = 30000;
        public static final int CONNECTION_TIMEOUT = AdFetchTimeout;

        @Override
        protected String doInBackground(String... params) {
            AdRequestParam adRequestObj = new AdRequestParam();

            String result;
            String inputLine;

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(params[0]);
                Log.d("myUrl-1", "myUrl-1" + myUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();

                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }

            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

}