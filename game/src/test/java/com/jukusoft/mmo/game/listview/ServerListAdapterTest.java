package com.jukusoft.mmo.game.listview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.jukusoft.mmo.game.GameUnitTest;
import com.jukusoft.mmo.server.Server;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerListAdapterTest extends GameUnitTest {

    @BeforeClass
    public static void beforeClass () {
        VisUI.load();
    }

    @AfterClass
    public static void afterClass () {
        VisUI.dispose();
    }

    @Test
    public void testConstructor () {
        Array<Server> array = new Array<>();
        new ServerListAdapter(array);
    }

    @Test
    public void testSetBackground () {
        Array<Server> array = new Array<>();
        ServerListAdapter adapter = new ServerListAdapter(array);

        Drawable background = VisUI.getSkin().getDrawable("window-bg");
        Drawable selectBackground = VisUI.getSkin().getDrawable("list-selection");

        adapter.setBackground(background, selectBackground);

        assertEquals(background, adapter.background);
        assertEquals(selectBackground, adapter.selectBackground);
    }

    @Test
    public void testCreateView () {
        Array<Server> array = new Array<>();
        ServerListAdapter adapter = new ServerListAdapter(array);

        adapter.createView(new Server());
    }

    @Test
    public void testUpdateView () {
        Array<Server> array = new Array<>();
        ServerListAdapter adapter = new ServerListAdapter(array);

        VisTable view = adapter.createView(new Server());

        adapter.updateView(view, new Server());
    }

    @Test
    public void testSelectView () {
        Array<Server> array = new Array<>();
        ServerListAdapter adapter = new ServerListAdapter(array);

        VisTable view = adapter.createView(new Server());

        adapter.selectView(view);
    }

    @Test
    public void testDeselectView () {
        Array<Server> array = new Array<>();
        ServerListAdapter adapter = new ServerListAdapter(array);

        VisTable view = adapter.createView(new Server());

        adapter.deselectView(view);
    }

}
