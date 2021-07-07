package com.example.sumit.srms;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;

public class DatabaseDemo extends AppCompatActivity implements View.OnClickListener {

    //Declaration Part
    private FirebaseDatabase db;
    private DatabaseReference table;
    Query search_query;
    ChildEventListener childEventListener;
    String userId;

    Button btn_sign_in, btn_search_data, btn_update_data, btn_delete_data;
    EditText email_value, password_value;
    Spinner spinner_user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_demo);

        //Screen Rotation Disable Code
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table = db.getReference("demo");
        email_value = findViewById(R.id.email);
        password_value = findViewById(R.id.password);
        btn_sign_in = findViewById(R.id.btn_AddData);
        btn_search_data = findViewById(R.id.btn_SearchData);
        btn_update_data = findViewById(R.id.btn_UpdateData);
        btn_delete_data = findViewById(R.id.btn_DeleteData);
        spinner_user = findViewById(R.id.spinner_user);

        //Initialize SetOnClickListener() Method For Buttons
        btn_sign_in.setOnClickListener(this);
        btn_search_data.setOnClickListener(this);
        btn_update_data.setOnClickListener(this);
        btn_delete_data.setOnClickListener(this);

        //Fill Spinner With Database Values
        fill_spinner();

        //Event Listeners
        childEventListener = new ChildEventListener() {
            //Retrieve Data From Database
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    userId = snapshot.getKey();
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    if (data != null && !data.isEmpty())
                    {
                        email_value.setText(data.get("Email").toString());
                        password_value.setText(data.get("Password").toString());
                    }
                }
                else
                {
                    Toast.makeText(DatabaseDemo.this, "Error : Record Does Not Exist!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DatabaseDemo.this, "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        };
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
        if (email_value.getText().toString().trim().length() == 0 || password_value.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DatabaseDemo.this, "Error : All Fields Are Required!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            values.put("Email", email_value.getText().toString());
            values.put("Password", password_value.getText().toString());

            String key = table.push().getKey();
            table.child(key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(DatabaseDemo.this, "Record Inserted Successfully!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(DatabaseDemo.this, "Error : Something Went Wrong While Inserting Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Code : Update Particular Data From Table
    public void UpdateData()
    {
        if (email_value.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DatabaseDemo.this, "Error : Enter Email Id For Update The Record!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String, Object> values = new HashMap<>();
            //Query update_query = table.orderByChild("Email").equalTo(email_value.getText().toString());
            values.put("Email", email_value.getText().toString());
            values.put("Password", password_value.getText().toString());
            table.child(userId).updateChildren(values).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(DatabaseDemo.this, "Record Updated Successfully!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(DatabaseDemo.this, "Error : Something Went Wrong While Updating Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Code : Delete Particular Data From Table
    public void DeleteData()
    {
        if (email_value.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DatabaseDemo.this, "Error : Enter Email Id For Delete The Record!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            //Query delete_query = table.orderByChild("Email").equalTo(email_value.getText().toString());
            table.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void unused)
                {
                    Toast.makeText(DatabaseDemo.this, "Record Deleted Successfully!!", Toast.LENGTH_SHORT).show();
                    clear();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(DatabaseDemo.this, "Error : Something Went Wrong While Deleting Record!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Code : View Particular Data From Table
    public void SearchData()
    {
        if (email_value.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DatabaseDemo.this, "Error : Enter Email Id For Search The Record!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            Query search_query = table.orderByChild("Email").equalTo(email_value.getText().toString());
            search_query.addChildEventListener(childEventListener);
        }
    }


    // Code : View All Data From Table
    public void ViewData()
    {
        Query fetch_data_query = table.orderByChild("Email");
        fetch_data_query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                Toast.makeText(DatabaseDemo.this, "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        Query fetch_spinner_query = table.orderByChild("Email");
        fetch_spinner_query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    List<String> email_array = new ArrayList<String>();
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        email_array.add(data.child("Email").getValue().toString());
                    }
                    ArrayAdapter<String> email_adapter = new ArrayAdapter<String>(DatabaseDemo.this, android.R.layout.simple_spinner_item, email_array);
                    email_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_user.setAdapter(email_adapter);
                }
                else
                {
                    Toast.makeText(DatabaseDemo.this, "Error : Records Not Found!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                Toast.makeText(DatabaseDemo.this, "Error : Something Went Wrong While Fetching Record!!", Toast.LENGTH_SHORT).show();

            }
        });
    }




    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        email_value.setText("");
        password_value.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        search_query.removeEventListener(childEventListener);
    }
}