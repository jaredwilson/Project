package jaredwilson.project;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

public class PlayActions {
    // Private constructor. Prevents instantiation from other classes.
    private PlayActions() {
    }
    private static class PlayActionsHolder {
        private static final PlayActions INSTANCE = new PlayActions();
    }
    public static PlayActions getInstance() {
        return PlayActionsHolder.INSTANCE;
    }


    private MediaPlayer mPlayer;
    private boolean isPlaying;
    private String songPath = "";
    private ImageButton imgButt;
    private View someView;

    public void play_actions(View view) {
        if(songPath == null) { return; }
        someView = view;
        imgButt = (ImageButton)view;
        if (!isPlaying) {
            startPlaying();
            imgButt.setImageResource(R.drawable.pause_white);
        } else {
            stopPlaying();
            imgButt.setImageResource(R.drawable.play_white);
        }
        isPlaying = !isPlaying;
    }

    public void seekBck() {
        int duration =mPlayer.getDuration();
        int currentPosition = mPlayer.getCurrentPosition();
        int skipAmmount = duration / 5;

        if(duration == -1) { return; }

        if (currentPosition - skipAmmount < 0)
            mPlayer.seekTo(0);
        else
            mPlayer.seekTo(currentPosition - skipAmmount);
    }

    public void seekFwd() {
        int duration =mPlayer.getDuration();
        int currentPosition = mPlayer.getCurrentPosition();
        int skipAmmount =duration / 5;

        if(duration == -1) { return; }

        if (currentPosition + skipAmmount > duration)
            mPlayer.seekTo(duration);
        else
            mPlayer.seekTo(currentPosition + skipAmmount);
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(songPath);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer){
                    play_actions(someView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
    public void stop(){
        if(isPlaying) {
            stopPlaying();
        }
    }

    public void setSongPath(String s) { songPath = s; }

    public boolean getIsPlaying() { return isPlaying; }

}
