package com.example.sumit.srms;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BatchMgmt extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_batch;
    private DatabaseReference table_semester;
    private DatabaseReference table_course;
    ChildEventListener childEventListener;
    String userId;

    EditText txt_batch;
    Button btn_add_batch, btn_search_batch, btn_update_batch, btn_delete_batch;
    Spinner spinner_course, spinner_semester;
    ArrayAdapter<String> adapter_course, adapter_semester;
    List<String> semester_array;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_batch_mgmt, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_batch = db.getReference("batch_m");
        table_course = db.getReference("course_m");
        table_semester = db.getReference("semester_m");
        txt_batch = view.findViewById(R.id.txt_batch);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_course = view.findViewById(R.id.spinner_course);
        btn_add_batch = view.findViewById(R.id.btn_AddData);
        btn_update_batch = view.findViewById(R.id.btn_UpdateData);
        btn_delete_batch = view.findViewById(R.id.btn_DeleteData);
        btn_search_batch = view.findViewById(R.id.btn_SearchData);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_batch.setOnClickListener(this);
        btn_update_batch.setOnClickListener(this);
        btn_delete_batch.setOnClickListener(this);
        btn_search_batch.setOnClickListener(this);
        spinner_course.setOnItemSelectedListener(this);


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
                    int semester_pos, course_pos;
                    userId = snapshot.getKey();
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data != null && !data.isEmpty())
                    {
                        semester_pos = adapter_semester.getPosition(data.get("semester_name").toString());
                        course_pos = adapter_course.getPosition(data.get("course_name").toString());
                        txt_batch.setText(data.get("batch_name").toString());
                        spinner_semester.setSelection(semester_pos);
                        spinner_course.setSelection(course_pos);
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
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // Code : Insert Data In Table
    public void InsertData()
    {
        if (txt_batch.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Fill The Required Fields!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("batch_name", txt_batch.getText().toString());
            values.put("course_name", spinner_course.getSelectedItem().toString());
            values.put("semester_name", spinner_semester.getSelectedItem().toString());

            String key = table_batch.push().getKey();
            table_batch.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_batch.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Batch Name For Update The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("batch_name", txt_batch.getText().toString());
            values.put("course_name", spinner_course.getSelectedItem().toString());
            values.put("semester_name", spinner_semester.getSelectedItem().toString());
            table_batch.child(userId).updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_batch.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Batch Name For Delete The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            table_batch.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_batch.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Batch Name For Search The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Query query_batch = table_batch.orderByChild("batch_name").equalTo(txt_batch.getText().toString());
            query_batch.addChildEventListener(childEventListener);
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
    }


    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        txt_batch.setText("");
        spinner_course.setSelection(0);
        spinner_semester.setSelection(0);
    }

}