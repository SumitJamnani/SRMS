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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ImportUser extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;

    String file_path = null;
    TextView file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_user);

        //Side Drawer Code
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.admin_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //End Side Drawer Code

        //File Upload Code
        Button upload_file = findViewById(R.id.btn_AddUser);
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
                else {
                        requestPermission();
                }
            }
            else {
                filepicker();
            }
        }}
        );

        file_name = findViewById(R.id.filename);
        //File Upload Code End

    }

    //Side drawer open close related method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //End Side drawer open close related method


    //File Upload Code Related method
    private void filepicker()
    {
        Toast.makeText(ImportUser.this, "File Picker Called!!", Toast.LENGTH_SHORT).show();

        // Pick File
        Intent opengallery = new Intent(Intent.ACTION_GET_CONTENT);
        opengallery.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(opengallery, REQUEST_GALLERY);


    }

    private void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ImportUser.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(ImportUser.this, "Please Give Permission To Upload File", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(ImportUser.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(ImportUser.this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
                    Toast.makeText(ImportUser.this, "Permission Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ImportUser.this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK)
        {
            String filePath  = getRealPathFromUrl(data.getData(), ImportUser.this);
            Toast.makeText(ImportUser.this, "Path : " + filePath, Toast.LENGTH_SHORT).show();

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
    //File Upload Related Methods End
}
