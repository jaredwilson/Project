package jaredwilson.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

//System.out.println("");
public class Recording extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int progressInSeconds;
    //stuff
    public boolean isRecording;
    public boolean isPlaying;
    private MediaPlayer player = null;
    private MediaRecorder recorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        isRecording = false;
        isPlaying = false;
        catchIntent();
    }

    public void catchIntent() {
        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(key);

        // check message values. IF null set appropriate flags
        if (message.equals(",")) {
            // there's no file, so a recording will require creating a new file.
            filename = "";
            progress = "";

        } else {
            filename = (message.split(","))[0];
            progress = (message.split(","))[1];
            progressInSeconds = Integer.parseInt(progress);

            try {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setOutputFile(filename);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    recorder.prepare();
                } catch (Exception e) {}

            } catch (Exception e) {}
        }
    }

    // functions for Navigation
    public void filesTabPress(View v) {
        releaseResources();
        new ChangeTabs().execute("Files",(filename + "," + progress),this);
    }
    public void editTabPress(View v) {
        releaseResources();
        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }

    // RECORDING STUFF***********************************************************
    private void onRecord(View v) {
        if (isPlaying) {
            stopPlaying();
            isPlaying = false;
        }
        if (!isRecording) {
            startRecording();
            isRecording = true;
            // change the button to stop Recording
            ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
        } else {
            endRecording();

        }
    }


    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (Exception e) {}

        recorder.start();
    }

    private void endRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    // PLAYING STUFF***********************************************************
    private void onPlay(View v) {
        if (isRecording) {
            endRecording();
        }
        if (!isPlaying) {
            isPlaying = true;
            startPlaying();
            // change the button to pause
        } else {
            stopPlaying();
        }
    }

    public void onPlayPause(View v) {

    }

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

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(filename);
            player.prepare();
            player.start();
        } catch (Exception e) {}
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    // UBIQUITOUS STUFF***********************************************************

}





/*
    // functions for PlaybackModule
    public void pressPlay() {}

    public void pressFF() {}

    public void pressRW() {}
*/