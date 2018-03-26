package com.rug.gea.Client;

import com.rug.gea.Client.building.Data;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jk on 24/02/18.
 */
public class BuildingP2PClient {

    private ArrayList<byte[]> dataToSend = new ArrayList<>();

    private boolean shouldRun;

    BuildingP2PClient(int port) throws IOException {
        shouldRun = true;
        new Thread(new AcceptRunnable(port)).start();
    }

    public void connectToBuilding(String address, int port) throws IOException {
        // Initiate a connection
        Socket s = new Socket(address, port);
        new Thread(new ConnectionRunnable(s, true)).start();
        new Thread(new ConnectionRunnable(s, false)).start();
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
                    new Thread(new ConnectionRunnable(s, true)).start();
                    new Thread(new ConnectionRunnable(s, false)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConnectionRunnable implements Runnable {

        private int sent = 0;
        private final OutputStream os;
        private final InputStream is;
        private final boolean receive;

        ConnectionRunnable(Socket socket, boolean receive) throws IOException {
            System.out.println("Connected to building " + socket.getPort());
            this.receive = receive;
            os = socket.getOutputStream();
            is = socket.getInputStream();
        }

        @Override
        public void run() {
            while (shouldRun) {
                if (this.receive) {
                    // Receive data
                    try {
                        System.out.println(is.read());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Send data
                    if (sent < dataToSend.size()) {
                        try {
                            os.write(dataToSend.get(sent));
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
