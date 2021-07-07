package com.example.sumit.srms;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FacultyMgmt extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_faculty_mgmt, container,false);

        //Spinner Code
        Spinner spinner_course = (Spinner) view.findViewById(R.id.spinner_Course);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);

        Spinner spinner_semester = (Spinner) view.findViewById(R.id.spinner_Semester);
        ArrayAdapter<String> adapter_semester =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Sem));
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_semester);

        Spinner spinner_subject = (Spinner) view.findViewById(R.id.spinner_Subject);
        ArrayAdapter<String> adapter_subject =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subject.setAdapter(adapter_subject);

        Spinner spinner_faculty = (Spinner) view.findViewById(R.id.spinner_Faculty);
        ArrayAdapter<String> adapter_faculty =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Faculty));
        adapter_faculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_faculty.setAdapter(adapter_faculty);
        // Spinner Code End
        return view;
    }

}

