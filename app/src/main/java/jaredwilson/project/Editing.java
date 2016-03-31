package jaredwilson.project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;

public class Editing extends AppCompatActivity {
    public final static String key = "key";
    public String filename;
    public String progress;

    public int progressInSeconds;
    private final PlayActions player = PlayActions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_layout);
        black_outStatusBar();

    }

    private void black_outStatusBar() {
        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);
    }

    public void filesTabPress(View v) {
        new ChangeTabs().execute("Files", ("dummyString" + "," + 0), this);
    }

    public void recordingTabPress(View v) {
        new ChangeTabs().execute("Recording", ("dummyString" + "," + 0), this);
    }



}

/*
    // Experimental stuff
    public void run(View view) {
        isRecording = true;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        int buffersize = AudioRecord.getMinBufferSize(11025, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 11025, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize);
        AudioTrack atrack = new AudioTrack(AudioManager.USE_DEFAULT_STREAM_TYPE, 11025, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STREAM);
        atrack.setPlaybackRate(11025);
        byte[] buffer = new byte[buffersize];
        arec.startRecording();
        atrack.play();
        while(isRecording) {
            arec.read(buffer, 0, buffersize);
            atrack.write(buffer, 0, buffer.length);
        }
    }


    public void livePlayActions(View view) {
        if(!player.getIsPlaying()) {
            player.stop();
        }
        livePlayer.play_actions(view);
    }
*/