package com.example.sumit.srms;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;

public class FacultyDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Declaration Part
    SQLiteDatabase db;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_drawer);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Database Related Code : Create Database SRMS
        db=openOrCreateDatabase("SRMS", Context.MODE_PRIVATE, null);

        //Drawer Code
        Toolbar toolbar = findViewById(R.id.faculty_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.faculty_drawer_layout);
        NavigationView navigationView = findViewById(R.id.faculty_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new Home()).commit();
            navigationView.setCheckedItem(R.id.faculty_nav_home);
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
            case R.id.faculty_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new Home()).commit();
                break;

            case R.id.faculty_nav_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new CourseMgmt()).commit();
                break;

            case R.id.faculty_nav_semester:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new SemesterMgmt()).commit();
                break;

            case R.id.faculty_nav_division:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new DivisionMgmt()).commit();
                break;

            case R.id.faculty_nav_subject:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new SubjectMgmt()).commit();
                break;

            case R.id.faculty_nav_exam:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new ExamMgmt()).commit();
                break;

            case R.id.faculty_nav_manual_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new RegistrationActivity()).commit();
                break;

            case R.id.faculty_nav_import_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new ImportUser()).commit();
                break;

            case R.id.faculty_nav_update_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new UpdateProfile()).commit();
                break;

            case R.id.faculty_nav_semester_update:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new SemesterUpdate()).commit();
                break;

            case R.id.faculty_nav_manual_result:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new ManualResult()).commit();
                break;

            case R.id.faculty_nav_import_results:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new ImportResults()).commit();
                break;

            case R.id.faculty_nav_reports:
                getSupportFragmentManager().beginTransaction().replace(R.id.faculty_fragment_container, new Reports()).commit();
                break;

            case R.id.faculty_nav_logout:
                Intent intent = new Intent(FacultyDrawer.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Drawer Methods End
}