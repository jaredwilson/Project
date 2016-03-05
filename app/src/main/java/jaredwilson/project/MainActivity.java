package jaredwilson.project;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaRecorder mRecorder;
    MediaPlayer mPlayer;
    MediaPlayer jamsesh;
    File fileName;
    public boolean playpress;
    public boolean isRecording;
    public boolean isPlaying;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;
        playpress = true;
        isRecording = false;
        jamsesh = MediaPlayer.create(this, R.raw.jammin);

        try {
            jamsesh.prepare();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void stopIt (View v) {
        jamsesh.stop();
        playpress = true;
        // use the method onPreparedListener to check if file is prepared.
        jamsesh.prepareAsync();
        Button button = (Button) findViewById(R.id.play);
        button.setText("play");
    }

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

    public void record_actions(View view) {
        Button mRecorder = (Button)view;

        if(!isRecording) {
            startRecording();
            mRecorder.setText("Stop Recording");
        } else {
            stopRecording();
            mRecorder.setText("Start Recording");
        }
        isRecording = !isRecording;
    }

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

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

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

    private void startPlaying() {
        EditText edt = (EditText)findViewById(R.id.fileNamer);
        String filePath = this.getFilesDir().getPath() + edt.getText().toString();
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e){ e.printStackTrace(); }
    }
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
}
