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

import java.util.ArrayList;
import java.util.List;

public class CourseMgmt extends Fragment implements View.OnClickListener {

    //Declaration Part
    SQLiteDatabase db;
    EditText txt_course;
    Spinner spinner_director;
    String director_value, course_old_value;
    int director_id;
    Button btn_add_course, btn_search_course, btn_update_course, btn_delete_course;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_mgmt, container,false);

        //Initialization Part
        txt_course = view.findViewById(R.id.txt_course);
        txt_course = view.findViewById(R.id.txt_course);
        spinner_director = view.findViewById(R.id.spinner_director);
        btn_add_course = view.findViewById(R.id.btn_AddCourse);
        btn_update_course = view.findViewById(R.id.btn_UpdateCourse);
        btn_delete_course = view.findViewById(R.id.btn_DeleteCourse);
        btn_search_course = view.findViewById(R.id.btn_SearchCourse);

        //Initialize SetOnClickListener() Method For Buttons
        btn_add_course.setOnClickListener(this);
        btn_update_course.setOnClickListener(this);
        btn_delete_course.setOnClickListener(this);
        btn_search_course.setOnClickListener(this);

        //Connect Database And Create Table If Not Exist
        DbConnect_CreateTable();

        //Fill Spinner With Database Values
        fill_spinner();

        return view;
    }


    //CRUD operations on Button Click
    //OnClick Button Events
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_AddCourse:
                InsertData();
                break;

            case R.id.btn_UpdateCourse:
                UpdateData();
                break;

            case R.id.btn_DeleteCourse:
                DeleteData();
                break;

            case R.id.btn_SearchCourse:
                SearchData();
                break;
        }
    }

    //Fill Spinner With Database Values
    public void fill_spinner()
    {
        Cursor record = db.rawQuery("SELECT name FROM registration_m where user_role='Director'", null);
        if(record.getCount()==0)
        {
            msg(this.getActivity(), "Please Register Director First!!");
            return;
        }
        else
        {
            List<String> director_array = new ArrayList<String>();
            director_array.add("-- Select Director --");
            while (record.moveToNext())
            {
                director_array.add(record.getString(record.getColumnIndex("name")));
            }
            record.close();

            ArrayAdapter<String> director_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, director_array);
            director_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_director.setAdapter(director_adapter);
        }
    }


    // Code : Connect Database With Fragment And Create Table If Noe Exist
    public void DbConnect_CreateTable()
    {
        db = getActivity().openOrCreateDatabase("SRMS",android.content.Context.MODE_PRIVATE ,null);

        // Database Related Code : Create Table Course_m
        db.execSQL("CREATE TABLE IF NOT EXISTS course_m(course_id INTEGER PRIMARY KEY AUTOINCREMENT, course_name VARCHAR(30) UNIQUE, " +
                "director_id INTEGER REFERENCES registration_m(reg_id))");
    }

    // Code : Insert Data In Table
    public void InsertData()
    {
        if(txt_course.getText().toString().trim().length()==0)
        {
            msg(this.getActivity(), "Error : Please Enter Course Name!!");
            return;
        }
        else
        {
            // Initialize Selected Values of Spinners
            Fetch_SpinnerData();

            try
            {
                if(director_id!=0) {
                    db.execSQL("INSERT INTO course_m(course_name,director_id)VALUES('" + txt_course.getText() + "', " + director_id + ")");
                    msg(this.getActivity(), "Record Inserted!!");
                    clear();
                }
                else
                {
                    msg(this.getActivity(), "Error : No Director Id Found For Selected Director Name!!");

                }

            }
            catch(Exception e)
            {
                msg(this.getActivity(), "Error : Something Went Wrong While Inserting Data or Duplicate Record Found!!");
            }
        }
    }


    // Code : Update Particular Data From Table
    public void UpdateData()
    {
        if(txt_course.getText().toString().trim().length()==0)
        {
            msg(this.getActivity(), "Error : Enter Course Name For Update The Record!!");
            return;
        }

        else {
            // Initialize Selected Values of Spinners
            Fetch_SpinnerData();

            try {
                if(director_id!=0)
                {
                    Cursor record = db.rawQuery("SELECT * FROM course_m WHERE course_name like '" + course_old_value + "'", null);
                    if (record.moveToFirst())
                    {
                        db.execSQL("UPDATE course_m  SET course_name = '" + txt_course.getText().toString() + "', director_id ='" + director_id + "' " +
                                "WHERE course_name like '" + course_old_value + "'");
                        msg(this.getActivity(), "Record Updated Successfully!!");
                        clear();
                    }
                    else
                    {
                        msg(this.getActivity(), "Error : Something Went Wrong While Updating Data or Duplicate Record Found!!");
                    }
                }
                else
                {
                    msg(this.getActivity(), "Error : No Director Id Found For Selected Director Name!!");

                }
            }
            catch(Exception e)
            {
                msg(this.getActivity(), "Error : Something Went Wrong While Updating Data or Duplicate Record Found!!");
            }
        }
    }


    // Code : Delete Particular Data From Table
    public void DeleteData()
    {
        if(txt_course.getText().toString().trim().length()==0)
        {
            msg(this.getActivity(), "Error : Enter Course Name For Delete The Record!!");
            return;
        }
        Cursor record = db.rawQuery("SELECT * FROM course_m WHERE course_name like '"+ txt_course.getText()+"'", null);
        if(record.moveToFirst())
        {
            db.execSQL("DELETE FROM course_m WHERE course_name like '"+ txt_course.getText()+"'");
            msg(this.getActivity(), "Record Deleted Successfully!!");
            clear();
        }
        else
        {
            msg(this.getActivity(), "Error : Record Not Found or Invalid Course Name!!");
        }
    }


    // Code : View Particular Data From Table
    public void SearchData()
    {
        if(txt_course.getText().toString().trim().length()==0)
        {
            msg(this.getActivity(), "Error : Enter Course Name For Search The Record!!");
            return;
        }
        else {
            course_old_value = txt_course.getText().toString();
            Cursor record = db.rawQuery("SELECT * FROM course_m WHERE course_name like '" + txt_course.getText() + "'", null);
            if (record.moveToFirst()) {
                txt_course.setText(record.getString(record.getColumnIndex("course_name")));
                spinner_director.setSelection(record.getInt(record.getColumnIndex("director_id")));
            } else {
                msg(this.getActivity(), "Error : Record Not Found or Invalid Course Name!!");
            }
        }
    }


    // Fetch Id Of Spinner SelectedItem From Database
    public void Fetch_SpinnerData()
    {
        director_value = spinner_director.getSelectedItem().toString();
        Cursor record = db.rawQuery("SELECT reg_id FROM registration_m WHERE name like '"+director_value+"' AND user_role like 'Director'", null);
        record.moveToFirst();

        if(record.getCount()==0)
        {
            msg(this.getActivity(), "Error : No Director Id Found For Selected Director Name!!");
            return;
        }

        else
        {
            director_id = record.getInt(record.getColumnIndex("reg_id"));
        }
        record.close();
    }

    // For Displaying Messages on Screen
    public void msg(Context context, String str)
    {
        Toast.makeText(this.getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    // For Clearing Data From Views After CRUD operation
    public void clear()
    {
        spinner_director.setSelection(0);
        txt_course.setText("");
    }

}