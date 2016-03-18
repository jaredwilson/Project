package jaredwilson.project;

import android.content.Intent;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
//System.out.println("");
public class Recording extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int progressInSeconds;
    //stuff
    MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_layout);

        catchIntent();
        createRecording();


    }

    public void catchIntent() {
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
    public void createRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (Exception e) {}
    }

    public void pressRECORD(View v) {

    }

    // functions for PlaybackModule
    public void pressPlay() {}

    public void pressFF() {}

    public void pressRW() {}

    // functions for Navigation
    public void recordingTabPress(View v) {/* we're there already, so do nothing */}

    public void filesTabPress(View v) {
        new ChangeTabs().execute("Files",(filename + "," + progress),this);
    }

    public void editTabPress(View v) {
        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }
}
