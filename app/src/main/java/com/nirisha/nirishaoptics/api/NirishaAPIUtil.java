package com.nirisha.nirishaoptics.api;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nirisha.nirishaoptics.volleyCustom.CustomStringRequest;

import org.json.JSONObject;

/**
 * Created by hp on 09-08-2017.
 */

public class NirishaAPIUtil {
    private int id;
    private String Authentication;

    private NirishaAPIUtil() {
    }

    private static class NirishaHelper{
        private static final NirishaAPIUtil INSTANCE=new NirishaAPIUtil();
    }
    public static NirishaAPIUtil getInstance() {
        return NirishaHelper.INSTANCE;
    }

    void doRegister(JSONObject payload, Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
//        CustomStringRequest req=new CustomStringRequest()
    }
}
