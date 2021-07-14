package com.example.sumit.srms;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemesterUpdate extends Fragment implements AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_reg;

    Button btn_update_semester;
    Spinner spinner_course, spinner_semester, spinner_batch;
    ArrayAdapter<String> adapter_course, adapter_semester, adapter_batch;
    List<String> semester_array, batch_array;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_semester_update, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_reg = db.getReference("registration_m");
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_batch = view.findViewById(R.id.spinner_batch);
        btn_update_semester = view.findViewById(R.id.btn_UpdateSemester);

        //Initialize setOnItemSelectedListener() Method For Spinner
        spinner_course.setOnItemSelectedListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Update Data on Button CLick
        btn_update_semester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UpdateData();
            }
        });

        return view;
    }


    //onItemSelectedListner Spinner Events
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spinner_course:
            {
                //Spinner Course -> Spinner Batch
                if (spinner_course.getSelectedItemPosition() != 0) {
                    Query batch_spinner_query = db.getReference("batch_m").orderByChild("course_name").equalTo(spinner_course.getSelectedItem().toString());
                    batch_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                batch_array.clear();
                                batch_array.add("-- Select Batch --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    batch_array.add(data.child("batch_name").getValue().toString());
                                }
                                adapter_batch.notifyDataSetChanged();
                                spinner_batch.setAdapter(adapter_batch);
                            } else {
                                batch_array.clear();
                                batch_array.add("-- Select Batch --");
                                adapter_batch.notifyDataSetChanged();
                                spinner_batch.setAdapter(adapter_batch);
                                Toast.makeText(getActivity(), "Error : Batch Records Not Found For Selected Course!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //Initialize Spinner Batch
                    batch_array = new ArrayList<String>();
                    batch_array.add("-- Select Batch --");
                    adapter_batch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, batch_array);
                    adapter_batch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_batch.setAdapter(adapter_batch);
                }
            }

            //Spinner Course -> Spinner Semester
            if (spinner_course.getSelectedItemPosition() != 0) {
                Query semester_spinner_query = db.getReference("semester_m").orderByChild("course_name").equalTo(spinner_course.getSelectedItem().toString());
                semester_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            semester_array.clear();
                            semester_array.add("-- Select Semester --");
                            for (DataSnapshot data : snapshot.getChildren()) {
                                semester_array.add(data.child("semester_name").getValue().toString());
                            }
                            adapter_semester.notifyDataSetChanged();
                            spinner_semester.setAdapter(adapter_semester);
                        } else {
                            //spinner_semester.setSelection(semester_pos);
                            semester_array.clear();
                            semester_array.add("-- Select Semester --");
                            adapter_semester.notifyDataSetChanged();
                            spinner_semester.setAdapter(adapter_semester);
                            Toast.makeText(getActivity(), "Error : Semester Records Not Found For Selected Course!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //Initialize Spinner Semester
                semester_array = new ArrayList<String>();
                semester_array.add("-- Select Semester --");
                adapter_semester = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, semester_array);
                adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_semester.setAdapter(adapter_semester);
            }
            break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void fill_spinner()
    {
        Query fetch_spinner_query = db.getReference("course_m").orderByChild("course_name");
        fetch_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    List<String> course_array = new ArrayList<String>();
                    course_array.add("-- Select Course --");
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        course_array.add(data.child("course_name").getValue().toString());
                    }
                    adapter_course = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, course_array);
                    adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_course.setAdapter(adapter_course);
                }

                else
                {
                    Toast.makeText(getActivity(), "Error : Records Not Found!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateData()
    {
        if (spinner_semester.getSelectedItemPosition() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Select Correct Semester!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Query search_batch_query = db.getReference("registration_m").orderByChild("batch_name").equalTo(spinner_batch.getSelectedItem().toString());
            search_batch_query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    int flag = 0;
                    if (snapshot.exists())
                    {
                        for (DataSnapshot data : snapshot.getChildren())
                        {
                            if(data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()))
                            {
                                Map<String, Object> values = new HashMap<>();
                                values.put("semester_name", spinner_semester.getSelectedItem().toString());
                                db.getReference("registration_m").child(data.getKey()).updateChildren(values);
                                flag = 1;
                            }
                        }

                        Query search_batch_m_query = db.getReference("batch_m").orderByChild("batch_name").equalTo(spinner_batch.getSelectedItem().toString());
                        search_batch_m_query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if (snapshot.exists())
                                {
                                    for (DataSnapshot data : snapshot.getChildren())
                                    {
                                        if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()))
                                        {
                                            Map<String, Object> values = new HashMap<>();
                                            values.put("semester_name", spinner_semester.getSelectedItem().toString());
                                            db.getReference("batch_m").child(data.getKey()).updateChildren(values);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if(flag == 1)
                        {
                            Toast.makeText(getActivity(), "Semester Updated Successfully of Selected Batch Students!!", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error : No Students Are Registered For Selected Batch!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Error : Records Not Found!!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}


