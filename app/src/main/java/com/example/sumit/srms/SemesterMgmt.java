package com.example.sumit.srms;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SemesterMgmt extends Fragment implements View.OnClickListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_semester;
    private DatabaseReference table_course;
    ChildEventListener childEventListener;
    String userId;

    EditText txt_semester;
    Button btn_add_semester, btn_search_semester, btn_update_semester, btn_delete_semester;
    Spinner spinner_course;
    ArrayAdapter<String> adapter_course;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_semester_mgmt, container,false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_semester = db.getReference("semester_m");
        table_course = db.getReference("course_m");
        txt_semester = view.findViewById(R.id.txt_semester);
        spinner_course = view.findViewById(R.id.spinner_course);
        btn_add_semester = view.findViewById(R.id.btn_AddData);
        btn_update_semester = view.findViewById(R.id.btn_UpdateData);
        btn_delete_semester = view.findViewById(R.id.btn_DeleteData);
        btn_search_semester = view.findViewById(R.id.btn_SearchData);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_semester.setOnClickListener(this);
        btn_update_semester.setOnClickListener(this);
        btn_delete_semester.setOnClickListener(this);
        btn_search_semester.setOnClickListener(this);

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
                    int course_pos;
                    userId = snapshot.getKey();
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data != null && !data.isEmpty())
                    {
                        course_pos = adapter_course.getPosition(data.get("course_name").toString());
                        txt_semester.setText(data.get("semester_name").toString());
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


    // Code : Insert Data In Table
    public void InsertData()
    {
        if (txt_semester.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Fill The Required Fields!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("semester_name", txt_semester.getText().toString());
            values.put("course_name", spinner_course.getSelectedItem().toString());

            String key = table_semester.push().getKey();
            table_semester.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_semester.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Semester Name For Update The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("semester_name", txt_semester.getText().toString());
            values.put("course_name", spinner_course.getSelectedItem().toString());
            table_semester.child(userId).updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_semester.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Semester Name For Delete The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            table_semester.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_semester.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Semester Name For Search The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Query query_semester = table_semester.orderByChild("semester_name").equalTo(txt_semester.getText().toString());
            query_semester.addChildEventListener(childEventListener);
        }
    }


    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        Query fetch_spinner_query = table_course.orderByChild("course_name");
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


    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        txt_semester.setText("");
        spinner_course.setSelection(0);
    }

}