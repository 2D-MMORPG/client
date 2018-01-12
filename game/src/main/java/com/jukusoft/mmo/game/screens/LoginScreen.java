package com.jukusoft.mmo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jukusoft.mmo.EngineVersion;
import com.jukusoft.mmo.engine.graphics.screen.impl.BaseUIScreen;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginScreen extends BaseUIScreen {

    //labels
    protected VisLabel usernameLabel = null;
    protected VisLabel passwordLabel = null;
    protected VisLabel versionLabel = null;
    protected LinkLabel registerLabel = null;

    //input fields
    protected VisTextField usernameTextField = null;
    protected VisTextField passwordTextField = null;

    //login button
    protected VisTextButton loginButton = null;

    @Override
    public void initStage(Stage stage) {
        float listXPos = ((float) (Gdx.graphics.getWidth() - 100) / 2) - 50f;

        //create labels
        this.usernameLabel = new VisLabel("Username: ");
        this.usernameLabel.setPosition(listXPos, 430);
        stage.addActor(this.usernameLabel);

        this.passwordLabel = new VisLabel("Password: ");
        this.passwordLabel.setPosition(listXPos, 350);
        stage.addActor(this.passwordLabel);

        this.registerLabel = new LinkLabel("Registration (open link in browser)", "http://mmo.jukusoft.com/registration.php");
        this.registerLabel.setPosition(listXPos - 15, 100);
        stage.addActor(this.registerLabel);

        //create input fields
        this.usernameTextField = new VisTextField();
        float originHeight = this.usernameTextField.getHeight();
        this.usernameTextField.setBounds(listXPos, 400, 200, originHeight);
        stage.addActor(this.usernameTextField);

        this.passwordTextField = new VisTextField();
        this.passwordTextField.setPasswordMode(true);
        this.passwordTextField.setPasswordCharacter('*');
        this.passwordTextField.setBounds(listXPos, 320, 200, originHeight);
        stage.addActor(this.passwordTextField);

        //login button
        this.loginButton = new VisTextButton("Login");
        this.loginButton.setBounds(listXPos, 200, 200, 50);
        this.loginButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                login(usernameTextField.getText(), passwordTextField.getText());
            }
        });
        this.stage.addActor(this.loginButton);

        //add versionLabel
        this.versionLabel = new VisLabel("2D MMORPG Build version: " + EngineVersion.BUILD_NUMBER, Color.WHITE);
        this.versionLabel.setPosition(0, 0);
        stage.addActor(this.versionLabel);
    }

    @Override
    public void cleanUpStage(Stage stage) {

    }

    /**
    * try to login user
    */
    protected void login (String username, String password) {
        Logger.getAnonymousLogger().log(Level.INFO, "try to login user '" + username + "'.");
    }

}
