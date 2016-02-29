package jaredwilson.project;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        fileName = new File(this.getFilesDir(), "Most Recent");

        try {
            jamsesh.prepare();
        } catch (Exception e) {e.printStackTrace();}
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

        if(isRecording) {
            startRecording();
            mRecorder.setText("Stop Recording");
        } else {
            stopRecording();
            mRecorder.setText("Start Recording");
        }
        isRecording = !isRecording;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(fileName.getPath());
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

        if(isPlaying) {
            startPlaying();
            mPlayer.setText("Stop Playing");
        } else {
            stopPlaying();
            mPlayer.setText("Start Playing");
        }
        isPlaying = !isPlaying;
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName.getPath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e){ e.printStackTrace(); }
    }
    private void stopPlaying() {

    }
}
