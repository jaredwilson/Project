package jaredwilson.project;

import android.os.AsyncTask;
import java.util.Calendar;

public class TimerAsyncTask extends AsyncTask <Object, String, Void> {
    Recording temp;
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
}
