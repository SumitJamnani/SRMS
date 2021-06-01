package com.example.sumit.srms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FacultyMgmt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_mgmt);

        Spinner spinner_course = (Spinner) findViewById(R.id.spinner_Course);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(FacultyMgmt.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);

        Spinner spinner_semester = (Spinner) findViewById(R.id.spinner_Semester);
        ArrayAdapter<String> adapter_semester =  new ArrayAdapter<>(FacultyMgmt.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Sem));
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_semester);

        Spinner spinner_subject = (Spinner) findViewById(R.id.spinner_Subject);
        ArrayAdapter<String> adapter_subject =  new ArrayAdapter<>(FacultyMgmt.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subject.setAdapter(adapter_subject);

        Spinner spinner_faculty = (Spinner) findViewById(R.id.spinner_Faculty);
        ArrayAdapter<String> adapter_faculty =  new ArrayAdapter<>(FacultyMgmt.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Faculty));
        adapter_faculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_faculty.setAdapter(adapter_faculty);
    }
}
