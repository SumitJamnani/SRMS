package com.example.sumit.srms;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionMgmt
{
    Context context;
    private SharedPreferences sharedPreferences;
    private String username;
    private String password;
    private String user_role;
    private String user_id;

    public SessionMgmt(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo",  Context.MODE_PRIVATE);
    }


    public String getUsername() {
        username = sharedPreferences.getString("username", "");
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        sharedPreferences.edit().putString("username", username).commit();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_role() {
        user_role = sharedPreferences.getString("userrole", "");
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
        sharedPreferences.edit().putString("userrole", user_role).commit();
    }

    public String getUser_id() {
        user_id = sharedPreferences.getString("userid", "");
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        sharedPreferences.edit().putString("userid", user_id).commit();
    }

    public void remove_session()
    {
        sharedPreferences.edit().clear().commit();
    }

}
