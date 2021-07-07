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

public class ExamMgmt extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_exam_mgmt, container,false);

        //Spinner Code
        Spinner spinner_subject = (Spinner) view.findViewById(R.id.spinner_Subject);
        ArrayAdapter<String> adapter_subject =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Subject));
        adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subject.setAdapter(adapter_subject);
        spinner_subject.setFocusable(true);
        spinner_subject.setFocusableInTouchMode(true);
        // Spinner Code End
        return view;
    }

}

