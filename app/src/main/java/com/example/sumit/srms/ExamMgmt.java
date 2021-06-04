package com.example.sumit.srms;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ExamMgmt extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_mgmt);

        //Side Drawer Code
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.admin_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //End Side Drawer Code

        //Spinner Code
        Spinner spinner_subject = (Spinner) findViewById(R.id.spinner_Subject);
        ArrayAdapter<String> adapter_subject =  new ArrayAdapter<>(ExamMgmt.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subject.setAdapter(adapter_subject);
        spinner_subject.setFocusable(true);
        spinner_subject.setFocusableInTouchMode(true);
        // Spinner Code End

    }

    //Side drawer open close related method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //End Side drawer open close related method
}
