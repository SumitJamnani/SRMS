package com.example.sumit.srms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ImportResults extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    String file_path = null;

    TextView file_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_results);

        Spinner spinner_course = (Spinner) findViewById(R.id.spinner_Course);
        ArrayAdapter<String> adapter_course =  new ArrayAdapter<>(ImportResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Course));
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter_course);

        Spinner spinner_semester = (Spinner) findViewById(R.id.spinner_Semester);
        ArrayAdapter<String> adapter_semester =  new ArrayAdapter<>(ImportResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Sem));
        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_semester);

        Spinner spinner_faculty = (Spinner) findViewById(R.id.spinner_Faculty);
        ArrayAdapter<String> adapter_faculty =  new ArrayAdapter<>(ImportResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Faculty));
        adapter_faculty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_faculty.setAdapter(adapter_faculty);

        Spinner spinner_exam = (Spinner) findViewById(R.id.spinner_Exam);
        ArrayAdapter<String> adapter_exam =  new ArrayAdapter<>(ImportResults.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Exam));
        adapter_exam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_exam.setAdapter(adapter_exam);

        Button upload_file = findViewById(R.id.btn_AddResult);
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(Build.VERSION.SDK_INT>=23)
                {
                    if(checkPermission())
                    {
                        filepicker();
                    }
                    else
                        {
                            requestPermission();
                        }
                }
                else {
                        filepicker(); }
            }
        }
        );

        file_name = findViewById(R.id.filename);
    }

    private void filepicker()
    {
        Toast.makeText(ImportResults.this, "File Picker Called!!", Toast.LENGTH_SHORT).show();

        // Pick File
        Intent opengallery = new Intent(Intent.ACTION_GET_CONTENT);
        opengallery.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(opengallery, REQUEST_GALLERY);


    }

    private void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ImportResults.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(ImportResults.this, "Please Give Permission To Upload File", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(ImportResults.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(ImportResults.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ImportResults.this, "Permission Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ImportResults.this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK)
        {
            String filePath  = getRealPathFromUrl(data.getData(), ImportResults.this);
            Toast.makeText(ImportResults.this, "Path : " + filePath, Toast.LENGTH_SHORT).show();

            this.file_path = filePath;
            File file = new File(filePath);
            file_name.setText(file.getName());
        }
    }

    public String getRealPathFromUrl(Uri url, Activity activity)
    {
        Cursor cursor = activity.getContentResolver().query(url, null,null,null,null);
        if(cursor==null)
        {
            return url.getPath();
        }
        else
        {
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            return  cursor.getString(id);
        }
    }
}
