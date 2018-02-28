package Server;

import com.rabbitmq.client.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public class Server {

    private final static String SERVER_URL = "localhost";
    private final static String QUEUE_NAME = "perodic_data";
    private static ConnectionFactory mFactory;
    private static Connection mConnection;
    private static Channel mChannel;

    private void receiveMessage() throws IOException, TimeoutException {
        mFactory = new ConnectionFactory();
        mFactory.setHost(SERVER_URL);
        mConnection = mFactory.newConnection();
        mChannel = mConnection.createChannel();
        mChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press ctrl+C");
    }

    public static void main(String [] args)  {
        System.out.println("I'm server.");
        Server server = new Server();
        try {
            server.receiveMessage();
            Consumer consumer = new DefaultConsumer(mChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            mChannel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
