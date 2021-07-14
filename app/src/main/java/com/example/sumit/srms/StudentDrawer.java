package com.example.sumit.srms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.view.MenuItem;

public class StudentDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Declaration Part
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_drawer);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Drawer Code
        Toolbar toolbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.student_drawer_layout);
        NavigationView navigationView = findViewById(R.id.student_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new Home()).commit();
            navigationView.setCheckedItem(R.id.student_nav_home);
        }
        //Drawer Code End

        //Check Internet Connection
        checkInternet();
    }

    //Check If Internet Is Connected or Not!!
    public void checkInternet()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            new SweetAlertDialog(StudentDrawer.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("SRMS")
                    .setContentText("Welcome To Student Panel :)")
                    .setConfirmText("Thank You!")
                    .show();
        }
        else
        {
            new SweetAlertDialog(StudentDrawer.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Internet (:")
                    .setContentText("Please Connect Your Device With Internet For Get Better Experience of This App :)")
                    .setConfirmText("Got It!")
                    .show();
        }
    }

    //Drawer Methods
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit from SRMS?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StudentDrawer.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.student_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new Home()).commit();
                break;

            case R.id.student_nav_update_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new StudentUpdateProfile()).commit();
                break;

            case R.id.student_nav_results:
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new StudentResults()).commit();
                break;

            case R.id.student_nav_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new AboutUs()).commit();
                break;

            case R.id.student_nav_logout:
                SessionMgmt sessionMgmt = new SessionMgmt(StudentDrawer.this);
                sessionMgmt.remove_session();
                Intent intent = new Intent(StudentDrawer.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Drawer Methods End
}