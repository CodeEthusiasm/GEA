package com.rug.gea.Client;

import com.rabbitmq.client.*;
import com.rug.gea.Client.building.Building;
import com.rug.gea.Model.DataModel;
import com.rug.gea.Model.Information;
import com.rug.gea.Model.Serialize;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public abstract class BuildingClient {

    private final static String SERVER_URL = "localhost";
    private final static String QUEUE_NAME = "perodic_data";

    public BuildingClient(Building building) {
        building.addListener(this::sendMessage);
    }

    private void sendMessage(DataModel data) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER_URL);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicPublish("", QUEUE_NAME, null, Serialize.serialize(data));
        System.out.println(" [x] Sent '" + data + "'");
        channel.close();
        connection.close();
    }

    public void receiveMessage(String zip) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER_URL);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(zip, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, zip, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Information info = (Information)Serialize.deserialize(body);
                    System.out.println(" [x] Received '" + info + "'");
                    handlingInfo(info);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    public abstract void handlingInfo(Information info);
}
