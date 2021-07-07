package com.example.sumit.srms;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    //Declaration Part
    Button btn_sign_in;
    EditText email_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialization Part
        btn_sign_in = findViewById(R.id.SignIn);
        email_input = findViewById(R.id.txt_email);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, AdminDrawer.class);
                startActivity(intent);
            }
        });
    }
}