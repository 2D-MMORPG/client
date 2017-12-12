package com.jukusoft.mmo.launcher;

import com.jukusoft.mmo.launcher.window.LauncherWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

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

        //undecorated style
        //primaryStage.initStyle(StageStyle.UNDECORATED);

        //transparent window
        //primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("close application now.");

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

        //http://www.adam-bien.com/roller/abien/entry/completely_transparent_windows_stage_in
        //this.launcherWindow.setTransparent();

        this.launcherWindow.setVisible(true);

        //get pane
        //this.launcherWindow.getRootPane().setStyle("-fx-background-image: url('file:" + bgImage + "')");
    }

    /**
    * read default login values from configuration
    */
    protected void readDefaultConfig () {
        try {
            this.ini = new Ini(new File("./data/config/launcher.cfg"));
            this.defaultCfg = this.ini.get("Launcher");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
