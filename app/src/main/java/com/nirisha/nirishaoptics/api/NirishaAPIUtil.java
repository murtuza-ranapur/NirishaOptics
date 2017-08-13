package com.nirisha.nirishaoptics.api;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nirisha.nirishaoptics.MainActivity;
import com.nirisha.nirishaoptics.Order;
import com.nirisha.nirishaoptics.volleyCustom.CustomStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hp on 09-08-2017.
 */

public class NirishaAPIUtil {
    private int id;
    private String Authentication;
    private Map<String,String> headers=null;
    private ProgressDialog dialog;
    private Intent intent;

    private NirishaAPIUtil() {
    }

    private static class NirishaHelper{
        private static final NirishaAPIUtil INSTANCE=new NirishaAPIUtil();
    }
    public static NirishaAPIUtil getInstance() {
        return NirishaHelper.INSTANCE;
    }

    void init(int id, String Authentication){
        this.id=id;
        this.Authentication=Authentication;
        headers = new HashMap<>();
        headers.put("Authorization",Authentication);
    }
    public void doRegister(JSONObject payload, final Context context){
        dialog = ProgressDialog.show(context, "","Registering",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.POST,
                NirishaAPI.API_REGISTER.getText(),
                headers, payload.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                intent=new Intent(context,MainActivity.class);
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    public void doLogin(JSONObject payload, final Context context){
        dialog = ProgressDialog.show(context, "","Logging you in",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.POST,
                NirishaAPI.API_LOGIN.getText(),
                headers, payload.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                JSONObject resObject=null;
                try {
                    resObject=new JSONObject(response);
                    if(resObject.getInt("code")==200){
                        SharedPreferences sp=context.getSharedPreferences("Nirisha",MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("id",resObject.getString("id"));
                        edit.putString("auth",resObject.getString("auth"));
                        edit.apply();
                        intent = new Intent(context, Order.class);
                        ((Activity) context).finish();
                        context.startActivity(intent);
                    }
                    else {
                        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                        dlgAlert.setMessage(resObject.getString("msg"));
                        dlgAlert.setTitle("Message");
                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }
}
