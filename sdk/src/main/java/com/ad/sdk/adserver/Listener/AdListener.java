package com.ad.sdk.adserver.Listener;

import com.ad.sdk.adserver.AdView;

public interface AdListener {
	void param_required(AdView ad, boolean flag);
	
	void internet_connection_failed(AdView ad, boolean flag);
	
	void load_ad_failed(AdView ad, boolean flag, String ecode, String edesc);
	
}
