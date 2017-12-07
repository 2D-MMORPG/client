package com.jukusoft.mmo.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jukusoft.mmo.game.Game;
import com.jukusoft.mmo.utils.ReportUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts the application for the desktop-based builds.
 *
 * @author Leeks & Dragons
 */
public class DesktopLauncher {

    protected static int WIDTH = 1280;
    protected static int HEIGHT = 720;

    /**
     * start method for game application
     */
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("2D MMORPG");
        config.setWindowedMode(WIDTH, HEIGHT);
        //config.setWindowIcon("./data/icon/app_icon.png");
        config.setResizable(true);

        //https://gamedev.stackexchange.com/questions/109047/how-to-close-an-app-correctly-on-desktop
        //config.forceExit = false;

        try {
            // start game
            new Lwjgl3Application(new Game(), config);
        } catch (Exception e) {
            Logger.getLogger("DesktopLauncher").log(Level.SEVERE, "an exception was thrown", e);

            //send exception to server
            ReportUtils.sendExceptionToServer(e);

            System.exit(-1);
        }
    }

}
