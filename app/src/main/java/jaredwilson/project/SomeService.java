package jaredwilson.project;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.IBinder;

public class SomeService extends Service {

    boolean isRecording;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRecording = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                findinBackground();
            }
        }).start();

        return START_NOT_STICKY;
    }

    private  void findinBackground() {
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
        arec.stop();
        arec.release();

        atrack.stop();
        atrack.release();
    }

    @Override
    public void onDestroy() {
        isRecording = false;
    }

}
