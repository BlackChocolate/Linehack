package com.example.sukurax.linehack.adapter;

/**
 * Created by sukurax on 16/4/12.
 */
public class Msg {
    public String username,sex,age,hobby,qqnum;
    public Msg(String username, String sex, String age, String hobby, String qqnum){
        this.username=username;
        this.sex=sex;
        this.age=age;
        this.hobby=hobby;
        this.qqnum=qqnum;
    }
    public String getUsername(){
        return username;
    }
    public String getSex(){
        return sex;
    }
    public String getAge(){
        return age;
    }
    public String getHobby(){
        return hobby;
    }
    public String getQqnum(){
        return qqnum;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public void setSex(String sex){
        this.sex=sex;
    }
    public void setAge(String age){
        this.age=age;
    }
    public void setHobby(String hobby){
        this.hobby=hobby;
    }
    public void setQqnum(String qqnum){
        this.qqnum=qqnum;
    }
    @Override
    public boolean equals(Object arg0) {
        return arg0 != null && arg0 instanceof Msg && this.getUsername().equals(((Msg) arg0).getUsername());
    }
}
