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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FacultyMgmt extends Fragment implements AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_fculty;

    Button btn_allocate_subject, btn_update_allocation;
    Spinner spinner_course, spinner_batch, spinner_semester, spinner_subject, spinner_faculty;
    ArrayAdapter<String> adapter_course,  adapter_batch, adapter_semester, adapter_subject, adapter_faculty;
    List<String> batch_array, semester_array, subject_array, faculty_array ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_faculty_mgmt, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_fculty = db.getReference("faculty_m");
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_batch = view.findViewById(R.id.spinner_batch);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_subject = view.findViewById(R.id.spinner_subject);
        spinner_faculty = view.findViewById(R.id.spinner_faculty);

        btn_allocate_subject = view.findViewById(R.id.btn_AllocateSubject);
        btn_update_allocation = view.findViewById(R.id.btn_UpdateAllocation);

        //Initialize setOnItemSelectedListener() Method For Spinner
        spinner_course.setOnItemSelectedListener(this);
        spinner_semester.setOnItemSelectedListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Update Data on Button CLick
        btn_allocate_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InsertData();
            }
        });

        btn_update_allocation.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(getActivity(), "Error : Batch or Faculty Records Not Found For Selected Course!!", Toast.LENGTH_SHORT).show();
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

            //Spinner Course -> Spinner Faculty
            if (spinner_course.getSelectedItemPosition() != 0) {
                Query faculty_spinner_query = db.getReference("registration_m").orderByChild("course_name").equalTo(spinner_course.getSelectedItem().toString());
                faculty_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            faculty_array.clear();
                            faculty_array.add("-- Select Faculty --");
                            for (DataSnapshot data : snapshot.getChildren())
                            {
                                if(data.child("user_role").getValue().toString().equals("Faculty"))
                                    faculty_array.add(data.child("name").getValue().toString());
                            }
                            adapter_faculty.notifyDataSetChanged();
                            spinner_faculty.setAdapter(adapter_faculty);
                        } else {
                            faculty_array.clear();
                            faculty_array.add("-- Select Faculty --");
                            adapter_faculty.notifyDataSetChanged();
                            spinner_faculty.setAdapter(adapter_faculty);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //Initialize Spinner Faculty
                faculty_array = new ArrayList<String>();
                faculty_array.add("-- Select Faculty --");
                adapter_faculty = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, faculty_array);
                adapter_faculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_faculty.setAdapter(adapter_faculty);
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

            case R.id.spinner_semester:
                if(spinner_semester.getSelectedItemPosition() != 0)
                {
                    Query subject_spinner_query = db.getReference("subject_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    subject_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                subject_array.clear();
                                subject_array.add("-- Select Subject --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString())) {
                                        subject_array.add(data.child("subject_name").getValue().toString());
                                    }
                                }
                                if (subject_array.size() == 1) {
                                    Toast.makeText(getActivity(), "Error : Subject Records Not Found For Selected Semester!!", Toast.LENGTH_SHORT).show();
                                }
                                adapter_subject.notifyDataSetChanged();
                                spinner_subject.setAdapter(adapter_subject);
                            }

                            else
                            {
                                subject_array.clear();
                                subject_array.add("-- Select Subject --");
                                adapter_subject.notifyDataSetChanged();
                                spinner_subject.setAdapter(adapter_subject);
                                Toast.makeText(getActivity(), "Error : Subject Records Not Found For Selected Semester!!", Toast.LENGTH_SHORT).show();                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    //Initialize Spinner Subject
                    subject_array = new ArrayList<String>();
                    subject_array.add("-- Select Subject --");
                    adapter_subject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject_array);
                    adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_subject.setAdapter(adapter_subject);
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


    // Code : Insert Data In Table
    public void InsertData()
    {
        if (spinner_subject.getSelectedItemPosition() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Select Correct Subject!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("course_name", spinner_course.getSelectedItem().toString());
            values.put("batch_name", spinner_batch.getSelectedItem().toString());
            values.put("faculty_name", spinner_faculty.getSelectedItem().toString());
            values.put("semester_name", spinner_semester.getSelectedItem().toString());
            values.put("subject_name", spinner_subject.getSelectedItem().toString());

            String key = table_fculty.push().getKey();
            table_fculty.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(getActivity(), "Subject Allocated To Faculty Successfully!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getActivity(), "Error : Something Went Wrong While Inserting Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Code : Update Data In Table
    public void UpdateData()
    {
        if (spinner_subject.getSelectedItemPosition() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Select Correct Subject!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Query search_faculty_query = db.getReference("faculty_m").orderByChild("faculty_name").equalTo(spinner_faculty.getSelectedItem().toString());
            search_faculty_query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    int flag = 0;
                    if (snapshot.exists())
                    {
                        for (DataSnapshot data : snapshot.getChildren())
                        {
                            if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()))
                            {
                                Map<String, Object> values = new HashMap<>();
                                values.put("course_name", spinner_course.getSelectedItem().toString());
                                values.put("batch_name", spinner_batch.getSelectedItem().toString());
                                values.put("faculty_name", spinner_faculty.getSelectedItem().toString());
                                values.put("semester_name", spinner_semester.getSelectedItem().toString());
                                values.put("subject_name", spinner_subject.getSelectedItem().toString());
                                db.getReference("faculty_m").child(data.getKey()).updateChildren(values);
                                flag = 1;
                            }
                        }

                        if(flag == 1)
                        {
                            Toast.makeText(getActivity(), "Faculty Subject Allocation Updated Successfully!!", Toast.LENGTH_LONG).show();
                            clear();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error : No Faculties Are Registered For This Subject!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        spinner_course.setSelection(0);
        spinner_batch.setSelection(0);
        spinner_faculty.setSelection(0);
        spinner_semester.setSelection(0);
        spinner_subject.setSelection(0);
    }

}

