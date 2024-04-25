package com.ad.sdk.adserver;

import android.util.Log;

import com.ad.sdk.utils.Cdlog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


class ServiceHandler {

    public ServiceHandler() {

        // TODO Auto-generated constructor stub
    }
/*
	List<Pair<String, String>> params = new ArrayList<>();
	params.add(new Pair<>("username", username));
	params.add(new Pair<>("password", password));
	 */

    public String makeServiceCall(String trackURL) {

        URL url;
        String response = "";
        HttpURLConnection conn = null;
//        HashMap<String, String> hParams;
//        HashMap<String, String> uParams;
//        Map<String, String> headervalue = null;

        try {
            String requestURL = trackURL;
//            uParams = AdRequestParam.getURL_PARAMS();
//            hParams = AdRequestParam.getHEADER_PARAMS();
            Cdlog.d(Cdlog.httpReqLogTag, requestURL);
            Cdlog.setLastRequest(requestURL);
            url = new URL(requestURL);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//            headervalue = new HashMap<String, String>();
//            for (Map.Entry me : hParams.entrySet()) {
//                if (me.getValue() != null) {
//                    String name = me.getKey().toString();
//                    String value = me.getValue().toString();
//                    Cdlog.d(Cdlog.httpReqLogTag, name + "=>" + value);
//                    conn.setRequestProperty(name, value);
//                    headervalue.put(name, value);
//                } else {
//                    String name = me.getKey().toString();
//                    String value = "";
//                    Cdlog.d(Cdlog.httpReqLogTag, name + "=>" + "");
//                    conn.setRequestProperty(name, value);
//                    headervalue.put(name, value);
//                }
//            }
            OutputStream os = conn.getOutputStream();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.i("autolog", "responseCode: " + responseCode);
            response = readFullyAsString(conn.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Cdlog.d(Cdlog.httpResLogTag, response);
        Cdlog.setLastResponse(response);
        //Cdlog.setLastRequestheader(hParams);

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } else {
                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode("", "UTF-8"));
            }

        }

        return result.toString();
    }

    private String readFullyAsString(InputStream inputStream) throws IOException {
        return readFully(inputStream).toString("UTF-8");
    }

    private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
