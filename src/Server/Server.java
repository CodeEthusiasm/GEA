package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jk on 24/02/18.
 */
public class Server {

    private static final int PORT = 9900;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private ByteArrayOutputStream out;

    public Server() {
        try {
            initializeServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server started");
        System.out.println("Waiting for a client ...");
        clientSocket = serverSocket.accept();
        System.out.println("Client accepted");

        in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        out = new ByteArrayOutputStream();

        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        System.out.println("Closing connection");
        System.out.println(new String(out.toByteArray()));

        clientSocket.close();
        in.close();
        out.close();
    }

    public static void main(String [] args)  {
        System.out.println("I'm server.");
        Server server = new Server();
    }
}
