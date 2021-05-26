package com.example.sumit.srms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Role));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        Spinner mySpinner1 = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Sem));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(myAdapter1);


    }
    }

