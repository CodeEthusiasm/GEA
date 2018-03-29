package com.rug.gea.Client;

import com.rug.gea.Model.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class API {

    private static byte[] getNeighborsBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with" + url.toString());
            }

            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while ((byteRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, byteRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private static String getNeighborString(String urlSpec) throws IOException {
        return new String(getNeighborsBytes(urlSpec));
    }

    public static List<Client> fetchData(String zip) {
        List<Client> clients = new ArrayList<>();
        try {
            String urlSpec = "http://localhost:8080/neighbours?zip=" + zip;
            String jsonString = getNeighborString(urlSpec);
            JSONArray jsonArray = new JSONArray(jsonString);
            parseNeighbors(clients, jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private static void parseNeighbors(List<Client> data, JSONArray array) {
        if (array == null) return;

        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            String address = object.getString("address");
            String zip = object.getString("zip");
            int sqm = object.getInt("sqm");
            String connectAddress = object.getString("connectAddress");
            String buildingType = object.getString("buildingType");
            Client client = new Client(address, zip, sqm, connectAddress, buildingType);
            data.add(client);
        }
    }
}
