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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class RegistrationActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_reg, table_login;
    ChildEventListener childEventListener;
    String userId;

    EditText txt_enrollment, txt_name, txt_email, txt_password;
    Spinner spinner_role, spinner_course, spinner_semester, spinner_division, spinner_batch, spinner_electivesubject;
    Button btn_add_user, btn_update_user, btn_delete_user, btn_search_user;
    LinearLayout student_views;
    ArrayAdapter<String> adapter_role, adapter_course, adapter_semester, adapter_division, adapter_batch, adapter_electivesubject;
    Map<String, Object> values_reg, values_login;
    List<String> semester_array, division_array, batch_array, electivesubject_array;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration, container, false);


        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_reg = db.getReference("registration_m");
        table_login = db.getReference("login_m");
        txt_enrollment = view.findViewById(R.id.txt_enrollment);
        txt_name = view.findViewById(R.id.txt_name);
        txt_email = view.findViewById(R.id.txt_email);
        txt_password = view.findViewById(R.id.txt_password);
        spinner_role = view.findViewById(R.id.spinner_role);
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_division = view.findViewById(R.id.spinner_division);
        spinner_batch = view.findViewById(R.id.spinner_batch);
        spinner_electivesubject = view.findViewById(R.id.spinner_elective_subject);
        btn_add_user = view.findViewById(R.id.btn_AddUser);
        btn_update_user = view.findViewById(R.id.btn_UpdateUser);
        btn_delete_user = view.findViewById(R.id.btn_DeleteUser);
        btn_search_user = view.findViewById(R.id.btn_SearchUser);
        student_views = view.findViewById(R.id.student_views);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_user.setOnClickListener(this);
        btn_search_user.setOnClickListener(this);
        btn_update_user.setOnClickListener(this);
        btn_delete_user.setOnClickListener(this);
        spinner_role.setOnItemSelectedListener(this);
        spinner_course.setOnItemSelectedListener(this);
        spinner_semester.setOnItemSelectedListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Event Listeners
        childEventListener = new ChildEventListener() {
            //Retrieve Data From Database
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String selectedItem = data.get("user_role").toString();
                    int role_pos, course_pos, sem_pos, div_pos, batch_pos, subject_pos;
                    userId = snapshot.getKey();

                    if (data != null && !data.isEmpty())
                    {
                        role_pos = adapter_role.getPosition(data.get("user_role").toString());
                        course_pos = adapter_course.getPosition(data.get("course_name").toString());
                        spinner_role.setSelection(role_pos);
                        spinner_course.setSelection(course_pos);
                        txt_name.setText(data.get("name").toString());
                        txt_email.setText(data.get("email").toString());

                        Query search_query_login = table_login.orderByChild("email").equalTo(txt_email.getText().toString());
                        search_query_login.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                                if (snapshot.exists()) {
                                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                                    txt_password.setText(data.get("password").toString());
                                }
                            }
                            @Override
                            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

                            }
                            @Override
                            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

                            }
                            @Override
                            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

                            }
                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {

                            }
                        });

                        switch (selectedItem)
                        {
                            case "Student":
                                sem_pos = adapter_semester.getPosition(data.get("semester_name").toString());
                                div_pos = adapter_division.getPosition(data.get("division_name").toString());
                                batch_pos = adapter_batch.getPosition(data.get("batch_name").toString());
                                subject_pos = adapter_electivesubject.getPosition(data.get("elective_subject").toString());
                                spinner_semester.setSelection(sem_pos);
                                spinner_division.setSelection(div_pos);
                                spinner_batch.setSelection(batch_pos);
                                spinner_electivesubject.setSelection(subject_pos);
                                txt_enrollment.setText(data.get("enrollment_number").toString());
                                break;
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Error : Record Does Not Exist!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_AddUser:
                InsertData();
                break;

            case R.id.btn_UpdateUser:
                UpdateData();
                break;

            case R.id.btn_DeleteUser:
                DeleteData();
                break;

            case R.id.btn_SearchUser:
                SearchData();
                break;
        }
    }


    //onItemSelectedListner Spinner Events
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spinner_role:
                String selectedItem = spinner_role.getSelectedItem().toString();

                switch (selectedItem) {
                    case "Director":
                        student_views.setVisibility(View.GONE);
                        break;

                    case "Faculty":
                        student_views.setVisibility(View.GONE);
                        break;

                    default:
                        student_views.setVisibility(View.VISIBLE);
                        break;
                }
                break;

            case R.id.spinner_course:
                //Spinner Course -> Spinner Semester
                if(spinner_course.getSelectedItemPosition() != 0)
                {
                    Query semester_spinner_query = db.getReference("semester_m").orderByChild("course_name").equalTo(spinner_course.getSelectedItem().toString());
                    semester_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                semester_array.clear();
                                semester_array.add("-- Select Semester --");
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    semester_array.add(data.child("semester_name").getValue().toString());
                                }
                                adapter_semester.notifyDataSetChanged();
                                spinner_semester.setAdapter(adapter_semester);
                            }
                            else
                            {
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

            //Spinner Semester -> Spinner Division & Batch & Elective Subject
            case R.id.spinner_semester:
                if(spinner_semester.getSelectedItemPosition() != 0)
                {
                    Query division_spinner_query = db.getReference("division_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    division_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if (snapshot.exists())
                            {
                                division_array.clear();
                                division_array.add("-- Select Division --");
                                for (DataSnapshot datasnapshot : snapshot.getChildren())
                                {
                                    if(datasnapshot.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()))
                                    {
                                        division_array.add(datasnapshot.child("division_name").getValue().toString());
                                    }
                                }
                                adapter_division.notifyDataSetChanged();
                                spinner_division.setAdapter(adapter_division);
                            }

                            else
                            {
                                division_array.clear();
                                division_array.add("-- Select Division --");
                                adapter_division.notifyDataSetChanged();
                                spinner_division.setAdapter(adapter_division);
                            }
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
                    //Initialize Spinner Division
                    division_array = new ArrayList<String>();
                    division_array.add("-- Select Division --");
                    adapter_division = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, division_array);
                    adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_division.setAdapter(adapter_division);
                }

                if(spinner_semester.getSelectedItemPosition() != 0)
                {
                    Query subject_spinner_query = db.getReference("batch_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    subject_spinner_query.addListenerForSingleValueEvent(new ValueEventListener()
                    {
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
                            }

                            else {
                                batch_array.clear();
                                batch_array.add("-- Select Batch --");
                                adapter_batch.notifyDataSetChanged();
                                spinner_batch.setAdapter(adapter_batch);
                            }
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
                    //Initialize Spinner Batch
                    batch_array = new ArrayList<String>();
                    batch_array.add("-- Select Batch --");
                    adapter_batch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, batch_array);
                    adapter_batch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_batch.setAdapter(adapter_batch);
                }

                if(spinner_semester.getSelectedItemPosition() != 0)
                {
                    Query subject_spinner_query = db.getReference("subject_m").orderByChild("semester_name").equalTo(spinner_semester.getSelectedItem().toString());
                    subject_spinner_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                electivesubject_array.clear();
                                electivesubject_array.add("-- Select Elective Subject --");
                                for (DataSnapshot data : snapshot.getChildren())
                                {
                                    if(data.child("subject_type").getValue().toString().equals("Elective") && data.child("course_name").getValue().toString().equals(spinner_course.getSelectedItem().toString()))
                                    {
                                        electivesubject_array.add(data.child("subject_name").getValue().toString());
                                    }
                                }

                                if(electivesubject_array.size() == 1)
                                {
                                    Toast.makeText(getActivity(), "Error : Elective Subject or Division Records Not Found For Selected Semester!!", Toast.LENGTH_SHORT).show();
                                }
                                adapter_electivesubject.notifyDataSetChanged();
                                spinner_electivesubject.setAdapter(adapter_electivesubject);
                            } else {
                                electivesubject_array.clear();
                                electivesubject_array.add("-- Select Elective Subject --");
                                adapter_electivesubject.notifyDataSetChanged();
                                spinner_electivesubject.setAdapter(adapter_electivesubject);
                                Toast.makeText(getActivity(), "Error : Batch or Division or Elective Subject Records Not Found For Selected Semester!!", Toast.LENGTH_SHORT).show();
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
                    //Initialize Spinner Elective Subject
                    electivesubject_array = new ArrayList<String>();
                    electivesubject_array.add("-- Select Elective Subject --");
                    adapter_electivesubject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, electivesubject_array);
                    adapter_electivesubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_electivesubject.setAdapter(adapter_electivesubject);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // Code : Insert Data In Table
    public void InsertData() {
        if (txt_name.getText().toString().trim().length() == 0 || txt_email.getText().toString().trim().length() == 0 || txt_password.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Please Fill The Required Fields!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(txt_email.getText().toString().matches(emailPattern)) {
                InitializeData();
                String key = table_reg.push().getKey();
                table_login.child(key).setValue(values_login);
                table_reg.child(key).setValue(values_reg).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Record Inserted Successfully!!", Toast.LENGTH_SHORT).show();
                        clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error : Something Went Wrong While Inserting Record!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(),"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void InitializeData()
    {
        values_reg = new HashMap<>();
        values_login = new HashMap<>();
        String selectedItem = spinner_role.getSelectedItem().toString();

        switch (selectedItem)
        {
            case "Student" :
                values_reg.put("user_role", spinner_role.getSelectedItem().toString());
                values_reg.put("enrollment_number", txt_enrollment.getText().toString());
                values_reg.put("name", txt_name.getText().toString());
                values_reg.put("email", txt_email.getText().toString());
                values_login.put("email", txt_email.getText().toString());
                values_login.put("password", txt_password.getText().toString());
                values_reg.put("course_name", spinner_course.getSelectedItem().toString());
                values_reg.put("semester_name", spinner_semester.getSelectedItem().toString());
                values_reg.put("division_name", spinner_division.getSelectedItem().toString());
                values_reg.put("batch_name", spinner_batch.getSelectedItem().toString());
                values_reg.put("elective_subject", spinner_electivesubject.getSelectedItem().toString());
                break;

            case "Faculty" :
                values_reg.put("user_role", spinner_role.getSelectedItem().toString());
                values_reg.put("name", txt_name.getText().toString());
                values_reg.put("email", txt_email.getText().toString());
                values_login.put("email", txt_email.getText().toString());
                values_login.put("password", txt_password.getText().toString());
                values_reg.put("course_name", spinner_course.getSelectedItem().toString());
                break;

            case "Director" :
                values_reg.put("user_role", spinner_role.getSelectedItem().toString());
                values_reg.put("name", txt_name.getText().toString());
                values_reg.put("email", txt_email.getText().toString());
                values_login.put("email", txt_email.getText().toString());
                values_login.put("password", txt_password.getText().toString());
                values_reg.put("course_name", spinner_course.getSelectedItem().toString());

                Query dir_name = db.getReference("course_m").orderByChild("course_name").equalTo(spinner_course.getSelectedItem().toString());
                dir_name.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            Map<String,Object> course_name = new HashMap<String,Object>();
                            course_name.put("director_name", txt_name.getText().toString());
                            for (DataSnapshot data : snapshot.getChildren()) {
                                db.getReference("course_m").child(data.getKey()).updateChildren(course_name).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Director Allocated To Course!!", Toast.LENGTH_LONG);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });


                break;

            default :
                Toast.makeText(this.getActivity(), "Error : Please Select Correct User Role!!", Toast.LENGTH_SHORT).show();
        }
    }

    // Code : Update Particular Data From Table
    public void UpdateData()
    {
        if (txt_email.getText().toString().trim().length() == 0)
        {
            Toast.makeText(this.getActivity(), "Error : Enter Email Id For Update The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(txt_email.getText().toString().matches(emailPattern)) {
                InitializeData();
                table_login.child(userId).updateChildren(values_login);
                table_reg.child(userId).updateChildren(values_reg).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Record Updated Successfully!!", Toast.LENGTH_SHORT).show();
                        clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error : Something Went Wrong While Updating Record!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(),"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
            }
        }
    }





    // Code : Delete Particular Data From Table
    public void DeleteData() {
        if (txt_email.getText().toString().trim().length() == 0)
        {
            Toast.makeText(this.getActivity(), "Error : Enter Email Id For Delete The Record!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(txt_email.getText().toString().matches(emailPattern)) {
                table_login.child(userId).removeValue();
                table_reg.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Record Deleted Successfully!!", Toast.LENGTH_SHORT).show();
                        clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error : Something Went Wrong While Deleting Record!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(),"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Code : View Particular Data From Table
    public void SearchData() {
        if (txt_email.getText().toString().trim().length() == 0)
        {
            Toast.makeText(getActivity(), "Error : Enter Email Id For Search The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(txt_email.getText().toString().matches(emailPattern)) {
                Query search_query = table_reg.orderByChild("email").equalTo(txt_email.getText().toString());
                search_query.addChildEventListener(childEventListener);
            }
            else
            {
                Toast.makeText(getActivity(),"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        //Spinner Role
        SessionMgmt sessionMgmt =  new SessionMgmt(getActivity());
        if(sessionMgmt.getUser_role().equals("Admin"))
        {
            adapter_role = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Role_All));
            adapter_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_role.setAdapter(adapter_role);
        }
        else if(sessionMgmt.getUser_role().equals("Director"))
        {
            adapter_role = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Role));
            adapter_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_role.setAdapter(adapter_role);
        }
        else if(sessionMgmt.getUser_role().equals("Faculty"))
        {
            adapter_role = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Role_Faculty));
            adapter_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_role.setAdapter(adapter_role);
        }

        //Spinner Course
        Query course_spinner_query = db.getReference("course_m").orderByChild("course_name");
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
        spinner_role.setSelection(0);
        txt_name.setText("");
        txt_email.setText("");
        txt_password.setText("");
        txt_enrollment.setText("");
        spinner_course.setSelection(0);
        spinner_semester.setSelection(0);
        spinner_division.setSelection(0);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}

