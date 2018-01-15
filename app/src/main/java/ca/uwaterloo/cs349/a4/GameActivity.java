package ca.uwaterloo.cs349.a4;

/**
 * Created by y43ji on 2017-12-01.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import java.lang.Thread;

import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer
{
    // Private Vars
    Model Model;
    TextView Message;
    TextView Score;
    Button MainButton;
    Button AgainButton;
    RelativeLayout Buttons;
    int number;
    //Thread thread;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(String.valueOf(R.string.DEBUG_A4_ID), "GameActivity: OnCreate");
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_game);

        // Get Model instance
        Model = Model.getInstance();
        Model.addObserver(this);

        number = Model.getNumber();

        // Get TextView Reference
        Message = (TextView) findViewById(R.id.game_message);

        Score = (TextView) findViewById(R.id.game_score);

        // Get button
        MainButton = (Button) findViewById(R.id.game_button_main);
        MainButton.setEnabled(false);

        MainButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
            }
        });

        // Game Buttons
        Buttons = (RelativeLayout) findViewById(R.id.game_buttons);

        // Button
        for (int i=1; i<=number; i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    500,
                    280);
            Button button = new Button(this);
            button.setText(Integer.toString(i));
            button.setTypeface(null, Typeface.BOLD);
            button.setTextSize(60);
            button.setId(i);
            button.setTextColor(Color.GRAY);
            if ((i==2) || (i==4) || (i==6)) {
                params.addRule(RelativeLayout.RIGHT_OF, i-1);
            }
            if ((i==3) || (i==4) || (i==5) || (i==6)){
                params.addRule(RelativeLayout.BELOW, i - 2);
            }

            button.setLayoutParams(params);
            button.setEnabled(false);
            Buttons.addView(button);

            final int buttonnum = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Model.setCurrentPlay (buttonnum);
                    Model.verifyButton(buttonnum);
                }
            });
        }

        // Get button
        AgainButton = (Button) findViewById(R.id.game_button_startagain);

        AgainButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //startActivity(new Intent(GameActivity.this, GameActivity.class));
                //start again
                Model.newRound();
                //computer play
                if (Model.getState() == ca.uwaterloo.cs349.a4.Model.State.COMPUTER) {
                   //Model.nextButton();
                    startFlash(Buttons);
                    Handler h = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Model.setState(ca.uwaterloo.cs349.a4.Model.State.HUMAN);
                        }
                    };
                    h.postDelayed(r,500 * ((Model.getScore()+1) + (Model.getLevel() * (Model.getScore()+1))));
                }
            }
        });

        // Init observers
        Model.initObservers();

    }

    public void flash (Button btn){
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        // animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        //final Button btn = (Button) findViewById(i);
        btn.startAnimation(animation);
    }


    public void startFlash(View view) {
        // do something long
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Model.sequence.size(); i++) {
                    final int value = Model.sequence.elementAt(i);
                   // WaitTime();
                    final Button btn = (Button) findViewById(value);
                    btn.post(new Runnable() {
                        @Override
                        public void run() {
                            flash(btn);
                        }
                    });
                    WaitTime();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void WaitTime() {
        SystemClock.sleep(500 * Model.getLevel());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(String.valueOf(R.string.DEBUG_A4_ID), "GameActivity: onDestory");

        // Remove observer when activity is destroyed.
        Model.deleteObserver(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Options Menu
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


   /*
     //Initialize the contents of the Activity's standard options menu.

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.game, menu);
        //inflater.inflate(R.menu.setting, menu);

        return super.onCreateOptionsMenu(menu);
    }


     //This hook is called whenever an item in your options menu is selected.

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        switch (item.getItemId())
        {
            case R.id.menu_gotoMain:
                // Create Intent to launch second activity
                Intent intent = new Intent(this, MainActivity.class);

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
    public void update(Observable o, Object arg)
    {
        Log.d(String.valueOf(R.string.DEBUG_A4_ID), "GameView: Update TextView");

        switch (Model.getState()) {
            case START:
                String l = "Challenge level: ";
                if (Model.getLevel()==1) {
                    l = l + "Hard";
                } else if (Model.getLevel()==3) {
                    l = l + "Easy";
                } else {
                    l = l + "Normal";
                }
                Message.setText("Playing with " + Model.getNumber() + " buttons \n" + l + "\nPress START to Play");
                Score.setText("Score: " + Integer.toString(Model.getScore()));
                AgainButton.setText("START");
                MainButton.setEnabled(true);
                break;
            case COMPUTER:
                Message.setText("Watch what I do ... \nYou can not press the buttons yet.");
                Score.setText("Current Score: " + Integer.toString(Model.getScore()));
                /*for (int i=0; i< Model.sequence.size(); i++) {
                    s = s + " " +Integer.toString(Model.sequence.elementAt(i) );
                }
                for(int i=0;i<Buttons.getChildCount();i++){
                    View child = Buttons.getChildAt(i);
                    child.setEnabled(false);
                }*/
                for (int i=1; i<=Model.getNumber(); i++) {
                    Button btn = (Button) findViewById(i);
                    btn.setEnabled(false);
                    btn.setTextColor(Color.GRAY);
                }
                AgainButton.setEnabled(false);
                MainButton.setEnabled(false);
                break;
            case HUMAN:
                Message.setText("Now, It is your turn ... \nPress the correct buttons in flashing order");
                Score.setText("Current Score: " + Integer.toString(Model.getScore()));
                for (int i=1; i<=Model.getNumber(); i++) {
                    Button button = (Button) findViewById(i);
                    button.setEnabled(true);
                    if (i == 1) {
                        button.setTextColor(Color.RED);
                    } else if (i == 2) {
                        button.setTextColor(Color.YELLOW);
                    } else if (i == 3) {
                        button.setTextColor(Color.GREEN);
                    } else if (i == 4) {
                        button.setTextColor(Color.BLUE);
                    } else if (i == 5) {
                        button.setTextColor(Color.MAGENTA);
                    } else {
                        button.setTextColor(Color.BLACK);
                    }
                }
                AgainButton.setEnabled(false);
                MainButton.setEnabled(false);
                break;
            case WIN:
                Message.setText("You won! :) \nPress CONTINUE to continue." +
                        "\nPress MAIN MENU to go back to main menu and start again!");
                Score.setText("Current Score: " + Integer.toString(Model.getScore()));
                AgainButton.setText("Continue");
                AgainButton.setEnabled(true);
                MainButton.setEnabled(true);
                for (int i=1; i<=Model.getNumber(); i++) {
                    Button btn = (Button) findViewById(i);
                    btn.setEnabled(false);
                    btn.setTextColor(Color.GRAY);
                }
                break;
            case LOSE:
                Message.setText("You lose :( \nPress PLAY AGAIN to start again. " +
                        "\nPress MAIN MENU to read the instructions and start again!");
                Score.setText("Final Score: " + Integer.toString(Model.getScore()));
                AgainButton.setText("Play Again");
                AgainButton.setEnabled(true);
                MainButton.setEnabled(true);
                for (int i=1; i<=Model.getNumber(); i++) {
                    Button btn = (Button) findViewById(i);
                    btn.setEnabled(false);
                    btn.setTextColor(Color.GRAY);
                }
                break;
            default:
                Message.setText(Integer.toString(Model.getNumber()) + " " + Integer.toString(Model.getLevel()) );
                break;
        }

    }
}
