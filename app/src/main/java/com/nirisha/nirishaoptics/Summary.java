package com.nirisha.nirishaoptics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nirisha.nirishaoptics.api.NirishaAPIUtil;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class Summary extends AppCompatActivity implements View.OnClickListener {

    private JSONObject order;
    private TextView tv_product,tv_indx,tv_dia,tv_height,tv_quantity,tv_coating;
    private TextView tv_product_r,tv_indx_r,tv_dia_r,tv_height_r,tv_quantity_r,tv_coating_r;
    private ExpandableLayout expandableLayoutleft,expandableLayoutright;
    private Button paybutton;
    private TextView tv_address,tv_state;
    private TextView tv_total,tv_cgst,tv_sgst,tv_payable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        findAllViews();
        Intent oldIntent=getIntent();
        try {
            order=new JSONObject(oldIntent.getStringExtra("order"));
            Log.e("Address", "Old Json:"+order.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        populateValues();
        paybutton.setOnClickListener(this);
    }

    private void findAllViews() {
        tv_product = (TextView)findViewById(R.id.tv_sum_product);
        tv_indx=(TextView)findViewById(R.id.tv_index);
        tv_dia=(TextView)findViewById(R.id.tv_dia);
        tv_height=(TextView)findViewById(R.id.tv_fithght);
        tv_quantity=(TextView)findViewById(R.id.tv_quant);
        tv_coating=(TextView)findViewById(R.id.tv_coating);
        expandableLayoutleft=(ExpandableLayout)findViewById(R.id.expand_sum_left);

        tv_product_r = (TextView)findViewById(R.id.tv_sum_product_r);
        tv_indx_r=(TextView)findViewById(R.id.tv_index_r);
        tv_dia_r=(TextView)findViewById(R.id.tv_dia_r);
        tv_height_r=(TextView)findViewById(R.id.tv_fithght_r);
        tv_quantity_r=(TextView)findViewById(R.id.tv_quant_r);
        tv_coating_r=(TextView)findViewById(R.id.tv_coating_r);
        expandableLayoutright=(ExpandableLayout)findViewById(R.id.expand_sum_right);

        paybutton=(Button)findViewById(R.id.place_order);
        tv_address=(TextView)findViewById(R.id.tv_sum_address);
        tv_state=(TextView)findViewById(R.id.tv_sum_city_state);

        tv_total=(TextView)findViewById(R.id.tv_total);
        tv_cgst=(TextView)findViewById(R.id.tv_cgst);
        tv_sgst=(TextView)findViewById(R.id.tv_sgst);
        tv_payable=(TextView)findViewById(R.id.tv_payamount);
    }

    private void populateValues() {
        try {
            if (order.has("left")) {
                JSONObject left=order.getJSONObject("left");
                tv_product.setText(left.getString("type").toUpperCase());
                tv_coating.setText(left.getString("coating").toUpperCase());
                tv_indx.setText(left.getString("index"));
                tv_dia.setText(left.getString("dia"));
                tv_height.setText(left.getString("height").equals("0")?"-":left.getString("height"));
                tv_quantity.setText(left.getString("qty"));
            }
            else {
                expandableLayoutleft.collapse();
            }
            if (order.has("right")) {
                JSONObject right=order.getJSONObject("right");
                tv_product_r.setText(right.getString("type").toUpperCase());
                tv_coating_r.setText(right.getString("coating").toUpperCase());
                tv_indx_r.setText(right.getString("index"));
                tv_dia_r.setText(right.getString("dia"));
                tv_height_r.setText(right.getString("height").equals("0")?"-":right.getString("height"));
                tv_quantity_r.setText(right.getString("qty"));
            }
            else {
                expandableLayoutright.collapse();
            }
            JSONObject address=order.getJSONObject("address");
            tv_address.setText(address.getString("address").replaceFirst("\\|","\n"));
            tv_state.setText(address.getString("city")+" "+address.getString("state")+" "+address.getString("pin"));
            tv_total.setText(order.getString("total"));
            tv_cgst.setText(String.valueOf(calcGST(order.getDouble("total"))));
            tv_sgst.setText(String.valueOf(calcGST(order.getDouble("total"))));
            double pay=order.getDouble("total")+calcGST(order.getDouble("total"))*2;
            tv_payable.setText(String.valueOf(pay));
            order.put("total",pay);
            Log.e("Summary", order.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private double calcGST( double total) {
        return total*0.06;
    }

    @Override
    public void onClick(View view) {
        if(view==paybutton){

            SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
            NirishaAPIUtil util = NirishaAPIUtil.getInstance();
            util.init(Integer.parseInt(sp.getString("id",null)),sp.getString("auth",null));
            util.doOrder(order,this);
        }
    }
}
