package com.nirisha.nirishaoptics.services;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nirisha.nirishaoptics.Pojo.DynamicValues;
import com.nirisha.nirishaoptics.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hp on 13-08-2017.
 */

public class ValueAdapter {
    private static ArrayAdapter<String> adapter=null;

    public static final String EYE="eye";
    public static final String TYPE="type";
    public static final String INDEX="index";
    public static final String COATING="coating";
    public static final String DIAMETER="diameter";
    public static final String SPHERE="sphere";
    public static final String CYLINDER="cylinder";
    public static final String AXIS="axis";
    public static final String ADDITION="addition";
    public static final String HEIGHT="height";

    public static final String STATES="states";


    private static String [] eye={"Left","Right"};

    private static String [] index={"1.498","1.56","1.6","1.67","1.74"};

    private static String [] diameter={"55","60","65","70"};
    private static String [] height={"13","15","17","19"};
    public static ArrayAdapter<String> getAdapter(Context context,String view){
        switch (view)
        {
            case EYE:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,eye);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case TYPE:
                DynamicValues dv=DynamicValues.getInstance();
                dv.init(context);
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,dv.getProduct());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case INDEX:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,index);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case COATING:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,DynamicValues.getInstance().getCoating());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case DIAMETER:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,diameter);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case SPHERE:
                List<String> sphere=new ArrayList<>();
                for(double i=-10;i<=10;i+=0.25)
                    sphere.add(String.valueOf(i));
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,sphere);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case CYLINDER:
                List<String> cylinder=new ArrayList<>();
                for(double i=-4.50;i<=4.50;i+=0.25)
                    cylinder.add(String.valueOf(i));
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,cylinder);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case AXIS:
                List<String> axis=new ArrayList<>();
                for(int i=0;i<=180;i++)
                    axis.add(String.valueOf(i));
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,axis);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case ADDITION:
                List<String> addition=new ArrayList<>();
                for(double i=0.0;i<=3.50;i+=0.25)
                    addition.add(String.valueOf(i));
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,addition);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case HEIGHT:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,height);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case STATES:
                String [] states=context.getResources().getStringArray(R.array.india_states);
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,states);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
        }
        return null;
    }

    public static int defaultValueService(String defValue, Spinner spinner){
        int position;
        ArrayAdapter<String> adapter =(ArrayAdapter<String>)spinner.getAdapter();
        position=adapter.getPosition(defValue);
        return position;
    }

}
