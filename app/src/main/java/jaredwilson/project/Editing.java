package jaredwilson.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class Editing extends AppCompatActivity {
    public final static String key = "key";
    public String filename;
    public String progress;

    public int progressInSeconds;
    private final PlayActions player = PlayActions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_layout);
        black_outStatusBar();

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
                // prepare the audio file for playing
                // Q: How're we going to handle recording here? Record over the file? Insert recording? Decisions...

            } catch (Exception e) {}


        }
    }

    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
    }

    // functions for PlaybackModule
    public void pressPlay(View view) {
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.play_actions(view);
    }

    public void pressFF() {}

    public void pressRW() {}


    // functions for Navigation
    public void editTabPress(View v) {/* we're there already, so do nothing */}

    public void filesTabPress(View v) {
        new ChangeTabs().execute("Files", (filename + "," + progress), this);
    }

    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording", (filename + "," + progress), this);
    }

    public void finish() {super.finish();}
}

/*
    // Experimental stuff
    public void run(View view) {
        isRecording = true;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        int buffersize = AudioRecord.getMinBufferSize(11025, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 11025, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize);
        AudioTrack atrack = new AudioTrack(AudioManager.USE_DEFAULT_STREAM_TYPE, 11025, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STREAM);
        atrack.setPlaybackRate(11025);
        byte[] buffer = new byte[buffersize];
        arec.startRecording();
        atrack.play();
        while(isRecording) {
            arec.read(buffer, 0, buffersize);
            atrack.write(buffer, 0, buffer.length);
        }
    }


    public void livePlayActions(View view) {
        if(!player.getIsPlaying()) {
            player.stop();
        }
        livePlayer.play_actions(view);
    }
*/