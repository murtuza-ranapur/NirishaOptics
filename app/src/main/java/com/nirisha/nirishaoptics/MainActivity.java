package com.nirisha.nirishaoptics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nirisha.nirishaoptics.api.NirishaAPIUtil;
import com.nirisha.nirishaoptics.services.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private Button login;
    private TextView tv_signin;
    private EditText et_email, et_password;
    private JSONObject logObject;
    private Intent intent;
    private ConstraintLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        findAllElements();
        tv_signin.setOnClickListener(this);
        login.setOnClickListener(this);

//        SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
//        if(sp.getString("id",null)!=null) {
//            intent = new Intent(this, Order.class);
//            finish();
//            startActivity(intent);
//        }
    }

    private void findAllElements() {
        tv_signin=(TextView)findViewById(R.id.tv_signup);
        login=(Button)findViewById(R.id.btn_login);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        activity_main=(ConstraintLayout)findViewById(R.id.activity_main);
    }

    @Override
    public void onClick(View view) {
        if(view==tv_signin){
            Intent intent=new Intent(this,Register.class);
            finish();
            startActivity(intent);
        }
        else if(view==login){
            activity_main.setBackgroundColor(ContextCompat.getColor(this,R.color.background_dark));
            logObject =new JSONObject();
            boolean flag=true;
            try {
                if(Validator.forEmpty(et_email.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_email.setError(Validator.getErrorMessage());
                }

                if(Validator.forEmpty(et_password.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_password.setError(Validator.getErrorMessage());
                }

                logObject.put("email",et_email.getText());
                logObject.put("password",md5(et_password.getText().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (flag) {
                NirishaAPIUtil util = NirishaAPIUtil.getInstance();
                util.doLogin(logObject, this);
            }
        }
    }

    public String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }
}
