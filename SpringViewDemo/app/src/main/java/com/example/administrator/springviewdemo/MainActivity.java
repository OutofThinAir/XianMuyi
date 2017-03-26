package com.example.administrator.springviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.liaoinstan.springview.container.AcFunFooter;
import com.liaoinstan.springview.container.AcFunHeader;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.container.RotationFooter;
import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SpringView springView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        springView = (SpringView) findViewById(R.id.spring_view);
        ListView listView = (ListView) findViewById(R.id.list_view);

        //
        ArrayList<String> list =new ArrayList<>();
        for (int i = 0; i <50; i++) {
            list.add("No"+i);
        }
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 3000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 3000);
            }
        });
        springView.setHeader(new AcFunHeader(this,R.drawable.zhen_anim));
        springView.setFooter(new AcFunFooter(this,R.drawable.zhen_anim));
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list));

    }



}
