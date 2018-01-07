package com.jukusoft.mmo.game.listview;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.jukusoft.mmo.server.Server;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.SimpleListAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class ServerListAdapter extends ArrayAdapter<Server, VisTable> {

    //consider creating Style class, see SimpleListAdapter source for example
    protected Drawable background = VisUI.getSkin().getDrawable("window-bg");
    protected Drawable selectBackground = VisUI.getSkin().getDrawable("list-selection");

    public ServerListAdapter (Array<Server> array) {
        super(array);

        //user can only select one server and not multiple servers
        this.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setBackground (Drawable bg, Drawable selection) {
        this.background = bg;
        this.selectBackground = selection;
    }

    @Override
    protected VisTable createView(Server proxyServer) {
        VisLabel label = new VisLabel(proxyServer.getName());
        VisLabel channelLabel = new VisLabel(" (" + proxyServer.getChannel() + ")");

        VisTable table = new VisTable(true);
        table.left();
        table.add(label);
        table.add(channelLabel);

        table.pack();

        return table;
    }

    @Override
    protected void updateView (VisTable view, Server proxyServer) {
        //update your view here...
    }

    @Override
    protected void selectView (VisTable view) {
        view.setBackground(this.selectBackground);
    }

    @Override
    protected void deselectView (VisTable view) {
        view.setBackground(this.background);
    }

}
