package com.ad.sdk.adserver;

public class AdResponseValue {
    String zone_id;
    String imp_url;
    String ad_type;
    String ad_tag;

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    String click_url;

    String req_url;
    String image_path;

    public String getImp_url() {
        return imp_url;
    }

    public void setImp_url(String imp_url) {
        this.imp_url = imp_url;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getAd_tag() {
        return ad_tag;
    }

    public void setAd_tag(String ad_tag) {
        this.ad_tag = ad_tag;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getReq_url() {
        return req_url;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
