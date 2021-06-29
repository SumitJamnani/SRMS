package com.example.sumit.srms;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CourseMgmt extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_mgmt, container,false);

        //Spinner Code
        Spinner spinner_director = (Spinner) view.findViewById(R.id.spinner_director);
        ArrayAdapter<String> adapter_director =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Director));
        adapter_director.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_director.setAdapter(adapter_director);
        // Spinner Code End
        return view;
    }

}

