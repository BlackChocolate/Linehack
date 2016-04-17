package com.example.sukurax.linehack.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sukurax.linehack.R;
import com.example.sukurax.linehack.activity.ShowActivity;

import java.util.ArrayList;

/**
 * Created by sukurax on 16/4/12.
 */
public class Msgadapter extends BaseAdapter {
    private ArrayList<Msg> mdata;
    private LayoutInflater mInflater;
    private Context context;
    public Msgadapter(Context context, ArrayList<Msg> data) {
        mdata=data;
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }



    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        //判断是否缓存
        if(convertView == null){
            holder=new ViewHolder();
            //通过LayoutInflater实例化布局
            convertView=mInflater.inflate(R.layout.list_item,null);
            holder.content=(TextView)convertView.findViewById(R.id.content_item);
            convertView.setTag(holder);
        }
        else{
            //通过TAG找到缓存的布局
            holder = (ViewHolder) convertView.getTag();
        }
        //设置布局中控件要显示的试视图
        holder.content.setText(mdata.get(position).getUsername()+" "+mdata.get(position).getSex()+" "+mdata.get(position).getAge()+"\n");

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String username=mdata.get(position).getUsername();
                    String sex=mdata.get(position).getSex();
                    String age=mdata.get(position).getAge();
                    String hobby=mdata.get(position).getHobby();
                    String qqnum=mdata.get(position).getQqnum();
                    Intent intent=new Intent(context, ShowActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("sex",sex);
                    intent.putExtra("age",age);
                    intent.putExtra("hobby",hobby);
                    intent.putExtra("qqnum",qqnum);
                    context.startActivity(intent);
                }catch (Exception e){
                    return;
                }

            }
        });
        return convertView;
    }
    public final class ViewHolder{
        public TextView content;
    }
}
