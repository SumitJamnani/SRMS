package com.example.sumit.srms;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

public class StudentDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Declaration Part
    SQLiteDatabase db;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_drawer);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Database Related Code : Create Database SRMS
        db=openOrCreateDatabase("SRMS", Context.MODE_PRIVATE, null);

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
            super.onBackPressed();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new Home()).commit();
                break;

            case R.id.student_nav_logout:
                Intent intent = new Intent(StudentDrawer.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Drawer Methods End
}