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
        System.out.println("Enter ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;
        playpress = true;
        isRecording = false;
        jamsesh = MediaPlayer.create(this, R.raw.jammin);
        listOfFileNames = new ArrayList<>();
        if(this.getFilesDir().length() != 0){
            System.out.println("adding files");
            listOfFileNames.addAll(Arrays.asList(this.getFilesDir().list())); }

        fileAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, listOfFileNames);

        ListView lv = (ListView)findViewById(R.id.listViewSongs);

        lv.setAdapter(fileAdapter);
        Log.i("OnCreate", "This has been a successful onCreate!");


        try {
            System.out.println("...prepping jamsesh");
            jamsesh.prepare();
        } catch (Exception e) {
            System.out.println("songErr 1");
            Log.e("SongError", "The jamsesh.prepare function failed!");
            e.printStackTrace();}

        System.out.println("Exit ONCREATE");
    }


    /**
     * getListFiles is incomplete file grabber
     * @param parentDir should give the directory for files
     * @return list of files
     */
    private List<String> getListFiles(File parentDir) {
        System.out.println("\tEnter GETLISTFILES");
        ArrayList<String> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();


        for (File file : files) {
            System.out.println("\tfile \'" + file.getName() + "\' from FILES[]...");
            if (file.isDirectory()) {
                System.out.println("\t\'" + file.getName() + "\'.isDirectory() = true");

                // QUESTION: do we want to call "addAll()" from within a forEach loop?

                inFiles.addAll(Arrays.asList(this.getFilesDir().list()));
                System.out.println("ArrayList INFILES:\n" + inFiles);
            } else {
               // if(true){//file.getName().endsWith(".mp3")){
                System.out.println("\t\'" + file.getName() + "\'.isDirectory() = false");
                    inFiles.add(file.getName());
                System.out.println("ArrayList INFILES:\n" + inFiles);
               // }
            }
        }
        System.out.println("\tinFiles = " + inFiles);
        System.out.println("\tExit GETLISTFILES");
        return inFiles;

    }

    /**
     * stopIt function stops the media player and changes the button
     * @param v View which provides context
     */
    public void stopIt (View v) {
        System.out.println("\tEnter STOPIT");
        jamsesh.stop();
        playpress = true;
        // use the method onPreparedListener to check if file is prepared.
        jamsesh.prepareAsync();
        Button button = (Button) findViewById(R.id.play);
        button.setText("play");

        fileAdapter.notifyDataSetChanged();
        System.out.println("\tExit STOPIT");
    }

    /**
     * do_things plays and pauses the jamsesh
     * @param vee view from which context is gathered
     */
    public void do_things(View vee) {
        System.out.println("\tEnter DO_THINGS");
        Button just_pressed = (Button)vee;
        /*switch (just_pressed.getId()) {
            //play/pause is pressed
            case R.id.play: */
        if (playpress) {
            System.out.println("\tplaypress = true");
            jamsesh.start();
            System.out.println("\tjamsesh.start()...");
            just_pressed.setText("pause");
            playpress = false;
        }
        else {
            System.out.println("\tplaypress = false");
            jamsesh.pause();
            System.out.println("\tjamsesh.pause()...");
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
        System.out.println("\tExit DO_THINGS");
    }

    /**
     * startRecording will start the recording, saving at a certain place
     */
    private void startRecording() {
        System.out.println("\t\tEnter STARTRECORDING");
        EditText edt = (EditText)findViewById(R.id.fileNamer);
        String filePath = this.getFilesDir().getPath() + edt.getText().toString();
        mRecorder = new MediaRecorder();
        System.out.print("\t\tnew MediaRecorder 1,");
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        System.out.print(" 2,");
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        System.out.print(" 3,");
        mRecorder.setOutputFile(filePath);
        System.out.print(" 4,");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        System.out.print(" 5.");

        try {
            mRecorder.prepare();
            System.out.println("\t\tmRecorder prepping...");
        } catch (IOException e) { e.printStackTrace(); }

        mRecorder.start();
        System.out.println("\t\tmRecorder start()...");
        System.out.println("\t\tExit STARTRECORDING");
    }

    /**
     * stopRecording with stop the recorder and release it
     */
    private void stopRecording() {
        System.out.println("\t\tEnter STOPRECORDING");
        mRecorder.stop();
        System.out.print("\t\tmRecorder stop()...");
        mRecorder.release();
        System.out.print("\t\trelease()...");
        mRecorder = null;
        System.out.print("\t\tmRecorder = null");
        System.out.println("\t\tExit STOPRECORDING");
    }

    /**
     * play_actions with change the button to start/stop, and call the proper function
     * @param view
     */
    public void play_actions(View view) {
        System.out.println("\tEnter PLAY_ACTIONS");
        Button mPlayer = (Button)view;

        if(!isPlaying) {
            System.out.println("\tisPlaying = false");
            startPlaying();
            System.out.println("\tstartPlaying()...");
            mPlayer.setText("Stop Playing");
        } else {
            System.out.println("\tisPlaying = true");
            stopPlaying();
            System.out.println("\tstopPlaying()...");
            mPlayer.setText("Start Playing");
        }
        isPlaying = !isPlaying;
        System.out.println("\tisPlaying = !isPlaying");
        System.out.println("\tExit PLAY_ACTIONS");
    }

    /**
     * record_actions will start the media recorder and change the button
     * @param view provides the context
     */
    public void record_actions(View view) {
        System.out.println("\tEnter RECORD_ACTIONS");
        Button mRecorder = (Button)view;

        EditText name = (EditText)findViewById(R.id.fileNamer);
        if(!isRecording) {
            System.out.println("\tisRecording = false");
            startRecording();
            System.out.println("\tstartRecording()...");
            Log.i("Record_Actions", "File STARTed recording under the name \"" + name.getText().toString() + "\".");
            mRecorder.setText("Stop Recording");
        } else {
            System.out.println("\tisRecording = true");
            stopRecording();
            System.out.println("\tstopRecording()...");
            Log.i("Record_Actions", "File STOPPed recording under the name \"" + name.getText().toString() + "\".");
            mRecorder.setText("Start Recording");
        }
        isRecording = !isRecording;
        System.out.println("\tisRecording = !isRecording");
        System.out.println("\tExit RECORD_ACTIONS");
    }

    /**
     * startPlaying() will start the mediaPlayer, catching errors
     */
    private void startPlaying() {
        System.out.println("\t\tEnter STARTPLAYING");
        EditText edt = (EditText)findViewById(R.id.fileNamer);
        String filePath = this.getFilesDir().getPath() + edt.getText().toString();
        System.out.println("\t\tfilePath = " + filePath);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
            System.out.println("\t\tnew MediaPlayer created...");
        } catch (IOException e) {
            Log.e("SongError", "The media player was not able to be played.");
            e.printStackTrace();
        }
        System.out.println("\t\tExit STARTPLAYING");
    }

    /**
     * stopPlaying() will stop the mediaPlayer, and log it
     */
    private void stopPlaying() {
        System.out.println("\t\tEnter STOPPLAYING");
        mPlayer.release();
        System.out.println("\t\tmPlayer release()...");
        mPlayer = null;
        System.out.println("\t\tmPlayer = null");
        Log.i("stopped", "The player has successfully been told to stop playing!");
        System.out.println("\t\tExit STOPPLAYING");
    }
}
