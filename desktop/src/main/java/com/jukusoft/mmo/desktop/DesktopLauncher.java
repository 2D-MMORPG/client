package com.jukusoft.mmo.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jukusoft.mmo.desktop.config.WindowConfig;
import com.jukusoft.mmo.game.Game;
import com.jukusoft.mmo.launcher.LauncherMain;
import com.jukusoft.mmo.updater.Updater;
import com.jukusoft.mmo.utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts the application for the desktop-based builds.
 *
 * @author Leeks & Dragons
 */
public class DesktopLauncher {

    //dimension of window
    protected static final int WIDTH = 1280;
    protected static final int HEIGHT = 720;

    //
    public DesktopLauncher () {
        //
    }

    /**
     * start method for game application
     */
    public static void main(String[] args) {
        for (String param : args) {
            if (param.contains("--launcher")) {
                //start launcher
                LauncherMain.main(args);

                return;
            } else if (param.contains("--checksum")) {
                //generate file checksum
                Updater updater = new Updater();
                try {
                    updater.load("./updater/", ".");
                    updater.invalideFileHashes();
                    updater.prepareFileHashes(".");
                } catch (IOException e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "IOException: ", e);
                } catch (Exception e) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Exception: ", e);
                }

                return;
            }
        }

        //set application name for app directory in user.home directory
        AppUtils.setAppName("2d-mmorpg");

        try {
            //create log directory, if not exists
            LogUtils.createLogDirIfAbsent();

            //create map cache directory, if directory does not exists
            MapCacheUtils.createMapCacheDirIfAbsent();

            //load window config
            WindowConfig windowConfig = new WindowConfig("./data/config/window.cfg");

            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

            //load window config
            windowConfig.fillConfig(config);

            // start game
            new Lwjgl3Application(new Game(), config);
        } catch (Exception e) {

            Logger.getLogger("DesktopLauncher").log(Level.SEVERE, "an exception was thrown, sended report to server now", e);

            try {
                Logger.getAnonymousLogger().log(Level.SEVERE, "working directory: " + new File( "." ).getCanonicalPath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JavaFXUtils.startJavaFX();

                    Platform.runLater(() -> {
                        JavaFXUtils.showExceptionDialog("Error", "Game crashed!", e);

                        System.exit(-1);
                    });
                }
            });

            //send exception to server
            ReportUtils.sendExceptionToServer(e);
        }
    }

}
