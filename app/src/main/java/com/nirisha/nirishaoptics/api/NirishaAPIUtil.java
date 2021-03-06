package com.nirisha.nirishaoptics.api;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.nirisha.nirishaoptics.MainActivity;
import com.nirisha.nirishaoptics.Order;
import com.nirisha.nirishaoptics.Pojo.DynamicValues;
import com.nirisha.nirishaoptics.volleyCustom.CustomStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

    private String authKey="174724ABcDF88Q2HC59bb694f";
    private String senderid="NIROPC";


    private NirishaAPIUtil() {
    }

    private static class NirishaHelper{
        private static final NirishaAPIUtil INSTANCE=new NirishaAPIUtil();
    }
    public static NirishaAPIUtil getInstance() {
        return NirishaHelper.INSTANCE;
    }

    public void init(int id, String Authentication){
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

    public void getDynamicValues(final Context context){
        dialog = ProgressDialog.show(context, "","Please Wait",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.GET,
                NirishaAPI.API_VALUES.getText()+"id="+id,
                headers,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DynamicValues dvalues=DynamicValues.getInstance();
                    JSONObject values= new JSONObject(response);
                    ArrayList<String> product=new ArrayList<>();
                    ArrayList<String> coating=new ArrayList<>();
                    JSONArray jproduct=values.getJSONArray("product");
                    JSONArray jcoating=values.getJSONArray("coating");
                    for(int i=0;i<jproduct.length();i++)
                        product.add(wordFirstCap(jproduct.getString(i).toLowerCase()));
                    for(int i=0;i<jcoating.length();i++)
                        coating.add(wordFirstCap(jcoating.getString(i).toLowerCase()));
                    dvalues.setProduct(product);
                    dvalues.setCoating(coating);
                    dvalues.setFlag();
                    dialog.cancel();
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


    public void doDeleteOrder(final Context context,int order){
        dialog = ProgressDialog.show(context, "","Please Wait",true);
        Log.e("Util", "doDeleteOrder: "+NirishaAPI.API_ORDER_DELETER.getText()+"id="+id+"&order="+order );
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.GET,
                NirishaAPI.API_ORDER_DELETER.getText()+"id="+id+"&order="+order,
                headers,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("DoOrderDelete", "onResponse: "+response );
                dialog.cancel();
                intent = new Intent(context, Order.class);
                intent.putExtra("session",1);
                ((Activity) context).finish();
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Log.e("DoOrderDelete", "onErrorResponse: "+error.getMessage() );
            }
        });
        queue.add(req);
    }

    public void doOrder(JSONObject payload, final Context context){
        dialog = ProgressDialog.show(context, "","Placing Order..",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.POST,
                NirishaAPI.API_ORDER.getText()+"id="+id,
                headers, payload.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                JSONObject resObject=null;
                try {
                    resObject=new JSONObject(response);
                    if (resObject.has("code"))
                    if(resObject.getInt("code")==200){
                        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                        dlgAlert.setMessage("Your Order has been successfully placed. You will soon receive " +
                                "confirmation mail.");
                        dlgAlert.setTitle("Message");
                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                intent = new Intent(context, Order.class);
                                intent.putExtra("session",1);
                                ((Activity) context).finish();
                                context.startActivity(intent);
                            }
                        });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                    else {
                        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                        dlgAlert.setMessage(response);
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

    public void getOrders(final Context context){
        dialog = ProgressDialog.show(context, "","Please Wait",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.GET,
                NirishaAPI.APi_ORDER_GETTER.getText() + "id=" + id,
                headers, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject orders=new JSONObject(response);
                    if(orders.length()==0){
                        dialog.cancel();
                    }
                    else {
                        Map<String,JSONObject> orderList=new HashMap<>();
                        Iterator<String> keys=orders.keys();
                        while (keys.hasNext()){
                            String key=keys.next();
                            Log.e("getOrders", "onResponse: "+key);
                            orderList.put(key,orders.getJSONObject(key));
                        }
                        DynamicValues.getInstance().setOrderData(orderList);
                        DynamicValues.getInstance().setOrderFlag();
                        dialog.cancel();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.cancel();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getOrders", "onErrorResponse: something went Wrong"+error.getMessage() );
                        dialog.cancel();
                    }
                }
        );
        queue.add(req);
    }

    public void getProducts(final Context context){
        dialog = ProgressDialog.show(context, "","Please Wait",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        CustomStringRequest req=new CustomStringRequest(
                Request.Method.GET,
                NirishaAPI.API_PRODUCTS.getText()+"id="+id,
                headers,"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SQLiteDatabase nirisha = context.openOrCreateDatabase("nirisha",MODE_PRIVATE,null);
                nirisha.execSQL("drop table if exists product");
                nirisha.execSQL("drop table if exists coatingprice");
                nirisha.execSQL("CREATE TABLE if not exists `product` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `indx` double DEFAULT NULL," +
                        "  `prdown` double DEFAULT NULL," +
                        "  `prup` double DEFAULT NULL," +
                        "  `fh3` double DEFAULT NULL," +
                        "  `fh2` double DEFAULT NULL," +
                        "  `fh1` double DEFAULT NULL," +
                        "  `dia` double DEFAULT NULL," +
                        "  `product` varchar(80) DEFAULT NULL," +
                        "  `version` int(11) DEFAULT '0'," +
                        "  `updateOn` datetime DEFAULT NULL," +
                        "  `isActive` tinyint(4) DEFAULT '1'" +
                        ");");
                nirisha.execSQL("CREATE TABLE if not exists `coatingprice` (" +
                        "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  `product_id` int(11) DEFAULT NULL," +
                        "  `coating` varchar(45) DEFAULT NULL," +
                        "  `price` varchar(45) DEFAULT NULL," +
                        "  `isActive` tinyint(4) DEFAULT '1'," +
                        "  `version` int(11) DEFAULT '0'," +
                        "  `updatedOn` datetime DEFAULT NULL" +
                        ")");
                try {
                    JSONObject result=new JSONObject(response);
                    JSONArray results=result.getJSONArray("result");
                    Set<String> set=new HashSet<>();
                    for (int i=0;i<results.length();i++){
                        JSONObject prods=results.getJSONObject(i);
                        String product=prods.getString("product");
                        double index=prods.getDouble("index");
                        double dia=prods.getDouble("dia");
                        JSONArray fh=prods.getJSONArray("fh");
                        JSONArray pr=prods.getJSONArray("pr");
                        JSONArray coating=prods.getJSONArray("coating");
                        JSONArray price=prods.getJSONArray("price");
                        String [] fhs=new String[3];
                        String [] prs=new String[3];
                        String [] coatings=new String[3];
                        String [] prices=new String[3];
                        for (int k =0;k<fh.length();k++)
                        {
                            fhs[k]=fh.getString(k);
                        }
                        for (int k =0;k<pr.length();k++)
                        {
                            prs[k]=pr.getString(k);
                        }
                        for (int k =0;k<coating.length();k++)
                        {
                            coatings[k]=coating.getString(k);
                        }
                        for (int k =0;k<price.length();k++)
                        {
                            prices[k]=price.getString(k);
                        }
                        nirisha.execSQL("insert into product (product,indx,dia,fh1,fh2,fh3,prup,prdown) " +
                                "values ('"+wordFirstCap(product.toLowerCase())+"',"+index+","+dia+","+fhs[0]+","+fhs[1]+","+fhs[2]+","+prs[0]+","+prs[1]+")");
                        long prod_id=0;
                        set.add(wordFirstCap(product.toLowerCase()));
                        String query = "SELECT ROWID from product order by ROWID DESC limit 1";
                        Cursor c = nirisha.rawQuery(query,null);
                        if (c != null && c.moveToFirst()) {
                            prod_id = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
                        }
                        Log.e("status", "onResponse: "+prod_id);
                        for (int k=0;k<coatings.length;k++){
                            nirisha.execSQL("insert into coatingprice (product_id,coating,price) " +
                                    "values ("+prod_id+",'"+coatings[k]+"',"+prices[k]+")");
                        }

                    }
                    DynamicValues dv=DynamicValues.getInstance();
                    dv.setProduct(new ArrayList<>(set));
                    dv.setFlag();
                    dialog.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    dialog.cancel();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }

    public void doSendOtp(final Context context,JSONObject object){
        try {
            Random rand=new Random();
            int otp = 1000+rand.nextInt(9000);
            String url = String.format("https://control.msg91.com/api/sendotp.php?authkey=%s&mobile=%s" +
                            "&message=%s&sender=%s&otp=%s", Uri.encode(authKey), Uri.encode("91" + object.getString("phone")),
                    Uri.encode("Your OTP is " + otp),Uri.encode(senderid), Uri.encode("" + otp));
            Log.e("Util", "doSendOtp url: "+url );
            RequestQueue queue = Volley.newRequestQueue(context);
            CustomStringRequest req=new CustomStringRequest(
                    Request.Method.GET,
                    url,
                    headers, "", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Send Opt", "onResponse: "+response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("SendOtp", "onErrorResponse: something went Wrong" );
                        }
                    }
            );
            queue.add(req);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void doResendOtp(final Context context,JSONObject object){
        try {
            String url = String.format("https://control.msg91.com/api/retryotp.php?" +
                            "authkey=%s&mobile=%s&retrytype=voice", Uri.encode(authKey), Uri.encode("91" + object.getString("phone")));
            Log.e("Util", "doSendOtp url: "+url );
            RequestQueue queue = Volley.newRequestQueue(context);
            CustomStringRequest req=new CustomStringRequest(
                    Request.Method.GET,
                    url,
                    headers, "", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("ReSend Opt", "onResponse: "+response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ReSendOtp", "onErrorResponse: something went Wrong" );
                        }
                    }
            );
            queue.add(req);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void doVerifyOtp(final Context context, final JSONObject object, String otp){
        try {
            dialog = ProgressDialog.show(context, "","Please Wait",true);
            String url = String.format("https://control.msg91.com/api/verifyRequestOTP.php?" +
                            "authkey=%s&mobile=%s&otp=%s", Uri.encode(authKey),
                    Uri.encode("91" + object.getString("phone")),
                    Uri.encode(otp));
            Log.e("Util", "doSendOtp url: "+url );
            RequestQueue queue = Volley.newRequestQueue(context);
            CustomStringRequest req=new CustomStringRequest(
                    Request.Method.GET,
                    url,
                    headers, "", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Verify Opt", "onResponse: "+response);
                    try{
                        JSONObject rep=new JSONObject(response);
                        if(rep.getString("type").equalsIgnoreCase("success")){
                            dialog.cancel();
                            doRegister(object,context);
                        }
                        else {
                            dialog.cancel();
                            final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                            dlgAlert.setMessage("Invalid Otp Entered please re-enter OTP or try Resend OTP to receive voice message");
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
                    }catch (JSONException e){
                        e.printStackTrace();
                        dialog.cancel();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VerifyOtp", "onErrorResponse: something went Wrong" );
                            dialog.cancel();
                        }
                    }
            );
            queue.add(req);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String wordFirstCap(String str)
    {
        String[] words = str.trim().split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++)
        {

            if(words[i].trim().length() > 0)
            {
                ret.append(Character.toUpperCase(words[i].trim().charAt(0)));
                ret.append(words[i].trim().substring(1));
                if(i < words.length - 1) {
                    ret.append(' ');
                }
            }
        }

        return ret.toString();
    }
}
