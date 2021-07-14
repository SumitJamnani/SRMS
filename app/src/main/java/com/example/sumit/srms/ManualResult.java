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

public class ManualResult extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_result;
    ChildEventListener childEventListener;

    EditText txt_enrollment, txt_marks;
    int passing_marks = 0, flag = 0;
    String stud_name = "";
    Button btn_add_result, btn_update_result;
    Spinner spinner_course, spinner_batch, spinner_semester, spinner_subject, spinner_faculty, spinner_exam;
    ArrayAdapter<String> adapter_course,  adapter_batch, adapter_semester, adapter_subject, adapter_faculty, adapter_exam;
    List<String> batch_array, semester_array, subject_array, faculty_array, exam_array;
    Map<String, Object> values = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manul_result, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_result = db.getReference("result_m");
        txt_enrollment = view.findViewById(R.id.txt_enrollment);
        txt_marks = view.findViewById(R.id.txt_marks);
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_batch = view.findViewById(R.id.spinner_batch);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_subject = view.findViewById(R.id.spinner_subject);
        spinner_faculty = view.findViewById(R.id.spinner_faculty);
        spinner_exam = view.findViewById(R.id.spinner_exam);

        btn_add_result = view.findViewById(R.id.btn_AddResult);
        btn_update_result = view.findViewById(R.id.btn_UpdateResult);

        //Initialize setOnItemSelectedListener() Method For Spinner
        spinner_course.setOnItemSelectedListener(this);
        spinner_semester.setOnItemSelectedListener(this);
        spinner_faculty.setOnItemSelectedListener(this);
        spinner_subject.setOnItemSelectedListener(this);
        btn_add_result.setOnClickListener(this);
        btn_update_result.setOnClickListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Event Listeners
        childEventListener = new ChildEventListener()
        {
            //Retrieve Data From Database
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if (snapshot.exists())
                {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data != null && !data.isEmpty())
                    {
                        if(data.get("user_role").equals("Student"))
                        {
                            stud_name = data.get("name").toString();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Error : Record Does Not Exist!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        };

        return view;
    }


    //CRUD operations on Button Click
    //OnClick Button Events
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_AddResult:
                flag = 1;
                InitializeData();
                break;

            case R.id.btn_UpdateResult:
                flag = 2;
                InitializeData();
                break;
        }
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
                                if (batch_array.size() == 1) {
                                    Toast.makeText(getActivity(), "Error : Batch Records Not Found For Selected Course!!", Toast.LENGTH_SHORT).show();
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
                            if (semester_array.size() == 1) {
                                Toast.makeText(getActivity(), "Error : Semester Records Not Found For Selected Course!!", Toast.LENGTH_SHORT).show();
                            }
                            adapter_semester.notifyDataSetChanged();
                            spinner_semester.setAdapter(adapter_semester);
                        } else {
                            semester_array.clear();
                            semester_array.add("-- Select Semester --");
                            adapter_semester.notifyDataSetChanged();
                            spinner_semester.setAdapter(adapter_semester);
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
                //Spinner Semester -> Spinner Faculty
                if (spinner_semester.getSelectedItemPosition() != 0) {
                    Query faculty_spinner_query = db.getReference("faculty_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    faculty_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                faculty_array.clear();
                                faculty_array.add("-- Select Faculty --");
                                for (DataSnapshot data : snapshot.getChildren())
                                {
                                    if(data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("batch_name").getValue().toString().equals(spinner_batch.getSelectedItem().toString()))
                                        faculty_array.add(data.child("faculty_name").getValue().toString());
                                }
                                if (faculty_array.size() == 1) {
                                    Toast.makeText(getActivity(), "Error : No Faculties Found For Selected Semester & Course!!", Toast.LENGTH_SHORT).show();
                                }
                                adapter_faculty.notifyDataSetChanged();
                                spinner_faculty.setAdapter(adapter_faculty);
                            } else {
                                faculty_array.clear();
                                faculty_array.add("-- Select Faculty --");
                                adapter_faculty.notifyDataSetChanged();
                                spinner_faculty.setAdapter(adapter_faculty);
                                Toast.makeText(getActivity(), "Error : No Faculties Found For Selected Semester & Course!!", Toast.LENGTH_SHORT).show();
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
                break;

            case R.id.spinner_faculty:
                //Spinner Faculty -> Spinner Subject
                if (spinner_faculty.getSelectedItemPosition() != 0) {
                    Query subject_spinner_query = db.getReference("faculty_m").orderByChild("faculty_name").equalTo(spinner_faculty.getSelectedItem().toString());
                    subject_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                subject_array.clear();
                                subject_array.add("-- Select Subject --");
                                for (DataSnapshot data : snapshot.getChildren())
                                {
                                    if(data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("batch_name").getValue().toString().equals(spinner_batch.getSelectedItem().toString()) && data.child("faculty_name").getValue().toString().equals(spinner_faculty.getSelectedItem().toString()))
                                        subject_array.add(data.child("subject_name").getValue().toString());
                                }
                                if (subject_array.size() == 1) {
                                    Toast.makeText(getActivity(), "Error : No Subjects Found For Selected Faculty!!", Toast.LENGTH_SHORT).show();
                                }
                                adapter_subject.notifyDataSetChanged();
                                spinner_subject.setAdapter(adapter_subject);
                            } else {
                                subject_array.clear();
                                subject_array.add("-- Select Subject --");
                                adapter_subject.notifyDataSetChanged();
                                spinner_subject.setAdapter(adapter_subject);
                                Toast.makeText(getActivity(), "Error : No Subjects Found For Selected Faculty!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //Initialize Spinner Subject
                    subject_array = new ArrayList<String>();
                    subject_array.add("-- Select Subject --");
                    adapter_subject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject_array);
                    adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_subject.setAdapter(adapter_subject);
                }
                break;

            case R.id.spinner_subject:
                //Spinner Subject -> Spinner Exams
                if (spinner_subject.getSelectedItemPosition() != 0) {
                    Query exam_spinner_query = db.getReference("faculty_m").orderByChild("subject_name").equalTo(spinner_subject.getSelectedItem().toString());
                    exam_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                exam_array.clear();
                                exam_array.add("-- Select Exam --");
                                for (DataSnapshot data : snapshot.getChildren())
                                {
                                    if(data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("batch_name").getValue().toString().equals(spinner_batch.getSelectedItem().toString()) && data.child("faculty_name").getValue().toString().equals(spinner_faculty.getSelectedItem().toString()) && data.child("subject_name").getValue().toString().equals(spinner_subject.getSelectedItem().toString()))
                                    {
                                        Query examname_spinner_query = db.getReference("exam_m").orderByChild("subject_name").equalTo(spinner_subject.getSelectedItem().toString());
                                        examname_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                if (snapshot.exists())
                                                {
                                                    exam_array.clear();
                                                    exam_array.add("-- Select Exam --");
                                                    for (DataSnapshot data : snapshot.getChildren())
                                                    {
                                                        exam_array.add(data.child("exam_name").getValue().toString());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
//                                    if (exam_array.size() == 1) {
                                    //Toast.makeText(getActivity(), "Error : No Exams Declared Yet For Selected Subject!!", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                                adapter_exam.notifyDataSetChanged();
                                spinner_exam.setAdapter(adapter_exam);
                            } else {
                                exam_array.clear();
                                exam_array.add("-- Select Exam --");
                                adapter_exam.notifyDataSetChanged();
                                spinner_exam.setAdapter(adapter_exam);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    //Initialize Spinner Exam
                    exam_array = new ArrayList<String>();
                    exam_array.add("-- Select Exam --");
                    adapter_exam = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, exam_array);
                    adapter_exam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_exam.setAdapter(adapter_exam);
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


    // Code For Insert or Update Data From Database
    public void InitializeData()
    {
        //For Fetching Passing Marks of Exam Based on Exam Name
        Query examname_spinner_query = db.getReference("exam_m").orderByChild("exam_name").equalTo(spinner_exam.getSelectedItem().toString());
        examname_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        passing_marks = Integer.parseInt(data.child("passing_marks").getValue().toString());
                    }
                } else {
                    Toast.makeText(getActivity(), "Error : Error occurred!!!!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        });

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(String stud_name) {

                values = new HashMap<>();
                values.put("course_name", spinner_course.getSelectedItem().toString());
                values.put("batch_name", spinner_batch.getSelectedItem().toString());
                values.put("semester_name", spinner_semester.getSelectedItem().toString());
                values.put("faculty_name", spinner_faculty.getSelectedItem().toString());
                values.put("subject_name", spinner_subject.getSelectedItem().toString());
                values.put("exam_name", spinner_exam.getSelectedItem().toString());
                values.put("enrollment_number", txt_enrollment.getText().toString());
                values.put("name", stud_name);
                values.put("marks_obtained", txt_marks.getText().toString());
                if(Integer.parseInt(txt_marks.getText().toString()) >= passing_marks)
                    values.put("status", "Pass");
                else
                    values.put("status", "Fail");

                if(flag == 1)
                {
                    String key = table_result.push().getKey();
                    table_result.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Result Declared Successfully For Given Enrollment Number!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Inserting Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(flag == 2)
                {
                    Query result_spinner_query = db.getReference("result_m").orderByChild("exam_name").equalTo(spinner_exam.getSelectedItem().toString());
                    result_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                int flag = 0;
                                for (DataSnapshot data : snapshot.getChildren())
                                {
                                    if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("enrollment_number").getValue().toString().equals(txt_enrollment.getText().toString())) {
                                        db.getReference("result_m").child(data.getKey()).updateChildren(values);
                                        flag = 1;
                                    }
                                }

                                if(flag == 1)
                                {
                                    Toast.makeText(getActivity(), "Result Updated Successfully For Given Enrollment Number!!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error : Given Enrollment Not Found!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }


    private interface FirebaseCallback
    {
        void onCallback(String stud_name);
    }

    private  void readData(FirebaseCallback firebaseCallback)
    {
        //For Fetching Student Name Based on Enrollment Number
        Query studname_spinner_query = db.getReference("registration_m").orderByChild("enrollment_number").equalTo(txt_enrollment.getText().toString());
        studname_spinner_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        stud_name = data.child("name").getValue().toString();
                    }
                    firebaseCallback.onCallback(stud_name);
                }
                else
                {
                    Toast.makeText(getActivity(), "Error : Enrollment Number Not Found!!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}