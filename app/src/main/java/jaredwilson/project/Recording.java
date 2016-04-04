package jaredwilson.project;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

public class Recording extends AppCompatActivity  {
    public String filename;
    public int recPresetHrs;
    public int recPresetMins;
    public int recPresetSecs;
    public boolean pomo;
    public boolean isRecording;
    public boolean recTimeSet;
    public int recTime;
    public MediaRecorder recorder;
    //public List<String> sessionRecordings;
    public Stack<String> sessionRecordings;

    // Get rid of...
    //private final PlayActions player = PlayActions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        filename = "dummyString";
        //sessionRecordings = new ArrayList<String>();
        sessionRecordings = new Stack<String>();
        //sessionRecordings.add(filename);
        sessionRecordings.push(filename);
        isRecording = false;
        recTimeSet = false;
        recTime = 0;
        recorder = null;
        pomo = true;
        black_outStatusBar();
        resetSpinners();
    }

    private void resetSpinners() {
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
        new ChangeTabs().execute("Files", sessionRecordings.lastElement(), this);
    }
    public void editTabPress(View v) {

        new ChangeTabs().execute("Editing", ("dummyString"), this);
    }

    // RECORDING STUFF***********************************************************
    //public MediaPlayer mediaPlayer; // for testing

    public void onRecord(View v) {
        if (!isRecording) {
            isRecording = !isRecording; // redundant, but necessary
            startRecording();
            // for testing...
            /*try {
                mediaPlayer = MediaPlayer.create(this, R.raw.jammin);
                new TimerTask().execute(this);
                mediaPlayer.start();
                ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
            } catch (Exception e) {}*/

        } else {
            isRecording = !isRecording;
            endRecording();
            // for testing...
            /*mediaPlayer.release();
            mediaPlayer = null;
            ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.rec_03copy);
            setTimer("00:00:00");*/
        }
    }
    private void generateFilename() {
        filename = this.getFilesDir().getPath() + "/" + Calendar.getInstance().getTime().toString().replaceAll(":", "_");
        //sessionRecordings.add(filename);
        sessionRecordings.push(filename);
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
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.recstop_00);
        new TimerTask().execute(this);
        recorder.start();
    }

    public void endRecording() {
        if (recorder == null) {return;}
        recorder.stop();
        recorder.release();
        recorder = null;
        ((ImageButton)findViewById(R.id.recButt)).setImageResource(R.drawable.rec_03copy);
        recTimeSet = false;
        recTime = 0;
        setTimer("00:00:00");
    }

    public void setTimer(String progress) {
        TextView timeLeft = (TextView) findViewById(R.id.recordingCount);
        timeLeft.setText(progress);
    }

    // TOOL BAR STUFF***********************************************************

    private void resetToolBar(String id, boolean show) {
        ((LinearLayout) findViewById(R.id.timerBar)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.saveBar)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.deleteBar)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.pomoBar)).setVisibility(View.GONE);

        switch (id) {
            case "Timer":
                if (show)
                    ((LinearLayout) findViewById(R.id.timerBar)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout) findViewById(R.id.timerBar)).setVisibility(View.GONE);
                break;
            case "Save":
                if (show)
                    ((LinearLayout) findViewById(R.id.saveBar)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout) findViewById(R.id.saveBar)).setVisibility(View.GONE);
                break;
            case "Delete":
                if (show)
                    ((LinearLayout) findViewById(R.id.deleteBar)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout) findViewById(R.id.deleteBar)).setVisibility(View.GONE);
                break;
            case "Pocket":
                if (show)
                    ((LinearLayout) findViewById(R.id.pomoBar)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout) findViewById(R.id.pomoBar)).setVisibility(View.GONE);
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
            int diff_H = tempTime / (60*60);
            int diff_M = (tempTime - (diff_H*60*60)) / 60;
            int diff_S = (tempTime - ((diff_H*60*60)+(diff_M*60)));

            String h = (diff_H < 10)? "0"+diff_H+":":diff_H+":";
            String m = (diff_M < 10)? "0"+diff_M+":":diff_M+":";
            String s = (diff_S < 10)? "0"+diff_S+"":diff_S+"";

            setTimer(h+m+s);

            resetToolBar("Timer",
                    !(((LinearLayout) findViewById(R.id.timerBar)).getVisibility() == View.VISIBLE));
        }
        else {
            recTime = 0;
            recTimeSet = false;
        }

    }

    public void onClick_saveRec (View v) {
        TextView field = (TextView) findViewById(R.id.file_to_save);
        if((sessionRecordings.lastElement()).equals("dummyString")) {
            field.setText("<empty>");
        } else {
            field.setText(sessionRecordings.lastElement());
        }
        resetToolBar("Save",
                !(((LinearLayout) findViewById(R.id.saveBar)).getVisibility() == View.VISIBLE));

    }
    public void onClick_save(View v) {
        if((sessionRecordings.lastElement()).equals("dummyString")) {
            return;
        }
        Toast.makeText(getApplicationContext(), "recording saved", Toast.LENGTH_SHORT).show();
        resetToolBar("Save",
                !(((LinearLayout) findViewById(R.id.saveBar)).getVisibility() == View.VISIBLE));
    }

    public void onClick_delRec (View v) {
        TextView field = (TextView) findViewById(R.id.file_to_delete);
        if((sessionRecordings.lastElement()).equals("dummyString")) {
            field.setText("<empty>");
        } else {
            field.setText(sessionRecordings.lastElement());
        }
        resetToolBar("Delete",
                !(((LinearLayout) findViewById(R.id.deleteBar)).getVisibility() == View.VISIBLE));

    }
    public void onClick_delete(View v){
        //if(sessionRecordings.get(sessionRecordings.indexOf(filename)).equals("dummyString")) {return;}
        TextView field = (TextView) findViewById(R.id.file_to_delete);
        if((sessionRecordings.lastElement()).equals("dummyString")) {
            return;
        }
        Toast.makeText(getApplicationContext(), "recording \'"+sessionRecordings.lastElement()+"\' deleted." ,
                Toast.LENGTH_SHORT).show();
        (new File(sessionRecordings.pop())).delete();
        resetToolBar("Delete",
                !(((LinearLayout) findViewById(R.id.deleteBar)).getVisibility() == View.VISIBLE));
        //field.setText(sessionRecordings.lastElement());

    }

    // this should actually work now...
    public void onClick_pocketMode (View v) {
        resetToolBar("Pocket",
                !(((LinearLayout) findViewById(R.id.pomoBar)).getVisibility() == View.VISIBLE));

    }
    public void onClick_pomo (View v) {
        pomo = !pomo;
        if (pomo){
            ((ImageButton)findViewById(R.id.pocketMode)).setImageResource(R.drawable.pocket_large_phone);
        } else {
            ((ImageButton)findViewById(R.id.pocketMode)).setImageResource(R.drawable.pocket_large);
        }
        TextView field = (TextView) findViewById(R.id.pomoMessage);
        field.setText("Pocket Mode is " + ((pomo)? "ON":"OFF") + "\n(you may "+ ((pomo)? "":"NOT ") +"record\n  with your screen off.)");

    }
    public boolean enforcePocketMode() {
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn(); //don't hate...
        return !(pomo && isScreenOn);
    }

    public void toggleToolbar(View v) {
        ImageButton a = (ImageButton)findViewById(R.id.emphasisBar1Arrow);
        if (((LinearLayout) findViewById(R.id.toolbarContainer)).getVisibility() == View.GONE) {
            ((ImageButton)a).setImageResource(R.drawable.chev_pink_up);
            ((LinearLayout) findViewById(R.id.toolbarContainer)).setVisibility(View.VISIBLE);
        }
        else {
            ((ImageButton)a).setImageResource(R.drawable.chev_pink_down);
            ((LinearLayout) findViewById(R.id.toolbarContainer)).setVisibility(View.GONE);
        }


    }


    // PLAYING STUFF***********************************************************
/*
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
    */
}

































