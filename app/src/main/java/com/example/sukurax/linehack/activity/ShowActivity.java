package com.example.sukurax.linehack.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sukurax.linehack.R;
import com.example.sukurax.linehack.adapter.Msg;

/**
 * Created by sukurax on 16/4/13.
 */
public class ShowActivity extends AppCompatActivity {
    TextView tusername,tsex,tage,thobby,tqqnum;
    Button button,collectbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlayout);

        tusername=(TextView)findViewById(R.id.username_show);
        tsex=(TextView)findViewById(R.id.sex_show);
        tage=(TextView)findViewById(R.id.age_show);
        thobby=(TextView)findViewById(R.id.hobby_show);
        tqqnum=(TextView)findViewById(R.id.qqnum_show);
        button=(Button)findViewById(R.id.qqbutton);
        collectbutton=(Button)findViewById(R.id.collect);

        Intent intent=getIntent();
        final String username=intent.getStringExtra("username");
        final String sex=intent.getStringExtra("sex");
        final String age=intent.getStringExtra("age");
        final String love=intent.getStringExtra("love");
        final String hobby=intent.getStringExtra("hobby");
        final String qqnum=intent.getStringExtra("qqnum");
        tusername.setText(username);
        tsex.setText(sex);
        tage.setText(age);
        thobby.setText(hobby);
        tqqnum.setText(qqnum);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqnum;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        final Msg msg=new Msg(username,sex,age,hobby,qqnum);
        if(CollectActivity.collectlistdata.contains(msg)){
            collectbutton.setText("取消收藏");
        }else{
            collectbutton.setText("收藏");
        }
        collectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CollectActivity.collectlistdata.contains(msg)){
                    Toast.makeText(ShowActivity.this,"您移除了"+username,Toast.LENGTH_SHORT).show();
                    CollectActivity.collectlistdata.remove(msg);
                    collectbutton.setText("收藏");
                }else{
                    Toast.makeText(ShowActivity.this,"您收藏了"+username,Toast.LENGTH_SHORT).show();
                    CollectActivity.collectlistdata.add(msg);
                    collectbutton.setText("取消收藏");
                }



            }
        });


    }
}
