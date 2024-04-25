package com.ad.sdk.adserver;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ad.sdk.utils.Cdlog;
import com.ad.sdk.utils.LoadData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class AdResponse {

    private JSONObject nativeadsres;


    String res = "{\"response\":\"success\",\"ads\":[{\"response\":\"success\",\"imp_url\":\"http%3A%2F%2F43.204.47.201%2FAdmobileadSDK541%2Fwww%2Fdelivery%2Flg.php%3Fbannerid%3D19%26campaignid%3D1%26zoneid%3D14%26cb%3Dac2d080509zoneid%3D14%26width%3D%26height%3D%26keywords%3D%26lattitude%3D%26longitude%3D%26systemtype%3D%26ip%3D%26layerstyle%3D%26screenwidth%3D%26screenheight%3D%26displaywidth%3D%26displayheight%3D%26displaytype%3D%26devicemodel%3D%26devicebrand%3D%26deviceos%3D%26deviceosversion%3D%26is_js_enabled%3D%26carrier%3D%26country%3D%26countryname%3D%26region%3D%26city%3D%26useragent%3D%26language%3D%26postalcode%3D%26device_appid%3D%26device_app_cat%3D%26device_app_sha1%3D%26device_app_md5%3D%26device_app_dpidsha1%3D%26device_app_dpidmd5%3D%26device_app_ipv6%3D%26udid%3D%26timezone%3D%26dataspeed%3D%26connection%3Dkeep-alive%26connectiontype%3D%26Viewername%3D%26Vieweremail%3D%26Viewerphone%3D%26Viewergender%3D%26Viewerage%3D%26userid%3D%26pincode%3D\",\"ad_type\":\"Image\",\"ad_tag\":\"<script type='text\\/javascript' src='http:\\/\\/43.204.47.201\\/AdmobileadSDK541\\/www\\/admin\\/plugins\\/mobileAdsDelivery\\/floatal.php?zoneid=14&width=&height=&keywords=&lattitude=&longitude=&systemtype=&ip=&layerstyle=&screenwidth=&screenheight=&displaywidth=&displayheight=&displaytype=&devicemodel=&devicebrand=&deviceos=&deviceosversion=&is_js_enabled=&carrier=&country=&countryname=&region=&city=&useragent=&language=&postalcode=&device_appid=&device_app_cat=&device_app_sha1=&device_app_md5=&device_app_dpidsha1=&device_app_dpidmd5=&device_app_ipv6=&udid=&timezone=&dataspeed=&connection=keep-alive&connectiontype=&Viewername=&Vieweremail=&Viewerphone=&Viewergender=&Viewerage=&userid=&pincode=&layerstyle=simple&request_id=&viewerid=&hide=0&trail=0&stickyness=2'><\\/script>\",\"click_url\":\"http%3A%2F%2F43.204.47.201%2FAdmobileadSDK541%2Fwww%2Fdelivery%2Fcl.php%3Fbannerid%3D19%26zoneid%3D14%26userid%3D%26device_appid%3D%26connectiontype%3D%26devicebrand%3D%26deviceos%3D%26deviceosversion%3D%26screenwidth%3D%26screenheight%3D%26carrier%3D%26timezone%3D%26udid%3D%26useragent%3D%26Viewername%3D%26Vieweremail%3D%26Viewerphone%3D%26language%3D%26dataspeed%3D%26displaytype%3D%26device_app_dpidmd5%3D%26device_app_dpidsha1%3D%26devicemodel%3D%26sig%3Dec414369831fa1abf37e965700d510f2555625dfd45f7d53d72cb0b31e5d8be5%26dest%3Dhttp%253A%252F%252Fwww.google.com\",\"width\":\"320\",\"height\":\"50\",\"image_path\":\"http:\\/\\/43.204.47.201\\/AdmobileadSDK541\\/www\\/images\\/15c8d3d827719a7c5825865a0cd57556.jpg\"}]}";

    //Rewarded Video
    private JSONObject rewardedres;

    ArrayList<AdResponseValue> adResponseValues;

    private String adtag = null;
    private String ad_type = null;
    private String imp_url = null;
    private String click_url = null;
    private String req_url = null;
    public String status = "error";

    private String nativead_adtag = null;
    public String video = null;

    String interstitialVideoTag = null;
    String interstitialImageTag = null;
    String rewardedVideoTag = null;


    private volatile boolean parsingComplete = true;

    private volatile boolean failed = false;
    private String error_code = null;
    private String error_desc = null;

    //Mediation
    public String ad_network = null;
    public String adunit = null;
    public String notinid;
    public int networkid;

    public String ad_network_type;

    public String getAd_network_type() {
        return ad_network_type;
    }

    public void setAd_network_type(String ad_network_type) {
        this.ad_network_type = ad_network_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdtag() {
        return adtag;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getImp_url() {
        return imp_url;
    }

    public String getReq_url() {
        return req_url;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    public String getClick_url() {
        return click_url;
    }

    public boolean isParsingComplete() {
        return parsingComplete;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public String getError_code() {
        return error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    //InterstitialVideo

    public String getInterstitialVideoTag() {
        return interstitialVideoTag;
    }

    public void setInterstitialVideoTag(String interstitialVideoTag) {
        this.interstitialVideoTag = interstitialVideoTag;
    }


    //IntertitialImage

    public String getInterstitialImageTag() {
        return interstitialImageTag;
    }

    public void setInterstitialImageTag(String interstitialImageTag) {
        this.interstitialImageTag = interstitialImageTag;
    }

    //Rewarded

    public String getRewardedVideoTag() {
        return rewardedVideoTag;
    }

    public void setRewardedVideoTag(String rewardedVideoTag) {
        this.rewardedVideoTag = rewardedVideoTag;
    }


    //Native video

    public JSONObject getNativeadsres() {
        return nativeadsres;
    }

    public JSONObject getrewardadsres() {
        return rewardedres;
    }

    public String getAdunit() {
        return adunit;
    }

    public void setAdunit(String adunit) {
        this.adunit = adunit;
    }

    public String getAd_network() {
        return ad_network;
    }

    public void setAd_network(String ad_network) {
        this.ad_network = ad_network;
    }

    public ArrayList<AdResponseValue> getAdResponseValues() {
        return adResponseValues;
    }

    public void setAdResponseValues(ArrayList<AdResponseValue> adResponseValues) {
        this.adResponseValues = adResponseValues;
    }

    public void readAndParseJSON(String result, Context context) {

        try {
            if (result != null) {
                adResponseValues = new ArrayList<AdResponseValue>();

                JSONObject reader = new JSONObject(result);
                //JSONObject reader = new JSONObject(res);
                String ad_response = reader.getString(Config.TAG_AD_RESPONSE);
                System.out.println("@@ ad_response" + ad_response);

                ad_response = ad_response.trim();
                new LoadData().logoutClear(context);

                if (ad_response.equalsIgnoreCase("success")) {

                    //JSONObject ads = reader.getJSONObject(Config.TAG_ADS);
                    JSONArray ads = reader.getJSONArray(Config.TAG_ADS);

                    for (int i = 0; i < ads.length(); i++) {
                        try {
                            JSONObject jsonObj = ads.getJSONObject(i);

                            String sub_status = jsonObj.getString(Config.TAG_AD_RESPONSE);

                            System.out.println("@@ sub_status" + sub_status);

                            if (sub_status.equalsIgnoreCase("success")) {
                                ad_network = jsonObj.getString("ad_network");
                                status = "success";
                                new LoadData().saveMediationNetwork(context, ad_network);
                                if (ad_network.equalsIgnoreCase("internal")) {
                                    ad_type = jsonObj.getString(Config.TAG_AD_TYPE);
                                    Log.d("dJAXM", "ad_type : " + ad_type);
                                    Log.d("dJAXM", "ad_network : " + ad_network);
                                    if (ad_type.equalsIgnoreCase("M_NAT_VID") || ad_type.equalsIgnoreCase("IN_ART_VID") || ad_type.equalsIgnoreCase("IN_FEED_VID")) {
                                        imp_url = URLDecoder.decode(jsonObj.getString(Config.TAG_BEACON_URL), "UTF-8");

                                        click_url = URLDecoder.decode(jsonObj.getString(Config.TAG_CLICK_URL), "UTF-8");
                                        nativead_adtag = URLDecoder.decode(jsonObj.getString(Config.TAG_NATIVE_TAG), "UTF-8");

                                        nativeadsres = jsonObj.getJSONObject("Native_ad");
                                        Log.d("nativeadsres", "nativeadsres : " + nativeadsres);
                                    } else if (ad_type.equalsIgnoreCase("Banner")) {
                                        String ad_url = jsonObj.getString("ad_tag");
                                        new LoadData().saveBanner(context, ad_url);
                                    }
                                    //Rewarded Video Ads
                                    else if (ad_type.equalsIgnoreCase("REWARDED_VID")) {
                                        rewardedVideoTag = jsonObj.getString("ad_tag");
                                        String screenType = jsonObj.getString("layout");

                                        JSONObject ad_values = jsonObj.getJSONObject("ad_values");
                                        JSONArray rewards = ad_values.getJSONArray("rewards");
                                        Log.e("reward", rewards.toString());


                                        String amount = null;
                                        JSONObject amount_object = rewards.getJSONObject(0);
                                        amount = amount_object.getString("amount");

                                        Log.e("amount", amount);

                                        SharedPreferences sharedPreferences = context.getSharedPreferences("reward_amount", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("amount", amount);
                                        editor.apply();

                                        new LoadData().saveRewardedVideo(context, rewardedVideoTag, screenType);
                                    }

                                    //Interstitial Video Ads
                                    else if (ad_type.equalsIgnoreCase("INTERSTITIAL_VID")) {
                                        interstitialVideoTag = jsonObj.getString("ad_tag");
                                        String screenType = jsonObj.getString("layout");

                                        Log.d("SDK", "interstitialVideoTag" + interstitialVideoTag);

                                        new LoadData().saveInterstitialVideo(context, interstitialVideoTag, screenType);


                                        //Interstitial Image

                                    } else if (ad_type.equalsIgnoreCase("Interstitial")) {
                                        interstitialImageTag = jsonObj.getString("ad_tag");

                                        Log.d("SDK", "interstitialImageTag" + interstitialImageTag);

                                        new LoadData().saveInterstitialImage(context, interstitialImageTag, "");

                                    } else {
                                        imp_url = URLDecoder.decode(jsonObj.getString(Config.TAG_BEACON_URL), "UTF-8");

                                        click_url = URLDecoder.decode(jsonObj.getString(Config.TAG_CLICK_URL), "UTF-8");

                                        adtag = URLDecoder.decode(jsonObj.getString(Config.TAG_ADTAG), "UTF-8");


                                        Log.d("SDK", "Banner Ad Tag : " + adtag);


                                        AdResponseValue adv = new AdResponseValue();
                                        adv.setZone_id("0");
                                        adv.setImp_url(imp_url);
                                        adv.setClick_url(click_url);
                                        adv.setAd_tag(adtag);
                                        adResponseValues.add(adv);

                                    }
                                } else if (ad_network.equalsIgnoreCase("mediation")) {

//                                    imp_url = URLDecoder.decode(jsonObj.getString(Config.TAG_BEACON_URL), "UTF-8");
//
//                                    System.out.println("@@ imp_url : " + imp_url);
//
//                                    click_url = URLDecoder.decode(jsonObj.getString(Config.TAG_CLICK_URL), "UTF-8");
//                                    System.out.println("@@ click_url : " + click_url);
//
//                                    req_url = URLDecoder.decode(jsonObj.getString(Config.TAG_REQUEST_URL), "UTF-8");
//                                    System.out.println("@@ req_url : " + req_url);


                                    ad_type = jsonObj.getString(Config.TAG_AD_TYPE);


                                    //Mediation Banner
                                    if (ad_type.equalsIgnoreCase("Banner")) {
//                                        ad_network_type = jsonObj.getString("ad_network");
//                                        String ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneID;


                                        JSONArray zoneidList = jsonObj.getJSONArray("zoneidList");

                                        String responseArray = zoneidList.toString();

                                        Log.e("Banner ", "Zone List :" + responseArray);

                                        new LoadData().saveBannerZoneList(context, responseArray);

//                                        if (ad_network_type.equalsIgnoreCase("Admob")) {
//                                            ad_unit = ad_tag.getString("adunit");
//                                            new LoadData().saveMediationBanner(context, ad_type, ad_network_type, ad_unit, null, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            app_signature = ad_tag.getString("app_signature");
//                                            new LoadData().saveMediationBanner(context, ad_type, ad_network_type, null, app_id, app_signature, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            new LoadData().saveMediationBanner(context, ad_type, ad_network_type, null, app_id, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("Unity")) {
//                                            gameID = ad_tag.getString("game_id");
//                                            placementId = ad_tag.getString("placement_id");
//                                            testMode = ad_tag.getString("testmode");
//                                            new LoadData().saveMediationBanner(context, ad_type, ad_network_type, null, null, null, gameID, placementId, testMode, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("AdColony")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            zoneID = ad_tag.getString("zone_id");
//
//                                            new LoadData().saveMediationBanner(context, ad_type, ad_network_type, null, app_id, null, null, null, null, zoneID, click_url, imp_url, req_url);
//                                        }


                                        //Mediation Interstitial
                                    } else if (ad_type.equalsIgnoreCase("Interstitial")) {

                                        JSONArray zoneidList = jsonObj.getJSONArray("zoneidList");

                                        String responseArray = zoneidList.toString();

                                        Log.e("Interstitial", "Zone List :" + responseArray);


                                        new LoadData().saveInterstitialZoneList(context, responseArray);

//                                        JSONObject ad_tag = jsonObj.getJSONObject("ad_tag");
//                                        ad_network_type = jsonObj.getString("ad_network_type");
//
//                                        String ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneID;
//
//
//                                        if (ad_network_type.equalsIgnoreCase("Admob")) {
//                                            ad_unit = String.valueOf(ad_tag.getJSONObject("adunit"));
//                                            new LoadData().saveMediationInterstitial(context, ad_type, ad_network_type, ad_unit, null, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            app_signature = ad_tag.getString("app_signature");
//                                            new LoadData().saveMediationInterstitial(context, ad_type, ad_network_type, null, app_id, app_signature, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            new LoadData().saveMediationInterstitial(context, ad_type, ad_network_type, null, app_id, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("Unity")) {
//                                            gameID = ad_tag.getString("game_id");
//                                            placementId = ad_tag.getString("placement_id");
//                                            testMode = ad_tag.getString("testmode");
//                                            new LoadData().saveMediationInterstitial(context, ad_type, ad_network_type, null, null, null, gameID, placementId, testMode, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("AdColony")) {
//
//                                            app_id = ad_tag.getString("app_id");
//                                            zoneID = ad_tag.getString("zone_id");
//                                            new LoadData().saveMediationInterstitial(context, ad_type, ad_network_type, null, app_id, null, null, null, null, zoneID, click_url, imp_url, req_url);
//
//                                        }


                                        //Mediation Interstitial Video
                                    }


                                    //Mediation Interstitial Video

                                    else if (ad_type.equalsIgnoreCase("INTERSTITIAL_VID")) {

                                        JSONArray zoneidList = jsonObj.getJSONArray("zoneidList");

                                        String responseArray = zoneidList.toString();

                                        Log.e("Interstitial_Video", "Zone List :" + responseArray);

                                        new LoadData().saveInterstitialVideoZoneList(context, responseArray);


//                                        JSONObject ad_tag = jsonObj.getJSONObject("ad_tag");
//                                        ad_network_type = jsonObj.getString("ad_network_type");
//
//                                        String ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneID;
//
//
//                                        if (ad_network_type.equalsIgnoreCase("Admob")) {
//                                            ad_unit = ad_tag.getString("adunit");
//                                            new LoadData().saveMediationInterstitialVideo(context, ad_type, ad_network_type, ad_unit, null, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            app_signature = ad_tag.getString("app_signature");
//                                            new LoadData().saveMediationInterstitialVideo(context, ad_type, ad_network_type, null, app_id, app_signature, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            new LoadData().saveMediationInterstitialVideo(context, ad_type, ad_network_type, null, app_id, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("Unity")) {
//                                            gameID = ad_tag.getString("game_id");
//                                            placementId = ad_tag.getString("placement_id");
//                                            testMode = ad_tag.getString("testmode");
//                                            new LoadData().saveMediationInterstitialVideo(context, ad_type, ad_network_type, null, null, null, gameID, placementId, testMode, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("AdColony")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            zoneID = ad_tag.getString("zone_id");
//                                            new LoadData().saveMediationInterstitialVideo(context, ad_type, ad_network_type, null, app_id, null, null, null, null, zoneID, click_url, imp_url, req_url);
//
//                                        }


                                        //Mediation Rewarded
                                    } else if (ad_type.equalsIgnoreCase("REWARDED_VID")) {


                                        JSONArray zoneidList = jsonObj.getJSONArray("zoneidList");

                                        String responseArray = zoneidList.toString();

                                        Log.e("Rewarded_Video", "Zone List :" + responseArray);

                                        new LoadData().saveRewardedVideoZoneList(context, responseArray);


//                                        JSONObject ad_tag = jsonObj.getJSONObject("ad_tag");
//                                        ad_network_type = jsonObj.getString("ad_network_type");
//
//                                        String ad_unit, app_id, app_signature, gameID, placementId, testMode, zoneID;
//
//
//                                        if (ad_network_type.equalsIgnoreCase("Admob")) {
//                                            ad_unit = ad_tag.getString("adunit");
//                                            new LoadData().saveMediationRewardedVideo(context, ad_type, ad_network_type, ad_unit, null, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("ChartBoost")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            app_signature = ad_tag.getString("app_signature");
//                                            new LoadData().saveMediationRewardedVideo(context, ad_type, ad_network_type, null, app_id, app_signature, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("IronSource")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            new LoadData().saveMediationRewardedVideo(context, ad_type, ad_network_type, null, app_id, null, null, null, null, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("Unity")) {
//                                            gameID = ad_tag.getString("game_id");
//                                            placementId = ad_tag.getString("placement_id");
//                                            testMode = ad_tag.getString("testmode");
//                                            new LoadData().saveMediationRewardedVideo(context, ad_type, ad_network_type, null, null, null, gameID, placementId, testMode, null, click_url, imp_url, req_url);
//
//                                        } else if (ad_network_type.equalsIgnoreCase("AdColony")) {
//                                            app_id = ad_tag.getString("app_id");
//                                            zoneID = ad_tag.getString("zone_id");
//
//                                            Log.e("APP_ID", "Adcolony APP_ID : " + app_id);
//                                            Log.e("ZONE_ID", "Adcolony ZONE_ID : " + zoneID);
//                                            new LoadData().saveMediationRewardedVideo(context, ad_type, ad_network_type, null, app_id, null, null, null, null, zoneID, click_url, imp_url, req_url);
//                                        }
                                    }
                                }


                            } else {

                                failed = true;
                                JSONObject error = jsonObj.getJSONObject(Config.TAG_ERROR);
                                error_code = error.getString(Config.TAG_ERR_CODE);
                                error_desc = error.getString(Config.TAG_ERR_DESC);
                                SharedPreferences sharedPreferences = context.getSharedPreferences("InternalError", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("status", sub_status);
                                editor.apply();
                            }


                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    if (adResponseValues.size() > 0) {
                        new LoadData().saveBasicAdsData(context, adResponseValues);
                    }
                   /* if(ads!=null)
                    {
                        try {
                            //ad_network =ads.getString("ad_network");
                            ad_network ="internal";
                            ad_type = ads.getString(Config.TAG_AD_TYPE);
                            Log.d("dJAXM", "ad_type : " + ad_type);
                            if(ad_network.equalsIgnoreCase("internal")) {
                                if (ad_type.equalsIgnoreCase("IMAGE")) {
                                    adtag = URLDecoder.decode(ads.getString(Config.TAG_IMAGE_PATH), "UTF-8");
                                }
                                //native & Article & in-Feed video ads
                                else if (ad_type.equalsIgnoreCase("M_NAT_VID")) {
                                    imp_url = URLDecoder.decode(ads.getString(Config.TAG_BEACON_URL), "UTF-8");

                                    click_url = URLDecoder.decode(ads.getString(Config.TAG_CLICK_URL), "UTF-8");
                                    nativead_adtag = URLDecoder.decode(ads.getString(Config.TAG_NATIVE_TAG), "UTF-8");

                                    nativeadsres = ads.getJSONObject("Native_ad");
                                    Log.d("nativeadsres", "nativeadsres : " + nativeadsres);
                                }

                                //Rewarded Video Ads
                                else if (ad_type.equalsIgnoreCase("REWARDED_VID")) {
                                    //rewardedres = ads.getJSONObject("RewardedVideo_ad");
                                    rewardedres = ads.getJSONObject("ad_tag");
                                }
                                //Interstitial Video Ads
                                else if (ad_type.equalsIgnoreCase("M_INT_VID")) {
                                    //rewardedres = ads.getJSONObject("RewardedVideo_ad");
                                    rewardedres = ads.getJSONObject("ad_tag");
                                } else {
                                    imp_url = URLDecoder.decode(ads.getString(Config.TAG_BEACON_URL), "UTF-8");

                                    click_url = URLDecoder.decode(ads.getString(Config.TAG_CLICK_URL), "UTF-8");

                                    adtag = URLDecoder.decode(ads.getString(Config.TAG_ADTAG), "UTF-8");
                                }
                            }
                            else if(ad_network.equalsIgnoreCase("mediation"))
                            {

                                click_url 			= URLDecoder.decode(ads.getString(Config.TAG_CLICK_URL),"UTF-8");
                                imp_url 			= URLDecoder.decode(ads.getString(Config.TAG_BEACON_URL),"UTF-8");
                                req_url 			= URLDecoder.decode(ads.getString("request_url"),"UTF-8");

                                notinid = ads.getString("NOTINid");
                                networkid = ads.getInt("Mediation_id");
                                JSONObject server_extra=ads.getJSONObject("server_extra");
                                adunit=server_extra.getString("Adunit");
                                System.out.println("@ adunit "+adunit);
                            }


                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
*/


                } else {
                    // Handle Error Flow
                    Cdlog.d("mSDK Debug", ad_response);
                    failed = true;
                    JSONObject error = reader.getJSONObject(Config.TAG_ERROR);
                    error_code = error.getString(Config.TAG_ERR_CODE);
                    error_desc = error.getString(Config.TAG_ERR_DESC);
                }
                parsingComplete = false;
            } else {
                System.out.print("Caught NullPointerException: Null result return from given URL!!");
            }
        } catch (JSONException e) {
            Log.e("mSDK Debug", "unexpected JSON exception", e);
            e.printStackTrace();
        }
    }


}
