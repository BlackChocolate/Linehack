package com.example.sukurax.linehack.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import com.example.sukurax.linehack.R;
import com.example.sukurax.linehack.adapter.Msg;
import com.example.sukurax.linehack.adapter.Msgadapter;

import java.util.ArrayList;

/**
 * Created by sukurax on 16/4/9.
 */
public class CollectActivity extends Activity {
    public static Msgadapter collectlistadapter;
    public static ArrayList<Msg> collectlistdata=new ArrayList<Msg>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);

        ListView list = (ListView) findViewById(R.id.listtext);
        TextView textview = (TextView) findViewById(R.id.textview4);
        textview.setText("收藏记录");

        collectlistadapter = new Msgadapter(this,collectlistdata);
        list.setAdapter(collectlistadapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        CollectActivity.collectlistadapter.notifyDataSetChanged();
    }




}
