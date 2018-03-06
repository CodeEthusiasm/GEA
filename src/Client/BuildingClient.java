package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jk on 24/02/18.
 */
public class BuildingClient {

    private ArrayList<byte[]> dataToSend = new ArrayList<>();

    private boolean shouldRun;

    BuildingClient(int port) throws IOException {
        new Thread(new AcceptRunnable(port)).run();
    }

    public void connectToBuilding(String address, int port) throws IOException {
        // Initiate a connection
        Socket s = new Socket(address, port);
        new Thread(new ConnectionRunnable(s)).run();
    }

    public void stop() {
        this.shouldRun = false;
    }

    public void sendData(byte[] data) {
        this.dataToSend.add(data);
    }

    private class AcceptRunnable implements Runnable {

        private final ServerSocket serverSocket;

        AcceptRunnable(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            while (shouldRun) {
                // Accept a connection
                try {
                    Socket s = serverSocket.accept();
                    new Thread(new ConnectionRunnable(s)).run();
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

        ConnectionRunnable(Socket socket) throws IOException {
            os = socket.getOutputStream();
            is = socket.getInputStream();
        }

        @Override
        public void run() {
            while (shouldRun) {
                try {
                    if (is.available() > 0) {
                        System.out.println("Data");
                        System.out.println(is.read());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Send data
                if (sent < dataToSend.size()) {
                    try {
                        os.write(dataToSend.get(sent));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sent++;
                }
            }
        }
    }
}
