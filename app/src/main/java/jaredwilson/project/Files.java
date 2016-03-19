package jaredwilson.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Files extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int progressInSeconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_layout);
        catchIntent();

    }

    private void catchIntent() {
        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(key);

        // check message values. IF null set appropriate flags
        if (message.equals(",")) {
            // there's no file, so a recording will require creating a new file.
            filename = "";
            progress = "";

        } else {
            filename = (message.split(","))[0];
            progress = (message.split(","))[1];
            progressInSeconds = Integer.parseInt(progress);

            try {
                // prepare the audio file for playing
                // Q: How're we going to handle recording here? Record over the file? Insert recording? Decisions...

            } catch (Exception e) {}
        }
    }

    // functions for Navigation
    public void filesTabPress(View v) {/* we're there already, so do nothing */}
    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording",(filename + "," + progress),this);
    }
    public void editTabPress(View v) {
        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }
}