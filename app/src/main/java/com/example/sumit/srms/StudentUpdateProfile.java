package com.example.sumit.srms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentUpdateProfile extends Fragment {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_reg, table_login;

    EditText txt_enrollment, txt_course, txt_batch, txt_semester, txt_name, txt_email, txt_password;
    Button btn_update_profile;
    Map<String, Object> values_reg, values_login;
    SessionMgmt sessionMgmt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_update_profile, container, false);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_reg = db.getReference("registration_m");
        table_login = db.getReference("login_m");
        txt_enrollment =  view.findViewById(R.id.txt_enrollment);
        txt_course =  view.findViewById(R.id.txt_course);
        txt_batch =  view.findViewById(R.id.txt_batch);
        txt_semester =  view.findViewById(R.id.txt_semester);
        txt_name =  view.findViewById(R.id.txt_name);
        txt_email =  view.findViewById(R.id.txt_email);
        txt_password =  view.findViewById(R.id.txt_password);
        btn_update_profile = view.findViewById(R.id.btn_UpdateProfile);
        sessionMgmt = new SessionMgmt(this.getActivity());

        //Fill Spinner With Database Values
        fill_data();

        //Update User Data
        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                values_reg = new HashMap<>();
                values_login = new HashMap<>();

                if(txt_name.getText().toString().trim().length() == 0 || txt_email.getText().toString().trim().length() == 0 || txt_password.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getActivity(), "Error : Please Fill Required Fields For Update The Record!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if(txt_email.getText().toString().matches(emailPattern)) {
                        values_reg.put("name", txt_name.getText().toString());
                        values_reg.put("email", txt_email.getText().toString());
                        values_login.put("email", txt_email.getText().toString());
                        values_login.put("password", txt_password.getText().toString());

                        table_login.child(sessionMgmt.getUser_id()).updateChildren(values_login);
                        table_reg.child(sessionMgmt.getUser_id()).updateChildren(values_reg).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Profile Updated Successfully!!", Toast.LENGTH_SHORT).show();
                                fill_data();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error : Something Went Wrong While Updating Profile!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }


    public void fill_data()
    {

        //Search Data From Firebase Based on UserId of SharedPreference
        if(sessionMgmt.getUser_id().equals(""))
        {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Not Sing In (: ")
                    .setContentText("You Are Not Logged In! PLease Login For Get Better Experience of This App :)")
                    .setConfirmText("Got It!")
                    .show();
        }
        else {
            Query search_query_registration = table_reg.child(sessionMgmt.getUser_id());
            search_query_registration.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                        txt_enrollment.setText(data.get("enrollment_number").toString());
                        txt_course.setText(data.get("course_name").toString());
                        txt_batch.setText(data.get("batch_name").toString());
                        txt_semester.setText(data.get("semester_name").toString());
                        txt_name.setText(data.get("name").toString());
                        txt_email.setText(data.get("email").toString());

                        Query search_query_registration = table_login.child(sessionMgmt.getUser_id());
                        search_query_registration.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                                    txt_password.setText(data.get("password").toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}