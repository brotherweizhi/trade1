package com.bean;

public class Pic {
    String url;

    public String getUrlBig() {
        return urlBig;
    }

    public void setUrlBig(String urlBig) {
        this.urlBig = urlBig;
    }

    String urlBig;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url+"_200x200.jpg";
    }
}
