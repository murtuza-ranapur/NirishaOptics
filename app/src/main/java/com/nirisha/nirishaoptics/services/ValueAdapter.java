package com.nirisha.nirishaoptics.services;

import android.content.Context;
import android.widget.ArrayAdapter;

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


    private static String [] eye={"Left","Right"};

    private static String [] index={"1.498","1.56","1.6","1.67","1.74"};

    private static String [] diameter={"55","60","65","70"};
    public static ArrayAdapter<String> getAdapter(Context context,String view){
        switch (view)
        {
            case EYE:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,eye);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case TYPE:
                return adapter;
            case INDEX:
                adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,index);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                return adapter;
            case COATING:
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
                return adapter;
            case AXIS:
                return adapter;
            case ADDITION:
                return adapter;
            case HEIGHT:
                return adapter;

        }
        return null;
    }
}
