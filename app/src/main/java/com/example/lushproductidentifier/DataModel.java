package com.example.lushproductidentifier;

import android.os.Parcelable;

import java.io.Serializable;

public class DataModel {

    private String name, imgURL;

    public String getImgURL(){
        return imgURL;
    }

    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
