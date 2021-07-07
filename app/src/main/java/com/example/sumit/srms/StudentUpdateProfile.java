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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class StudentUpdateProfile extends Fragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;

    String file_path = null;
    TextView file_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_student_update_profile, container,false);

        // File Picker Code
        Button upload_file = view.findViewById(R.id.btn_img_file);
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

                                               else
                                               {
                                                   filepicker();
                                               }
                                           }
                                       }
        );

        file_name = view.findViewById(R.id.filename);
        // File Picker Code End
        return view;
    }

    // File Picker RElated Methods
    private void filepicker()
    {
        // Pick File
        Intent opengallery = new Intent(Intent.ACTION_PICK);
        opengallery.setType("image/*");
        startActivityForResult(opengallery, REQUEST_GALLERY);


    }

    private void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(this.getActivity(), "Please Give Permission To Upload File", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
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
                    Toast.makeText(this.getActivity(), "Permission Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this.getActivity(), "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK)
        {
            String filePath  = getRealPathFromUrl(data.getData(), this.getActivity());
            Toast.makeText(this.getActivity(), "Path : " + filePath, Toast.LENGTH_SHORT).show();

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
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
            return  cursor.getString(id);
        }
    }
    // File Picker Related Methods End
}

