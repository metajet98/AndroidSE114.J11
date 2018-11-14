package com.test.project.karaoke;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class Offline extends BaseActivity {

    ListView listViewFiles;
    BottomNavigationView bottomNavigationView;
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;



    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    //override su kien an nut back tro ve main
    @Override
    public void onBackPressed() {
        Intent intent1 =new Intent(Offline.this,MainActivity.class);
        startActivity(intent1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_offline);


        //setting bottom navigator
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_offline, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu =bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);


        //anh xa listview
        listViewFiles=(ListView) findViewById(R.id.listViewFiles);


        //request permission doc file trong bo nho
        requestForPermission();



        File files =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] listFile= files.listFiles();
        ArrayList<String> nameOfFiles= new ArrayList<String>();

        for(int i=0;i<listFile.length;i++)
        {
            String temp= listFile[i].getName();
            if(temp.contains(".mp4"))
            {
                nameOfFiles.add(listFile[i].getName());
            }

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nameOfFiles);
        listViewFiles.setAdapter(arrayAdapter);





        //Uri videoUri =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //Cursor musicCursor = musicResolver.query(videoUri, null, null, null, null);





        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.home_search:
                        Intent intent1 =new Intent(Offline.this,MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.offline:

                        break;
                    case R.id.top_trend:
                        Intent intent3 =new Intent(Offline.this,TopTrend.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });





    }
}
