package jaredwilson.project;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.security.Provider;

public class Editing extends AppCompatActivity {
    public final static String key = "key";
    public String filename;
    public String progress;

    public int progressInSeconds;
    private final PlayActions player = PlayActions.getInstance();
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_layout);
        black_outStatusBar();
        isRecording = false;

    }

    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
    }

    public void filesTabPress(View v) {
        if(isRecording) { stopService(new Intent(this, SomeService.class));}
        new ChangeTabs().execute("Files", ("dummyString" + "," + 0), this);
    }

    public void recordingTabPress(View v) {
        if(isRecording) { stopService(new Intent(this, SomeService.class));}
        new ChangeTabs().execute("Recording", ("dummyString" + "," + 0), this);
    }

    public void checkLive(View view) {

        if (isRecording) {
             isRecording = false;
            ((ImageButton)findViewById(R.id.liveFeedImage)).setImageResource(R.drawable.play_white);
            stopService(new Intent(this, SomeService.class));
        } else {
            isRecording = true;
            ((ImageButton)findViewById(R.id.liveFeedImage)).setImageResource(R.drawable.pause_white);
            startService(new Intent(this, SomeService.class));
            //startLive();
        }
    }
}

