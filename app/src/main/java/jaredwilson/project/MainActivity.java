package jaredwilson.project;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaRecorder rRecorder;
    MediaPlayer jamsesh;
    public boolean playpress;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;
        playpress = true;
        jamsesh = MediaPlayer.create(this, R.raw.jammin);


        try {
            jamsesh.prepare();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void onClick (View v) {
        counter++;
        Button button = (Button) findViewById(R.id.button);
        button.setText("" + counter + " Points");

    }

    public void resetClick (View v) {
        counter = 0;
        Button button = (Button)  findViewById(R.id.button);
        button.setText("Back to nothing...");
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


}
