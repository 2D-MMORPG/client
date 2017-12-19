package com.jukusoft.mmo.network;

@FunctionalInterface
public interface Callback<T> {

    public void handle(T param);

}
