package com.djax.sdkaddemo;

public class Datavalue {

    String name;
    String img;
    String des;
    String type;
    String nativeImg;

    public Datavalue(String name, String img, String des,String type,String nativeImg) {
        this.name = name;
        this.img = img;
        this.des = des;
        this.type = type;
        this.nativeImg = nativeImg;
    }

    public String getNativeImg() {
        return nativeImg;
    }

    public void setNativeImg(String nativeImg) {
        this.nativeImg = nativeImg;
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
