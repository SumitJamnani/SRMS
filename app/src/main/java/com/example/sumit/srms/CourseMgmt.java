package com.example.sumit.srms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class CourseMgmt extends Fragment implements View.OnClickListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_course;
    private DatabaseReference table_registration;
    ChildEventListener childEventListener;
    String userId;

    EditText txt_course;
    Button btn_add_course, btn_search_course, btn_update_course, btn_delete_course;
    Spinner spinner_director;
    ArrayAdapter<String> adapter_director;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_mgmt, container, false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_course = db.getReference("course_m");
        table_registration = db.getReference("registration_m");
        txt_course = view.findViewById(R.id.txt_course);
        spinner_director = view.findViewById(R.id.spinner_director);
        btn_add_course = view.findViewById(R.id.btn_AddData);
        btn_update_course = view.findViewById(R.id.btn_UpdateData);
        btn_delete_course = view.findViewById(R.id.btn_DeleteData);
        btn_search_course = view.findViewById(R.id.btn_SearchData);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_course.setOnClickListener(this);
        btn_update_course.setOnClickListener(this);
        btn_delete_course.setOnClickListener(this);
        btn_search_course.setOnClickListener(this);

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
                    int director_pos;
                    userId = snapshot.getKey();
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data != null && !data.isEmpty())
                    {
                        director_pos = adapter_director.getPosition(data.get("director_name").toString());
                        txt_course.setText(data.get("course_name").toString());
                        spinner_director.setSelection(director_pos);

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
        if (txt_course.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Fill The Required Fields!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("course_name", txt_course.getText().toString());
            values.put("director_name", spinner_director.getSelectedItem().toString());

            String key = table_registration.push().getKey();
            table_course.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_course.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Course Name For Update The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("course_name", txt_course.getText().toString());
            values.put("director_name", spinner_director.getSelectedItem().toString());
            table_course.child(userId).updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_course.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Course Name For Delete The Record!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            table_course.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
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
        if (txt_course.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Course Name For Search The Record!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            Query query_course = table_course.orderByChild("course_name").equalTo(txt_course.getText().toString());
            query_course.addChildEventListener(childEventListener);
        }
    }


    // Code : View All Data From Table
    public void ViewData()
    {
        Query fetch_data_query = table_course.orderByChild("course_name");
        fetch_data_query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        Query fetch_spinner_query = table_registration.orderByChild("name");
        fetch_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    List<String> director_array = new ArrayList<String>();
                    director_array.add("-- Select Director --");
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        switch (data.child("user_role").getValue().toString())
                        {
                            case "Director" :
                                director_array.add(data.child("name").getValue().toString());
                                break;
                        }
                    }
                    adapter_director = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, director_array);
                    adapter_director.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_director.setAdapter(adapter_director);
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
        txt_course.setText("");
        spinner_director.setSelection(0);
    }

}