package jaredwilson.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Editing extends AppCompatActivity implements SoundHubActivity{
    public final static String pass_to_next_activity = "com.mycompany.myfirstapp.MESSAGE";
    public String filename;
    public int progressInSeconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_layout);

        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(Recording.pass_to_next_activity);

        // check message values. IF null set appropriate flags
        if (message == "") {
            // there's no file... show a message "No file selected"?

        } else {
            filename = (message.split(","))[0];
            progressInSeconds = Integer.parseInt((message.split(","))[1]);

            try {
                // prepare the audio file for playing
                // make sure we can see that file tile and it has emphasis

            } catch (Exception e) {}


        }
    }

    // functions for PlaybackModule
    public void pressPlay() {}

    public void pressFF() {}

    public void pressRW() {}

    // functions for NavigationModule
    public void pressRecord() {}

    public void pressOpen() {}

    public void pressEdit() {}


    // Commented stuff is from previous testing work...
/*
    public void onRecClick(View v) {
        Intent intent = new Intent(this, Recording.class);

        // later, we'll pull the actual progress in seconds from here...
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);

        String message = filename + "," + Integer.toString(progressInSeconds);
        intent.putExtra(pass_to_next_activity, message);
        startActivity(intent);
    }

    public void onFSClick(View v) {
        Intent intent = new Intent(this, Files.class);

        // later, we'll pull the actual progress in seconds from here...
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);

        String message = filename + "," + Integer.toString(progressInSeconds);
        intent.putExtra(pass_to_next_activity, message);
        startActivity(intent);
    }
*/
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_layout);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Recording.pass_to_next_activity);


        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        LinearLayout layout = (LinearLayout) findViewById(R.id.content);
        layout.addView(textView);
    }
    */
}
