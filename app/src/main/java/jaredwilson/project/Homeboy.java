package jaredwilson.project;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Homeboy extends AppCompatActivity {
    public final static String pass_to_next_activity = "com.mycompany.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeboy_layout);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, Recording.class);
        intent.putExtra(pass_to_next_activity, "");
        startActivity(intent);
    }
}
