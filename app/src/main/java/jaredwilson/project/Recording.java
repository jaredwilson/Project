package jaredwilson.project;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.Calendar;

public class Recording extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int recPresetHrs;
    public int recPresetMins;
    public int recPresetSecs;
    public String pomoStatus = "ON";
    public boolean isRecording;
    private MediaRecorder recorder = null;
    CountDownTimer timer;
    private final PlayActions player = PlayActions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        isRecording = false;

        catchIntent();
        black_outStatusBar();
        setupSpinners();
        generateFilename();
        //findViewById(R.id.sliderBar).setVisibility(View.GONE);
        //findViewById(R.id.playBackButtons).setVisibility(View.GONE);
    }

    private void setupSpinners () {
        recPresetHrs = 0;
        recPresetMins = 0;
        recPresetSecs = 0;

        NumberPicker hrs = (NumberPicker) findViewById(R.id.hrsSlide);
        NumberPicker mins = (NumberPicker) findViewById(R.id.minSlide);
        NumberPicker secs = (NumberPicker) findViewById(R.id.secSlide);

        mins.setMinValue(0);
        mins.setMinValue(0);
        secs.setMinValue(0);

        // 9 hour max recording
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
        // Catch intent from sending Activity
        Intent intent = getIntent();
        /* Record is an adult. Record doesn't need anything from anyone. */
    }
    // functions for Navigation
    public void filesTabPress(View v) {
        for(String fn : this.getFilesDir().list()) {
            Log.i("FileName",fn);
        }
        new ChangeTabs().execute("Files", (filename + "," + 0), this);
    }
    public void editTabPress(View v) {

        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }

    // RECORDING STUFF***********************************************************
    public void onRecord(View v) {
        if (!isRecording) {
            isRecording = !isRecording; // redundant, but necessary
            startRecording();
        } else {
            isRecording = !isRecording;
            endRecording();
        }
    }
    private void generateFilename() {
        filename = this.getFilesDir().getPath() + "/" + Calendar.getInstance().getTime().toString().replaceAll(":", "_");
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (recTimeSet) {
            int recTime = 1000 * (recPresetHrs * 60 * 60 + recPresetMins * 60 + recPresetSecs);
            recorder.setMaxDuration(recTime);
            timer = new CountDownTimer(recTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (enforcePocketMode()) {onFinish();}
                    Calendar time = Calendar.getInstance();
                    time.setTimeInMillis(millisUntilFinished);
                    int h = time.get(Calendar.HOUR_OF_DAY);
                    int m = time.get(Calendar.MINUTE);
                    int s = time.get(Calendar.SECOND);
                    String mins0 = "";
                    String secs0 = "";
                    if (s < 10) {
                        secs0 = "0";
                    }
                    if (m < 10) {
                        mins0 = "0";
                    }
                    setTimer("0" + h + ":" + mins0 + m + ":" + secs0 + s);
                }
                @Override
                public void onFinish() {
                    // make sure time remaining = 00:00:00
                    setTimer("00:00:00");
                    endRecording();
                }
            }.start();
        } else {
            startRecTime = System.currentTimeMillis();
            new TimerAsyncTask().execute(this);
        }
        try {
            recorder.prepare();
        } catch (Exception e) {}
        // change the button to Stop
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
        recorder.start();
        //findViewById(R.id.playBackButtons).setVisibility(View.GONE);
    }

    private void endRecording() {
        if (recorder == null) {return;}
        recorder.stop();
        recorder.release();
        recorder = null;
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.rec_03copy);
        //findViewById(R.id.playBackButtons).setVisibility(View.VISIBLE);
        recTimeSet = false;
        startRecTime = 0;
    }



    public long startRecTime;
    public void setTimer(String progress) {
        TextView timeLeft = (TextView)findViewById(R.id.recordingCount);
        timeLeft.setText(progress);
    }

    public boolean enforcePocketMode() {
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn(); //don't hate...
        return (!pomo && isScreenOn);
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

    private void resetToolBar(String id, boolean show) {
        ((RelativeLayout) findViewById(R.id.sliderBar)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.saveBar)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.deleteBar)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.pomoBar)).setVisibility(View.GONE);



        switch (id) {
            case "Timer":
                if (show)
                    ((RelativeLayout) findViewById(R.id.sliderBar)).setVisibility(View.VISIBLE);
                else
                    ((RelativeLayout) findViewById(R.id.sliderBar)).setVisibility(View.GONE);
                break;
            case "Save":
                if (show)
                    ((RelativeLayout) findViewById(R.id.saveBar)).setVisibility(View.VISIBLE);
                else
                    ((RelativeLayout) findViewById(R.id.saveBar)).setVisibility(View.GONE);
                break;
            case "Delete":
                if (show)
                    ((RelativeLayout) findViewById(R.id.deleteBar)).setVisibility(View.VISIBLE);
                else
                    ((RelativeLayout) findViewById(R.id.deleteBar)).setVisibility(View.GONE);
                break;
            case "Pocket":
                if (show)
                    ((RelativeLayout) findViewById(R.id.pomoBar)).setVisibility(View.VISIBLE);
                else
                    ((RelativeLayout) findViewById(R.id.pomoBar)).setVisibility(View.GONE);
                break;
            default:
                // do nothin...
        }


        if (sliderBarOpen) {
            View dummy = null;
            onClick_setRecTime(dummy);
        }

    }

    public boolean sliderBarOpen = false;
    public void onClick_setRecTime (View v) {
        resetToolBar("Timer",
                !(((RelativeLayout) findViewById(R.id.sliderBar)).getVisibility() == View.VISIBLE));
    }

    public boolean recTimeSet = false;
    public void setMaxRec(View v) {
        recTimeSet = true;
        resetToolBar("Timer",
                !(((RelativeLayout) findViewById(R.id.sliderBar)).getVisibility() == View.VISIBLE));
    }

    public void onClick_saveRec (View v) {
        resetToolBar("Save",
                !(((RelativeLayout) findViewById(R.id.saveBar)).getVisibility() == View.VISIBLE));
        /*if ((new File(filename)).exists()) {
            Toast.makeText(getApplicationContext(), "recording saved", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void onClick_delRec (View v) {
        resetToolBar("Delete",
                !(((RelativeLayout) findViewById(R.id.deleteBar)).getVisibility() == View.VISIBLE));
        /*if ((new File(filename)).delete()) {
            Toast.makeText(getApplicationContext(), "recording deleted", Toast.LENGTH_SHORT).show();
        }*/
    }

    // this should actually work now...
    public boolean pomo = true;
    public void onClick_pocketMode (View v) {
        resetToolBar("Pocket",
                !(((RelativeLayout) findViewById(R.id.pomoBar)).getVisibility() == View.VISIBLE));

        /*resetToolBar("Pocket", true);
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
        */
    }

}

































