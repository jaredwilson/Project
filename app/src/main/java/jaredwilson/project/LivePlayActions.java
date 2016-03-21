package jaredwilson.project;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LivePlayActions {
    // Private constructor. Prevents instantiation from other classes.
    private LivePlayActions() {
    }
    private static class LivePlayActionsHolder {
        private static final LivePlayActions INSTANCE = new LivePlayActions();
    }
    public static LivePlayActions getInstance() {
        return LivePlayActionsHolder.INSTANCE;
    }

    private Button button;
    private int buffersize;
    private AudioRecord arec;
    private AudioTrack atrack;
    private boolean isLivePlaying;

    public void play_actions(View view) {
        //button = (Button)view.findViewById(R.id.testButton);
        if (!isLivePlaying) {
            isLivePlaying = true;
            startLivePlaying();
            button.setText("Recording");
        } else {
            isLivePlaying = false;
            stopLivePlaying();
            button.setText("Live Play");
        }
    }

    private void startLivePlaying() {
        buffersize = AudioRecord.getMinBufferSize(11025, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 11025, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize);
        atrack = new AudioTrack(AudioManager.STREAM_MUSIC, 11025, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STREAM);

        atrack.setPlaybackRate(11025);
        arec.startRecording();
        atrack.play();
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
                boolean shouldContinue = true;
                byte[] buffer = new byte[buffersize];
                while (shouldContinue) {
                    arec.read(buffer, 0, buffersize);
                    atrack.write(buffer, 0, buffer.length);
                    shouldContinue = isLivePlaying;
                }
            }
        }).start();

        arec.stop();
        arec.release();
        atrack.stop();
        atrack.release();
    }
    public void stopLivePlaying() {
        isLivePlaying = false;
    }


}