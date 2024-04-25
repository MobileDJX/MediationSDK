package com.ad.sdk.adserver;

import com.ad.sdk.adserver.Listener.NativeAdViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NativeImage {

    public ArrayList<NativeDatavalue> datavalueArrayList;


    public ArrayList<NativeDatavalue> getDatavalueArrayList() {
        return datavalueArrayList;
    }

    public void setDatavalueArrayList(ArrayList<NativeDatavalue> datavalueArrayList) {
        this.datavalueArrayList = datavalueArrayList;
    }

    public NativeImage() {
    }

    public void loadNativeImage(JSONObject jsonObject,NativeAdViewListener nativeAdViewListener)
    {
        try {
            String title=jsonObject.getString("title");
            String text=jsonObject.getString("text");
            String mainimage=jsonObject.getString("mainimage");
            String ctatext=jsonObject.getString("ctatext");
            String iconimage=jsonObject.getString("iconimage");
            datavalueArrayList =new ArrayList<NativeDatavalue>();
            datavalueArrayList.add(new NativeDatavalue(title,text,mainimage,ctatext,iconimage));

            if(nativeAdViewListener!=null)
            {
                nativeAdViewListener.onAdLoad(datavalueArrayList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
