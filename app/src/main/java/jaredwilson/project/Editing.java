package jaredwilson.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.ImageButton;


public class Editing extends AppCompatActivity {

    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_layout);
        black_outStatusBar();
        isRecording = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void finish() {
        if(isRecording) {
            stopService(new Intent(this, SomeService.class));
        }
    }
    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
    }

    public void filesTabPress(View v) {
        if(isRecording) { stopService(new Intent(this, SomeService.class));}
        new ChangeTabs().execute("Files", ("dummyString"), this);
    }

    public void recordingTabPress(View v) {
        if(isRecording) { stopService(new Intent(this, SomeService.class));}
        new ChangeTabs().execute("Recording", ("dummyString"), this);
    }

    public void checkLive(View view) {

        if (isRecording) {
             isRecording = false;
            ((ImageButton)findViewById(R.id.liveFeedImage)).setImageResource(R.drawable.play_white);
            stopService(new Intent(this, SomeService.class));
        } else {
            isRecording = true;
            ((ImageButton)findViewById(R.id.liveFeedImage)).setImageResource(R.drawable.pause_white);
            startService(new Intent(this, SomeService.class));
            //startLive();
        }
    }
}
