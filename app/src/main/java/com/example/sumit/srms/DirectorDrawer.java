package com.example.sumit.srms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class DirectorDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_drawer);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Drawer Code
        Toolbar toolbar = findViewById(R.id.director_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.director_drawer_layout);
        NavigationView navigationView = findViewById(R.id.director_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new Home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
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
            new SweetAlertDialog(DirectorDrawer.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("SRMS")
                    .setContentText("Welcome To Director Panel :)")
                    .setConfirmText("Thank You!")
                    .show();
        }
        else
        {
            new SweetAlertDialog(DirectorDrawer.this, SweetAlertDialog.WARNING_TYPE)
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
                            DirectorDrawer.this.finish();
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
            case R.id.director_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new Home()).commit();
                break;

            case R.id.director_nav_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new CourseMgmt()).commit();
                break;

            case R.id.director_nav_semester:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new SemesterMgmt()).commit();
                break;

            case R.id.director_nav_batch:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new BatchMgmt()).commit();
                break;

            case R.id.director_nav_division:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new DivisionMgmt()).commit();
                break;

            case R.id.director_nav_subject:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new SubjectMgmt()).commit();
                break;

            case R.id.director_nav_exam:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new ExamMgmt()).commit();
                break;

            case R.id.director_nav_manual_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new RegistrationActivity()).commit();
                break;

            case R.id.director_nav_update_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new UpdateProfile()).commit();
                break;

            case R.id.director_nav_semester_update:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new SemesterUpdate()).commit();
                break;

            case R.id.director_nav_manage_faculty:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new FacultyMgmt()).commit();
                break;

            case R.id.director_nav_manual_result:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new ManualResult()).commit();
                break;

            case R.id.director_nav_reports:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new Reports()).commit();
                break;

            case R.id.director_nav_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.director_fragment_container, new AboutUs()).commit();
                break;

            case R.id.director_nav_logout:
                SessionMgmt sessionMgmt = new SessionMgmt(DirectorDrawer.this);
                sessionMgmt.remove_session();
                Intent intent = new Intent(DirectorDrawer.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Drawer Methods End
}