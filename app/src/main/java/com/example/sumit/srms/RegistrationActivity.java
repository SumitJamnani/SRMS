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

public class RegistrationActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration, container,false);

        //Spinner Code
        Spinner mySpinner = (Spinner) view.findViewById(R.id.spinner5);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Role));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        Spinner mySpinner1 = (Spinner) view.findViewById(R.id.spinner6);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Sem));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(myAdapter1);

        Spinner mySpinner2 = (Spinner) view.findViewById(R.id.spinner7);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Div));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);
        // Spinner Code End
        return view;
    }

}

