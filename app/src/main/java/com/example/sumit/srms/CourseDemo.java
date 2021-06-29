package com.example.sumit.srms;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CourseDemo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_demo, container,false);


        //Spinner Code
        Spinner spinner_demo = (Spinner) view.findViewById(R.id.spinner_Demo);
        ArrayAdapter<String> adapter_demo = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Demo));
        adapter_demo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_demo.setAdapter(adapter_demo);
        // Spinner Code End

        return view;
    }
}

