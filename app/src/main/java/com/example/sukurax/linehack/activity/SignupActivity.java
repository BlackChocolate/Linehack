package com.example.sukurax.linehack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sukurax.linehack.R;


/**
 * Created by sukurax on 16/4/2.
 */
public class SignupActivity extends AppCompatActivity {
    EditText username,sex,age,hobby,qqnum;
    String username_,sex_,age_,hobby_,qqnum_;
    Button signupbutton;
    int tof;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplayout);
        username=(EditText)findViewById(R.id.username);
        sex=(EditText)findViewById(R.id.sex);
        age=(EditText)findViewById(R.id.age);
        hobby=(EditText)findViewById(R.id.hobby);
        qqnum=(EditText)findViewById(R.id.qqnum);
        signupbutton=(Button)findViewById(R.id.signupbutton);

        Intent intent=getIntent();
        username_=intent.getStringExtra("username");
        sex_=intent.getStringExtra("sex");
        age_=intent.getStringExtra("age");
        hobby_=intent.getStringExtra("hobby");
        qqnum_=intent.getStringExtra("qqnum");
        tof= Integer.parseInt(intent.getStringExtra("tof"));

        if(username_.length()>0&&sex_.length()>0&&age_.length()>0&&hobby_.length()>0&&qqnum_.length()>0){
            username.setText(username_);
            sex.setText(sex_);
            age.setText(age_);
            hobby.setText(hobby_);
            qqnum.setText(qqnum_);
        }

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity.showname.setText("本地昵称"+"\n"+username.getText().toString());
                username_="131:"+username.getText().toString()+":"+sex_+":"+age_+":"+hobby_+":"+qqnum_;
                sex_=sex.getText().toString();
                age_=age.getText().toString();
                hobby_=hobby.getText().toString();
                qqnum_=qqnum.getText().toString();
                if(username_.length()<=0||sex_.length()<=0||age_.length()<=0||hobby_.length()<=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle("警告!");
                    builder.setMessage("选项不能为空！");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    });
                    builder.show();
                }else {
                    String nUsername =new String(Base64.encode(username_.getBytes(),Base64.DEFAULT));
                    MainActivity.adapter.setName(nUsername);

                    preferences=getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("username",username_);
                    editor.putString("sex",sex_);
                    editor.putString("age",age_);
                    editor.putString("hobby", hobby_);
                    editor.putString("qqnum",qqnum_);
                    editor.apply();
                    finish();

                    Intent intent=new Intent(SignupActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    public void onBackPressed(){
        switch (tof){
            case 0:
                break;
            case 1:
                super.onBackPressed();
                break;
            default:
                break;

        }

    }
}
