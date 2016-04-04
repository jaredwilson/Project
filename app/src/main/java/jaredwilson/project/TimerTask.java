package jaredwilson.project;

import android.os.AsyncTask;
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
                // subtract diff from recTime and send that as a string
                if (diff > recTime) {
                    System.out.println("We've got a problem...01");
                }
                diff = recTime - diff;
            }
                /*int recT_diffSecs = ((int)recT_diff/1000);
                diff_H = recT_diffSecs / (60*60);
                diff_M = (recT_diffSecs - (diff_H*60*60)) / 60;
                diff_S = (recT_diffSecs - ((diff_H*60*60)+(diff_M*60)));
                if (diff_S > 59) {System.out.println("We've got a problem...02a");}

            } else {
                // send diff as a string
                int diffSecs = ((int)diff/1000);
                diff_H = diffSecs / (60*60);
                diff_M = (diffSecs - (diff_H*60*60)) / 60;
                diff_S = (diffSecs - ((diff_H*60*60)+(diff_M*60)));
                if (diff_S > 59) {System.out.println("We've got a problem...02b");}

            }*/
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
        stillRecording = temp.isRecording;
    }





























    /*
    protected Void doInBackground(Object[] params) {
        temp = (Recording)params[0];
        while (temp.recorder != null) {
            if (temp.enforcePocketMode()) {break;}

            Calendar time = Calendar.getInstance();
            long now = time.getTimeInMillis();
            long diff = now - temp.startRecTime;
            time.setTimeInMillis(diff);
            int h = time.get(Calendar.HOUR_OF_DAY);
            int m = time.get(Calendar.MINUTE);
            int s = time.get(Calendar.SECOND);
            String mins0 = "";
            String secs0 = "";
            if (s < 10) {
                secs0 = "0";
            }
            if (m < 10) {
                mins0 = "0";
            }
            h -= 17;// this is a patch-job...
            publishProgress("0" + h + ":" + mins0 + m + ":" + secs0 + s);
        }
        return null;
    }
    protected void onProgressUpdate(String... progress) {
        temp.setTimer(progress[0]);
    }
    */
}
