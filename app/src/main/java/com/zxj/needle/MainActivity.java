package com.zxj.needle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zxj.needle_annotations.GaAnnotation;
import com.zxj.needle_runtime.GaTracker;
import com.zxj.needle_runtime.Needle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String showName = "bt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        View bt2 = findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
        View bt3 = findViewById(R.id.bt3);
        Needle.initGaTracker(new GaTracker() {
            @Override
            public void track(String category, String action, String label) {
                Log.e("zxj", String.format("track: %s，%s，%s", category,action,label) );
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt3();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt1:
                bt1();
                break;
            case R.id.bt2:
                bt2();
                break;
        }
    }
    @GaAnnotation(category = "category1",label = "label1",action = "action1")
    private void bt1() {
        showName = "bt1";
        Toast.makeText(this,showName,Toast.LENGTH_SHORT).show();
    }
    @GaAnnotation(category = "category2",label = "label2",action = "action2")
    private void bt2() {
        showName = "bt2";
        Toast.makeText(this,showName,Toast.LENGTH_SHORT).show();
    }
    @GaAnnotation(category = "category3",label = "label3",action = "action3")
    private void bt3() {
        showName = "bt3";
        Toast.makeText(MainActivity.this,showName,Toast.LENGTH_SHORT).show();
    }
}
