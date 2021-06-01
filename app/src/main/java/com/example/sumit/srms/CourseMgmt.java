package com.example.sumit.srms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CourseMgmt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_mgmt);

        Spinner spinner_director = (Spinner) findViewById(R.id.spinner_director);
        ArrayAdapter<String> adapter_director =  new ArrayAdapter<>(CourseMgmt.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Director));
        adapter_director.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_director.setAdapter(adapter_director);
    }
}
