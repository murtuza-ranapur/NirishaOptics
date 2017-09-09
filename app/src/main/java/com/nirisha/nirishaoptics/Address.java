package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nirisha.nirishaoptics.api.NirishaAPIUtil;
import com.nirisha.nirishaoptics.services.Validator;
import com.nirisha.nirishaoptics.services.ValueAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

public class Address extends AppCompatActivity implements View.OnClickListener {

    private EditText et_add_phone,et_address_1,et_address_2,et_city,et_pin;
    private SearchableSpinner sp_states;
    private Button btn_checkout;
    private JSONObject order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        findAllViews();
        sp_states.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.STATES));
        sp_states.setTitle("Select State");
        sp_states.setPositiveButton("OK");
        sp_states.setSelection(ValueAdapter.defaultValueService("Gujarat",sp_states));

        Intent oldIntent=getIntent();
        try {
            order=new JSONObject(oldIntent.getStringExtra("order"));
            Log.e("Address", "Old Json:"+order.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_checkout.setOnClickListener(this);
    }

    private void findAllViews() {
        et_add_phone=(EditText)findViewById(R.id.et_add_phone);
        et_address_1=(EditText)findViewById(R.id.et_address_1);
        et_address_2=(EditText)findViewById(R.id.et_address_2);
        et_city=(EditText)findViewById(R.id.et_add_city);
        et_pin=(EditText)findViewById(R.id.et_pin);
        sp_states=(SearchableSpinner)findViewById(R.id.sp_states);
        btn_checkout=(Button)findViewById(R.id.btn_checkout);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_checkout){
            JSONObject address=new JSONObject();
            boolean flag=true;
            SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
            try {
                if(Validator.forEmpty(et_add_phone.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_add_phone.setError(Validator.getErrorMessage());
                }
                if(Validator.forEmpty(et_address_1.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_address_1.setError(Validator.getErrorMessage());
                }

                if(Validator.forEmpty(et_city.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_city.setError(Validator.getErrorMessage());
                }

                if(Validator.forEmpty(et_pin.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_pin.setError(Validator.getErrorMessage());
                }
                address.put("address",et_address_1.getText().toString()+et_address_2.getText().toString());
                address.put("state",sp_states.getSelectedItem());
                address.put("city",et_city.getText().toString());
                address.put("pin",et_pin.getText().toString());
                address.put("phone",et_add_phone.getText().toString());
                address.put("kind","common");
                order.put("address_id",-1);
                order.put("address",address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(flag){
                NirishaAPIUtil util = NirishaAPIUtil.getInstance();
                util.init(Integer.parseInt(sp.getString("id",null)),sp.getString("auth",null));
                Log.e("Address", order.toString());
                util.doOrder(order,this);
            }
        }
    }
}
