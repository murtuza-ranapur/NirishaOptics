package com.nirisha.nirishaoptics;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nirisha.nirishaoptics.Pojo.DynamicValues;
import com.nirisha.nirishaoptics.api.NirishaAPIUtil;
import com.nirisha.nirishaoptics.services.Validator;
import com.nirisha.nirishaoptics.services.ValueAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseView;


public class Order extends AppCompatActivity implements View.OnClickListener{

    private Spinner sp_type,sp_index,sp_coating,sp_diameter,sp_sphere,sp_cylinder,sp_axis,sp_addition,sp_height;
    private EditText et_tint,et_quality;
    private Spinner sp_type_r,sp_index_r,sp_coating_r,sp_diameter_r,sp_sphere_r,sp_cylinder_r,sp_axis_r,sp_addition_r,sp_height_r;
    private EditText et_tint_r,et_quality_r;
    private Button btn_proceed;
    private JSONObject orderObj;
    private ExpandableLayout expandableLayoutleft,expandableLayoutright;
    private TextView tv_pr_1,tv_pr_2;
    private TextView tv_pr_1_r,tv_pr_2_r;
    private boolean expandFlagLeft =false,expandFlagRight=false;
    private CardView cv_left,cv_right;
    private SQLiteDatabase nirisha;
    private static final String TAG="Order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        final Context context=this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,OrderList.class);
                startActivity(intent);
            }
        });
        SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
        Intent oldIntent=getIntent();
        int flag;
        flag=oldIntent.getIntExtra("session",0);
        Log.e("Order", "Old Session:"+flag);
        if(flag==0) {
            NirishaAPIUtil util = NirishaAPIUtil.getInstance();
            util.init(Integer.parseInt(sp.getString("id", null)), sp.getString("auth", null));
            util.getProducts(this);

            //https://android-arsenal.com/details/1/5440
//            Log.e(TAG, "onCreate: "+Calendar.getInstance().getTime() );
//            new FancyShowCaseView.Builder(this)
//                    .focusOn(fab)
//                    .title("View Your Orders")
//                    .build()
//                    .show();
        }
        Thread waiter=new Thread(new ValueUpdater());
        waiter.start();
        findAllViews();


        sp_index.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.INDEX));
        sp_diameter.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.DIAMETER));
        sp_sphere.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.SPHERE));
        sp_cylinder.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.CYLINDER));
        sp_axis.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.AXIS));
        sp_addition.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.ADDITION));
        sp_height.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.HEIGHT));

        sp_index_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.INDEX));
        sp_diameter_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.DIAMETER));
        sp_sphere_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.SPHERE));
        sp_cylinder_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.CYLINDER));
        sp_axis_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.AXIS));
        sp_addition_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.ADDITION));
        sp_height_r.setAdapter(ValueAdapter.getAdapter(this,ValueAdapter.HEIGHT));

        btn_proceed.setOnClickListener(this);
        cv_left.setOnClickListener(this);
        cv_right.setOnClickListener(this);

        nirisha=openOrCreateDatabase("nirisha",MODE_PRIVATE,null);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                List<String> index= getListFromDb(nirisha,"indx",(String)sp_type.getSelectedItem());
                List<String> coating= getListFromDb(nirisha,"coating",(String)sp_type.getSelectedItem());
//                List<String> dia= getListFromDb(nirisha,"dia",(String)sp_type.getSelectedItem());
                ArrayAdapter<String> aAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,index);
                aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_index.setAdapter(aAdapter);

                ArrayAdapter<String> bAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,coating);
                bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_coating.setAdapter(bAdapter);

//                ArrayAdapter<String> cAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,dia);
//                cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                sp_diameter.setAdapter(cAdapter);

                List<List<String>> list=getListFromDbLine(nirisha,(String)sp_type.getSelectedItem(),
                        (String)sp_index.getSelectedItem(),
//                        (String)sp_diameter.getSelectedItem(),
                        (String)sp_coating.getSelectedItem());

                List<String> fh=list.get(0);
                List<String> sp=list.get(1);

                ArrayAdapter<String> dAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,fh);
                dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_height.setAdapter(dAdapter);


//                List<String> sphere=null;
                if(sp.size()>1)
                {
                    tv_pr_1.setText(sp.get(0));
                    tv_pr_2.setText(sp.get(1));
//                    sphere=new ArrayList<>();
//                    for(double k=Double.parseDouble(sp.get(0));k<=Double.parseDouble(sp.get(1));k+=0.25)
//                        sphere.add(String.valueOf(k));
                }
                else
                {
                    tv_pr_1.setText(sp.get(0));
                    tv_pr_2.setText("-");
//                    sphere=new ArrayList<>();
//                    sphere.add(sp.get(0));
                }
//                ArrayAdapter<String> eAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,sphere);
//                eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                sp_sphere.setAdapter(eAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_type_r.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                List<String> index= getListFromDb(nirisha,"indx",(String)sp_type_r.getSelectedItem());
                List<String> coating= getListFromDb(nirisha,"coating",(String)sp_type_r.getSelectedItem());
                List<String> dia= getListFromDb(nirisha,"dia",(String)sp_type_r.getSelectedItem());
                ArrayAdapter<String> aAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,index);
                aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_index_r.setAdapter(aAdapter);

                ArrayAdapter<String> bAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,coating);
                bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_coating_r.setAdapter(bAdapter);

                ArrayAdapter<String> cAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,dia);
                cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_diameter_r.setAdapter(cAdapter);

                List<List<String>> list=getListFromDbLine(nirisha,(String)sp_type_r.getSelectedItem(),
                        (String)sp_index_r.getSelectedItem(),
//                        (String)sp_diameter_r.getSelectedItem(),
                        (String)sp_coating_r.getSelectedItem());

                List<String> fh=list.get(0);
                List<String> sp=list.get(1);

                ArrayAdapter<String> dAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,fh);
                dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_height_r.setAdapter(dAdapter);


//                List<String> sphere=null;
                if(sp.size()>1)
                {
                    tv_pr_1_r.setText(sp.get(0));
                    tv_pr_2_r.setText(sp.get(1));
//                    sphere=new ArrayList<>();
//                    for(double k=Double.parseDouble(sp.get(0));k<=Double.parseDouble(sp.get(1));k+=0.25)
//                        sphere.add(String.valueOf(k));
                }
                else
                {
                    tv_pr_1_r.setText(sp.get(0));
                    tv_pr_2_r.setText("-");
//                    sphere=new ArrayList<>();
//                    sphere.add(sp.get(0));
                }
//                ArrayAdapter<String> eAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,sphere);
//                eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                sp_sphere_r.setAdapter(eAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_logout:
                SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.remove("id");
                editor.remove("auth");
                editor.apply();
                Intent in=new Intent(this,MainActivity.class);
                finish();
                startActivity(in);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<List<String>> getListFromDbLine(SQLiteDatabase nirisha, String type, String index, String coating) {
        Log.e("Drop Vals", "getListFromDbLine: begin");
        List<String> pr=new ArrayList<>();
        List<String> fh=new ArrayList<>();
        List<List<String>> list= new ArrayList<>();
        Cursor c=nirisha.rawQuery("select prdown,prup,fh1,fh2,fh3 from product p join coatingprice cp on p.id=cp.product_id where p.product = '"+type+"' and p.indx="+index+" and coating ='"+coating+"';",null);
        c.moveToFirst();
        if(c.getCount()>0)
        {
            Log.e("Drop Vals", "getListFromDbLine: "+c.getDouble(0));
            Log.e("Drop Vals", "getListFromDbLine: "+c.getDouble(1));
            if(c.getDouble(0)!=0.0)
                pr.add(String.valueOf(c.getDouble(0)));
            if(c.getDouble(1)!=0.0)
                pr.add(String.valueOf(c.getDouble(1)));
            if(c.getDouble(2)!=0.0)
                fh.add(String.valueOf(c.getDouble(2)));
            if(c.getDouble(3)!=0.0)
                fh.add(String.valueOf(c.getDouble(3)));
            if(c.getDouble(4)!=0.0)
                fh.add(String.valueOf(c.getDouble(4)));
        }
        Log.e("Drop Vals", "getListFromDbLine: done");
        list.add(fh);
        list.add(pr);
        return list;
    }

    private List<String> getListFromDb(SQLiteDatabase nirisha, String val,String prod) {
        List<String> value=new ArrayList<>();
        Cursor c=nirisha.rawQuery("select distinct "+val+" from product p join coatingprice cp on p.id=cp.product_id where p.product ='"+prod+"' ",null);
        c.moveToFirst();
        if (c.getCount()>0)
        while (!c.isAfterLast()){
            if(!val.equalsIgnoreCase("coating"))
                value.add(String.valueOf(c.getDouble(0)));
            else{
                if(!c.getString(0).equals("null"))
                    value.add(c.getString(0));
                Log.e("My list", "getListFromDb: "+c.getString(0));
            }
            c.moveToNext();
        }
        return value;
    }

    private double getPrice(SQLiteDatabase nirisha,String type, String index, String diameter, String coating){
        Cursor c=nirisha.rawQuery("select price from product p join coatingprice cp on p.id=cp.product_id where p.product = '"+type+"' and p.indx="+index+"  and coating ='"+coating+"';",null);
        c.moveToFirst();
        if(c.getCount()>0) {
            Log.e("Order", "getPrice: "+c.getDouble(0) );
            return c.getDouble(0);
        }
        return 0D;
    }

    private void findAllViews() {
        sp_axis=(Spinner)findViewById(R.id.sp_axis);
        sp_addition=(Spinner)findViewById(R.id.ap_addition);
        sp_coating=(Spinner)findViewById(R.id.sp_coating);
        sp_cylinder=(Spinner)findViewById(R.id.sp_cylinder);
        sp_diameter=(Spinner)findViewById(R.id.sp_diameter);
        sp_height=(Spinner)findViewById(R.id.sp_height);
        sp_index=(Spinner)findViewById(R.id.sp_index);
        sp_type=(Spinner)findViewById(R.id.sp_type);
        sp_sphere=(Spinner)findViewById(R.id.sp_sphere);
        et_tint=(EditText)findViewById(R.id.et_tint);
        et_quality=(EditText)findViewById(R.id.et_quantity);
        tv_pr_1=(TextView)findViewById(R.id.tv_pr_1);
        tv_pr_2=(TextView)findViewById(R.id.tv_pr_2);
        expandableLayoutleft =(ExpandableLayout)findViewById(R.id.expand_left);
        cv_left=(CardView)findViewById(R.id.left_card_view);

        sp_axis_r=(Spinner)findViewById(R.id.sp_axis_r);
        sp_addition_r=(Spinner)findViewById(R.id.ap_addition_r);
        sp_coating_r=(Spinner)findViewById(R.id.sp_coating_r);
        sp_cylinder_r=(Spinner)findViewById(R.id.sp_cylinder_r);
        sp_diameter_r=(Spinner)findViewById(R.id.sp_diameter_r);
        sp_height_r=(Spinner)findViewById(R.id.sp_height_r);
        sp_index_r=(Spinner)findViewById(R.id.sp_index_r);
        sp_type_r=(Spinner)findViewById(R.id.sp_type_r);
        sp_sphere_r=(Spinner)findViewById(R.id.sp_sphere_r);
        et_tint_r=(EditText)findViewById(R.id.et_tint_r);
        et_quality_r=(EditText)findViewById(R.id.et_quantity_r);
        tv_pr_1_r=(TextView)findViewById(R.id.tv_pr_1_r);
        tv_pr_2_r=(TextView)findViewById(R.id.tv_pr_2_r);
        expandableLayoutright =(ExpandableLayout)findViewById(R.id.expand_right);
        cv_right=(CardView)findViewById(R.id.right_card_view);

        btn_proceed=(Button)findViewById(R.id.btn_proceed);
    }

    @Override
    public void onClick(View view) {
        if(view==cv_left){
            if(!expandFlagLeft) {
                expandableLayoutleft.expand();
                expandFlagLeft =true;
            }
            else {
                expandableLayoutleft.collapse();
                expandFlagLeft =false;
            }
        }
        else if(view==cv_right){
            if(!expandFlagRight) {
                expandableLayoutright.expand();
                expandFlagRight =true;
            }
            else {
                expandableLayoutright.collapse();
                expandFlagRight =false;
            }
        }
        else if(view==btn_proceed){
            double total=0D;
            nirisha=openOrCreateDatabase("nirisha",MODE_PRIVATE,null);
            orderObj=new JSONObject();
            boolean flag=true;
            try {
                if(expandFlagLeft) {
                    JSONObject left=new JSONObject();
                    //Validate Power range
                    if (Validator.powerRange(Double.parseDouble(sp_sphere.getSelectedItem().toString())
                            ,Double.parseDouble(sp_cylinder.getSelectedItem().toString())
                            ,Double.parseDouble(tv_pr_1.getText().toString()),
                            Double.parseDouble(tv_pr_2.getText().toString())))
                        flag &= true;
                    else {
                        flag &= true;
//                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                        dlgAlert.setMessage("Summation of left lens' Sphere and Cylinder is neither "+tv_pr_1.getText()+" or "+tv_pr_2.getText()+". " +
//                                "Prices may vary now in the actual bill");
//                        dlgAlert.setTitle("Invalid Value");
//                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                        dlgAlert.setCancelable(true);
//                        dlgAlert.create().show();
                    }
                    if (Validator.forEmpty(et_quality.getText().toString())  && Integer.parseInt(et_quality.getText().toString())>0)
                        flag &= true;
                    else {
                        flag &= false;
                        et_quality.setError(Validator.getErrorMessage());
                    }
                    if (Validator.forEmpty(et_tint.getText().toString()))
                        flag &= true;
                    else {
                        flag &= false;
                        et_tint.setError(Validator.getErrorMessage());
                    }
                    if(flag) {
                        left.put("type", sp_type.getSelectedItem());
                        left.put("index", sp_index.getSelectedItem());
                        left.put("coating", sp_coating.getSelectedItem());
                        left.put("dia", sp_diameter.getSelectedItem());
                        left.put("sphere", sp_sphere.getSelectedItem());
                        left.put("cylinder", sp_cylinder.getSelectedItem());
                        left.put("axis", sp_axis.getSelectedItem());
                        left.put("addition", sp_addition.getSelectedItem());
                        left.put("height", sp_height.getSelectedItem()==null?"0":sp_height.getSelectedItem());
                        left.put("qty", Integer.parseInt(et_quality.getText().toString()));
                        left.put("tint", et_tint.getText().toString());
                        left.put("price", getPrice(nirisha,sp_type.getSelectedItem().toString(),sp_index.getSelectedItem().toString(),
                                sp_diameter.getSelectedItem().toString(),sp_coating.getSelectedItem().toString())
                                *Integer.parseInt(et_quality.getText().toString()));
                        orderObj.put("left", left);
                        total+=left.getDouble("price");
                    }
                }
                if(expandFlagRight) {
                    JSONObject right=new JSONObject();
                    if (Validator.powerRange(Double.parseDouble(sp_sphere_r.getSelectedItem().toString())
                            ,Double.parseDouble(sp_cylinder_r.getSelectedItem().toString())
                            ,Double.parseDouble(tv_pr_1_r.getText().toString()),
                            Double.parseDouble(tv_pr_2_r.getText().toString())))
                        flag &= true;
                    else {
                        flag &= true;
//                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                        dlgAlert.setMessage("Summation of right lens' Sphere and Cylinder is neither " +
//                                ""+tv_pr_1_r.getText()+" or "+tv_pr_2_r.getText()+". " +
//                                "Prices may vary now in the actual bill");
//                        dlgAlert.setTitle("Invalid Value");
//                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                        dlgAlert.setCancelable(true);
//                        dlgAlert.create().show();
                    }
                    if (Validator.forEmpty(et_quality_r.getText().toString()) && Integer.parseInt(et_quality_r.getText().toString())>0)
                        flag &= true;
                    else {
                        flag &= false;
                        et_quality_r.setError(Validator.getErrorMessage());
                    }
                    if (Validator.forEmpty(et_tint_r.getText().toString()))
                        flag &= true;
                    else {
                        flag &= false;
                        et_tint_r.setError(Validator.getErrorMessage());
                    }
                    if(flag) {
                        right.put("type", sp_type_r.getSelectedItem());
                        right.put("index", sp_index_r.getSelectedItem());
                        right.put("coating", sp_coating_r.getSelectedItem());
                        right.put("dia", sp_diameter_r.getSelectedItem());
                        right.put("sphere", sp_sphere_r.getSelectedItem());
                        right.put("cylinder", sp_cylinder_r.getSelectedItem());
                        right.put("axis", sp_axis_r.getSelectedItem());
                        right.put("addition", sp_addition_r.getSelectedItem());
                        right.put("height", sp_height_r.getSelectedItem()==null?"0":sp_height_r.getSelectedItem());
                        right.put("qty", Integer.parseInt(et_quality_r.getText().toString()));
                        right.put("tint", et_tint_r.getText().toString());
                        right.put("price", getPrice(nirisha,sp_type_r.getSelectedItem().toString(),sp_index_r.getSelectedItem().toString(),
                                sp_diameter_r.getSelectedItem().toString(),sp_coating_r.getSelectedItem().toString())
                                *Integer.parseInt(et_quality_r.getText().toString()));
                        orderObj.put("right", right);
                        total+=right.getDouble("price");
                    }
                }

            if (expandFlagLeft || expandFlagRight)
            if(flag) {
                orderObj.put("total",total);
                orderObj.put("status","pending");
                Intent in = new Intent(this, Address.class);
                in.putExtra("order",orderObj.toString());
                startActivity(in);
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    class ValueUpdater implements Runnable{

        @Override
        public void run() {
            while (!DynamicValues.getInstance().isSet()){

            }
            final ArrayAdapter<String> productAdapter=new ArrayAdapter<>(Order.this,android.R.layout.simple_spinner_item,DynamicValues.getInstance().getProduct());
            productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productAdapter.notifyDataSetChanged();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sp_type_r.setAdapter(productAdapter);
                    sp_type.setAdapter(productAdapter);
                }
            });
        }
    }


}
