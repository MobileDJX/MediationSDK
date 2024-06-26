package com.ad.sdk.mtrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;

import com.ad.sdk.utils.Cdlog;
import com.ad.sdk.utils.ConnectionDetecter;

import java.util.Locale;
import java.util.TimeZone;

public class Device_settings {

    // DEVICE INFORMATION

    public String mediation = null;

    public String android_id = null;
    public String didsha1 = null;
    public String didmd5 = null;
    public String model = Build.DEVICE;
    public String make = Build.MANUFACTURER;

    public String os = "Android";
    public String os_ver = Build.VERSION.RELEASE;
    public boolean js_enable;
    public int screen_width;
    public int screen_height;
    public int display_width = 0;
    public int display_height = 0;
    public String timezone = TimeZone.getDefault().getID();
    public String language = Locale.getDefault().getLanguage();
//	public String language			= 	"arabic";


    // DEVICE INTERNET INFO

    boolean is_internet_enable;
    public String carrier;
    public String ipv6;
    public String connection_type;
    public String data_speed;
    public String device_type;
    public String external_ip;
    public String internal_ip;
    public String ua;

    private String advertisingid;
    private String gaid;
    private String var;

    // APPLICATION INFORMATION

    public String app_id;

    // USER INFORMATION

    public String user_latitude;
    public String user_longitude;
    public String user_name;
    public String user_email;
    public String user_gender;
    public String user_phone;
    public String user_account_name;

    public boolean first_launch;

    public volatile boolean readFirst = false;


    @SuppressLint("SetJavaScriptEnabled")
    public Device_settings(Context context) {
        super();

        Log.d("mSDK Debug", "Device Setting Constructor called");

        if (!readFirst) {

            mediation = "0";

            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);

            screen_width = dm.widthPixels;
            screen_height = dm.heightPixels;


            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);

            new Thread(new Runnable() {
                public void run() {
                    try {
                        AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                        var = adInfo.getId();
                        gaid = var;
                        if (gaid != null) {
                            setGAID(gaid);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            if (display_width == 0 || display_height == 0) {

                // since SDK_INT = 1;
                display_width = metrics.widthPixels;
                display_height = metrics.heightPixels;


                // includes window decorations (statusbar bar/menu bar)
                if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) try {
                    display_width = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                    display_height = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
                // includes window decorations (statusbar bar/menu bar)
                if (Build.VERSION.SDK_INT >= 17) try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    display_width = realSize.x;
                    display_height = realSize.y;
                } catch (Exception ignored) {
                }

            }

            Log.d("mSDK Debug", "Screen Resolution collected.");

            //android_id = android.provider.Settings.Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).toString();
            android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            didmd5 = HashingFunctions.md5(android_id);
            didsha1 = HashingFunctions.sha1(android_id);

            app_id = context.getApplicationContext().getPackageName();

//			app_id = "com.example.package";

            Log.d("mSDK Debug", "Device Information Collected.");

            user_name = "android";
            user_email = "android@gmail.com";
            user_phone = "9090909090";
            user_account_name = "mobile@android";

            if (context.checkCallingOrSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED && context.checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") == PackageManager.PERMISSION_GRANTED) {

                OwnerInfo owner = new OwnerInfo(context);

//                user_name = owner.name;
//                user_email = owner.email;
//                user_phone = owner.phone;
//                user_account_name = owner.accountName;


                Log.d("mSDK Debug", "Owner Information Collected.");

            } else {
                Cdlog.i(Cdlog.baseLogTag, "READ_CONTACTS & GET_ACCOUNTS permission required.");
            }

            readFirst = true;

        }


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm.getActiveNetwork() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                NetworkInfo con = Connectivity.getNetworkInfo(context);

                connection_type = con.getTypeName();

                if (connection_type.equalsIgnoreCase("WIFI")) {
                    data_speed = con.getTypeName();
                } else {
                    data_speed = con.getSubtypeName();
                }

                is_internet_enable = true;

                Log.d("mSDK Debug", "Internet Information Collected.");

            } else {
                is_internet_enable = false;
            }

            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {


                Log.d("mSDK Debug", "Internet Information Collected.");

                if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED &&

                        context.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED &&

                        context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == PackageManager.PERMISSION_GRANTED) {

                    //            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    //            if (conMgr.getActiveNetworkInfo() != null
                    //                    && conMgr.getActiveNetworkInfo().isAvailable()
                    //                    && conMgr.getActiveNetworkInfo().isConnected()) {
                    //
                    //                NetworkInfo con = Connectivity.getNetworkInfo(context);
                    //
                    //                connection_type = con.getTypeName();
                    //                data_speed = con.getSubtypeName();
                    //                is_internet_enable = true;
                    //
                    //                Log.d("mSDK Debug", "Internet Information Collected.");

                }
                //            else
                //                is_internet_enable = false;

            } else {
                Cdlog.i(Cdlog.baseLogTag, "INTERNET,ACCESS_WIFI_STATE & ACCESS_NETWORK_STATE permission required.");
            }
        }

        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED &&

                context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            Log.e("dJAXM", "context Device : " + context);
            GPSTracker geo = new GPSTracker(context);

            user_latitude = Double.valueOf(geo.getLatitude()).toString();
            user_longitude = Double.valueOf(geo.getLongitude()).toString();


			/*internal_ip = Utils.getIPAddress(true);

			external_ip = Utils.getIPAddress(true);

			ipv6 = Utils.getIPAddress(false);*/

            internal_ip = Utils.getLocalIpAddress();

            external_ip = Utils.getLocalIpAddress();

            ipv6 = Utils.getLocalIpAddress();


            Log.d("mSDK Debug", "GEO Information Collected.");

        } else {
            Cdlog.i(Cdlog.baseLogTag, "INTERNET & ACCESS_NETWORK_STATE permission required.");
        }


        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //carrier = manager.getNetworkOperatorName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {


            int simState = manager.getSimState();

            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    carrier = "No_Sim";
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    carrier = manager.getSimCarrierIdName().toString();
                    break;
            }

        }

        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED) {


		/*	TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			//carrier = manager.getNetworkOperatorName();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				carrier = manager.getSimCarrierIdName().toString();
			}
*/
        } else {
            Cdlog.i(Cdlog.baseLogTag, "READ_PHONE_STATE permission required.");
        }


        // Get orientation, the current rotation of the device
        //String orientation = context.getResources().getConfiguration().orientation
        //        == Configuration.ORIENTATION_LANDSCAPE ? "h" : "v";

        WebView wView = new WebView(context);

        // Store the UA in the settings
        ua = wView.getSettings().getUserAgentString();

        wView.getSettings().setJavaScriptEnabled(true);

        js_enable = wView.getSettings().getJavaScriptEnabled();


        boolean dd = new ConnectionDetecter(context).isTablet();

        if (dd) {
            device_type = "Tablet";
        } else {
            device_type = "Mobile";
        }

        Log.d("mSDK Debug", "End of Device Setting Constructor");
    }

    public String getGAID() {
        return advertisingid;
    }

    private void setGAID(String gaid) {
        this.advertisingid = gaid;
    }

    // STATICS
    private static Device_settings settings_instance = null;

    public static Device_settings getSettings(Context context) {
        if (settings_instance == null) {
            settings_instance = new Device_settings(context);
            Cdlog.v(Cdlog.baseLogTag, "The DragonMedia " + Cdlog.baseLogTag + " is initializing.");
        }
        return settings_instance;
    }


    @Override
    public String toString() {
        String res = "";

        res += "Model:" + model + "\n";
        res += "Make:" + make + "\n";
        res += "OS:" + os + "\n";
        res += "OS VER:" + os_ver + "\n";
        res += "TimeZone:" + timezone + "\n";
        res += "Language:" + language + "\n";
        res += "Screen Width:" + screen_width + "\n";
        res += "Screen Height:" + screen_height + "\n";
        res += "Display Width:" + display_width + "\n";
        res += "Display Height:" + display_height + "\n";
        res += "Connection Type:" + connection_type + "\n";
        res += "Data Speed:" + data_speed + "\n";
        res += "Android ID:" + android_id + "\n";
        res += "Didmd5:" + didmd5 + "\n";
        res += "Didsha1:" + didsha1 + "\n";
        res += "User Latitude:" + user_latitude + "\n";
        res += "User Longitude:" + user_longitude + "\n";
        res += "Internet Enable:" + is_internet_enable + "\n";
        res += "User Name:" + user_name + "\n";
        res += "User Email:" + user_email + "\n";
        res += "User Phone.No:" + user_phone + "\n";
        res += "User Account Name:" + user_account_name + "\n";
        res += "Carrier Name:" + carrier + "\n";
        res += "ua:" + ua + "\n";
        res += "app_id:" + app_id + "\n";
        res += "JS Enable::" + js_enable + "\n";
        res += "External IP::" + external_ip + "\n";
        res += "Device Type::" + device_type;

        Log.d("Reserved String", res);


        return res;
    }

}




