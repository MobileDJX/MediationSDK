package com.ad.sdk.adserver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ad.sdk.R;
import com.ad.sdk.utils.Cdlog;
import com.ad.sdk.utils.LoadData;
import com.ad.sdk.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AdFetcher {

    private AdView owner;
    private static Disposable mDisposable;

    private boolean autoRefresh = false;
    private int period = -1;
    private long lastFetchTime = -1;
    private long timePausedAt = -1;

    //final Handler handler = new Handler(Looper.getMainLooper());
    private final RequestHandler handler;

    private AdResponse adResponseObj;

    private ScheduledExecutorService scheduler;
    public static Context adContext;

    // Fires requests whenever it receives a message
    public AdFetcher(AdView owner) {
        this.owner = owner;
        handler = new RequestHandler(this);
    }

    private boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Context getAdContext() {
        return adContext;
    }

    public void setAdContext(Context adContext) {
        this.adContext = adContext;
    }

    public void stop() {
        try {
            if (mDisposable != null) {
                mDisposable.dispose();
                mDisposable = null;
            }

            if (scheduler == null)
                return;
            scheduler.shutdownNow();
            try {
                scheduler.awaitTermination(period, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                scheduler = null;
                return;
            }
            scheduler = null;
            Cdlog.d(Cdlog.baseLogTag, Cdlog.getString(R.string.stop));
            timePausedAt = System.currentTimeMillis();
        } catch (Exception r) {
            Log.d("DAJX SDK", "R" + r);
        }
    }

    public void start() {
        Cdlog.d(Cdlog.baseLogTag, Cdlog.getString(R.string.start));

        if (scheduler != null) {
            Cdlog.d(Cdlog.baseLogTag, Cdlog.getString(R.string.moot_restart));
            //requestFailed();
            return;
        }
        makeTasker();
    }

    @SuppressLint("NewApi")
    private synchronized void makeTasker() {

        Log.d("mSDK Debug", "Make Tasker Called");

        //Start a scheduler to execute recurring tasks
        scheduler = Executors.newScheduledThreadPool(Settings.getSettings().FETCH_THREAD_COUNT);

        // Get the period from the settings
        //final int msPeriod = owner.getAuto_refresh_time() <= 0 ? 30 * 1000 : owner.getAuto_refresh_time() * 1000;

        final int msPeriod = period <= 0 ? 30 * 1000 : period;

        Cdlog.d("Auto Refresh Enable", "Enable :" + owner.getAuto_refresh_time());

        if (!isAutoRefresh()) {

            Cdlog.v(Cdlog.baseLogTag, Cdlog.getString(R.string.fetcher_start_single));

            //Requesting an ad only once
            scheduler.execute(new MessageRunnable());

            Cdlog.i(Cdlog.baseLogTag, "Single THREAD Called");


        } else {
            Cdlog.v(Cdlog.baseLogTag, Cdlog.getString(R.string.fetcher_start_auto));

            //Start requesting recurring number of ad request
            long temp_stall;
            if (timePausedAt != -1 && lastFetchTime != -1) {
                temp_stall = msPeriod - (timePausedAt - lastFetchTime);
            } else {
                temp_stall = 0;
            }

            final long stall = temp_stall;

            Cdlog.v(Cdlog.baseLogTag,
                    Cdlog.getString(R.string.request_delayed_by_x_ms, stall));
            scheduler.schedule(() -> {
                Cdlog.i(Cdlog.baseLogTag, "AUTO REFRESH THREAD Called");
                // TODO Auto-generated method stub
                Cdlog.v(Cdlog.baseLogTag, Cdlog.getString(
                        R.string.request_delayed_by_x_ms, stall));
                scheduler.scheduleAtFixedRate(new MessageRunnable(), 0, msPeriod, TimeUnit.MILLISECONDS);
            }, stall, TimeUnit.MILLISECONDS);

        }

        Log.d("mSDK Debug", "Period::" + msPeriod);
    }


    /* Message Runnable is the runnable class */

    private class MessageRunnable implements Runnable {

        @Override
        public void run() {
            Cdlog.v(Cdlog.baseLogTag,
                    Cdlog.getString(R.string.handler_message_pass));
            handler.sendEmptyMessage(0);
            Cdlog.i(Cdlog.baseLogTag, "MessageRunnable Called");

        }

    }

    /**
     * Create a handler which will receive the AsyncTasks and also spawn from the
     * main Thread
     **/

    static class RequestHandler extends Handler {
        private final WeakReference<AdFetcher> mAdFetcher;
        private AdFetcher fetcher;

        RequestHandler(AdFetcher f) {
            // TODO Auto-generated constructor stub
            super(Looper.getMainLooper());
            mAdFetcher = new WeakReference<>(f);
        }

        @SuppressLint("NewApi")
        @Override
        synchronized public void handleMessage(@NonNull Message msg) {


            fetcher = mAdFetcher.get();
            //If the adFetcher is vanished foe some reason, do nothing with
            // this message.

            Cdlog.i(Cdlog.baseLogTag, " HANDLE MESSAGE PARAM :  Zone ID::" + fetcher.owner);

            if (fetcher.owner.getZoneid() != null) {

                // Update last fetch time once
                Cdlog.d(Cdlog.baseLogTag,
                        Cdlog.getString(
                                R.string.new_ad_since,
                                (int) (System.currentTimeMillis() - fetcher.lastFetchTime)));
                fetcher.lastFetchTime = System.currentTimeMillis();

                fetcher.owner.adRequestObj = fetcher.owner.adRequestObj.GenerateRequestURL(fetcher.owner.settings, fetcher.owner);
                Cdlog.e("RequestHandler obj", fetcher.owner.adRequestObj.toString());

                mDisposable = new AdRequest(fetcher.owner.adRequestObj,adContext).execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adResponse -> {
                            fetcher.adResponseObj = adResponse;
                            //Handle the Response for the fetcher
//                            handlingResponse(fetcher.adResponseObj);
                        });

            } else {
                // ZONE ID NOT EXISTS
                Cdlog.d(Cdlog.baseLogTag, "Zone ID is required.");
                fetcher.owner.dispatcher.param_required(fetcher.owner, true);
            }
        }

        /**
         * Handle the Response from the given request
         *
         * @param adResponseObj here we receive the ad response
         */

        private void handlingResponse(AdResponse adResponseObj) {
            if (!adResponseObj.isParsingComplete()) {
                Cdlog.i(Cdlog.baseLogTag, "Got Ad Response");
                if (!adResponseObj.isFailed()) {
                    if(adResponseObj.getStatus().equalsIgnoreCase("success"))
                    {
                        fetcher.owner.setAd_type(adResponseObj.getAd_type().trim());
                        if (fetcher.owner.getAd_type().equalsIgnoreCase("HTML")
                                || fetcher.owner.getAd_type().equalsIgnoreCase("Floating HTML")
                                || fetcher.owner.getAd_type().equalsIgnoreCase("TEXT")
                                || fetcher.owner.getAd_type().equalsIgnoreCase("Image")) {

                          /*  new LoadData().saveData(fetcher.owner.getContext(),adResponseObj.getAdResponseValues());
                            fetcher.owner.displayHTMLAd(adResponseObj);*/
                        }
                        else if (fetcher.owner.getAd_type().equalsIgnoreCase("Floating Interstitial")) {
                            //fetcher.owner.displayInterstitialResult(adResponseObj);
                        }
                        else if (fetcher.owner.getAd_type().equalsIgnoreCase("M_NAT_VID")) {
                            fetcher.owner.loadNativeImage(adResponseObj);
                            //new NativeImage().loadNativeImage(adResponseObj.getNativeadsres());
                        }
                        else if (fetcher.owner.getAd_type().equalsIgnoreCase("INTERSSTIAL_VID")) {
                           // fetcher.owner.loadNativeImage(adResponseObj);
                            //new NativeImage().loadNativeImage(adResponseObj.getNativeadsres());

                           // new LoadData().saveInterstitialVideo(fetcher.owner.getContext(),adResponseObj.getInterstitialVideoTag(),"");

                           }
                        else if(adResponseObj.getAd_network().equalsIgnoreCase("mediation"))
                        {
                            fetcher.owner.displaycustomsdknetwork(adResponseObj);
                        }
                        else {
                            Cdlog.i(Cdlog.debugLogTag, "No Ads Type Find in AdFetcher");
                        }
                    }




                } else {
                    Cdlog.i(Cdlog.debugLogTag, "Ad Response Failed");
                    fetcher.owner.dispatcher.load_ad_failed(fetcher.owner, true, adResponseObj.getError_code(), adResponseObj.getError_desc());

                }
            }
        }

    }


    public void clearDurations() {
        lastFetchTime = -1;
        timePausedAt = -1;

    }

}

