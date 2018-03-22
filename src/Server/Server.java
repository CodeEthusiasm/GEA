package Server;

import Model.DataModel;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.*;
import org.bson.Document;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public class Server {

    private final static String SERVER_URL = "localhost";
    private final static String QUEUE_NAME = "perodic_data";

    private MongoCollection<Document> mCollection;

    public Server() {
        MongoClientURI uri = new MongoClientURI("mongodb://server:jeongkyun@ds219879.mlab.com:19879/gaedatabase");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("gaedatabase");
        mCollection = database.getCollection("data");
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
                    Document document = new Document();
                    switch (data.getBuildingType()) {
                        case House:
                            document.append("type", "house");
                            break;
                        case Studio:
                            document.append("type", "studio");
                            break;
                        case Factroy:
                            document.append("type", "factory");
                            break;
                        case Apartment:
                            document.append("type", "apartment");
                            break;
                        default:
                            document.append("type", "default");
                    }
                    document.append("gas", data.getGas())
                            .append("electricity", data.getElectricity())
                            .append("size", data.getSquareMeter());
                    mCollection.insertOne(document);
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
