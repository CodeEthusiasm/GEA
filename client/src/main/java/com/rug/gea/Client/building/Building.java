package com.rug.gea.Client.building;

public abstract class Building {
    public interface OnDataReceivedListener {
        void onDataReceived(Data d);
    }

    private Data[] data;

    public Data[] getData() {
        return data;
    }
}
