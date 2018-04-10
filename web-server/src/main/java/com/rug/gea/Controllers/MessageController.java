package com.rug.gea.Controllers;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.*;
import com.rug.gea.DataModels.Data;
import com.rug.gea.DataModels.Information;
import com.rug.gea.DataModels.Serialize;
import org.bson.Document;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class MessageController {

    private static final String SERVER_URL = "145.90.151.200";
    private final static String QUEUE_NAME = "periodic_data";

    /**
     * Broadcast updated information to the buildings in a certain zip code
     * @param zip
     * @param info
     * @throws IOException
     * @throws TimeoutException
     */
    public static void sendMessage(String zip, Information info) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER_URL);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(zip, "fanout");

        channel.basicPublish(zip, "", null, Serialize.serialize(info));
        System.out.println(" [x] Sent '" + info + "'");

        channel.close();
        connection.close();
    }

    /**
     * Receives the consumption data from building clients and stores into database.
     * @throws IOException
     * @throws TimeoutException
     */
    public static void receiveMessage() throws IOException, TimeoutException {
        MongoClientURI uri = new MongoClientURI("mongodb://server:jeongkyun@ds219879.mlab.com:19879/gaedatabase");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("gaedatabase");
        MongoCollection<Document> collection = database.getCollection("data");

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
                    Data data = (Data)Serialize.deserialize(body);
                    Document document = new Document();
                    String buildingType = data.getType().toLowerCase();
                    if ("house".equals(buildingType)) {
                        document.append("type", "house");
                    } else if ("studio".equals(buildingType)) {
                        document.append("type", "studio");
                    } else if ("studio".equals(buildingType)) {
                        document.append("type", "factory");
                    } else if ("studio".equals(buildingType)) {
                        document.append("type", "apartment");
                    } else {
                        document.append("type", "default");
                    }
                    document.append("gas", data.getGas())
                            .append("electricity", data.getElectricity());
                    collection.insertOne(document);
                    System.out.println(" [x] Received '" + data + "'");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
