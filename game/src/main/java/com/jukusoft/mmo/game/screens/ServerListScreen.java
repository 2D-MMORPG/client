package com.jukusoft.mmo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.jukusoft.mmo.EngineVersion;
import com.jukusoft.mmo.engine.graphics.screen.IScreen;
import com.jukusoft.mmo.engine.graphics.screen.InjectScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.ScreenManager;
import com.jukusoft.mmo.engine.graphics.screen.impl.BaseUIScreen;
import com.jukusoft.mmo.engine.service.InjectService;
import com.jukusoft.mmo.engine.service.impl.NetworkService;
import com.jukusoft.mmo.game.listview.ServerListAdapter;
import com.jukusoft.mmo.server.Server;
import com.jukusoft.mmo.server.ServerFinder;
import com.jukusoft.mmo.utils.Platform;
import com.jukusoft.mmo.utils.PlatformUtils;
import com.kotcrab.vis.ui.util.adapter.SimpleListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListScreen extends BaseUIScreen {

    //labels
    protected VisLabel versionLabel = null;
    protected VisLabel errorLabel = null;

    //button
    protected VisTextButton submitButton = null;

    //background image
    protected Image bgImage = null;

    //list view
    protected ListView<Server> listView = null;
    protected ServerListAdapter listAdapter = null;
    protected Array<Server> modelList = new Array<>();

    protected String configFile = "./data/config/server.cfg";

    protected Server selectedServer = null;

    @InjectService
    protected NetworkService networkService;

    @InjectScreenManager
    protected ScreenManager<IScreen> screenManager;

    @Override
    public void initStage(Stage stage) {
        //https://github.com/kotcrab/vis-editor/wiki/VisUI

        //TODO: add background

        //add versionLabel
        this.versionLabel = new VisLabel("2D MMORPG Build version: " + EngineVersion.BUILD_NUMBER, Color.WHITE);
        this.versionLabel.setPosition(0, 0);
        stage.addActor(this.versionLabel);

        //add error label
        this.errorLabel = new VisLabel("Error!");
        this.errorLabel.setVisible(false);
        this.errorLabel.setColor(Color.RED);
        this.errorLabel.setPosition(400, 600);
        stage.addActor(this.errorLabel);

        try {
            this.initServerList();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Exception while reading serverlist: ", e);

            //show error message
            this.errorLabel.setVisible(true);
            this.errorLabel.setText("Error! Couldn't load serverlist! Is internet connection active? " + e.getLocalizedMessage());
        }

        float listXPos = (Gdx.graphics.getWidth() - 100) / 2 - 50f;

        this.listAdapter = new ServerListAdapter(this.modelList);
        this.listView = new ListView<>(this.listAdapter);
        Table table = this.listView.getMainTable();
        table.setBounds(listXPos, 200, 200, 200);

        //set footer
        VisTable footerTable = new VisTable();
        footerTable.addSeparator();
        footerTable.row();
        footerTable.add("Choose server!");
        listView.setFooter(footerTable);

        listView.setItemClickListener(new ListView.ItemClickListener<Server>() {
            @Override
            public void clicked(Server item) {
                selectedServer = item;
            }
        });

        listView.setUpdatePolicy(ListView.UpdatePolicy.ON_DRAW);

        stage.addActor(table);

        //add button
        this.submitButton = new VisTextButton("Submit");
        this.submitButton.setBounds(listXPos, 200, 200, 50);
        this.submitButton.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                click(selectedServer);
            }
        });
        stage.addActor(this.submitButton);
    }

    @Override
    public void cleanUpStage(Stage stage) {
        this.versionLabel = null;
    }

    protected void initServerList () throws IOException {
        ServerFinder serverFinder = new ServerFinder(this.configFile);

        //load serverlist
        serverFinder.load();

        //convert serverlist to libgdx array
        for (Server server : serverFinder.listServer()) {
            this.modelList.add(server);
        }

        Logger.getAnonymousLogger().log(Level.INFO, "{0} proxy servers found.", this.modelList.size);
    }

    protected void click (Server selectedServer) {
        if (selectedServer == null) {
            this.errorLabel.setText("Select an server first!");
            this.errorLabel.setVisible(true);

            return;
        }

        //disable button
        this.submitButton.setDisabled(true);
        this.submitButton.setText("Connecting...");

        this.errorLabel.setVisible(false);

        Logger.getAnonymousLogger().log(Level.SEVERE, "try to connect to proxy server " + selectedServer.getProxyIP() + ":" + selectedServer.getProxyPort());

        //check, if server is online
        if (networkService.isOnline(selectedServer)) {
            //set proxy server
            networkService.connect(selectedServer, (res -> {
                //run ui changes in UI thread
                Platform.runOnUIThread(() -> {
                    if (res.succeeded()) {
                        //connection was established, change screen
                        screenManager.leaveAllAndEnter("login");
                    } else {
                        //show error message
                        errorLabel.setVisible(true);
                        errorLabel.setText("Couldn't connect to proxy server '" + selectedServer.getName() + "'!");

                        //enable button
                        this.submitButton.setDisabled(false);
                        this.submitButton.setText("Submit");
                    }
                });
            }));
        } else {
            //show error message
            errorLabel.setVisible(true);
            errorLabel.setText("Game server '" + selectedServer.getName() + "' (Channel: " + selectedServer.getChannel() + ") isn't online!");

            //enable button
            this.submitButton.setDisabled(false);
            this.submitButton.setText("Submit");
        }
    }

}
