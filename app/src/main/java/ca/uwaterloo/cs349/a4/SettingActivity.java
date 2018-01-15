package ca.uwaterloo.cs349.a4;

/**
 * Created by y43ji on 2017-12-01.
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.Observable;
import java.util.Observer;

public class SettingActivity extends AppCompatActivity implements Observer
{
    // Private Vars
    Model Model;
    Button DoneButton;
    TextView Text1;
    TextView Text2;
    SeekBar Seek;
    TextView Number;
    RadioGroup Level;
    RadioButton Easy;
    RadioButton Normal;
    RadioButton Hard;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(String.valueOf(R.string.DEBUG_A4_ID), "SettingActivity: OnCreate");
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_setting);

        // Get Model instance
        Model = Model.getInstance();
        Model.addObserver(this);

        // Get Text
        Text1 = (TextView) findViewById(R.id.setting_text1);
        Text2 = (TextView) findViewById(R.id.setting_text2);

        // Get button reference.
        DoneButton = (Button) findViewById(R.id.setting_button_done);

        // Get Seekbar
        Seek = (SeekBar) findViewById(R.id.setting_seekBar);

        // Get RadioButton
        Level = (RadioGroup) findViewById(R.id.setting_level);
        Easy = (RadioButton) findViewById(R.id.setting_easy);
        Normal = (RadioButton) findViewById(R.id.setting_normal);
        Hard = (RadioButton) findViewById(R.id.setting_hard);
        Level.check(Normal.getId());

        // get a reference to widgets to manipulate on update
        Number = (TextView)findViewById(R.id.setting_number);

        Number.setText(Integer.toString(Seek.getProgress()+1));

        // create a controller to increment counter when clicked
        Seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.d("DEMO", "MainActivity: onProgressChanged");
                if (progress == seekBar.getMax()) {
                    Number.setText("6");
                    Model.setNumber (6);
                }
                else {
                    Number.setText(Integer.toString(progress + 1));
                    Model.setNumber (progress+1);
                }

            }
        });

        Level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if(checkedId == R.id.setting_easy) {

                    Model.setLevel(3);

                } else if(checkedId == R.id.setting_hard) {

                    Model.setLevel(1);

                } else {

                    Model.setLevel(2);

                }

            }
        });

        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.setState(ca.uwaterloo.cs349.a4.Model.State.START);
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });

        // Init observers
        Model.initObservers();
    }

    @Override
    protected void onDestroy()
    {
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        // inflater.inflate(R.menu.game, menu);
        inflater.inflate(R.menu.setting, menu);

        return super.onCreateOptionsMenu(menu);
    }


     // This hook is called whenever an item in your options menu is selected.

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        switch (item.getItemId())
        {
            case R.id.menu_gotoGame:
                // Create Intent to launch second activity
                Intent intent = new Intent(this, GameActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_gotoMain:
                // Create Intent to launch second activity
                Intent intent2 = new Intent(this, MainActivity.class);

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
    public void update(Observable o, Object arg)
    {
        Seek.setProgress(Model.getNumber()-1);
        if (Model.getLevel() == 3) {
            Level.check(Easy.getId());
        }
        else if (Model.getLevel() == 2) {
            Level.check(Normal.getId());
        } else {
            Level.check(Hard.getId());
        }
        // Update button with click count from model
        // mIncrementButton.setText(String.valueOf(mModel.getCounter()));
    }
}
