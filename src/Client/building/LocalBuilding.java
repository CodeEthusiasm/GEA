package Client.building;

import java.util.ArrayList;

/** A building that generates data from time to time. */

public class LocalBuilding extends Building {

    private ArrayList<Data> data = new ArrayList<>();
    private ArrayList<OnDataReceivedListener> listeners = new ArrayList<>();
    private boolean shouldRun = false;

    public void addListener(OnDataReceivedListener listener) {
        listeners.add(listener);
    }

    private void addData(Data d) {
        data.add(d);
        for (OnDataReceivedListener listener : listeners)
            listener.onDataReceived(d);
    }

    public void start() {
        if (shouldRun)
            throw new RuntimeException("Client.building.Building is already running");
        shouldRun = true;
        new Thread(() -> {
            while (shouldRun) {
                addData(new Data());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        if (!shouldRun)
            throw new RuntimeException("Client.building.Building is not running");
        shouldRun = false;
    }

    @Override
    public Data[] getData() {
        return (Data[]) data.toArray();
    }
}
