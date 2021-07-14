package com.example.sumit.srms;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentResults extends Fragment implements AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_result;
    ChildEventListener childEventListener;

    Button btn_result;
    String course, batch, semester, enrollment, user_id, division, exam,  faculty, total_marks, passing_marks, exam_date, marks_obtained, name, subject, status;
    TextView txt_enrollment, exam_name, course_name, batch_name, semester_name, division_name, faculty_name, subject_name, txt_marks_obtained, txt_status, tot_marks, txt_passing_marks, txt_exam_date, txt_name;
    Spinner spinner_subject, spinner_exam;
    ArrayAdapter<String> adapter_subject, adapter_exam;
    List<String> subject_array, exam_array;
    TableLayout result_container;
    SessionMgmt sessionMgmt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_students_results, container, false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_result = db.getReference("result_m");
        txt_enrollment = view.findViewById(R.id.txt_enrollment);
        txt_name = view.findViewById(R.id.txt_name);
        exam_name = view.findViewById(R.id.txt_exam);
        course_name = view.findViewById(R.id.txt_course_name);
        batch_name = view.findViewById(R.id.txt_batch);
        semester_name = view.findViewById(R.id.txt_semester);
        division_name = view.findViewById(R.id.txt_division);
        faculty_name = view.findViewById(R.id.txt_faculty);
        subject_name = view.findViewById(R.id.txt_subject);
        txt_marks_obtained = view.findViewById(R.id.txt_marks_obtained);
        txt_status = view.findViewById(R.id.txt_status);
        tot_marks = view.findViewById(R.id.txt_total_marks);
        txt_passing_marks = view.findViewById(R.id.txt_passing_marks);
        txt_exam_date = view.findViewById(R.id.txt_exam_date);
        spinner_subject = view.findViewById(R.id.spinner_subject);
        spinner_exam = view.findViewById(R.id.spinner_exam);
        btn_result = view.findViewById(R.id.btn_get_result);
        result_container = view.findViewById(R.id.tbl_result);
        sessionMgmt = new SessionMgmt(this.getActivity());

        //Initialize setOnItemSelectedListener() Method For Spinner
        spinner_subject.setOnItemSelectedListener(this);
        spinner_exam.setOnItemSelectedListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Update Data on Button CLick
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_subject.getSelectedItemPosition() == 0 || spinner_exam.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(getActivity(), "Error : PLease Select Proper Data For Generate Result!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Display Fetched Records In TextViews
                    txt_enrollment.setText(enrollment);
                    txt_name.setText(name);
                    exam_name.setText(exam);
                    course_name.setText(course);
                    batch_name.setText(batch);
                    semester_name.setText(semester);
                    division_name.setText(division);
                    faculty_name.setText(faculty);
                    subject_name.setText(subject);
                    txt_marks_obtained.setText(marks_obtained);
                    txt_status.setText(status);
                    tot_marks.setText(total_marks);
                    txt_passing_marks.setText(passing_marks);
                    txt_exam_date.setText(exam_date);
                    if(status.equals("Pass"))
                        txt_status.setTextColor(Color.parseColor("#4ad71b"));
                    else
                        txt_status.setTextColor(Color.parseColor("#FFFF5722"));

                    result_container.setVisibility(View.VISIBLE);
                }
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
            case R.id.spinner_subject: {
                //Spinner Subject -> Spinner Exam
                if (spinner_subject.getSelectedItemPosition() != 0) {
                    Query exam_spinner_query = db.getReference("exam_m").orderByChild("subject_name").equalTo(spinner_subject.getSelectedItem().toString());
                    exam_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                exam_array.clear();
                                exam_array.add("-- Select Exam --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("semester_name").getValue().equals(semester) && data.child("course_name").getValue().equals(course) && data.child("batch_name").getValue().equals(batch)) {
                                        exam_array.add(data.child("exam_name").getValue().toString());
                                    }
                                }
                                if (exam_array.size() == 1) {
                                    Toast.makeText(getActivity(), "Error : Exam Not Declared Yet For Selected Subject!!", Toast.LENGTH_SHORT).show();
                                }
                                adapter_exam.notifyDataSetChanged();
                                spinner_exam.setAdapter(adapter_exam);
                            } else {
                                exam_array.clear();
                                exam_array.add("-- Select Exam --");
                                adapter_exam.notifyDataSetChanged();
                                spinner_exam.setAdapter(adapter_exam);
                                Toast.makeText(getActivity(), "Error : Exam Not Declared Yet For Selected Subject!!", Toast.LENGTH_SHORT).show();
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

            case R.id.spinner_exam:
            {
                //Fetch Total MaArks, Passing Marks, Exam Date Based on Selected Exam Name
                if (spinner_exam.getSelectedItemPosition() != 0) {
                    Query exam_fetch_query = db.getReference("exam_m").orderByChild("exam_name").equalTo(spinner_exam.getSelectedItem().toString());
                    exam_fetch_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if(data.child("semester_name").getValue().equals(semester) && data.child("course_name").getValue().equals(course) && data.child("batch_name").getValue().equals(batch) && data.child("subject_name").getValue().equals(spinner_subject.getSelectedItem().toString()))
                                    {
                                        exam = data.child("exam_name").getValue().toString();
                                        total_marks = data.child("total_marks").getValue().toString();
                                        passing_marks = data.child("passing_marks").getValue().toString();
                                        exam_date = data.child("exam_date").getValue().toString();
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error : Exams Records Not Found For Selected Exam!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                if(spinner_exam.getSelectedItemPosition() != 0)
                {
                    //Fetch Faculty, Subject, Marks obtained, Status From Result_m Based on Selected Subject & Exam Name.
                    Query result_query = db.getReference("result_m").orderByChild("exam_name").equalTo(spinner_exam.getSelectedItem().toString());
                    result_query.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if (snapshot.exists())
                            {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("enrollment_number").getValue().toString().equals(enrollment) && data.child("course_name").getValue().toString().equals(course) && data.child("semester_name").getValue().toString().equals(semester) && data.child("batch_name").getValue().toString().equals(batch) && data.child("subject_name").getValue().toString().equals(spinner_subject.getSelectedItem().toString()))
                                    {
                                        faculty = data.child("faculty_name").getValue().toString();
                                        subject = data.child("subject_name").getValue().toString();
                                        marks_obtained = data.child("marks_obtained").getValue().toString();
                                        status = data.child("status").getValue().toString();
                                    }
                                }
                            }

                            else
                            {
                                Toast.makeText(getActivity(), "Error : Exam Records Not Found For Selected Subject!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



    //Populate Spinners With Firebase Database
    public void fill_spinner()
    {
        //Fetch Enrollment Number, Name, Course, Batch, Semester Based on UserId
        user_id = sessionMgmt.getUser_id();
        result_container.setVisibility(View.GONE);
        Query student_query = db.getReference("registration_m").orderByChild("enrollment_number");
        student_query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.getKey().equals(user_id))
                        {
                            enrollment = data.child("enrollment_number").getValue().toString();
                            name = data.child("name").getValue().toString();
                            course = data.child("course_name").getValue().toString();
                            batch = data.child("batch_name").getValue().toString();
                            semester = data.child("semester_name").getValue().toString();
                            division = data.child("division_name").getValue().toString();
                        }
                    }
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


        //Fill Subject Spinner Based on Student's Course, Batch, Semester.
        Query fetch_subject = db.getReference("subject_m").orderByChild("subject_name");
        fetch_subject.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    subject_array = new ArrayList<String>();
                    subject_array.add("-- Select Subject --");
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        if(data.child("semester_name").getValue().toString().equals(semester) && data.child("course_name").getValue().toString().equals(course) && data.child("batch_name").getValue().toString().equals(batch))
                        {
                            subject_array.add(data.child("subject_name").getValue().toString());
                        }
                    }
                    adapter_subject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject_array);
                    adapter_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_subject.setAdapter(adapter_subject);
                }
                else
                {
                    Toast.makeText(getActivity(), "Error : Subjects Not Found!!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}