package com.example.sukurax.linehack.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import com.example.sukurax.linehack.R;
import com.example.sukurax.linehack.adapter.Msg;
import com.example.sukurax.linehack.adapter.Msgadapter;

import java.util.UUID;

/**
 * Created by sukurax on 16/4/9.
 */
public class HistoryActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);

        ListView list = (ListView) findViewById(R.id.listtext);
        TextView textview = (TextView) findViewById(R.id.textview4);
        textview.setText("搜索历史");

        MainActivity.historylistdata.clear();
        SQLiteDatabase db=MainActivity.dbHelper.getWritableDatabase();
        Cursor cursor1=db.query("Data",null,null,null,null,null,null);
        if(cursor1.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                String username=cursor1.getString(cursor1.getColumnIndex("username"));
                String sex=cursor1.getString(cursor1.getColumnIndex("sex"));
                String age=cursor1.getString(cursor1.getColumnIndex("age"));
                String hobby=cursor1.getString(cursor1.getColumnIndex("hobby"));
                String qqnum=cursor1.getString(cursor1.getColumnIndex("qqnum"));
                Msg msg1=new Msg(username,sex,age,hobby,qqnum);
                MainActivity.historylistdata.add(msg1);

            }while (cursor1.moveToNext());
        }
        cursor1.close();

        MainActivity.historylistadapter = new Msgadapter(this,  MainActivity.historylistdata);
        list.setAdapter(MainActivity.historylistadapter);


    }
    @Override
    public void onResume(){
        super.onResume();
        MainActivity.historylistadapter.notifyDataSetChanged();
    }




}
