package com.ad.sdk.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.ad.sdk.adserver.AdResponseValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadData {
    ArrayList<AdResponseValue> adResponseValues;

    public LoadData() {

    }


    public void saveBasicAdsData(Context context, ArrayList<AdResponseValue> adResponseValues) {

        logoutAction(context);

        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpreferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(adResponseValues);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("SDK_LOCAL", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();


    }

    public ArrayList<AdResponseValue> adResponseValues(Context context) {

        ArrayList<AdResponseValue> adResponseValues = new ArrayList<AdResponseValue>();
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpreferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("SDK_LOCAL", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<AdResponseValue>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        adResponseValues = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (adResponseValues == null) {
            // if the array list is empty
            // creating a new array list.
            adResponseValues = new ArrayList<>();
        }

        return adResponseValues;
    }


    public void logoutAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpreferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    public void saveInterstitialVideo(Context context, String url, String screenType) {

        logoutInterstitialVideoAction(context);

        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        System.out.println("@@ InterstitialVideo save url : " + url);
        SharedPreferences sharedPreferences = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // prefs in the form of string.
        editor.putString("InterstitialVideo_URL", url);
        editor.putString("screenType", screenType);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();


    }

    public void logoutInterstitialVideoAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getInterstitialVideo(Context context) {
        String url = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences("InterstitialVideo", MODE_PRIVATE);
        url = sharedPreferences.getString("InterstitialVideo_URL", "");

        System.out.println("@@ url" + url);
        return url;
    }


    //InterstitialImage
    public void saveInterstitialImage(Context context, String url, String screenType) {

        logoutInterstitialImageAction(context);

        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        System.out.println("@@ save InterstitialImageurl" + url);
        SharedPreferences sharedPreferences = context.getSharedPreferences("InterstitialImage", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // prefs in the form of string.
        editor.putString("InterstitialImage_URL", url);
        editor.putString("screenType", screenType);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();


    }

    public void logoutInterstitialImageAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("InterstitialImage", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getInterstitialImage(Context context) {
        String url = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences("InterstitialImage", MODE_PRIVATE);
        url = sharedPreferences.getString("InterstitialImage_URL", "");

        System.out.println("@@ InterstitialImage_URL" + url);
        return url;
    }


    public void saveRewardedVideo(Context context, String url, String screenType) {

        logoutRewardedVideoAction(context);

        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        System.out.println("@@ RewardedVideo save url" + url);
        SharedPreferences sharedPreferences = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // prefs in the form of string.
        editor.putString("RewardedVideo_URL", url);
        editor.putString("screenType", screenType);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();


    }

    public void logoutRewardedVideoAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RewardedVideo", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    //Mediation AdMob Banner
    public void saveMediationBanner(Context context, String adtype, String ad_network_type, String adunit, String app_id, String app_signature, String game_id, String placement_id, String testmode, String zoneID, String click, String impression, String request) {

        logoutMediationBannerAction(context);

        System.out.println("@@ Mediation Banner save url" + adtype);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationBanner", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Mediation_adtype", adtype);
        editor.putString("Mediation_ad_network_type", ad_network_type);
        editor.putString("Mediation_adunit", adunit);
        editor.putString("Mediation_app_id", app_id);
        editor.putString("Mediation_app_signature", app_signature);
        editor.putString("Mediation_app_gameId", game_id);
        editor.putString("Mediation_app_placementId", placement_id);
        editor.putString("Mediation_app_testMode", testmode);
        editor.putString("Mediation_app_zoneid", zoneID);
        editor.putString("Mediation_click", click);
        editor.putString("Mediation_impression", impression);
        editor.putString("Mediation_request", request);

        editor.apply();


    }

    public void logoutMediationBannerAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationBanner", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    //Mediation Interstitial Image
    public void saveMediationInterstitial(Context context, String adtype, String ad_network_type, String adunit, String app_id, String app_signature, String game_id, String placement_id, String testmode, String zoneID, String click, String impression, String request) {

        logoutMediationInterstitialAction(context);

        System.out.println("@@ Mediation Interstitial save url" + adtype);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationInterstitial", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Mediation_adtype", adtype);
        editor.putString("Mediation_adunit", adunit);
        editor.putString("Mediation_ad_network_type", ad_network_type);
        editor.putString("Mediation_app_id", app_id);
        editor.putString("Mediation_app_signature", app_signature);
        editor.putString("Mediation_app_gameId", game_id);
        editor.putString("Mediation_app_placementId", placement_id);
        editor.putString("Mediation_app_testMode", testmode);
        editor.putString("Mediation_app_zoneid", zoneID);
        editor.putString("Mediation_click", click);
        editor.putString("Mediation_impression", impression);
        editor.putString("Mediation_request", request);


        editor.apply();


    }

    public void logoutMediationInterstitialAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationInterstitial", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    //Mediation Interstitial Video
    public void saveMediationInterstitialVideo(Context context, String adtype, String ad_network_type, String adunit, String app_id, String app_signature, String game_id, String placement_id, String testmode, String zoneID, String click, String impression, String request) {

        logoutMediationInterstitialVideoAction(context);

        System.out.println("@@ Mediation InterstitialVideo save url" + adtype);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationInterstitialVideo", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Mediation_adtype", adtype);
        editor.putString("Mediation_adunit", adunit);
        editor.putString("Mediation_ad_network_type", ad_network_type);
        editor.putString("Mediation_app_id", app_id);
        editor.putString("Mediation_app_signature", app_signature);
        editor.putString("Mediation_app_gameId", game_id);
        editor.putString("Mediation_app_placementId", placement_id);
        editor.putString("Mediation_app_testMode", testmode);
        editor.putString("Mediation_app_zoneid", zoneID);
        editor.putString("Mediation_click", click);
        editor.putString("Mediation_impression", impression);
        editor.putString("Mediation_request", request);

        editor.apply();


    }

    public void logoutMediationInterstitialVideoAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationInterstitialVideo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    //Mediation Rewarded Video
    public void saveMediationRewardedVideo(Context context, String adtype, String ad_network_type, String adunit, String app_id, String app_signature, String game_id, String placement_id, String testmode, String zoneID, String click, String impression, String request) {

        System.out.println("@@ Mediation Rewarded_Video save url" + adtype);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationRewardedVideo", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Mediation_adtype", adtype);
        editor.putString("Mediation_adunit", adunit);
        editor.putString("Mediation_ad_network_type", ad_network_type);
        editor.putString("Mediation_app_id", app_id);
        editor.putString("Mediation_app_signature", app_signature);
        editor.putString("Mediation_app_gameId", game_id);
        editor.putString("Mediation_app_placementId", placement_id);
        editor.putString("Mediation_app_testMode", testmode);
        editor.putString("Mediation_app_zoneid", zoneID);
        editor.putString("Mediation_click", click);
        editor.putString("Mediation_impression", impression);
        editor.putString("Mediation_request", request);


        editor.apply();


    }

    public void logoutMediationRewardedVideoAction(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MediationRewardedVideo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    //New Response


    //Banner
    public void saveBannerZoneList(Context context, String zoneidList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String zoneListString = gson.toJson(zoneidList);
        editor.putString("bannerZoneList", zoneidList);
        editor.apply();
    }


    //Interstitial

    public void saveInterstitialZoneList(Context context, String zoneidList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("interstitialZoneList", zoneidList);
        editor.apply();
    }


    //Interstitial Video
    public void saveInterstitialVideoZoneList(Context context, String zoneidList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("interstitialVideoZoneList", zoneidList);
        editor.apply();
    }


    //Rewarded Video
    public void saveRewardedVideoZoneList(Context context, String zoneidList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rewardedVideoZoneList", zoneidList);
        editor.apply();
    }


    //Clear ZoneList
    public void logoutZoneList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ZoneList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public void saveMediationNetwork(Context context, String network) {

        logoutMediationNetwork(context);

        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        System.out.println("@@ Mediation save network" + network);
        SharedPreferences sharedPreferences = context.getSharedPreferences("Mediation_network", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // prefs in the form of string.
        editor.putString("Mediation_network", network);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();


    }

    public void logoutMediationNetwork(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Mediation_network", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getMediationNetworkStatus(Context context) {
        String network = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Mediation_network", MODE_PRIVATE);
        network = sharedPreferences.getString("Mediation_network", "");
        System.out.println("@@ network : " + network);
        return network;
    }

    public void logoutClear(Context context) {
        logoutInterstitialImageAction(context);
        logoutInterstitialVideoAction(context);
        logoutRewardedVideoAction(context);
        logoutMediationBannerAction(context);
        logoutMediationInterstitialAction(context);
        logoutMediationInterstitialVideoAction(context);
        logoutMediationRewardedVideoAction(context);
        logoutMediationNetwork(context);
        logoutAction(context);
        logoutZoneList(context);
        logoutBanner(context);
    }


    public void saveBanner(Context context, String url) {
        logoutBanner(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences("BannerAd", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // prefs in the form of string.
        editor.putString("ad_tag", url);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();


    }

    private void logoutBanner(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("BannerAd", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }


}
