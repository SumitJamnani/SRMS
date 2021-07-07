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
import android.widget.Spinner;
import android.widget.Toast;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class RegistrationActivity extends Fragment implements View.OnClickListener{

    //Declaration Part
    SQLiteDatabase db;
    EditText txt_enrollment, txt_name, txt_email, txt_password;
    String role_value, course_value, semester_value, division_value;
    Spinner spinner_role, spinner_course, spinner_semester, spinner_division;
    Button btn_add_user, btn_update_user, btn_delete_user, btn_search_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration, container, false);


        //Initialization Part
        txt_enrollment = view.findViewById(R.id.txt_enrollment);
        txt_name = view.findViewById(R.id.txt_name);
        txt_email = view.findViewById(R.id.txt_email);
        txt_password = view.findViewById(R.id.txt_password);
        spinner_role = view.findViewById(R.id.spinner_role);
        spinner_course = view.findViewById(R.id.spinner_course);
        spinner_semester = view.findViewById(R.id.spinner_semester);
        spinner_division = view.findViewById(R.id.spinner_division);
        btn_add_user = view.findViewById(R.id.btn_AddUser);
        btn_update_user = view.findViewById(R.id.btn_UpdateUser);
        btn_delete_user = view.findViewById(R.id.btn_DeleteUser);
        btn_search_user = view.findViewById(R.id.btn_SearchUser);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_user.setOnClickListener(this);
        btn_search_user.setOnClickListener(this);
        btn_update_user.setOnClickListener(this);
        btn_delete_user.setOnClickListener(this);


        //Connect Database And Create Table If Not Exist
        DbConnect_CreateTable();

        //Fill Spinner With Database Values
        fill_spinner();


        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getSelectedItem().toString();
                switch (selectedItem)
                {
                    case "Director" :
                        txt_enrollment.setVisibility(View.INVISIBLE);
                        spinner_course.setVisibility(View.INVISIBLE);
                        spinner_semester.setVisibility(View.INVISIBLE);
                        spinner_division.setVisibility(View.INVISIBLE);
                        break;

                    case "Faculty" :
                        txt_enrollment.setVisibility(View.INVISIBLE);
                        spinner_course.setVisibility(View.INVISIBLE);
                        spinner_semester.setVisibility(View.INVISIBLE);
                        spinner_division.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        txt_enrollment.setVisibility(View.VISIBLE);
                        spinner_course.setVisibility(View.VISIBLE);
                        spinner_semester.setVisibility(View.VISIBLE);
                        spinner_division.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    //CRUD operations on Button Click
    //OnClick Button Events
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_AddUser:
                InsertData();
                break;

            case R.id.btn_UpdateUser:
                //UpdateData();
                break;

            case R.id.btn_DeleteUser:
                //DeleteData();
                break;

            case R.id.btn_SearchUser:
                //SearchData();
                break;
        }
    }


    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        ArrayAdapter<String> adapter_role = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Role));
        adapter_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_role.setAdapter(adapter_role);

        ArrayAdapter<String> adapter_course = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);

        ArrayAdapter<String> adapter_semester = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Sem));
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_semester);

        ArrayAdapter<String> adapter_division = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Div));
        adapter_division.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_division.setAdapter(adapter_division);
    }

    // Code : Connect Database With Fragment And Create Table If Noe Exist
    public void DbConnect_CreateTable()
    {
        db = getActivity().openOrCreateDatabase("SRMS",android.content.Context.MODE_PRIVATE ,null);

        // Database Related Code : Create Table Login_m
        db.execSQL("CREATE TABLE IF NOT EXISTS login_m(email VARCHAR(50) PRIMARY KEY, password VARCHAR(30))");

        // Database Related Code : Create Table Registration_m
        db.execSQL("CREATE TABLE IF NOT EXISTS registration_m(reg_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "enrollment_num DECIMAL(21,0), name VARCHAR(30) NOT NULL, user_role VARCHAR(20), course_id INTEGER REFERENCES course_m(course_id)," +
                "semester_id INTEGER REFERENCES semester_m(semester_id), division_id INTEGER REFERENCES division_m(division_id),email VARCHAR(50) REFERENCES login_m(email))");
    }


    // Code : Insert Data In Table
    public void InsertData()
    {
        if(txt_name.getText().toString().trim().length()==0|| txt_email.getText().toString().trim().length()==0 || txt_password.getText().toString().trim().length()==0)
        {
            msg(this.getActivity(), "Error : All Fields Are Required!!");
            return;
        }
        else
        {
            role_value = spinner_role.getSelectedItem().toString();
            semester_value = spinner_semester.getSelectedItem().toString();
            division_value = spinner_division.getSelectedItem().toString();
            try
            {
                db.execSQL("INSERT INTO login_m(email,password)VALUES('" + txt_email.getText() + "','" +
                        txt_password.getText() + "')");
                db.execSQL("INSERT INTO registration_m(enrollment_num,name,user_role,semester,division,email)" +
                        "VALUES('" + txt_enrollment.getText() + "','" + txt_name.getText() + "', '" + role_value + "')," +
                        "'" + semester_value + "','" + division_value + "', " +
                        "'" + txt_email.getText() + "'))");
                msg(this.getActivity(), "Record Inserted Successfully!!");
                clear();
            }
            catch(Exception e)
            {
                msg(this.getActivity(), "Error : Something Went Wrong While Inserting Data or Duplicate Record Found!!");
            }
        }
    }


    // Code : View All Data From Table
    public void ViewData()
    {
        Cursor record = db.rawQuery("SELECT * FROM login_m", null);
        if(record.getCount()==0)
        {
            return;
        }
        else
        {
            StringBuffer buffer = new StringBuffer();
            while (record.moveToNext()) {
                buffer.append("Email : " + record.getString(0) + "\n");
                buffer.append("Password : " + record.getString(1) + "\n\n");
            }
            Toast.makeText(this.getActivity(), buffer.toString(), Toast.LENGTH_LONG).show();
            record.close();

        }
    }

    // For Displaying Messages on Screen
    public void msg(Context context, String str)
    {
        Toast.makeText(this.getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        spinner_role.setSelection(0);
        txt_name.setText("");
        txt_email.setText("");
        txt_password.setText("");
        txt_enrollment.setText("");
        spinner_semester.setSelection(0);
        spinner_division.setSelection(0);
    }

}

