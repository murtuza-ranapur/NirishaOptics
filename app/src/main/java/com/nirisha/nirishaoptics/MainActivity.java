package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    Button login;
    TextView tv_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        findAllElements();
        tv_signin.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void findAllElements() {
        tv_signin=(TextView)findViewById(R.id.tv_signup);
        login=(Button)findViewById(R.id.btn_login);
    }

    @Override
    public void onClick(View view) {
        if(view==tv_signin){
            Intent intent=new Intent(this,Register.class);
            finish();
            startActivity(intent);
        }
        else if(view==login){
            Intent intent=new Intent(this,Order.class);
            finish();
            startActivity(intent);
        }
    }
}
