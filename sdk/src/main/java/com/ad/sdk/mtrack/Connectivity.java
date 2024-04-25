package com.ad.sdk.mtrack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check device's network connectivity and speed 
 * @author emil http://stackoverflow.com/users/220710/emil
 *
 */
class Connectivity{
  
	/**
	 * Get the network info
	 * @param context app context
	 * @return return the value
	 */
	public static NetworkInfo getNetworkInfo(Context context){
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return cm.getActiveNetworkInfo();
	}
}
