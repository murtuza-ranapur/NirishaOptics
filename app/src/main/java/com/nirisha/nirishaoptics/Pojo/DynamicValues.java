package com.nirisha.nirishaoptics.Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 15-08-2017.
 */

public class DynamicValues {
    private List<String> product=new ArrayList<>();
    private List<String> coating=new ArrayList<>();
    private volatile boolean flag=false;

    private DynamicValues(){}
    private static class DynamicValuesHelper{
        private static final DynamicValues INSTANCE=new DynamicValues();
    }
    public static DynamicValues getInstance(){
        return DynamicValuesHelper.INSTANCE;
    }

    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }

    public List<String> getCoating() {
        return coating;
    }

    public void setCoating(List<String> coating) {
        this.coating = coating;
    }

    public void setFlag(){
        flag=true;
    }
    public boolean isSet(){
        return flag;
    }
}
