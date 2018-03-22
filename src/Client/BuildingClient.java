package Client;

import Model.DataModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by jk on 24/02/18.
 */
public class BuildingClient {

    private final static String SERVER_URL = "localhost";
    private final static String QUEUE_NAME = "perodic_data";

    private DataModel mData;

    public BuildingClient(DataModel data) {
        mData = data;
    }

    private void sendMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER_URL);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicPublish("", QUEUE_NAME, null, DataModel.serialize(mData));
        System.out.println(" [x] Sent '" + mData + "'");
        channel.close();
        connection.close();
    }

    public static void main(String argv[]) {
        Date date = new Date(2018, 2, 14);
        DataModel data = new DataModel(date, "house", 1000, 100);
        BuildingClient client = new BuildingClient(data);
        try {
            client.sendMessage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
