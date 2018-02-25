package Client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by jk on 24/02/18.
 */
public class BuildingClient {

    private static final String DATA = "This is building client. I'm trying to send this message to client.";
    private static final String ADDRESS = "localhost";
    private static final int PORT = 9900;

    private Socket buildingSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public BuildingClient() {
        try {
            initializeBuilding();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeBuilding() throws IOException {
        buildingSocket = new Socket(ADDRESS, PORT);
        System.out.println("Connected");
        in = new DataInputStream(buildingSocket.getInputStream());
        out = new DataOutputStream(buildingSocket.getOutputStream());

        out.writeBytes(DATA);

        out.close();
        in.close();
        buildingSocket.close();
    }

    public static void main(String argv[]) {
        System.out.println("I'm a building client.");
        BuildingClient client = new BuildingClient();
    }
}
