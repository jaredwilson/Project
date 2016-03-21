package jaredwilson.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
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
    private MediaRecorder recorder = null;
    private final PlayActions player = PlayActions.getInstance();
    private final LivePlayActions livePlayer = LivePlayActions.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        isRecording = false;
        rCount = 1;
        catchIntent();
        black_outStatusBar();
    }
    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
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

        new ChangeTabs().execute("Files", (filename + "," + progress), this);
    }
    public void editTabPress(View v) {

        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }

    // RECORDING STUFF***********************************************************
    public void onRecord(View v) {
        if (!isRecording) {
            startRecording();

        } else {
            endRecording();
        }
        isRecording = !isRecording;


    }

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

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter new name");

            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rename(input.getText().toString());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    private void rename(String newFilename) {
        // rename file to new path
        (new File(filename)).renameTo(new File(this.getFilesDir().getPath() + "/" + newFilename));
    }

    // PLAYING STUFF***********************************************************
    public void playActions(View view) {
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.play_actions(view);
    }
    public void livePlayActions(View view) {
        if(!player.getIsPlaying()) {
            player.stop();
        }
        livePlayer.play_actions(view);
    }

    public void seekFwd(View view) {
        // indicate some kind of visual emphasis
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.seekFwd();
    }

    public void seekBck(View view) {
        // indicate some kind of visual emphasis
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.seekBck();
    }


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

}

