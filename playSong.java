package com.jaynesh.mytune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playSong extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
        }

    TextView textView;
ImageView play,previous,next;
ArrayList<File> songs;
MediaPlayer mediaPlayer;
String textContent;
Thread updateseek = new Thread();
SeekBar seekbar;
int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
    play = findViewById(R.id.pause);
    textView = findViewById(R.id.textView);
    previous = findViewById(R.id.previous);
    next = findViewById(R.id.next);
    seekbar = findViewById(R.id.seekBar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekbar.setMax(mediaPlayer.getDuration());

    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
        }
    });
     updateseek = new Thread(){
         @Override
         public void run(){
             int currentPosition = 0;
             position = intent.getIntExtra("position",0);
             try{
                 while (currentPosition <= mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                        sleep(800);
                 }
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     };
     updateseek.start();
    play.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.play);
                mediaPlayer.pause();
            }else{
                play.setImageResource(R.drawable.pause);
                mediaPlayer.start();
            }
        }
    });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position = position-1;
                }else{
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekbar.setMax(mediaPlayer.getDuration());
                seekbar.setProgress(0);
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!= songs.size()-1){
                    position = position+1;
                }else{
                    position =  0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekbar.setMax(mediaPlayer.getDuration());
                seekbar.setProgress(0);
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
    }
}