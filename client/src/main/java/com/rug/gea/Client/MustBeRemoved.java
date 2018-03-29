package com.rug.gea.Client;

import com.rug.gea.Client.building.Building;
import com.rug.gea.Client.building.LocalBuilding;
import com.rug.gea.Model.Information;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MustBeRemoved extends BuildingClient {

    public MustBeRemoved(Building building) {
        super(building);
    }

    public static void main(String args[]) throws IOException, TimeoutException {
        String zip = new Scanner(System.in).nextLine();
        new MustBeRemoved(new LocalBuilding()).receiveMessage(zip);
    }

    @Override
    public void handlingInfo(Information info) {
        System.out.println(info);
    }
}
