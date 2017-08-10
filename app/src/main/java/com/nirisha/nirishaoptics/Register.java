package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nirisha.nirishaoptics.api.NirishaAPIUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_login;
    private Button btn_register;
    private EditText et_fname,et_lname,et_email,et_password,et_mobile;
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
    }

    @Override
    public void onClick(View view) {
        if(view==tv_login){
            Intent intent=new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }
        else if(view==btn_register){
            regObj =new JSONObject();
            try {
                regObj.put("fname",et_fname.getText());
                regObj.put("lname",et_lname.getText());
                regObj.put("email",et_email.getText());
                regObj.put("password",et_password.getText());
                regObj.put("phone",et_mobile.getText());
                regObj.put("type","common");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NirishaAPIUtil util=NirishaAPIUtil.getInstance();
            util.doRegister(regObj,this);
        }
    }
}
