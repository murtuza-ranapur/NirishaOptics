package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nirisha.nirishaoptics.Pojo.DynamicValues;
import com.nirisha.nirishaoptics.api.NirishaAPIUtil;
import com.nirisha.nirishaoptics.services.Validator;
import com.nirisha.nirishaoptics.services.ValueAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class Order extends AppCompatActivity implements View.OnClickListener {

    private Spinner sp_eye,sp_type,sp_index,sp_coating,sp_diameter,sp_sphere,sp_cylinder,sp_axis,sp_addition,sp_height;
    private EditText et_tint,et_quality;
    private Button btn_proceed;
    private JSONObject orderObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
        NirishaAPIUtil util = NirishaAPIUtil.getInstance();
        util.init(Integer.parseInt(sp.getString("id",null)),sp.getString("auth",null));
        util.getDynamicValues(this);

        Thread waiter=new Thread(new ValueUpdater());
        waiter.start();

        findAllViews();
        btn_proceed.setOnClickListener(this);
        sp_eye.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.EYE));
//        sp_type.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.TYPE));
        sp_index.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.INDEX));

        sp_diameter.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.DIAMETER));
        sp_sphere.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.SPHERE));
        sp_cylinder.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.CYLINDER));
        sp_axis.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.AXIS));
        sp_addition.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.ADDITION));
        sp_height.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.HEIGHT));
    }

    private void findAllViews() {
        sp_axis=(Spinner)findViewById(R.id.sp_axis);
        sp_addition=(Spinner)findViewById(R.id.ap_addition);
        sp_coating=(Spinner)findViewById(R.id.sp_coating);
        sp_cylinder=(Spinner)findViewById(R.id.sp_cylinder);
        sp_diameter=(Spinner)findViewById(R.id.sp_diameter);
        sp_eye=(Spinner)findViewById(R.id.sp_eye);
        sp_height=(Spinner)findViewById(R.id.sp_height);
        sp_index=(Spinner)findViewById(R.id.sp_index);
        sp_type=(Spinner)findViewById(R.id.sp_type);
        sp_sphere=(Spinner)findViewById(R.id.sp_sphere);
        et_tint=(EditText)findViewById(R.id.et_tint);
        et_quality=(EditText)findViewById(R.id.et_quantity);
        btn_proceed=(Button)findViewById(R.id.btn_proceed);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_proceed){
            orderObj=new JSONObject();
            boolean flag=true;
            try {
                if(Validator.forEmpty(et_quality.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_quality.setError(Validator.getErrorMessage());
                }
                if(Validator.forEmpty(et_tint.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_tint.setError(Validator.getErrorMessage());
                }
                orderObj.put("eye",sp_eye.getSelectedItem());
                orderObj.put("type",sp_type.getSelectedItem());
                orderObj.put("index",sp_index.getSelectedItem());
                orderObj.put("coating",sp_coating.getSelectedItem());
                orderObj.put("dia",sp_diameter.getSelectedItem());
                orderObj.put("sphere",sp_sphere.getSelectedItem());
                orderObj.put("cylinder",sp_cylinder.getSelectedItem());
                orderObj.put("axis",sp_axis.getSelectedItem());
                orderObj.put("addition",sp_addition.getSelectedItem());
                orderObj.put("height",sp_height.getSelectedItem());
                orderObj.put("qty",Integer.parseInt(et_quality.getText().toString()));
                orderObj.put("tint",et_tint.getText().toString());
                orderObj.put("status","pending");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(flag) {
                Intent in = new Intent(this, Address.class);
                in.putExtra("order",orderObj.toString());
                startActivity(in);
            }
        }
    }


    class ValueUpdater implements Runnable{

        @Override
        public void run() {
            while (!DynamicValues.getInstance().isSet()){
                Log.e("Order","Waiting For value");
            }
            final ArrayAdapter<String> productAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,DynamicValues.getInstance().getProduct());
            productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productAdapter.notifyDataSetChanged();
            final ArrayAdapter<String> coatingAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,DynamicValues.getInstance().getCoating());
            coatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            coatingAdapter.notifyDataSetChanged();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sp_type.setAdapter(productAdapter);
                    sp_coating.setAdapter(coatingAdapter);
                }
            });
        }
    }

}
