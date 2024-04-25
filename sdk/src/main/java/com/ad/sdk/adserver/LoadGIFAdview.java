package com.ad.sdk.adserver;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ad.sdk.R;
import com.ad.sdk.adserver.Listener.GIFAdListener;


public class LoadGIFAdview {
    PopupWindow pop;
    private static final String TAG = "GIFPopupAds";
    public LoadGIFAdview() {
    }

    public void loadGIFAd(Context context, GIFAdListener listener)
    {
        try
        {

            Activity activity = (Activity) context;
            RelativeLayout relativelayout = new RelativeLayout(activity);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customview = layoutInflater.inflate(R.layout.gif_popup, null);
            ImageView close = (ImageView) customview.findViewById(R.id.img_close_btn);
            WebView webView=(WebView) customview.findViewById(R.id.webview);
            ConstraintLayout layout=(ConstraintLayout)customview.findViewById(R.id.pop_up_lay);

            //String HtmlCode = "<script type='text/javascript' src='https://revphpe.djaxbidder.com/AdmobileadSDK541/www/admin/plugins/mobileAdsDelivery/floatal.php?zoneid=103&width=&height=&keywords=&lattitude=&longitude=&systemtype=&ip=&layerstyle=&screenwidth=&screenheight=&displaywidth=&displayheight=&displaytype=&devicemodel=&devicebrand=&deviceos=&deviceosversion=&is_js_enabled=&carrier=&country=&countryname=&region=&city=&useragent=&language=&postalcode=&device_appid=&device_app_cat=&device_app_sha1=&device_app_md5=&device_app_dpidsha1=&device_app_dpidmd5=&device_app_ipv6=&udid=&timezone=&dataspeed=&connection=keep-alive&connectiontype=&Viewername=&Vieweremail=&Viewerphone=&Viewergender=&Viewerage=&userid=&pincode=&age=&gender=&layerstyle=simple&request_id=&viewerid=&hide=0&trail=0&stickyness=2'></script>";
            String HtmlCode = "<script type='text/javascript' src='https://revphpe.djaxbidder.com/AdmobileadSDK541/www/admin/plugins/mobileAdsDelivery/floatal.php?zoneid=104&width=&height=&keywords=&lattitude=&longitude=&systemtype=&ip=&layerstyle=&screenwidth=&screenheight=&displaywidth=&displayheight=&displaytype=&devicemodel=&devicebrand=&deviceos=&deviceosversion=&is_js_enabled=&carrier=&country=&countryname=&region=&city=&useragent=&language=&postalcode=&device_appid=&device_app_cat=&device_app_sha1=&device_app_md5=&device_app_dpidsha1=&device_app_dpidmd5=&device_app_ipv6=&udid=&timezone=&dataspeed=&connection=keep-alive&connectiontype=&Viewername=&Vieweremail=&Viewerphone=&Viewergender=&Viewerage=&userid=&pincode=&age=&gender=&layerstyle=simple&request_id=&viewerid=&hide=0&trail=0&stickyness=2'></script>";
            //String HtmlCode = new LoadData().adResponseValues(context).get(k).getAd_tag();
           // String HtmlCode = new LoadData().getInterstitialImage(context);
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
            pop = new PopupWindow(customview, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
            pop.showAtLocation(relativelayout, Gravity.CENTER, 0, 0);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            listener.onAdShown();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAdDismissed();
                    pop.dismiss();
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAdDismissed();
                    pop.dismiss();
                }
            });


            /*Animation animation1 =
                    AnimationUtils.loadAnimation(context,
                            R.anim.fade);
            webView.startAnimation(animation1);*/


            listener.onAdLoaded();
        }
        catch (Exception e)
        {
            Log.d("SDK", "Interstital Image Ad Exception:" + e);
        }



    }



}
