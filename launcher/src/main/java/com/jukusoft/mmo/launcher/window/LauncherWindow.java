package com.jukusoft.mmo.launcher.window;

import com.jukusoft.mmo.launcher.controller.LauncherController;
import com.jukusoft.mmo.launcher.javafx.FXMLWindow;
import javafx.stage.Stage;

public class LauncherWindow extends FXMLWindow {

    public LauncherWindow(Stage stage, String title, int width, int height) {
        super(stage, title, width, height, "./data/fxml/launcher.fxml", new LauncherController());
    }

}
