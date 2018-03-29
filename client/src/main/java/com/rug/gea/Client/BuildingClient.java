package com.rug.gea.Client;

import com.rug.gea.Client.building.Building;
import com.rug.gea.Model.DataModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public class BuildingClient {

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

        channel.basicPublish("", QUEUE_NAME, null, DataModel.serialize(data));
        System.out.println(" [x] Sent '" + data + "'");
        channel.close();
        connection.close();
    }
}
