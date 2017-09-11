package com.nirisha.nirishaoptics.Pojo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nirisha.nirishaoptics.api.NirishaAPIUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hp on 15-08-2017.
 */

public class DynamicValues {
    private List<String> product=new ArrayList<>();
    private List<String> coating=new ArrayList<>();
    private volatile boolean flag=false;
    private SQLiteDatabase nirisha;

    private Map<String,JSONObject> orderData;
    private volatile boolean orderFlag=false;

    private DynamicValues(){}
    private static class DynamicValuesHelper{
        private static final DynamicValues INSTANCE=new DynamicValues();
    }
    public static DynamicValues getInstance(){
        return DynamicValuesHelper.INSTANCE;
    }

    public void init(Context context){
        nirisha = context.openOrCreateDatabase("nirisha",MODE_PRIVATE,null);
    }
    public List<String> getProduct() {
        Collections.sort(this.product);
        return this.product;
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

    public Map<String, JSONObject> getOrderData() {
        return orderData;
    }

    public void setOrderData(Map<String, JSONObject> orderData) {
        this.orderData = orderData;
    }

    public void setFlag(){
        flag=true;
    }
    public boolean isSet(){
        return flag;
    }

    public void setOrderFlag(){orderFlag=true;}
    public boolean isOrderFlagSet(){return  orderFlag;}
}
