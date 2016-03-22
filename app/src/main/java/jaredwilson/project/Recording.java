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
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//System.out.println("");
public class Recording extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int progressInSeconds;
    public int recPresetHrs = 0;
    public int recPresetMins = 0;
    public int recPresetSecs = 0;
    public String pomoStatus = "ON";
    //stuff
    public boolean isRecording;
    private MediaRecorder recorder = null;
    CountDownTimer timer;
    private final PlayActions player = PlayActions.getInstance();
    private final LivePlayActions livePlayer = LivePlayActions.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        isRecording = false;

        catchIntent();
        black_outStatusBar();
        setupSpinners();
        findViewById(R.id.sliderBar).setVisibility(View.GONE);
        //findViewById(R.id.playBackButtons).setVisibility(View.GONE);
    }

    private void setupSpinners () {
        NumberPicker hrs = (NumberPicker) findViewById(R.id.hrsSlide);
        NumberPicker mins = (NumberPicker) findViewById(R.id.minSlide);
        NumberPicker secs = (NumberPicker) findViewById(R.id.secSlide);

        mins.setMinValue(0);
        mins.setMinValue(0);
        secs.setMinValue(0);

        hrs.setMaxValue(8);
        mins.setMaxValue(59);
        secs.setMaxValue(59);


        hrs.setWrapSelectorWheel(true);
        mins.setWrapSelectorWheel(true);
        secs.setWrapSelectorWheel(true);

        hrs.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                recPresetHrs = newVal;
            }
        });

        mins.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                recPresetMins = newVal;
            }
        });

        secs.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                recPresetSecs = newVal;
            }
        });
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
/*
    private void rename(String newFilename) {
        // rename file to new path
        (new File(filename)).renameTo(new File(this.getFilesDir().getPath() + "/" + newFilename));
    }
*/
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
        if (recTimeSet) {
            int time = 1000 * (recPresetHrs * 60 * 60 + recPresetMins * 60 + recPresetSecs);
            recorder.setMaxDuration(time);
            timer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // tic tic tic
                    long t = millisUntilFinished / 1000;
                    long h = t / (60 * 60);
                    t -= (h * 60 * 60);
                    long m = t / 60;
                    t -= (m * 60);
                    long s = t;

                    TextView timeLeft = (TextView)findViewById(R.id.recordingCount);
                    timeLeft.setText((int)h+":"+(int)m+":"+(int)s);
                }

                @Override
                public void onFinish() {

                    endRecording();
                }
            }.start();
        }
        try {
            recorder.prepare();
        } catch (Exception e) {}

        // change the button to Stop
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
        recorder.start();
        findViewById(R.id.playBackButtons).setVisibility(View.GONE);
    }

    private void endRecording() {
        if (recorder == null) {return;}
        recorder.stop();
        recorder.release();
        recorder = null;
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.rec_03copy);
        findViewById(R.id.playBackButtons).setVisibility(View.VISIBLE);
        recTimeSet = false;



/*
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
            */
    }



    // PLAYING STUFF***********************************************************
    public void playActions(View view) {
        if (!(new File(filename)).exists()) {
            System.out.println("no file to Play");
            return;
        }
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.play_actions(view);
    }

    public void seekFwd(View view) {
        if (!(new File(filename)).exists()) {
            System.out.println("no file to FFwd");
            return;
        }
        // indicate some kind of visual emphasis
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.seekFwd();
    }

    public void seekBck(View view) {
        if (!(new File(filename)).exists()) {
            System.out.println("No file to Rwnd");
            return;
        }
        // indicate some kind of visual emphasis
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.seekBck();
    }

    // TOOL BAR STUFF***********************************************************

    private void resetToolBar() {
        if (sliderBarOpen) {
            View dummy = null;
            onClick_timer(dummy);
        }

    }

    public boolean sliderBarOpen = false;
    public void onClick_timer (View v) {
        if (sliderBarOpen) {
            ((RelativeLayout) findViewById(R.id.sliderBar)).setVisibility(View.GONE);
        } else {
            ((RelativeLayout) findViewById(R.id.sliderBar)).setVisibility(View.VISIBLE);
        }
        sliderBarOpen = !sliderBarOpen;
    }

    public boolean recTimeSet = false;
    public void setMaxRec(View v) {
        recTimeSet = true;
        resetToolBar();
    }

    public void onClick_saveRec (View v) {
        resetToolBar();
        if ((new File(filename)).exists()) {
            Toast.makeText(getApplicationContext(), "recording saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_delRec (View v) {
        resetToolBar();
        if ((new File(filename)).delete()) {
            Toast.makeText(getApplicationContext(), "recording deleted", Toast.LENGTH_SHORT).show();
        }
    }

    // this doesn't actually work...
    public boolean pomo = true;
    public void onClick_pocketMode (View v) {
        resetToolBar();
        if (!pomo) {
            pomoStatus = "ON";
            ((ImageButton)v).setImageResource(R.drawable.pocket_large_phone);
        } else {
            pomoStatus = "OFF";
            ((ImageButton)v).setImageResource(R.drawable.pocket_large);
        }
        pomo = !pomo;
        Toast.makeText(getApplicationContext(), "Pocket Mode is " + pomoStatus + "\nPocket Mode lets you record with the screen off." ,
                Toast.LENGTH_LONG).show();

    }

}
































/*
    // Experimental stuff
    static final int bufferSize = 20000;
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
