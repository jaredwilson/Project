package jaredwilson.project;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Homeboy extends AppCompatActivity {
    public final static String key = "key";
    public String message = ",";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeboy_layout);

        // change color of status bar to black
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.BLACK);

        // create a function that waits 2 second, then runs onClick() automatically.
    }

    public void onClick(View v) {
        new ChangeTabs().execute("Recording", message, this);
        /*
        Intent intent = new Intent(this, Recording.class);
        intent.putExtra(key, message);
        startActivity(intent);
        super.finish();*/
    }
}
