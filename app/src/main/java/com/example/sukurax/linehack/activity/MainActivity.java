package com.example.sukurax.linehack.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sukurax.linehack.BluetoothManager;
import com.example.sukurax.linehack.MyDatabaseHelper;
import com.example.sukurax.linehack.R;
import com.example.sukurax.linehack.adapter.Msg;
import com.example.sukurax.linehack.adapter.Msgadapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Msgadapter listadapter;
    private ArrayList<Msg> listdata= new ArrayList<>();
    private TextView textview;
    ProgressDialog progressBar;
    public static Msgadapter historylistadapter;
    public static ArrayList<Msg> historylistdata= new ArrayList<>();
    private ArrayList<Msg> historylistdatatemp= new ArrayList<>();
    public static BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
    static MyDatabaseHelper dbHelper;
    private Thread newThread;
    SharedPreferences preferences;
    public static String username;
    public static String sex;
    public static String age;
    public static String hobby;
    public static String qqnum;
    public static String settingsex;
    public static String settingage;
    public static TextView textviewhint;
    public static Handler handler;
    public static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView list = (ListView) findViewById(R.id.listtext2);
        textviewhint=(TextView)findViewById(R.id.textview5);
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        TextView usernameview=(TextView) headerLayout.findViewById(R.id.username_);


        if (textviewhint != null) {
            textviewhint.setVisibility(View.INVISIBLE);
            textviewhint.setText("正在后台扫描...");
        }

        dbHelper=new MyDatabaseHelper(this,"DataStore.db",null,1);
        dbHelper.getWritableDatabase();

        preferences=getSharedPreferences("data", MODE_PRIVATE);
        username=preferences.getString("username", "");
        if(username.length()>0){
            String[] str=username.split(":");
            username=str[1];
            if (usernameview != null&&str[1]!=null) {
                usernameview.setText(str[1]);
            }
        }
        sex=preferences.getString("sex","");
        age=preferences.getString("age","");
        hobby=preferences.getString("hobby","");
        qqnum=preferences.getString("qqnum","");

        preferences=getSharedPreferences("setting_data", MODE_PRIVATE);
        settingsex=preferences.getString("settingsex","");
        settingage=preferences.getString("settingage","");

        if(username.length()<=0){
            finish();
            Intent intent=new Intent(MainActivity.this,SignupActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("sex",sex);
            intent.putExtra("age",age);
            intent.putExtra("hobby",hobby);
            intent.putExtra("qqnum",qqnum);
            intent.putExtra("tof","0");
            startActivity(intent);
        }

        //注册，当一个设备被发现时调用onReceive
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);
        //当搜索结束后调用onReceive
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(receiver, filter);



        if(newThread==null){
            new Thread() {
                @Override
                public void run() {
                    if(!BluetoothManager.isBluetoothSupported()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("警告!");
                        builder.setMessage("当前设备不支持蓝牙！！");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(1);
                            }
                        });
                        builder.show();
                    }
                    if(!BluetoothManager.isBluetoothEnabled()){
                        BluetoothManager.turnOnBluetooth();
                    }
                    if (adapter.isDiscovering()) {
                        adapter.cancelDiscovery();
                    }
                    adapter.startDiscovery();


                    //已配对设备
                    Set<BluetoothDevice> pairedDevices = MainActivity.adapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            String decoded = null;
                            try {
                                decoded =new String(Base64.decode(device.getName().getBytes(),Base64.DEFAULT),"utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            if(decoded!=null&&decoded.contains(":")&&decoded.length()>0){
                                String[] str = decoded.split(":");
//
                                if("131".equals(str[0])){
//                                    Notification notification=new Notification.Builder(getBaseContext())
//                                            .setContentTitle("您发现了新朋友")
//                                            .setContentText("昵称:"+str[1]+" 性别:"+str[2]+"年龄:"+str[3])
//                                            .build();
//                                    notification.notify();

                                    Msg msg=new Msg(str[1],str[2],str[3],str[4],str[5]);

                                    SQLiteDatabase db=dbHelper.getWritableDatabase();
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
                                            historylistdatatemp.add(msg1);

                                        }while (cursor1.moveToNext());
                                    }
                                    cursor1.close();

                                    if(!historylistdatatemp.contains(msg)){

                                        ContentValues values=new ContentValues();
                                        values.put("username",str[1]);
                                        values.put("sex",str[2]);
                                        values.put("age",str[3]);
                                        values.put("hobby",str[4]);
                                        values.put("qqnum",str[5]);
                                        db.insert("Data", null, values);
                                        values.clear();
                                    }
                                    historylistdata.clear();
                                    historylistdatatemp.clear();
                                }


                            }


                        }
                    }

                }


            }.start();

        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        handler= new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    dialog = ProgressDialog.show(MainActivity.this, "提示", "正在扫描中",
                            false,false);
                }
                if (msg.what == 2) {
                    listadapter = new Msgadapter(MainActivity.this, listdata);
                    assert list != null;
                    list.setAdapter(listadapter);
                    if(dialog!=null){
                        dialog.cancel();
                    }
                }
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressBar = new ProgressDialog(MainActivity.this);
//                progressBar.setCancelable(true);
//                progressBar.setMessage("Loading");
//                progressBar.show();
                Message msg11 = new Message();
                msg11.what = 1;
                handler.sendMessage(msg11);


                if (MainActivity.adapter.isDiscovering()) {
//                    MainActivity.adapter.cancelDiscovery();
                }
                MainActivity.adapter.startDiscovery();
                textviewhint.setVisibility(View.VISIBLE);
                textviewhint.setText("正在扫描中");
                //已配对设备
                Set<BluetoothDevice> pairedDevices = MainActivity.adapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        if(device.getName()!=null){
                            String decoded = null;
                            try {
                                decoded =new String(Base64.decode(device.getName().getBytes(),Base64.DEFAULT),"utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                            if(decoded!=null&&decoded.contains(":")&&decoded.length()>0){
                                String[] str = decoded.split(":");
//
                                if("131".equals(str[0])){
//                            Notification notification=new Notification.Builder(getBaseContext())
//                                    .setContentTitle("您发现了新朋友")
//                                    .setContentText("昵称:"+str[1]+" 性别:"+str[2]+"年龄:"+str[3])
//                                    .build();
//                            notification.notify();
                                    Msg msg=new Msg(str[1],str[2],str[3],str[4],str[5]);
                                    //昵称，性别，年龄，爱好，qq号码
                                    if(str[2].equals(settingsex)&&(((Integer.parseInt(str[3])-Integer.parseInt(settingage))<=5)||((Integer.parseInt(settingage)-Integer.parseInt(str[3]))<=5))){
                                        Toast.makeText(MainActivity.this,"符合要求，特别提醒"+str[1],Toast.LENGTH_SHORT).show();
                                    }
                                    if(!listdata.contains(msg)){
                                        Toast.makeText(MainActivity.this,"当前记录扫描到"+str[1],Toast.LENGTH_SHORT).show();
                                        listdata.add(msg);
                                    }
                                    SQLiteDatabase db=dbHelper.getWritableDatabase();
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
                                            historylistdatatemp.add(msg1);

                                        }while (cursor1.moveToNext());
                                    }
                                    cursor1.close();

                                    if(!historylistdatatemp.contains(msg)) {
                                        ContentValues values = new ContentValues();
                                        values.put("username", str[1]);
                                        values.put("sex", str[2]);
                                        values.put("age", str[3]);
                                        values.put("hobby", str[4]);
                                        values.put("qqnum", str[5]);
                                        db.insert("Data", null, values);
                                        values.clear();
//                                        Toast.makeText(MainActivity.this,"历史记录扫描到"+str[1],Toast.LENGTH_SHORT).show();
                                    }
                                    historylistdata.clear();
                                    historylistdatatemp.clear();
                                }
                            }

                        }



//                listdata.add("已配对："+device.getName() + ":" + device.getAddress() + "\n");
                    }
                }


            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_collect) {
            Intent intent=new Intent(MainActivity.this,CollectActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent=new Intent(MainActivity.this,HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_information) {
            Intent intent=new Intent(MainActivity.this,SignupActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("sex",sex);
            intent.putExtra("age",age);
            intent.putExtra("hobby",hobby);
            intent.putExtra("qqnum",qqnum);
            intent.putExtra("tof","1");
            startActivity(intent);

        }else if(id==R.id.nav_love){
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        historylistdata.clear();
        listdata.clear();
        super.onDestroy();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 已经配对的则跳过
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if(device.getName()!=null){
                        String decoded = null;
                        if(device.getName().contains("+")){
                            try {
                                decoded =new String(Base64.decode(device.getName().getBytes(),Base64.DEFAULT),"utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        if(decoded!=null&&decoded.contains(":")&&decoded.length()>0){
                                Toast.makeText(MainActivity.this,"扫描到"+decoded,Toast.LENGTH_SHORT).show();

                                String[] str = decoded.split(":");
//
                                if("131".equals(str[0])){

//                            Notification notification=new Notification.Builder(getBaseContext())
//                                    .setContentTitle("您发现了新朋友")
//                                    .setContentText("昵称:"+str[1]+" 性别:"+str[2]+"年龄:"+str[3])
//                                    .build();
//                            notification.notify();
                                    Msg msg=new Msg(str[1],str[2],str[3],str[4],str[5]);
                                    //昵称，性别，年龄，爱好，qq号码
                                    if(str[2].equals(settingsex)&&(((Integer.parseInt(str[3])-Integer.parseInt(settingage))<=5)||((Integer.parseInt(settingage)-Integer.parseInt(str[3]))<=5))){
                                        Toast.makeText(MainActivity.this,"符合要求，特别提醒"+str[1],Toast.LENGTH_SHORT).show();
                                    }
                                    if(!listdata.contains(msg)){
//                                    Toast.makeText(MainActivity.this,"当前记录扫描到"+str[1],Toast.LENGTH_SHORT).show();
                                        listdata.add(msg);
                                    }
                                    SQLiteDatabase db=dbHelper.getWritableDatabase();
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
                                            historylistdatatemp.add(msg1);

                                        }while (cursor1.moveToNext());
                                    }
                                    cursor1.close();

                                    if(!historylistdatatemp.contains(msg)) {
                                        ContentValues values = new ContentValues();
                                        values.put("username", str[1]);
                                        values.put("sex", str[2]);
                                        values.put("age", str[3]);
                                        values.put("hobby", str[4]);
                                        values.put("qqnum", str[5]);
                                        db.insert("Data", null, values);
                                        values.clear();
//                                    Toast.makeText(MainActivity.this,"历史记录扫描到"+str[1],Toast.LENGTH_SHORT).show();
                                    }
                                    historylistdata.clear();
                                    historylistdatatemp.clear();
                                }
                            }
                        }

                    }



//

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
                //搜索结束
//                Toast.makeText(MainActivity.this,"当前搜索结束",Toast.LENGTH_SHORT).show();
                if (textviewhint != null) {
                    textviewhint.setText("扫描完毕");
                }
            }
        }
    };
}
