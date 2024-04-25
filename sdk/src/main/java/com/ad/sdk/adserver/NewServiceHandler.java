package com.ad.sdk.adserver;


import static com.ad.sdk.adserver.Config.ADSERVER_URL;

import com.ad.sdk.utils.Cdlog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

class NewServiceHandler {

    @SuppressWarnings("unused")
    public final static int GET = 1;
    @SuppressWarnings("unused")
    public final static int POST = 2;

    public NewServiceHandler() {

        // TODO Auto-generated constructor stub
    }
/*
	List<Pair<String, String>> params = new ArrayList<>();
	params.add(new Pair<>("username", username));
	params.add(new Pair<>("password", password));
	 */

    /*
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
/*	public String makeServiceCall(AdRequestParam reqObj, int method) {
		return this.makeServiceCall();
	}*/

    /**
     * Making service call
     */
    //DAC158
    public String makeServiceCall() {

        URL url;
        String response = "";
        HashMap<String, String> hParams;
        HashMap<String, String> uParams;
        Map<String, String> headervalue = null;

        try {

            String requestURL = ADSERVER_URL;
            uParams = AdRequestParam.getURL_PARAMS();
            hParams = AdRequestParam.getHEADER_PARAMS();
            requestURL = requestURL + "?" + getPostDataString(uParams);
            Cdlog.d(Cdlog.httpReqLogTag, requestURL);
            Cdlog.setLastRequest(requestURL);

            System.out.println("@@ requestURL :" + requestURL);
            url = new URL(requestURL);


            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            headervalue = new HashMap<String, String>();
            for (Map.Entry<String, String> me : hParams.entrySet()) {
                if (me.getValue() != null) {
                    String name = me.getKey();
                    String value = me.getValue();
                    Cdlog.d(Cdlog.httpReqLogTag, name + "=>" + value);
                    httpConnection.setRequestProperty(name, value);
                    headervalue.put(name, value);
                } else {
                    String name = me.getKey();
                    String value = "";
                    Cdlog.d(Cdlog.httpReqLogTag, name + "=>" + "");
                    httpConnection.setRequestProperty(name, value);
                    headervalue.put(name, value);
                }
            }

            httpConnection.setRequestMethod("GET");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(100000);
            httpConnection.setReadTimeout(100000);

            httpConnection.connect();

            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                response = sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Cdlog.setLastRequestheader(headervalue);
        Cdlog.d(Cdlog.httpResLogTag, response);
        Cdlog.setLastResponse(response);
        return response;


    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            //if (first) {
            //	first = false;
            //} else {

            if (entry.getValue() != null) {
                if (!first) {
                    result.append("&");
                }
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                first = false;
            } else {
                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode("", "UTF-8"));
            }
            //}

        }




        return result.toString();
    }
}
