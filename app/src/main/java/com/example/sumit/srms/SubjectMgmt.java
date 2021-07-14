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

public class SubjectMgmt extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_subject;
    private DatabaseReference table_semester;
    private DatabaseReference table_course;
    ChildEventListener childEventListener;
    String userId;

    EditText txt_subject, txt_subject_code;
    Button btn_add_subject, btn_search_subject, btn_update_subject, btn_delete_subject;
    RadioGroup rbg_subject_type_choice;
    RadioButton rbtn_regular_subject, rbtn_elective_subject;
    Spinner spinner_course, spinner_semester, spinner_batch;
    ArrayAdapter<String> adapter_course, adapter_semester, adapter_batch;
    List<String> semester_array, batch_array;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_subject_mgmt, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_subject = db.getReference("subject_m");
        table_course = db.getReference("course_m");
        table_semester = db.getReference("semester_m");
        txt_subject = view.findViewById(R.id.txt_subject);
        txt_subject_code = view.findViewById(R.id.txt_subjectcode);
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_batch = view.findViewById(R.id.spinner_batch);
        btn_add_subject = view.findViewById(R.id.btn_AddData);
        btn_update_subject = view.findViewById(R.id.btn_UpdateData);
        btn_delete_subject = view.findViewById(R.id.btn_DeleteData);
        btn_search_subject = view.findViewById(R.id.btn_SearchData);
        rbg_subject_type_choice = view.findViewById(R.id.subject_type_choice);
        rbtn_regular_subject = view.findViewById(R.id.regular_subject);
        rbtn_elective_subject = view.findViewById(R.id.elective_subject);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_subject.setOnClickListener(this);
        btn_update_subject.setOnClickListener(this);
        btn_delete_subject.setOnClickListener(this);
        btn_search_subject.setOnClickListener(this);
        spinner_course.setOnItemSelectedListener(this);
        spinner_semester.setOnItemSelectedListener(this);

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
                    int semester_pos, course_pos, batch_pos;
                    String subject_type;
                    userId = snapshot.getKey();
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data != null && !data.isEmpty())
                    {
                        semester_pos = adapter_semester.getPosition(data.get("semester_name").toString());
                        course_pos = adapter_course.getPosition(data.get("course_name").toString());
                        batch_pos = adapter_batch.getPosition(data.get("batch_name").toString());
                        txt_subject.setText(data.get("subject_name").toString());
                        txt_subject_code.setText(data.get("subject_code").toString());
                        subject_type = data.get("subject_type").toString();
                        switch (subject_type)
                        {
                            case "Regular" :
                                rbtn_regular_subject.setChecked(true);
                                break;
                            case "Elective" :
                                rbtn_elective_subject.setChecked(true);
                                break;
                        }
                        spinner_semester.setSelection(semester_pos);
                        spinner_course.setSelection(course_pos);
                        spinner_batch.setSelection(batch_pos);
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
            case R.id.btn_AddData:
                InsertData();
                break;

            case R.id.btn_UpdateData:
                UpdateData();
                break;

            case R.id.btn_DeleteData:
                DeleteData();
                break;

            case R.id.btn_SearchData:
                SearchData();
                break;
        }
    }


    //onItemSelectedListner Spinner Events
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_course:
                //Spinner Course -> Spinner Semester
                if (spinner_course.getSelectedItemPosition() != 0) {
                    Query semester_spinner_query = table_semester.orderByChild("course_name").equalTo(spinner_course.getSelectedItem().toString());
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
                }
                else
                {
                    //Initialize Spinner Semester
                    semester_array = new ArrayList<String>();
                    semester_array.add("-- Select Semester --");
                    adapter_semester = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, semester_array);
                    adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_semester.setAdapter(adapter_semester);
                }
                break;

            case R.id.spinner_semester: {
                if (spinner_semester.getSelectedItemPosition() != 0) {
                    Query subject_spinner_query = db.getReference("batch_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    subject_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                batch_array.clear();
                                batch_array.add("-- Select Batch --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString())) {
                                        batch_array.add(data.child("batch_name").getValue().toString());
                                    }
                                }
                                if (batch_array.size() == 1) {
                                    Toast.makeText(getActivity(), "Error : Batch Records Not Found For Selected Course or Semester!!", Toast.LENGTH_SHORT).show();
                                }
                                adapter_batch.notifyDataSetChanged();
                                spinner_batch.setAdapter(adapter_batch);
                            } else {
                                batch_array.clear();
                                batch_array.add("-- Select Batch --");
                                adapter_batch.notifyDataSetChanged();
                                spinner_batch.setAdapter(adapter_batch);
                                Toast.makeText(getActivity(), "Error : Batch Records Not Found For Selected Course or Semester!!", Toast.LENGTH_SHORT).show();
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
                break;
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // Code : Insert Data In Table
    public void InsertData()
    {
        if (txt_subject.getText().toString().trim().length() == 0 || txt_subject_code.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Fill The Required Fields!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("subject_name", txt_subject.getText().toString());
            values.put("subject_code", txt_subject_code.getText().toString());
            if(rbtn_regular_subject.isChecked())
                values.put("subject_type", rbtn_regular_subject.getText().toString());
            else
                values.put("subject_type", rbtn_elective_subject.getText().toString());
            values.put("course_name", spinner_course.getSelectedItem().toString());
            values.put("semester_name", spinner_semester.getSelectedItem().toString());
            values.put("batch_name", spinner_batch.getSelectedItem().toString());

            String key = table_subject.push().getKey();
            table_subject.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(getActivity(), "Record Inserted Successfully!!", Toast.LENGTH_SHORT).show();
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


    // Code : Update Particular Data From Table
    public void UpdateData()
    {
        if (txt_subject.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Subject Name For Update The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("subject_name", txt_subject.getText().toString());
            values.put("subject_code", txt_subject_code.getText().toString());
            if(rbtn_regular_subject.isChecked())
                values.put("subject_type", rbtn_regular_subject.getText().toString());
            else
                values.put("subject_type", rbtn_elective_subject.getText().toString());
            values.put("course_name", spinner_course.getSelectedItem().toString());
            values.put("semester_name", spinner_semester.getSelectedItem().toString());
            values.put("batch_name", spinner_batch.getSelectedItem().toString());

            table_subject.child(userId).updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(getActivity(), "Record Updated Successfully!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getActivity(), "Error : Something Went Wrong While Updating Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Code : Delete Particular Data From Table
    public void DeleteData()
    {
        if (txt_subject.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Subject Name For Delete The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            table_subject.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(getActivity(), "Record Deleted Successfully!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getActivity(), "Error : Something Went Wrong While Deleting Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Code : View Particular Data From Table
    public void SearchData()
    {
        if (txt_subject.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Subject Name For Search The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Query query_subject = table_subject.orderByChild("subject_name").equalTo(txt_subject.getText().toString());
            query_subject.addChildEventListener(childEventListener);
        }
    }


    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        //Spinner Course
        Query course_spinner_query = table_course.orderByChild("course_name");
        course_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
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

//        //Spinner Semester
//        Query semester_spinner_query = table_semester.orderByChild("semester_name");
//        semester_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if (snapshot.exists())
//                {
//                    List<String> semester_array = new ArrayList<String>();
//                    semester_array.add("-- Select Semester --");
//                    for (DataSnapshot data : snapshot.getChildren())
//                    {
//                        semester_array.add(data.child("semester_name").getValue().toString());
//                    }
//                    adapter_semester = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, semester_array);
//                    adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner_semester.setAdapter(adapter_semester);
//                }
//
//                else
//                {
//                    Toast.makeText(getActivity(), "Error : Records Not Found!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error)
//            {
//                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        txt_subject.setText("");
        txt_subject_code.setText("");
        rbtn_regular_subject.setChecked(true);
        spinner_course.setSelection(0);
        spinner_semester.setSelection(0);
    }

}


