package com.jukusoft.mmo.launcher.controller;

import com.jukusoft.mmo.launcher.javafx.FXMLController;
import com.jukusoft.mmo.updater.Channel;
import com.jukusoft.mmo.updater.UpdateListener;
import com.jukusoft.mmo.updater.Updater;
import com.jukusoft.mmo.utils.PlatformUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LauncherController implements FXMLController, Initializable {

    @FXML
    protected Button startButton;

    @FXML
    protected ProgressBar progressBar;

    @FXML
    protected Label progressLabel;

    @FXML
    protected Label versionLabel;

    @FXML
    protected ComboBox<String> channelComboBox;

    //instance of updater
    protected Updater updater = null;

    @Override
    public void init(Stage stage, Scene scene, Pane pane) {
        progressLabel.setText("Check for updates...");

        startButton.setDisable(true);

        this.channelComboBox.setVisible(false);

        this.updater = new Updater();

        new Thread(() -> {
            try {
                this.updater.load("./updater/", ".");

                Logger.getAnonymousLogger().log(Level.INFO, "Updater loaded successfully.");

                Platform.runLater(() -> {
                    //set current version
                    versionLabel.setText("Installed version: " + this.updater.getCurrentVersion().getFullVersion());

                    this.channelComboBox.setVisible(true);

                    for (Channel channel : this.updater.listChannels()) {
                        this.channelComboBox.getItems().add(channel.getTitle());
                    }

                    //select first option
                    this.channelComboBox.getSelectionModel().select(0);

                    /*this.channelComboBox.setOnAction((event) -> {
                        checkForUpdates();
                    });*/

                    this.channelComboBox.valueProperty().addListener(new ChangeListener<String>() {
                        @Override public void changed(ObservableValue ov, String t, String t1) {
                            checkForUpdates();
                        }
                    });

                    this.progressLabel.setText("Calculate file checksum... Wait...");

                    checkForUpdates();
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }).start();
    }

    protected void checkForUpdates () {
        //get selected item
        int index = this.channelComboBox.getSelectionModel().getSelectedIndex();

        //get channel
        Channel channel = this.updater.listChannels().get(index);

        Logger.getAnonymousLogger().log(Level.INFO, "selected update channel: " + channel.getName());

        //check, if channel is activated
        if (!channel.isActivated()) {
            progressLabel.setText("Sorry, this update channel isn't active yet!");
            this.startButton.setDisable(true);
            return;
        }

        progressLabel.setText("try to start update...");

        //get newest build number of channel
        try {
            int newestBuildNumber = channel.getNewestBuildNumber();

            if (newestBuildNumber > this.updater.getCurrentVersion().getBuildNumber()) {
                //an new version is available
                this.startButton.setText("UPDATE");
                this.startButton.setDisable(false);
                this.startButton.setOnAction((event) -> {
                    startButton.setDisable(true);

                    //execute code in new thread
                    new Thread(() -> {
                        this.updater.startUpdate(channel, new UpdateListener() {
                            @Override
                            public void onProgress(boolean finished, float progress) {

                            }

                            @Override
                            public void onError(String errorMessage) {
                                Platform.runLater(() -> {
                                    progressLabel.setText("Error: " + errorMessage);
                                    startButton.setDisable(false);
                                });
                            }

                            @Override
                            public void onFinish(String newVersion) {

                            }
                        });
                    }).start();
                });
            } else {
                //client is up to date
                this.progressLabel.setText("Client is up to date. :)");

                this.startButton.setText("START GAME");
                this.startButton.setDisable(false);
                this.startButton.setOnAction((event) -> {
                    try {
                        //start process
                        switch (PlatformUtils.getType()) {
                            case WINDOWS:
                                Runtime.getRuntime().exec("cmd.exe /c start.bat");

                                //quit launcher
                                System.exit(0);

                                break;
                            case LINUX:
                                Runtime.getRuntime().exec("start.sh");

                                //quit launcher
                                System.exit(0);

                                break;
                            case MAC_OS:
                                Runtime.getRuntime().exec("start.cmd");

                                //quit launcher
                                System.exit(0);

                                break;
                            default:
                                Logger.getAnonymousLogger().log(Level.SEVERE, "Unknown operating system.");
                                progressLabel.setText("Error: Unknown operating system.");

                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            progressLabel.setText("server error occurred!");

            e.printStackTrace();
            return;
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
