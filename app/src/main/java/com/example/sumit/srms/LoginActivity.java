package com.example.sumit.srms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table_login;
    String userId;

    Button btn_sign_in;
    EditText txt_email, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_login = db.getReference("login_m");
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_sign_in = findViewById(R.id.btn_SignIn);
        SessionMgmt sessionMgmt = new SessionMgmt(LoginActivity.this);

        //Check whether user loggedIn or not! if user loggedIn then it will redirect on to activity Based on respected user role.
        checkUserLoggedIn();

        //setOnClickListener Button Events
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_email.getText().toString().trim().length() == 0 || txt_password.getText().toString().trim().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Error : Please Fill Required Fields!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if(txt_email.getText().toString().matches(emailPattern)) {
                        Query query_login = table_login.orderByChild("email").equalTo(txt_email.getText().toString());
                        query_login.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    userId = snapshot.getKey();
                                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                        if (datasnapshot.child("password").getValue().toString().equals(txt_password.getText().toString())) {
                                            Query query_role = db.getReference("registration_m").orderByChild("email").equalTo(txt_email.getText().toString());
                                            query_role.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                                        // User Session Management
//                                                    SessionMgmt sessionMgmt = new SessionMgmt(LoginActivity.this);
                                                        sessionMgmt.setUser_id(datasnapshot.getKey().toString());
                                                        sessionMgmt.setUser_role(datasnapshot.child("user_role").getValue().toString());
                                                        checkUserLoggedIn();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Error : Incorrect Email Id or Password!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error : Incorrect Email Id or Password!!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void checkUserLoggedIn()
    {
        SessionMgmt sessionMgmt = new SessionMgmt(LoginActivity.this);
        if(sessionMgmt.getUser_role().equals("Admin"))
        {
            Intent intent = new Intent(LoginActivity.this, AdminDrawer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        else if (sessionMgmt.getUser_role().equals("Director"))
        {
            Intent intent = new Intent(LoginActivity.this, DirectorDrawer.class);
            startActivity(intent);
        }

        else if (sessionMgmt.getUser_role().equals("Faculty"))
        {
            Intent intent = new Intent(LoginActivity.this, FacultyDrawer.class);
            startActivity(intent);
        }

        else if (sessionMgmt.getUser_role().equals("Student")) {
            Intent intent = new Intent(LoginActivity.this, StudentDrawer.class);
            startActivity(intent);
        }
    }
}