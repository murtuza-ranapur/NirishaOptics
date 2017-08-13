package com.nirisha.nirishaoptics;

import android.content.Intent;
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

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_login;
    private Button btn_register;
    private EditText et_fname,et_lname,et_email,et_password,et_mobile,et_repass;
    private JSONObject regObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();
        findAllElements();
        tv_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void findAllElements() {
        tv_login=(TextView)findViewById(R.id.tv_login);
        btn_register=(Button)findViewById(R.id.btn_register);
        et_fname=(EditText)findViewById(R.id.et_fname);
        et_lname=(EditText)findViewById(R.id.et_lname);
        et_password=(EditText)findViewById(R.id.et_pass);
        et_email=(EditText)findViewById(R.id.et_reg_email);
        et_mobile=(EditText)findViewById(R.id.et_mob);
        et_repass=(EditText)findViewById(R.id.et_repass);
    }

    @Override
    public void onClick(View view) {
        if(view==tv_login){
            Intent intent=new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }
        else if(view==btn_register){
            boolean flag=true;
            regObj =new JSONObject();
            try {
                if(Validator.forEmpty(et_fname.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_fname.setError(Validator.getErrorMessage());
                }

                if(Validator.forEmpty(et_lname.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_lname.setError(Validator.getErrorMessage());
                }

                if(Validator.forEmail(et_email.getText().toString()))
                    flag &=true;
                else {
                    flag &=false;
                    et_email.setError(Validator.getErrorMessage());
                }

                if(Validator.forPassword(et_password.getText().toString(),8))
                    flag &=true;
                else {
                    flag &=false;
                    et_password.setError(Validator.getErrorMessage());
                }

                if(Validator.forLength(et_mobile.getText().toString(),10))
                    flag &=true;
                else {
                    flag &=false;
                    et_mobile.setError(Validator.getErrorMessage());
                }

                if(et_repass.getText().toString().equals(et_password.getText().toString()))
                    flag &=true;
                else {
                    flag &= false;
                    et_repass.setError("Passwords doesn't Match");
                }
                regObj.put("fname",et_fname.getText());
                regObj.put("lname",et_lname.getText());
                regObj.put("email",et_email.getText());
                regObj.put("password",et_password.getText());
                regObj.put("phone",et_mobile.getText());
                regObj.put("type","common");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(flag) {
                NirishaAPIUtil util = NirishaAPIUtil.getInstance();
                util.doRegister(regObj, this);
            }
        }
    }
}
