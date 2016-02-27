package jaredwilson.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = 0;
    }

    public void onClick (View v) {
        counter++;
    Button button = (Button) findViewById(R.id.button);
        button.setText("" + counter);
    }
}
