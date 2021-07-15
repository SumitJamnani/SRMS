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
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Map;

public class Reports extends Fragment implements AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_result;
    ChildEventListener childEventListener;

    Button btn_report;
    String enrollment, user_id;
    int tot_stud = 0, pass = 0, fail = 0;
    TextView exam_name, course_name, batch_name, semester_name, faculty_name, subject_name, txt_tot_stud, txt_pass_stud, txt_fail_stud;
    Spinner spinner_course, spinner_batch, spinner_semester, spinner_subject, spinner_faculty, spinner_exam;
    ArrayAdapter<String> adapter_course,  adapter_batch, adapter_semester, adapter_subject, adapter_faculty, adapter_exam;
    List<String> batch_array, semester_array, subject_array, faculty_array, exam_array;
    CardView report_card;

    RecyclerView report_container;
    ReportAdapter reportAdapter;
    ReportHelper reportHelper;
    ArrayList<ReportHelper> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reports, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_result = db.getReference("result_m");
        txt_tot_stud = view.findViewById(R.id.txt_totstud);
        txt_pass_stud = view.findViewById(R.id.txt_pass_stud);
        txt_fail_stud = view.findViewById(R.id.txt_fail_stud);
        exam_name = view.findViewById(R.id.txt_examname);
        course_name = view.findViewById(R.id.txt_coursename);
        batch_name = view.findViewById(R.id.txt_batchname);
        faculty_name = view.findViewById(R.id.txt_facultyname);
        semester_name = view.findViewById(R.id.txt_semestername);
        subject_name = view.findViewById(R.id.txt_subjectname);
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_batch = view.findViewById(R.id.spinner_batch);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_faculty = view.findViewById(R.id.spinner_faculty);
        spinner_subject = view.findViewById(R.id.spinner_subject);
        spinner_exam = view.findViewById(R.id.spinner_exam);
        report_container = (RecyclerView)  view.findViewById(R.id.recycler_report);
        report_container.setHasFixedSize(true);
        report_container.setLayoutManager(new LinearLayoutManager(getActivity()));
        report_card = view.findViewById(R.id.cardView_report);
        btn_report = view.findViewById(R.id.btn_Report);

        //Initialize setOnItemSelectedListener() Method For Spinner
        spinner_course.setOnItemSelectedListener(this);
        spinner_semester.setOnItemSelectedListener(this);
        spinner_faculty.setOnItemSelectedListener(this);
        spinner_subject.setOnItemSelectedListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Update Data on Button CLick
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                list = new ArrayList<>();
                reportAdapter = new ReportAdapter(getActivity(), list);
                report_container.setAdapter(reportAdapter);

                if(spinner_semester.getSelectedItemPosition() == 0 || spinner_faculty.getSelectedItemPosition() == 0 || spinner_subject.getSelectedItemPosition() == 0 || spinner_exam.getSelectedItemPosition() == 0 || spinner_course.getSelectedItemPosition() == 0 || spinner_batch.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(getActivity(), "Error : PLease Select Proper Data For Generate Reports!!", Toast.LENGTH_SHORT);
                }

                else
                {
                    db.getReference("result_m").orderByChild("exam_name").equalTo(spinner_exam.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if(snapshot.exists())
                            {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    user_id = dataSnapshot.getKey();
                                    enrollment = dataSnapshot.child("enrollment_number").getValue().toString();
                                    if(dataSnapshot.child("status").getValue().toString().equals("Pass"))
                                        pass++;
                                    else if(dataSnapshot.child("status").getValue().toString().equals("Fail"))
                                        fail++;
                                    tot_stud++;
                                    reportHelper = dataSnapshot.getValue(ReportHelper.class);
                                    list.add(reportHelper);
                                }

                                exam_name.setText(reportHelper.getExam_name());
                                course_name.setText(reportHelper.getCourse_name());
                                batch_name.setText(reportHelper.getBatch_name());
                                semester_name.setText(reportHelper.getSemester_name());
                                faculty_name.setText(reportHelper.getFaculty_name());
                                subject_name.setText(reportHelper.getSubject_name());
                                txt_tot_stud.setText(String.valueOf(tot_stud));
                                txt_pass_stud.setText(String.valueOf(pass));
                                txt_fail_stud.setText(String.valueOf(fail));
                                report_card.setVisibility(View.VISIBLE);
                                report_container.setVisibility(View.VISIBLE);
                                reportAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Error : Result Is Not Declared Yet of Selected Exam!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Records!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return  view;
    }

    //onItemSelectedListner Spinner Events
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spinner_course: {
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
            }

            case R.id.spinner_semester: {
                //Spinner Semester -> Spinner Faculty
                if (spinner_semester.getSelectedItemPosition() != 0) {
                    Query faculty_spinner_query = db.getReference("faculty_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    faculty_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                faculty_array.clear();
                                faculty_array.add("-- Select Faculty --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("batch_name").getValue().toString().equals(spinner_batch.getSelectedItem().toString()))
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
            }

            case R.id.spinner_faculty: {
                //Spinner Faculty -> Spinner Subject
                if (spinner_faculty.getSelectedItemPosition() != 0) {
                    Query subject_spinner_query = db.getReference("faculty_m").orderByChild("faculty_name").equalTo(spinner_faculty.getSelectedItem().toString());
                    subject_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                subject_array.clear();
                                subject_array.add("-- Select Subject --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("batch_name").getValue().toString().equals(spinner_batch.getSelectedItem().toString()) && data.child("faculty_name").getValue().toString().equals(spinner_faculty.getSelectedItem().toString()))
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
            }

            case R.id.spinner_subject: {
                //Spinner Subject -> Spinner Exams
                if (spinner_subject.getSelectedItemPosition() != 0) {
                    Query exam_spinner_query = db.getReference("faculty_m").orderByChild("subject_name").equalTo(spinner_subject.getSelectedItem().toString());
                    exam_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                exam_array.clear();
                                exam_array.add("-- Select Exam --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()) && data.child("batch_name").getValue().toString().equals(spinner_batch.getSelectedItem().toString()) && data.child("faculty_name").getValue().toString().equals(spinner_faculty.getSelectedItem().toString()) && data.child("subject_name").getValue().toString().equals(spinner_subject.getSelectedItem().toString())) {
                                        Query examname_spinner_query = db.getReference("exam_m").orderByChild("subject_name").equalTo(spinner_subject.getSelectedItem().toString());
                                        examname_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    exam_array.clear();
                                                    exam_array.add("-- Select Exam --");
                                                    for (DataSnapshot data : snapshot.getChildren()) {
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
                } else {
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
}

