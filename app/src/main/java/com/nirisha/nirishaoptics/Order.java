package com.nirisha.nirishaoptics;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nirisha.nirishaoptics.services.ValueAdapter;

public class Order extends AppCompatActivity implements View.OnClickListener {

    private Spinner sp_eye,sp_type,sp_index,sp_coating,sp_diameter,sp_sphere,sp_cylinder,sp_axis,sp_addition,sp_height;
    private EditText et_tint,et_quality;
    private Button btn_proceed;
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

        findAllViews();
        btn_proceed.setOnClickListener(this);
        sp_eye.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.EYE));

        sp_index.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.INDEX));

        sp_diameter.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.DIAMETER));
        sp_sphere.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.SPHERE));
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
        et_tint=(EditText)findViewById(R.id.et_tint);
        et_quality=(EditText)findViewById(R.id.et_quantity);
        btn_proceed=(Button)findViewById(R.id.btn_proceed);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_proceed){

        }
    }
}
