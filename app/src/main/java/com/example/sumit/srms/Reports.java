package com.example.sumit.srms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Reports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Spinner spinner_course = (Spinner) findViewById(R.id.spinner_Course);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(Reports.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);

        Spinner spinner_semester = (Spinner) findViewById(R.id.spinner_Semester);
        ArrayAdapter<String> adapter_semester =  new ArrayAdapter<>(Reports.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Sem));
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_semester);

        Spinner spinner_faculty = (Spinner) findViewById(R.id.spinner_Faculty);
        ArrayAdapter<String> adapter_faculty =  new ArrayAdapter<>(Reports.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Faculty));
        adapter_faculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_faculty.setAdapter(adapter_faculty);

        Spinner spinner_exam = (Spinner) findViewById(R.id.spinner_Exam);
        ArrayAdapter<String> adapter_exam =  new ArrayAdapter<>(Reports.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Exam));
        adapter_exam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_exam.setAdapter(adapter_exam);
    }
}
