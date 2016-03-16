package jaredwilson.project;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Main activity of the app
 * @author Andy
 * @author Jared
 * @author Nathan
 */
public class MainActivity extends AppCompatActivity {

    MediaRecorder mRecorder;
    MediaPlayer mPlayer;
    MediaPlayer jamsesh;
    List<String> listOfFileNames;
    ArrayAdapter<String> fileAdapter;
    List<String> testList;
    ArrayAdapter <String> testString;
    public boolean playpress;
    public boolean isRecording;
    public boolean isPlaying;
    int counter;


    @Override
    /**
     * onCreate sets up the frame work for the app.
     * Also is like constructor, instantiating variables
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;
        playpress = true;
        isRecording = false;
        jamsesh = MediaPlayer.create(this, R.raw.jammin);
        listOfFileNames = new ArrayList<>();
        if(this.getFilesDir().length() != 0){
            listOfFileNames.addAll(Arrays.asList(this.getFilesDir().list())); }

        fileAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, listOfFileNames);

        ListView lv = (ListView)findViewById(R.id.listViewSongs);

        lv.setAdapter(fileAdapter);
        Log.i("OnCreate", "This has been a successful onCreate!");


        try {
            jamsesh.prepare();
        } catch (Exception e) {
            Log.e("SongError", "The jamsesh.prepare function failed!");
            e.printStackTrace();}
    }


    /**
     * getListFiles is incomplete file grabber
     * @param parentDir should give the directory for files
     * @return list of files
     */
    private List<String> getListFiles(File parentDir) {
        ArrayList<String> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(Arrays.asList(this.getFilesDir().list()));
            } else {
               // if(true){//file.getName().endsWith(".mp3")){
                    inFiles.add(file.getName());
               // }
            }
        }
        return inFiles;
    }

    /**
     * stopIt function stops the media player and changes the button
     * @param v View which provides context
     */
    public void stopIt (View v) {
        jamsesh.stop();
        playpress = true;
        // use the method onPreparedListener to check if file is prepared.
        jamsesh.prepareAsync();
        Button button = (Button) findViewById(R.id.play);
        button.setText("play");

        fileAdapter.notifyDataSetChanged();
    }

    /**
     * do_things plays and pauses the jamsesh
     * @param vee view from which context is gathered
     */
    public void do_things(View vee) {
        Button just_pressed = (Button)vee;
        /*switch (just_pressed.getId()) {
            //play/pause is pressed
            case R.id.play: */
        if (playpress) {
            jamsesh.start();
            just_pressed.setText("pause");
            playpress = false;
        }
        else {
            jamsesh.pause();
            just_pressed.setText("play");
            playpress = true;
        }
                /*break;

            //stop is pressed
            case R.id.stop:
                    jamsesh.stop();
                    playpress = true;
                break;
        }*/
    }

    /**
     * record_actions will start the media recorder and change the button
     * @param view provides the context
     */
    public void record_actions(View view) {
        Button mRecorder = (Button)view;

        EditText name = (EditText)findViewById(R.id.fileNamer);
        if(!isRecording) {
            startRecording();
            Log.i("Record_Actions", "File STARTed recording under the name \"" + name.getText().toString() + "\".");
            mRecorder.setText("Stop Recording");
        } else {
            stopRecording();
            Log.i("Record_Actions", "File STOPPed recording under the name \"" + name.getText().toString() + "\".");
            mRecorder.setText("Start Recording");
        }
        isRecording = !isRecording;
    }

    /**
     * startRecording will start the recording, saving at a certain place
     */
    private void startRecording() {
        EditText edt = (EditText)findViewById(R.id.fileNamer);
        String filePath = this.getFilesDir().getPath() + edt.getText().toString();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(filePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) { e.printStackTrace(); }

        mRecorder.start();
    }

    /**
     * stopRecording with stop the recorder and release it
     */
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    /**
     * play_actions with change the button to start/stop, and call the proper function
     * @param view
     */
    public void play_actions(View view) {
        Button mPlayer = (Button)view;

        if(!isPlaying) {
            startPlaying();
            mPlayer.setText("Stop Playing");
        } else {
            stopPlaying();
            mPlayer.setText("Start Playing");
        }
        isPlaying = !isPlaying;
    }

    /**
     * startPlaying() will start the mediaPlayer, catching errors
     */
    private void startPlaying() {
        EditText edt = (EditText)findViewById(R.id.fileNamer);
        String filePath = this.getFilesDir().getPath() + edt.getText().toString();
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("SongError", "The media player was not able to be played.");
            e.printStackTrace();
        }
    }

    /**
     * stopPlaying() will stop the mediaPlayer, and log it
     */
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        Log.i("stopped", "The player has successfully been told to stop playing!");
    }
}
