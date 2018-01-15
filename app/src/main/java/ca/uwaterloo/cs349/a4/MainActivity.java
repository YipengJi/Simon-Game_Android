package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    // Private Vars
    Model Model;
    Button StartButton;
    Button SettingButton;
    TextView Text;
    TextView Simon;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(String.valueOf(R.string.DEBUG_A4_ID), "MainActivity: OnCreate");
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_main);

        // Get Model instance
        Model = Model.getInstance();
        Model.addObserver(this);

        // Get Text
        Text = (TextView) findViewById(R.id.main_text);

        SpannableString text = new SpannableString("SIMON");

        text.setSpan(new ForegroundColorSpan(Color.RED),0,1,0);
        text.setSpan(new ForegroundColorSpan(Color.MAGENTA),1,2,0);
        text.setSpan(new ForegroundColorSpan(Color.YELLOW),2,3,0);
        text.setSpan(new ForegroundColorSpan(Color.GREEN),3,4,0);
        text.setSpan(new ForegroundColorSpan(Color.CYAN),4,5,0);


        // Get textview Simon
        Simon = (TextView) findViewById(R.id.main_simon);
        Simon.setTextSize(80);
        Simon.setTypeface(null, Typeface.BOLD);
        Simon.setText(text);

        // Get button
        StartButton = (Button) findViewById(R.id.main_button_start);

        StartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Model.newStart();
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        // Get button
        SettingButton = (Button) findViewById(R.id.main_button_setting);

        SettingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });


        // Init observers
        Model.initObservers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(String.valueOf(R.string.DEBUG_A4_ID), "MainActivity: onDestory");

        // Remove observer when activity is destroyed.
        Model.deleteObserver(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Options Menu
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /*
     // Initialize the contents of the Activity's standard options menu.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        //inflater.inflate(R.menu.game, menu);
        //inflater.inflate(R.menu.setting, menu);

        return super.onCreateOptionsMenu(menu);
    }


    // This hook is called whenever an item in your options menu is selected.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle interaction based on item selection
        switch (item.getItemId()) {
            case R.id.menu_gotoGame:
                // Create Intent to launch second activity
                Intent intent = new Intent(this, GameActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_gotoSetting:
                // Create Intent to launch second activity
                Intent intent2 = new Intent(this, SettingActivity.class);

                // Start activity
                startActivity(intent2);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /**
     * This method is called whenever the observed object is changed.
     */
    @Override
    public void update(Observable o, Object arg) {
        // Update button with click count from model
        // mIncrementButton.setText(String.valueOf(mModel.getCounter()));
    }
}
