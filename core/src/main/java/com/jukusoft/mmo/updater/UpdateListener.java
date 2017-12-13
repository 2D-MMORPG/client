package com.jukusoft.mmo.updater;

public interface UpdateListener {

    public void onProgress (boolean finished, float progress, String message);

    public void onError (String errorMessage);

    public void onFinish (String newVersion);

}
