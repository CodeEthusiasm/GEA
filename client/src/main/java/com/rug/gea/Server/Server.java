package com.rug.gea.Server;

import com.rug.gea.Model.Client;
import com.rug.gea.Model.DataModel;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.*;
import com.rug.gea.Model.Information;
import com.rug.gea.Model.Serialize;
import org.bson.Document;

import java.io.*;
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

    public void sendMessage(String zip, Information info) throws IOException, TimeoutException {
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
                    DataModel data = (DataModel)Serialize.deserialize(body);
                    Document document = new Document();
                    String buildingType = data.getBuildingType().toLowerCase();
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
                    document.append("gas", data.getGasPerSqr())
                            .append("electricity", data.getElecPerSqr());
                    mCollection.insertOne(document);
                    System.out.println(" [x] Received '" + data + "'");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public static void main(String [] args) throws IOException, TimeoutException {
        System.out.println("I'm server.");
        Server server = new Server();
//        server.sendMessage("zipcode", new Information());
        server.receiveMessage();
    }
}
