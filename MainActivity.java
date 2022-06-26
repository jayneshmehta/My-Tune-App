package com.jaynesh.mytune;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

       ListView listView;
       String[] items;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            listView = findViewById(R.id.listview);

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
                Dexter.withContext(this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                ArrayList<File> mysongs = findSong(Environment.getExternalStorageDirectory());
                                items = new String[mysongs.size()];
                                for(int i=0; i<mysongs.size();i++){
                                items[i] = mysongs.get(i).getName().replace(".mp3" ,"");
                                }
                                ArrayAdapter<String> myadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                                listView.setAdapter(myadapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> Parent, View view, int position, long id) {
                                    Intent intent = new Intent(MainActivity.this,playSong.class);
                                    String currentSong = listView.getItemAtPosition(position).toString();
                                    intent.putExtra("songList",mysongs);
                                    intent.putExtra("currentSong",currentSong);
                                    intent.putExtra("position",position);
                                    startActivity(intent);
                                    }
                                });
                            }
                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        })
                        .check();
            }
            public  ArrayList<File> findSong (File file){
                 ArrayList <File> arrayList = new ArrayList<>();
                 File[] files = file.listFiles();
                 if(files != null){
                     for (File singlefile : files) {
                         if (singlefile.isDirectory()) {
                            arrayList.addAll(findSong(singlefile));
                           } else {
                             if (singlefile.getName().endsWith(".mp3") && !singlefile.getName().startsWith(".")) {
                                arrayList.add(singlefile);
                             }
                           }
                        }
                    }return arrayList;
        }
    }