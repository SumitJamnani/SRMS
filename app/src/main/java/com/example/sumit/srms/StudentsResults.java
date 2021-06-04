package com.example.sumit.srms;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StudentsResults extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_results);

        // Side Drawer Code
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.student_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //End Side Drawer Code


        // Spinner Code
        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter2 =  new ArrayAdapter<>(StudentsResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        Spinner mySpinner3 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<>(StudentsResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Exam));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner3.setAdapter(myAdapter3);
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