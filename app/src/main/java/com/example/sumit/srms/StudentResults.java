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

public class StudentResults extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_students_results, container, false);

        //Spinner Code
        Spinner spinner_course = (Spinner) view.findViewById(R.id.spinner_Subject);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);
        spinner_course.setFocusable(true);
        spinner_course.setFocusableInTouchMode(true);

        Spinner spinner_exam = (Spinner) view.findViewById(R.id.spinner_Exam);
        ArrayAdapter<String> adapter_exam =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Exam));
        adapter_exam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_exam.setAdapter(adapter_exam);
        // Spinner Code End

        return view;
    }
}

