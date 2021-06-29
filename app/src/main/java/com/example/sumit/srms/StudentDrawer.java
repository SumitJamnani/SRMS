package com.example.sumit.srms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class StudentDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_drawer);

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
    }

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
                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container, new DivisionMgmt()).commit();
                break;

            case R.id.student_nav_logout:
                Intent intent = new Intent(StudentDrawer.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}