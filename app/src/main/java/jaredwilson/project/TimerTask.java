package jaredwilson.project;

import android.os.AsyncTask;
import android.view.View;

import java.util.Calendar;

public class TimerTask extends AsyncTask <Object, String, Void> {
    private Recording temp;
    private boolean stillRecording;
    private long recTime;
    private long start;
    private Calendar time;

    protected void onPreExecute() {
        super.onPreExecute();
        stillRecording = false;
        recTime = 0;
        time = Calendar.getInstance();
        start = time.getTimeInMillis();
    }

    protected Void doInBackground(Object[] params) {
        temp = (Recording)params[0];
        stillRecording = temp.isRecording;
        recTime = temp.recTime * 1000; // in millisecs
        long now;
        long recT_diff = 0;
        long diff = 0;
        int diff_H = 0;
        int diff_M = 0;
        int diff_S = 0;

        while (stillRecording) {
            time = Calendar.getInstance();
            now = time.getTimeInMillis();
            diff = now - start;

            if (recTime != 0) {
                if (diff > recTime){
                    System.out.println("We've got a problem...01");
                    publishProgress("00:00:00");
                    return null;
                }

                diff = recTime - diff;
            }
            int diffSecs = ((int)diff/1000);
            diff_H = diffSecs / (60*60);
            diff_M = (diffSecs - (diff_H*60*60)) / 60;
            diff_S = (diffSecs - ((diff_H*60*60)+(diff_M*60)));
            if (diff_S > 59) {System.out.println("We've got a problem...02");}

            String h = (diff_H < 10)? "0"+diff_H+":":diff_H+":";
            String m = (diff_M < 10)? "0"+diff_M+":":diff_M+":";
            String s = (diff_S < 10)? "0"+diff_S+"":diff_S+"";

            publishProgress(h+m+s);
        }
        return null;
    }
    protected void onProgressUpdate(String... progress) {
        temp.setTimer(progress[0]);
        stillRecording = temp.isRecording && !(temp.enforcePocketMode());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        temp.endRecording();
    }
}
