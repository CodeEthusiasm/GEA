package com.rug.gea.Client;

import com.rug.gea.Client.building.RemoteBuilding;
import com.rug.gea.DataModels.Data;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Building peer to peer client class which establish a connection between buildings
 * through the sockets so that the data is possibly sent/received.
 */
public class BuildingP2PClient {

    public interface OnBuildingConnectedListener {
        void onBuildingConnected(RemoteBuilding b);
    }

    private ArrayList<byte[]> dataToSend = new ArrayList<>();

    private final ArrayList<OnBuildingConnectedListener> listeners = new ArrayList<>();

    private boolean shouldRun;

    public BuildingP2PClient(int port) throws IOException {
        shouldRun = true;
        new Thread(new AcceptRunnable(port)).start();
    }

    public void addOnBuildingConnectedListener(OnBuildingConnectedListener listener) {
        listeners.add(listener);
    }

    public void connectToBuilding(String address, int port) throws IOException {
        // Initiate a connection
        Socket s = new Socket(address, port);
        RemoteBuilding building = new RemoteBuilding();
        new Thread(new ConnectionRunnable(s, true, building)).start();
        new Thread(new ConnectionRunnable(s, false, null)).start();
        for (OnBuildingConnectedListener listener : listeners)
            listener.onBuildingConnected(building);
    }

    public void stop() {
        this.shouldRun = false;
    }

    private void sendData(byte[] data) {
        this.dataToSend.add(data);
    }

    public void sendData(Data data) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream stream1 = new ObjectOutputStream(stream);
            stream1.writeObject(data);
            this.sendData(stream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runnable class in order to establish a connection
     */
    private class AcceptRunnable implements Runnable {

        private final ServerSocket serverSocket;

        AcceptRunnable(int port) throws IOException {
            System.out.println("Accepting connections on " + port);
            this.serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            while (shouldRun) {
                // Accept a connection
                try {
                    Socket s = serverSocket.accept();
                    System.out.println("Accepted connection: " + serverSocket.getInetAddress());
                    RemoteBuilding building = new RemoteBuilding();
                    new Thread(new ConnectionRunnable(s, true, building)).start();
                    new Thread(new ConnectionRunnable(s, false, null)).start();
                    for (OnBuildingConnectedListener listener : listeners)
                        listener.onBuildingConnected(building);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Runnable class for sending/receiving data through the established connection between buildings.
     */
    private class ConnectionRunnable implements Runnable {

        private int sent = 0;
        private Socket socket;
        private final OutputStream os;
        private final InputStream is;
        private final boolean receive;
        private final RemoteBuilding building;

        ConnectionRunnable(Socket socket, boolean receive, RemoteBuilding building) throws IOException {
            this.socket = socket;
            System.out.println("Connected to building " + socket.getPort());
            this.receive = receive;
            os = socket.getOutputStream();
            is = socket.getInputStream();
            this.building = building;
        }

        @Override
        public void run() {
            while (shouldRun) {
                if (this.receive) {
                    try {
                        // Receive data
                        ObjectInputStream inputStream = new ObjectInputStream(is);
                        Data model = (Data) inputStream.readObject();
                        building.addData(model);
                    } catch (SocketException | EOFException e) {
                        try {
                            this.socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } catch (IOException | ClassNotFoundException | TimeoutException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Send data
                    if (sent < dataToSend.size()) {
                        try {
                            os.write(dataToSend.get(sent));
                        } catch (SocketException e) {
                            try {
                                this.socket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sent++;
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
