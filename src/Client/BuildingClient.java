package Client;

import Client.building.Building;
import Model.BuildingType;
import Model.DataModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
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

    private static DataModel getRandomData() {
        Random r = new Random();
        int n = r.nextInt() & Integer.MAX_VALUE;
        BuildingType randomType;
        switch (n % 4) {
            case 0:
                randomType = BuildingType.Studio;
                break;
            case 1:
                randomType = BuildingType.Apartment;
                break;
            case 2:
                randomType = BuildingType.Factroy;
                break;
            default:
                randomType = BuildingType.House;
        }
        return new DataModel(randomType,
                (r.nextInt() & Integer.MAX_VALUE) % 1000,
                (r.nextInt() & Integer.MAX_VALUE) % 100,
                (r.nextInt() & Integer.MAX_VALUE) % 100);
    }

    public static void main(String[] argv) {
        while (true) {
            DataModel data = getRandomData();
            BuildingClient client = new BuildingClient(data);
            try {
                client.sendMessage();
                Thread.sleep(1000);
            } catch (IOException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
