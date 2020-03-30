package com.bean;

import java.util.ArrayList;
import java.util.List;

public class ImageBean {
    public ImageBean(int picResID) {
        this.picResID = picResID;
    }

    public int getPicResID() {
        return picResID;
    }

    public void setPicResID(int picResID) {
        this.picResID = picResID;
    }

    int picResID;
    String picUri;
    boolean selected;
    List<ImageBean> mListSelect=new ArrayList<>();


    public List<ImageBean> getmListSelect() {
        return mListSelect;
    }

    public void setmListSelect(List<ImageBean> mListSelect) {
        this.mListSelect = mListSelect;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ImageBean(String picUri) {
        this.picUri = picUri;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }
}
