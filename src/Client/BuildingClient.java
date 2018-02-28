package Client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public class BuildingClient {

    private final static String SERVER_URL = "localhost";
    private final static String QUEUE_NAME = "perodic_data";
    private ConnectionFactory mFactory;
    private Connection mConnection;
    private Channel mChannel;

    private void sendMessage() throws IOException, TimeoutException {
        mFactory = new ConnectionFactory();
        mFactory.setHost(SERVER_URL);
        mConnection = mFactory.newConnection();
        mChannel = mConnection.createChannel();
        mChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "hello, world!";
        mChannel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        mChannel.close();
        mConnection.close();
    }

    public static void main(String argv[]) {
        BuildingClient client = new BuildingClient();
        try {
            client.sendMessage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
