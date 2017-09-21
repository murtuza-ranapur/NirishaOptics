package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nirisha.nirishaoptics.api.NirishaAPIUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Otp extends AppCompatActivity implements View.OnClickListener {

    EditText et_otp;
    Button btn_verify_otp,btn_resend_otp;
    JSONObject regObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt);
        findAllViews();
        Intent oldIntent=getIntent();
        try {
            regObj=new JSONObject(oldIntent.getStringExtra("reg"));
            Log.e("OTP", "Old Json:"+regObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btn_verify_otp.setOnClickListener(this);
        btn_resend_otp.setOnClickListener(this);
    }

    private void findAllViews() {
        et_otp=(EditText)findViewById(R.id.et_otp);
        btn_verify_otp=(Button)findViewById(R.id.btn_verify_otp);
        btn_resend_otp=(Button)findViewById(R.id.btn_resend_otp);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_verify_otp){
            NirishaAPIUtil util=NirishaAPIUtil.getInstance();
            util.doVerifyOtp(this,regObj,et_otp.getText().toString());
        }
        else if(view == btn_resend_otp){
            NirishaAPIUtil util=NirishaAPIUtil.getInstance();
            util.doResendOtp(this,regObj);
        }
    }
}
