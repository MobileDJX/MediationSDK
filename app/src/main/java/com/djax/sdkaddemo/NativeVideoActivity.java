package com.djax.sdkaddemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.sdk.adserver.AdView;
import com.ad.sdk.adserver.Listener.NativeAdViewListener;
import com.ad.sdk.adserver.NativeDatavalue;

import java.util.ArrayList;

public class NativeVideoActivity extends AppCompatActivity implements NativeAdViewListener {

    private RecyclerView mRecyclerView;
    private ListAdapterNativeVideo listAdapter;
    private ArrayList<NativeVideoDatavalue> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_video);

        mRecyclerView = findViewById(R.id.recycler_view);

        String zone_id=AppApplication.sharedPreferences.getString("Zone_ID","");
        System.out.println("@ zone_id "+zone_id);

        /*AdView ad1 = new AdView(this,this);
        ad1.setZoneid(zone_id); //Place Your Zone id
        ad1.LoadAd();*/

        list=new ArrayList<NativeVideoDatavalue>();

        list.add(new NativeVideoDatavalue("Android 13","https://www.qries.com/images/banner_logo.png","Tiramisu-Build for user privacy with photo picker and notification permission. Improve productivity with themed app icons, per-app languages, and clipboard preview.","",null));
        list.add(new NativeVideoDatavalue("Android 12","https://www.qries.com/images/banner_logo.png","Snow Cone-12L is a special feature drop that makes Android 12 even better on tablets and foldable devices. With 12L, we’ve optimized and polished the system UI for large screens, made multitasking more powerful and intuitive, and improved compatibility support so apps look better right out of the box.","",null));
        list.add(new NativeVideoDatavalue("Android 11","https://www.qries.com/images/banner_logo.png","Red Velvet Cake-People-centric and expressive, with a new controls space and more privacy features.\n" +
                "Extend your apps with conversation notifications and bubbles, try one-time permissions, surface devices and media in the controls.","",null));
        list.add(new NativeVideoDatavalue("Android 10","https://www.qries.com/images/banner_logo.png","Red Velvet Cake-Android 10 is built around three important themes. First, Android 10 is shaping the leading edge of mobile innovation with advanced machine-learning and support for emerging devices like foldables and 5G enabled phones.","",null));
        list.add(new NativeVideoDatavalue("Android 9","https://www.qries.com/images/banner_logo.png","Quince Tart-Android 9 (API level 28) introduces great new features and capabilities for users and developers. This document highlights what's new for developers.","",null));
        list.add(new NativeVideoDatavalue("Android 8","https://i.imgur.com/CzXTtJV.jpg","Oreo-Android 8.1 (API level 27) introduces a variety of new features and capabilities for users and developers. This document highlights what's new for developers.","",null));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listAdapter = new ListAdapterNativeVideo(NativeVideoActivity.this,list);
        mRecyclerView.setAdapter(listAdapter);



    }


    private void adPosition() {

        //list.add(2,new Datavalue(new NativeImage().datavalueArrayList.get(0).getTitle(),"Level 27 (Android 8.1)","Oreo-Android 8.1 (API level 27) introduces a variety of new features and capabilities for users and developers. This document highlights what's new for developers."));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NativeVideoActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public void onAdLoad(ArrayList<NativeDatavalue> nativeDatavalues) {
        System.out.println("@@ Native Listener Success");

      /*  for(int i=0;i<=list.size();i++)
        {

        }*/
        /*list.add(2,new NativeVideoDatavalue(nativeDatavalues.get(0).getTitle(),nativeDatavalues.get(0).getIconimage(),nativeDatavalues.get(0).getText(),"true",nativeDatavalues.get(0).getMainimage()));
        listAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onAdFail() {
        System.out.println("@@ Native Listener failed");
    }

/*    @Override
    public void onAdLoad() {

        System.out.println("@@ Native Listener Success");
        list.add(2,new Datavalue(new NativeImage().getDatavalueArrayList().get(0).getTitle(),"Level 27 (Android 8.1)","Oreo-Android 8.1 (API level 27) introduces a variety of new features and capabilities for users and developers. This document highlights what's new for developers."));

    }

    @Override
    public void onAdFail() {
        System.out.println("@@ Native Listener failed");
    }*/
}