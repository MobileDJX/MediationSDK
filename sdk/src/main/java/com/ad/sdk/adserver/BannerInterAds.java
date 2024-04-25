package com.ad.sdk.adserver;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BannerInterAds {

    public void loadBannerAd(Context adViewContext, WebView webView) {
        SharedPreferences sharedPreferences1 = adViewContext.getSharedPreferences("BannerAd", MODE_PRIVATE);

        String HtmlCode = sharedPreferences1.getString("ad_tag", "");
        System.out.println("@@ Banner AdTag : " + HtmlCode);
        int maxLogSize = 4000;
        for (int j = 0; j <= HtmlCode.length() / maxLogSize; j++) {
            int start = j * maxLogSize;
            int end = (j + 1) * maxLogSize;
            end = end > HtmlCode.length() ? HtmlCode.length() : end;
//            Log.d("mulitZone", "HTML CODE:" + HtmlCode.substring(start, end));
        }
        webView.setBackgroundColor(0);
        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setJavaScriptEnabled(true);
        String html = "<!DOCTYPE html><html>" + "<style type='text/css'>" + "html,body {margin: 0;padding: 0;width: 100%;height: 100%;}" + "html {display: table;}" + "body {display: table-cell;vertical-align: middle;text-align: center;}" + "img{display: inline;height: auto;max-width: 100%;}" + "</style>" + "<body style= \"width=\"100%\";height=\"100%\";initial-scale=\"1.0\"; maximum-scale=\"1.0\"; user-scalable=\"no\";>" + HtmlCode + "</body></html>";

        webView.loadData(html, "text/html", "UTF-8");
        webView.setClickable(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //Your code to do
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("WebViewError", "Your Internet Connection May not be active Or " + error.getDescription());
                }
            }
        });
    }
}
