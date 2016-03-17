package jaredwilson.project;

/**
 * Created by nathanulmer on 3/16/2016.
 * This interface names methods that each Activity in the project will have to define in order to
 * work as part of the app.
 */
public interface SoundHubActivity {

    // functions for PlaybackModule
    abstract public void pressPlay();
    abstract public void  pressFF();
    abstract public void  pressRW();

    // functions for NavigationModule
    abstract public void  pressRecord();
    abstract public void  pressOpen();
    abstract public void  pressEdit();

}
