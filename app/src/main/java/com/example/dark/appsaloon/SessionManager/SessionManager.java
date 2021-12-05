package com.example.dark.appsaloon.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dark.appsaloon.Constants;



public class SessionManager {

    private String id, name , email , phone,address,username,type,city;
    SharedPreferences session ;

    public String getType(){return type;}

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public SessionManager(){}

    public SessionManager(Context c)
    {
        session= c.getSharedPreferences(new Constants().SESSION,Context.MODE_PRIVATE);
        this. id = session.getString("id",null);
        this.email= session.getString("email",null);
        this.name = session.getString("name",null);
        this.address = session.getString("address",null);
        this.phone = session.getString("phone",null);
        this.username = session.getString("username",null);
        this.type = session.getString("type",null);
        this.city = session.getString("city",null);
    }

    public SessionManager(Context c , String id, String name , String email, String phone,String address,String username,String type,String city)
    {
        session= c.getSharedPreferences(new Constants().SESSION,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = session.edit();
        editor.putString("id", id);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("phone",phone);
        editor.putString("address",address);
        editor.putString("username",username);
        editor.putString("type",type);
        editor.putString("city",city);
        editor.commit();

        new SessionManager(c);

    }

    public boolean CheckIfSessionExist(){
        if (session.contains("id"))
            return true;
        else
            return  false;

    }
    public void Logout (){
        SharedPreferences.Editor editor = session.edit();
        editor.clear();
        editor.commit();

        this.id=null;
        this.name=null;
        this.email=null;
        this.phone=null;
        this.address = null;
        this.username = null;
        this.type = null;
        this.city = null;
    }




}
