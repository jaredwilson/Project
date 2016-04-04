package jaredwilson.project;

import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.Calendar;

public class Recording extends AppCompatActivity  {
    public String filename;
    public int recPresetHrs;
    public int recPresetMins;
    public int recPresetSecs;
    public boolean isRecording;
    public MediaRecorder recorder = null;

    // Get rid of...
    CountDownTimer timer;
    private final PlayActions player = PlayActions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        filename = "dummyString";
        isRecording = false;
        black_outStatusBar();
        setupSpinners();
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
    // functions for Navigation
    public void filesTabPress(View v) {
        for(String fn : this.getFilesDir().list()) {
            Log.i("FileName",fn);
        }
        new ChangeTabs().execute("Files", filename, this);
    }
    public void editTabPress(View v) {

        new ChangeTabs().execute("Editing", ("dummyString"), this);
    }

    // RECORDING STUFF***********************************************************
    public MediaPlayer mediaPlayer; // for testing

    public void onRecord(View v) {
        if (!isRecording) {
            isRecording = !isRecording; // redundant, but necessary
            //startRecording();
            mediaPlayer = MediaPlayer.create(this, R.raw.jammin);
            new TimerTask().execute(this);
            mediaPlayer.start();
        } else {
            isRecording = !isRecording;
            //endRecording();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void generateFilename() {
        filename = this.getFilesDir().getPath() + "/" + Calendar.getInstance().getTime().toString().replaceAll(":", "_");
    }
    private void startRecording() {
        generateFilename();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (recTimeSet)
            recorder.setMaxDuration(recTime*1000); // millisecs
        try {
            recorder.prepare();
        } catch (Exception e) {}
        // change the button to Stop
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
        new TimerTask().execute(this);
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
        recTime = 0;
        startRecTime = 0;
    }

    public long startRecTime;
    public void setTimer(String progress) {
        TextView timeLeft = (TextView)findViewById(R.id.recordingCount);
        timeLeft.setText(progress);
    }

    // TIMER STUFF***********************************************************
    public String getMaxRecTime() {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(recTime);
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
        return ("0" + h + ":" + mins0 + m + ":" + secs0 + s);
    }

    public boolean recTimeSet = false;

    public int recTime = 0;

    public void setMaxRec(View v) {
        recTime = 1000 * (recPresetHrs * 60 * 60 + recPresetMins * 60 + recPresetSecs);
        recTimeSet = true;
        setTimer(getMaxRecTime());
        resetToolBar("Timer",
                !(((LinearLayout) findViewById(R.id.timerBar)).getVisibility() == View.VISIBLE));
    }



    // TOOL BAR STUFF***********************************************************

    private void resetToolBar(String id, boolean show) {
        ((LinearLayout) findViewById(R.id.timerBar)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.saveBar)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.deleteBar)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.pomoBar)).setVisibility(View.GONE);



        switch (id) {
            case "Timer":
                if (show)
                    ((LinearLayout) findViewById(R.id.timerBar)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout) findViewById(R.id.timerBar)).setVisibility(View.GONE);
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

    }

    public void onClick_hourGlass(View v) {
        resetToolBar("Timer",
                !(((LinearLayout) findViewById(R.id.timerBar)).getVisibility() == View.VISIBLE));
    }
    public void onClick_setRecTime(View v) {
        int tempTime = (recPresetHrs * 60 * 60 + recPresetMins * 60 + recPresetSecs);
        if (tempTime > 1) {
            recTime = tempTime;
            recTimeSet = true;
        }
        else {
            recTime = 0;
            recTimeSet = false;
        }

        resetToolBar("Timer",
                !(((LinearLayout) findViewById(R.id.timerBar)).getVisibility() == View.VISIBLE));
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

        pomo = !pomo;
        Toast.makeText(getApplicationContext(), "Pocket Mode is " + ((pomo)? "ON":"OFF") + "\nPocket Mode lets you record with the screen off." ,
                Toast.LENGTH_LONG).show();

    }
    public boolean enforcePocketMode() {
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn(); //don't hate...
        return (!pomo && isScreenOn);
    }

    public void toggleToolbar(View v)
    {
        if (((LinearLayout) findViewById(R.id.toolbarContainer)).getVisibility() == View.GONE) {
            ((ImageButton)v).setImageResource(R.drawable.chev_pink_up);
            ((LinearLayout) findViewById(R.id.toolbarContainer)).setVisibility(View.VISIBLE);
        }
        else {
            ((ImageButton)v).setImageResource(R.drawable.chev_pink_down);
            ((LinearLayout) findViewById(R.id.toolbarContainer)).setVisibility(View.GONE);
        }


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
}

































