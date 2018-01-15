package ca.uwaterloo.cs349.a4;

/**
 * Created by y43ji on 2017-12-01.
 */

import android.util.Log;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.lang.String;
import java.util.Random;


class Model extends Observable
{
    // Create static instance of this mModel
    private static final Model ourInstance = new Model();
    static Model getInstance()
    {
        return ourInstance;
    }

    // Private Variables

    private int number; // number of buttons
    private int level;
    enum State { START, COMPUTER, HUMAN, LOSE, WIN };
    State state;
    int score;
    int length;
    int index;
    int current; //computer
    int currentplay; //human
    Vector<Integer> sequence = new Vector<Integer>();

    /**
     * Model Constructor:
     * - Init member variables
     */
    Model() {
        number = 4;
        level = 2;
        init(number);
    }

    public void init (int number) {
        this.length = 1;
        this.number = number;
        this.state = State.START;
        this.score = 0;
    }

    public int getScore () {
    return score;
}
    public State getState() {
        return state;
    }

    public int getCurrentPlay () {
        return currentplay;
    }

    public void setCurrentPlay(int i) {
        this.currentplay = i;
        setChanged();
        notifyObservers();
    }

    public void setState (State s) {
        this.state = s;
        setChanged();
        notifyObservers();
    }

    public void newStart () {
        // start a new game
        if (sequence != null) {
            sequence.clear();
        }
        index = 0;
        current = 0;
        currentplay = 0;
        score = 0;
        length = 1;
        setState(State.START);
    }

    public void newRound () {
        // reset if lose
        if (state == State.LOSE) {
            length = 1;
            score = 0;
        }
        if (sequence != null) {
            sequence.clear();
        }
        Random rand = new Random();
        for (int i=0; i<length; i++) {
            int b = rand.nextInt() % number;
            if (b<0) {
                b = -1*b + 1;
            } else if (b==0) {
                b+=1;
            }
            //current = b;
            sequence.add(i,b);
        }
        index = 0;
        current = 0;
        currentplay = 0;
        setState(State.COMPUTER);
    }

    public void setCurrent(int i) {
        this.current = i;
        setChanged();
        notifyObservers();
    }

    public int getCurrent () {
        return current;
    }

    // show next button when computer playing
    public void nextButton() {
        if (state != State.COMPUTER) {
            setCurrent(-1);
        }

        // next button to show
        int button = sequence.elementAt(index);

        // advance to next button
        index ++;

        setCurrent(button);

        // now human
        if (index >= sequence.size()) {
            index = 0;
            setState(State.HUMAN);
        } else {
            setState(State.COMPUTER);
        }
        //return button;
    }

    boolean verifyButton (int button) {
        if (state != State.HUMAN) {
            return false;
        }

        // if correct button
        boolean correct = (button == sequence.elementAt(index));

        // next button;
        index++;

        // push the wrong button
        if (!correct) {
            setState(State.LOSE);
        }

        // right button
        else {
            // if last button, win
            if (index == sequence.size()) {
                // update score and length
                score++;
                length++;
                setState (State.WIN);
            }

        }
        return correct;
    }

    public int getNumber()
    {
        return number;
    }
    public int getLevel()
    {
        return level;
    }

    public void setNumber(int i)
    {
        this.number = i;
    }

    public void setLevel(int i)
    {
        this.level = i;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Observable Methods
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method to make it easier to initialize all observers
     */
    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    @Override
    public synchronized void deleteObserver(Observer o)
    {
        super.deleteObserver(o);
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }
}
