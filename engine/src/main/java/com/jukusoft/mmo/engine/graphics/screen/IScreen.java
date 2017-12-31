package com.jukusoft.mmo.engine.graphics.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Screen interface - screens are responsible for drawing, not for updating your game state!
 *
 * Created by Justin on 06.02.2017.
 */
public interface IScreen {

    /**
    * method which should be executed if screen is created
    */
    public void onStart ();

    /**
     * method which should be executed if screen has stopped
     */
    public void onStop ();

    /**
    * method is executed, if screen isn't active anymore
    */
    public void onPause ();

    /**
    * method is executed, if screen is set to active state now.
    */
    public void onResume ();

    /**
     * update game screen
     */
    public void update();

    /**
     * draw game screen
     */
    public void draw();

}
