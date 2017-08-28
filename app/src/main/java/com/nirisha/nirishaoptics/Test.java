package com.nirisha.nirishaoptics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.cachapa.expandablelayout.ExpandableLayout;

public class Test extends AppCompatActivity {

    private ExpandableLayout expandableLayout;
    private Button click;
    private boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findAllElemets();
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    expandableLayout.expand();
                    flag=false;
                }
                else {
                    expandableLayout.collapse();
                    flag=true;
                }
            }
        });
    }

    private void findAllElemets() {
        expandableLayout=(ExpandableLayout)findViewById(R.id.expandlay);
        click=(Button)findViewById(R.id.clickme);
    }
}
