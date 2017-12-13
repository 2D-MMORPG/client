package com.jukusoft.mmo.launcher;

import com.jukusoft.mmo.launcher.window.LauncherWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Justin on 29.08.2017.
 */
public class JavaFXApplication extends Application {

    //primary stage
    protected Stage primaryStage = null;

    //default configuration
    protected Ini ini = new Ini();
    protected Profile.Section defaultCfg = null;

    protected LauncherWindow launcherWindow = null;

    /**
    * default constructor
    */
    public JavaFXApplication() {
        //
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //save primary stage
        this.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest(event -> {
            Logger.getAnonymousLogger().log(Level.INFO, "close application now.");

            System.exit(0);
        });

        //read default config
        this.readDefaultConfig();

        //get title, width & height
        String title = defaultCfg.get("title");
        int width = Integer.parseInt(defaultCfg.get("width"));
        int height = Integer.parseInt(defaultCfg.get("height"));
        boolean fullscreen = Boolean.parseBoolean(defaultCfg.getOrDefault("fullscreen", "false"));

        //create & show new launcher window
        this.launcherWindow = new LauncherWindow(primaryStage, title, width, height);
        this.launcherWindow.setFullscreen(fullscreen);
        this.launcherWindow.getStage().setResizable(false);

        this.launcherWindow.setVisible(true);
    }

    /**
    * read default login values from configuration
    */
    protected void readDefaultConfig () {
        try {
            this.ini = new Ini(new File("./data/config/launcher.cfg"));
            this.defaultCfg = this.ini.get("Launcher");
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Exception was thrown: ", e);
            System.exit(1);
        }
    }

}
