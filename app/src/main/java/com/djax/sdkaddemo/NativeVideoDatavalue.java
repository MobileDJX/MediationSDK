package com.djax.sdkaddemo;

import android.view.View;

public class NativeVideoDatavalue {

    String name;
    String img;
    String des;
    String type;
    View viewNativeVideo;

    public NativeVideoDatavalue(String name, String img, String des, String type, View viewNativeVideo) {
        this.name = name;
        this.img = img;
        this.des = des;
        this.type = type;
        this.viewNativeVideo=viewNativeVideo;
    }

    public View getViewNativeVideo() {
        return viewNativeVideo;
    }

    public void setViewNativeVideo(View viewNativeVideo) {
        this.viewNativeVideo = viewNativeVideo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
