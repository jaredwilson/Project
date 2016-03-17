package jaredwilson.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Recording extends AppCompatActivity implements SoundHubActivity {
    public final static String pass_to_next_activity = "com.mycompany.myfirstapp.MESSAGE";
    public String filename;
    public int progressInSeconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);

        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(Recording.pass_to_next_activity);

        // check message values. IF null set appropriate flags
        if (message == "") {
            // there's no file, so a recording will require creating a new file.

        } else {
            filename = (message.split(","))[0];
            progressInSeconds = Integer.parseInt((message.split(","))[1]);

            try {
                // prepare the audio file for playing
                // Q: How're we going to handle recording here? Record over the file? Insert recording? Decisions...

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
    public void onFSClick(View v) {
        String input = ((EditText) findViewById(R.id.theInput)).getText().toString();
        filename = (input.split(","))[0];
        progressInSeconds = Integer.parseInt((input.split(","))[1]);
        // later, we'll pull the actual progress in seconds from here...
        //ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = new Intent(this, Files.class);


        String message = filename + "," + Integer.toString(progressInSeconds);
        intent.putExtra(pass_to_next_activity, message);
        startActivity(intent);
    }

    public void onEditClick(View v) {
        Intent intent = new Intent(this, Editing.class);

        // later, we'll pull the actual progress in seconds from here...
        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);

        String message = filename + "," + Integer.toString(progressInSeconds);
        intent.putExtra(pass_to_next_activity, message);
        startActivity(intent);
    }
*/

  /*public int data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);
        data = 42;
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, Files.class);
        EditText shared_Bref = (EditText) findViewById(R.id.book_input);
        EditText shared_Cref = (EditText) findViewById(R.id.chapter_input);
        EditText shared_Vref = (EditText) findViewById(R.id.verse_input);
        String message =    (shared_Bref.getText().toString()) + " " +
                            (shared_Cref.getText().toString()) + ":" +
                            (shared_Vref.getText().toString());
        intent.putExtra(scripture_ref, message);
        startActivity(intent);
    }
    */
}
