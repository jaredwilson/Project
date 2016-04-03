package jaredwilson.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;

public class Files extends AppCompatActivity implements MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    public final static String key = "key";
    public String filename;
    public String progress;
    private MyCustomArrayAdapter mcaa;
    private final PlayActions player = PlayActions.getInstance();
    public boolean fileExists;

    private MediaPlayer mPlayer;
    private MediaController mController;
    private boolean isPrepared = false;
    private RelativeLayout prevRL;
    private boolean isSelected;


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
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mController = new MediaController(this) {
            @Override
            public void hide() {
                if(!mPlayer.isPlaying()) {
                    super.hide();
                }
            }
        };
    }

    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
    }
    private void catchIntent() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(key);
        if (message.equals("dummyString")) {
            filename = "";
            fileExists = false;
        } else {
            filename = message;
            fileExists = true;
        }
    }

    private void listClickActions(View view) {
        filename = this.getFilesDir().getPath()+ "/" + ((TextView)(view).findViewById(R.id.firstLine)).getText().toString();
        progress = "0";
        TextView tv = (TextView)findViewById(R.id.selectedTextView);
        tv.setText(((TextView) (view).findViewById(R.id.firstLine)).getText().toString());
        tv.setHint("");
        fileExists = true;
        if (isSelected) {
            prevRL.setBackgroundColor(Color.parseColor("#000000"));
        }
        prevRL = (RelativeLayout)(view);
        prevRL.setBackgroundColor(Color.parseColor("#AF34A520"));
        isSelected = true;
        if(!isPrepared){
            setup();
        }else {
            mPlayer.reset();
            setup();
        }
    }

    // functions for Navigation
    public void filesTabPress(View v) {/* we're there already, so do nothing */}
    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording", "dummyString", this);
    }
    public void editTabPress(View v) {
        new ChangeTabs().execute("Editing", "dummyString", this);
    }

    private void updateChanges() {
        mcaa = new MyCustomArrayAdapter(this, this.getFilesDir().listFiles(), this.getFilesDir());
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(mcaa);
    }
    public void renameFile(View v) {
        if(fileExists) {
            final EditText input = new EditText(this);
            input.setSingleLine(true);
            input.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(30),


            });
            final AlertDialog dialog = new AlertDialog.Builder(this)
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
            }).create();
            input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            });
            dialog.show();
        }
    }
    private void rename(String stringInput) {
        mController.hide();
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
        mPlayer.stop();
        mController.hide();
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


    public void setup() {
        try {
            mPlayer.setDataSource(filename);
            mPlayer.prepare();
            isPrepared = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        mController.show(0);
        mPlayer.start();
    }
    @Override
    public void pause() {
        mController.show(0);
        mPlayer.pause();
    }
    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }
    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }
    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);
    }
    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }
    @Override
    public int getBufferPercentage() {
        return 0;
    }
    @Override
    public boolean canPause() {
        return true;
    }
    @Override
    public boolean canSeekBackward() { return true; }
    @Override
    public boolean canSeekForward() { return true; }
    @Override
    public int getAudioSessionId() {
        return 0; }
    @Override
    public void onPrepared(MediaPlayer mp) {
        mController.setMediaPlayer(this);
        mController.setAnchorView(findViewById(R.id.surfaceView));
        mController.setEnabled(true);
        mController.show(3600000);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayer.seekTo(0);
        setup();
    }

}

