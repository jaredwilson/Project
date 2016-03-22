package jaredwilson.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Files extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int progressInSeconds;
    private MyCustomArrayAdapter mcaa;
    private final PlayActions player = PlayActions.getInstance();
    public boolean fileExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_layout);
        catchIntent();
        black_outStatusBar();

        mcaa = new MyCustomArrayAdapter(this, this.getFilesDir().listFiles(), this.getFilesDir());
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(mcaa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listClickActions(view);
            }
        });
    }

    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
    }

    private void catchIntent() {
        // Catch intent from sending Activity (filter?)
        Intent intent = getIntent();
        String message = intent.getStringExtra(key);

        // check message values. IF null set appropriate flags
        if (message.equals(",")) {
            filename = "";
            progress = "";
            fileExists = false;
        } else {
            filename = (message.split(","))[0];
            progress = (message.split(","))[1];
            progressInSeconds = Integer.parseInt(progress);
            fileExists = true;
        }
    }

    private void listClickActions(View view) {
        filename = this.getFilesDir().getPath()+ "/" + ((TextView)(view).findViewById(R.id.firstLine)).getText().toString();
        progress = "0";
        TextView tv = (TextView)findViewById(R.id.selectedTextView);
        tv.setText(((TextView) (view).findViewById(R.id.firstLine)).getText().toString());
        fileExists = true;
    }

    // functions for Navigation
    public void filesTabPress(View v) {/* we're there already, so do nothing */}
    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording", (filename + "," + progress), this);
    }
    public void editTabPress(View v) {
        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }

    public void playActions(View view) {
        if(filename.isEmpty()) { return; }
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.play_actions(view);
    }

    private void updateChanges() {
        mcaa = new MyCustomArrayAdapter(this, this.getFilesDir().listFiles(), this.getFilesDir());
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(mcaa);
    }

    public void renameFile(View v) {
        if(fileExists) {
            final EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Enter new name")
                    .setView(input)
                    .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rename(input.getText().toString());
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
            }).show();
        }
    }
    private void rename(String stringInput) {
        // rename file to new path
        if(!stringInput.isEmpty()) {
            (new File(filename)).renameTo(new File(this.getFilesDir().getPath() + "/" + stringInput));
            filename = this.getFilesDir().getPath() + "/" + stringInput;
            updateChanges();
            TextView tv = (TextView) findViewById(R.id.selectedTextView);
            tv.setText(stringInput);
        }
    }

    public void deleteFile (View v) {
        if (fileExists) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete File")
                    .setMessage("Are you sure you wish to delete " + filename + "?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
    private void delete () {
        if(filename.isEmpty()) { return; }
        File file = new File(filename);
        try {
            file.getCanonicalFile().delete();
            updateChanges();
            TextView tv = (TextView)findViewById(R.id.selectedTextView);
            tv.setText("");
        } catch (IOException e) {
            Log.e("Delete Error", "Could not delete, " + e);
        }
        filename = "";
        fileExists = false;
        progress = "";
    }

    public void seekForward(View view) {
        // indicate some kind of visual emphasis
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.seekFwd();
    }

    public void seekBack(View view) {
        // indicate some kind of visual emphasis
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.seekBck();
    }
}

