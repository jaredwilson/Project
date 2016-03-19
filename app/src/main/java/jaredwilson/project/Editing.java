package jaredwilson.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class Editing extends AppCompatActivity {
    public final static String key = "key";
    public String filename;
    public String progress;
    public String path;
    public boolean fileExists;
    public int progressInSeconds;
    private final PlayActions player = PlayActions.getInstance();
    public String newFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_layout);

        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);

        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(key);

        // check message values. IF null set appropriate flags
        if (message.equals(",")) {
            // there's no file, so a recording will require creating a new file.
            filename = "";
            progress = "";
            fileExists = false;

        } else {
            filename = (message.split(","))[0];
            progress = (message.split(","))[1];
            fileExists = true;
            progressInSeconds = Integer.parseInt(progress);
            path = this.getFilesDir().getPath();

            try {
                // prepare the audio file for playing
                // Q: How're we going to handle recording here? Record over the file? Insert recording? Decisions...

            } catch (Exception e) {}


        }
    }

    // functions for PlaybackModule
    public void pressPlay(View view) {
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.play_actions(view);
    }

    public void pressFF() {}

    public void pressRW() {}

    public void renameFile(View v) {

        if(fileExists) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter new name");

            final EditText input = new EditText(this);
            builder.setView(input);


            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newFilename = input.getText().toString();
                    File file = new File(path + "/" + filename);

                    // rename file to new path
                    file.renameTo(new File(path + "/" + newFilename));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

    }

    public void deleteFile (View v) {
        if (fileExists) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete File")
                    .setMessage("Are you sure you wish to delete this file?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(path + "/" + filename);
                            try {
                                file.getCanonicalFile().delete();
                                new ChangeTabs().execute("Files", (filename + "," + progress), this);
                            } catch (IOException e) {
                                Log.e("Delete Error", "Could not delete, " + e);
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
    // functions for Navigation
    public void editTabPress(View v) {/* we're there already, so do nothing */}

    public void filesTabPress(View v) {
        new ChangeTabs().execute("Files", (filename + "," + progress), this);
    }

    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording", (filename + "," + progress), this);
    }

    public void finish() {super.finish();}
}