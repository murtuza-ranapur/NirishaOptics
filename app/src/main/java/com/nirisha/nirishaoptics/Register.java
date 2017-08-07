package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Register extends AppCompatActivity implements View.OnClickListener {

    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();
        findAllElements();
        tv_login.setOnClickListener(this);
    }

    private void findAllElements() {
        tv_login=(TextView)findViewById(R.id.tv_login);
    }

    @Override
    public void onClick(View view) {
        if(view==tv_login){
            Intent intent=new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
