package com.nirisha.nirishaoptics;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nirisha.nirishaoptics.Pojo.DynamicValues;
import com.nirisha.nirishaoptics.api.NirishaAPIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class OrderList extends AppCompatActivity {

    private ListView listView;
    private ArrayList<HashMap<String,String>> data=new ArrayList<>();
    private String [] from={"ordernum","left","leftqty","right","rightqty","amount","status","date"};
    private int [] to={R.id.tv_Order_num,R.id.tv_ol_right,R.id.tv_ol_8,R.id.tv_ol_left,R.id.tv_ol_9
            ,R.id.tv_ol_amount,R.id.tv_ol_status,R.id.tv_ol_date};
    private SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        findAllViews();

        SharedPreferences sp=getSharedPreferences("Nirisha",MODE_PRIVATE);
        NirishaAPIUtil util = NirishaAPIUtil.getInstance();
        util.init(Integer.parseInt(sp.getString("id", null)), sp.getString("auth", null));
        util.getOrders(this);

        Thread waiter=new Thread(new ValueUpdater());
        waiter.start();

        sa=new SimpleAdapter(this,data,R.layout.complex_layout_order_list,from,to);
        listView.setAdapter(sa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Clicked", "onItemClick: "+((HashMap<String,String>)sa.getItem(i)).get("ordernum"));

            }
        });
    }

    private void populate() {
        try {
            Map<String, JSONObject> vals = DynamicValues.getInstance().getOrderData();
            Set<String> keys = vals.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                HashMap<String, String> val = new HashMap<>();
                String ordnum = it.next();
                val.put("ordernum", ordnum);

                JSONObject obj = vals.get(ordnum);
                if (obj.has("left")) {
                    JSONObject left = obj.getJSONObject("left");
                    val.put("left",left.getString("type").toUpperCase());
                    val.put("leftqty",left.getString("qunatity"));
                }
                else {
                    val.put("left","-");
                    val.put("leftqty","-");
                }

                if (obj.has("right")) {
                    JSONObject right = obj.getJSONObject("right");
                    val.put("right",right.getString("type").toUpperCase());
                    val.put("rightqty",right.getString("qunatity"));
                }
                else {
                    val.put("right","-");
                    val.put("rightqty","-");
                }

                val.put("amount",obj.getString("total"));
                val.put("status",obj.getString("status").toUpperCase());
                val.put("date",obj.getString("date"));
                data.add(val);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void findAllViews() {
        listView=(ListView) findViewById(R.id.lv_ol);
    }

    class ValueUpdater implements Runnable{

        @Override
        public void run() {
            while (!DynamicValues.getInstance().isOrderFlagSet()){

            }
            populate();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sa.notifyDataSetChanged();
                }
            });
        }
    }
}
