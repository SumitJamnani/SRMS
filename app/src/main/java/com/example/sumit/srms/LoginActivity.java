package com.example.sumit.srms;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
                String emailInput = email_input.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
                {

                }
                Intent intent = new Intent(LoginActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
