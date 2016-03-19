package jaredwilson.project;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//System.out.println("");
public class Recording extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String last_saved_filename;
    public String progress;
    public int rCount;
    public int progressInSeconds;
    //stuff
    public boolean isRecording;
    public boolean isPlaying;
    public boolean canPlay;
    private MediaPlayer player = null;
    private MediaRecorder recorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);

        isRecording = false;
        isPlaying = false;
        rCount = 1;
        catchIntent();
    }

    public void catchIntent() {
        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(key);
        filename = (message.split(","))[0];
        progress = (message.split(","))[1];
        progressInSeconds = Integer.parseInt(progress);
        // check message values. IF null set appropriate flags
        /*if (message.equals("untitled,0")) {
            // there's no file, so a recording will require creating a new file.
            filename = "untitled";
            progress = "0";

        } else {
            filename = (message.split(","))[0];
            progress = (message.split(","))[1];
            progressInSeconds = Integer.parseInt(progress);
        }*/
    }

    // functions for Navigation
    public void filesTabPress(View v) {
        for(String fn : this.getFilesDir().list()) {
            Log.i("FileName",fn);
        }
        releaseResources();
        new ChangeTabs().execute("Files",(filename + "," + progress),this);
    }
    public void editTabPress(View v) {
        releaseResources();
        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }

    // RECORDING STUFF***********************************************************
    public void onRecord(View v) {
        if (isPlaying) {
            stopPlaying();
        }
        if (!isRecording) {
            startRecording();

        } else {
            endRecording();
        }
        isRecording = !isRecording;


    }

    /*
    Calendar c = Calendar.getInstance();
    int seconds = c.get(Calendar.SECOND);
     */

    private void startRecording() {
        filename = this.getFilesDir().getPath() + "/" + Calendar.getInstance().getTime().toString().replaceAll(":", "_");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (Exception e) {}

        // change the button to Stop
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
        recorder.start();
    }

    private void endRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.rec_03copy);
            last_saved_filename = filename + rCount;
            rCount++;
        }
    }

    // PLAYING STUFF***********************************************************
    public void onPlay(View v) {
        if (!isPlaying && last_saved_filename != null) {
            startPlaying();
        } else if (isPlaying && last_saved_filename != null){
            stopPlaying();
        }
        else {
            // grey out play
        }
    }



    private void startPlaying() {
            player = new MediaPlayer();
            try {
                player.setDataSource(filename + rCount);
                player.prepare();
                player.start();
                isPlaying = true;
                ((ImageButton)findViewById(R.id.playButt)).setImageResource(R.drawable.pause_white);
            } catch (Exception e) { }
    }

    private void stopPlaying() {
        player.release();
        isPlaying = false;
        player = null;
    }


    // UBIQUITOUS STUFF***********************************************************
    private void releaseResources() {
        super.onPause();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}





/*
    // functions for PlaybackModule
    public void pressPlay() {}

    public void pressFF() {}

    public void pressRW() {}
*/