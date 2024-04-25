package com.ad.sdk.adserver;

public class NativeDatavalue {

    String title;
    String text;
    String mainimage;
    String ctatext;
    String iconimage;

    public NativeDatavalue(String title, String text, String mainimage, String ctatext, String iconimage) {
        this.title = title;
        this.text = text;
        this.mainimage = mainimage;
        this.ctatext = ctatext;
        this.iconimage = iconimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMainimage() {
        return mainimage;
    }

    public void setMainimage(String mainimage) {
        this.mainimage = mainimage;
    }

    public String getCtatext() {
        return ctatext;
    }

    public void setCtatext(String ctatext) {
        this.ctatext = ctatext;
    }

    public String getIconimage() {
        return iconimage;
    }

    public void setIconimage(String iconimage) {
        this.iconimage = iconimage;
    }
}
