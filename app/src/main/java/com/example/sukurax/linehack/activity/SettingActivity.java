package com.example.sukurax.linehack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sukurax.linehack.R;


/**
 * Created by sukurax on 16/4/2.
 */
public class SettingActivity extends AppCompatActivity {
    EditText settingsex,settingage;
    String settingsex_;
    String settingsex_input;
    String settingage_input;
    String settingage_;
    Button settingbutton;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinglayout);
        settingsex=(EditText)findViewById(R.id.settingsex);
        settingage=(EditText)findViewById(R.id.settingage);
        settingbutton=(Button)findViewById(R.id.settingbutton);

        preferences=getSharedPreferences("setting_data", MODE_PRIVATE);
        settingsex_input=preferences.getString("settingsex","");
        settingage_input=preferences.getString("settingage","");

        settingsex.setText(settingsex_input);
        settingage.setText(settingage_input);

        settingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsex_=settingsex.getText().toString();
                settingage_=settingage.getText().toString();
                if(!settingsex_.equals("男")&&!(settingsex_.equals("女"))&&Integer.parseInt(settingage_)<=0&&Integer.parseInt(settingage_)>100){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("请合法输入!");
//                    builder.setMessage("当前设备不支持蓝牙！！");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else{
                    preferences = getSharedPreferences("setting_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("settingsex", settingsex_);
                    editor.putString("settingage", settingage_);
                    editor.apply();
                    finish();
                }


            }
        });


    }
}
