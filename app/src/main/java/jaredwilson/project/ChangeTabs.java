package jaredwilson.project;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by nathanulmer on 3/16/2016.
 */
public class ChangeTabs extends AsyncTask <Object, Void, Void> {
    public String nextActivity;
    public String message;
    public Context callingClass;
    public Intent intent;

    protected Void doInBackground(Object... params) {
        nextActivity = (String)params[0];
        message = (String)params[1];
        callingClass = (Context)params[2];

        switch (nextActivity)
        {
            case "Recording":
                intent = new Intent(callingClass, Recording.class);
                break;
            case "Editing":
                intent = new Intent(callingClass, Editing.class);
                break;
            case "Files":
                intent = new Intent(callingClass, Files.class);
                break;
        }
        intent.putExtra("key", message);
        return null;
    }

    protected void onPostExecute(Void result) {
        callingClass.startActivity(intent);
        ((AppCompatActivity)callingClass).finish();

    }

}