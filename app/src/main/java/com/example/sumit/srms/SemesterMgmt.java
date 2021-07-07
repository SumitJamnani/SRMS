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

public class SemesterMgmt extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_semester_mgmt, container,false);


        //Spinner Code
        Spinner spinner_course = (Spinner) view.findViewById(R.id.spinner_Course);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);
        // Spinner Code End
        return view;
    }
}

