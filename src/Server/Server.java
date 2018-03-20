package Server;

import Model.DataModel;
import com.rabbitmq.client.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public class Server {

    private final static String SERVER_URL = "localhost";
    private final static String QUEUE_NAME = "perodic_data";

    private Map<Date, DataModel> mModelMap;

    public Server() {
        mModelMap = new HashMap<>();
    }

    private void receiveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER_URL);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press ctrl+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    DataModel data = DataModel.deserialize(body);
                    mModelMap.put(data.getDate(), data);
                    System.out.println(" [x] Received '" + data + "'");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public static void main(String [] args)  {
        System.out.println("I'm server.");
        Server server = new Server();
        try {
            server.receiveMessage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
