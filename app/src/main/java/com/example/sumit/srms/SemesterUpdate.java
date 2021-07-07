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

public class SemesterUpdate extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_semester_update, container,false);


        //Spinner Code
        Spinner spinner_course = (Spinner) view.findViewById(R.id.spinner_Course);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);

        Spinner spinner_semester = (Spinner) view.findViewById(R.id.spinner_Semester);
        ArrayAdapter<String> adapter_semester =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Sem));
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_semester);

        Spinner spinner_new_sem = (Spinner) view.findViewById(R.id.spinner_NewSemester);
        ArrayAdapter<String> adapter_new_sem =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.New_Sem));
        adapter_new_sem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_new_sem.setAdapter(adapter_new_sem);
        // Spinner Code End
        return view;
    }
}

