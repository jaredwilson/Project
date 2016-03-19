package jaredwilson.project;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Files extends AppCompatActivity  {
    public final static String key = "key";
    public String filename;
    public String progress;
    public int progressInSeconds;
    private List<String> listOfFileNames;
    private ArrayAdapter<String> fileAdapter;
    private MyCustomArrayAdapter mcaa;
    private final PlayActions player = PlayActions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_layout);
        catchIntent();

        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);

        mcaa = new MyCustomArrayAdapter(this, this.getFilesDir().listFiles(), this.getFilesDir());
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(mcaa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listClickActions(view);
            }
        });
/*
        listOfFileNames = new ArrayList<>();
        String[] listOfFN = this.getFilesDir().list();
        for(String str : listOfFN) {
            listOfFileNames.add(str);
        }
        fileAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, listOfFileNames);
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(fileAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listClickActions(view);
            }
        });
        */


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

    private void listClickActions(View view) {
        //
        // Holy moly this was ridiculous!
        //
        filename = this.getFilesDir().getPath()+ "/" + ((TextView)(view).findViewById(R.id.firstLine)).getText().toString();
        progress = "0";
        TextView tv = (TextView)findViewById(R.id.selectedTextView);
        tv.setText(((TextView)(view).findViewById(R.id.firstLine)).getText().toString());
        Log.i("Str", "path: " + this.getFilesDir().getPath());
    }

    // functions for Navigation
    public void filesTabPress(View v) {/* we're there already, so do nothing */}
    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording",(filename + "," + progress),this);
    }
    public void editTabPress(View v) {
        new ChangeTabs().execute("Editing", (filename + "," + progress), this);
    }

    public void playActions(View view) {
        if(!player.getIsPlaying()) {
            player.setSongPath(filename);
        }
        player.play_actions(view);
    }
}

