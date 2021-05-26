package com.example.sumit.srms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StudentsResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_results);

        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter2 =  new ArrayAdapter<>(StudentsResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        Spinner mySpinner3 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<>(StudentsResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Exam));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner3.setAdapter(myAdapter3);

    }
}