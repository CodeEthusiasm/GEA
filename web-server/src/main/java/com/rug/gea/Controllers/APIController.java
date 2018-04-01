package com.rug.gea.Controllers;


import com.rug.gea.Collections.ClientsRepository;
import com.rug.gea.Collections.DataRepository;
import com.rug.gea.DataModels.Client;
import com.rug.gea.DataModels.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

@RestController
public class APIController {

    @Autowired
    private ClientsRepository clients;
    @Autowired
    private DataRepository data;

    @GetMapping("/neighbours")
    List<Client> returnNeighbours(@RequestParam(value = "zip",required = true) String zip)
    {
        return clients.findByZip(zip);
    }

    @PostMapping("/clients")
    Client addClient
            (
            @RequestParam(value = "address",required = true) String address,
            @RequestParam(value = "zip",required = true) String zip,
            @RequestParam(value = "sqm",required = true) int sqm,
            @RequestParam(value = "connectAddress",required = true) String connectAddress,
            @RequestParam(value = "buildingType",required = true) String buildingType
            )
    {
        Client client = new Client(address,zip,sqm,connectAddress,buildingType);
        clients.save(client);
        try {
            MessageController.sendMessage(client.zip, new Information(Information.Request.Create, client));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return client;
    }


    @PutMapping("/clients")
    Client editClient
            (
            @RequestParam(value = "id",required = true) String id,
            @RequestParam(value = "address",required = true) String address,
            @RequestParam(value = "zip",required = true) String zip,
            @RequestParam(value = "sqm",required = true) int sqm,
            @RequestParam(value = "connectAddress",required = true) String connectAddress,
            @RequestParam(value = "buildingType",required = true) String buildingType
            )
    {
        Client client = new Client(id,address,zip,sqm,connectAddress,buildingType);
        clients.save(client);
        return client;
    }

    @DeleteMapping("/clients")
    ResponseEntity<?> deleteClient(@RequestParam(value = "id",required = true) String id) {
        Optional<Client> client = clients.findById(id);
        if(client.isPresent()){
            clients.delete(client.get());
            try {
                MessageController.sendMessage(client.get().zip, new Information(Information.Request.Delete, client.get()));
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();

        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/clients")
    Client getClient(@RequestParam(value = "id",required = true) String id) {
        Optional<Client> client = clients.findById(id);
        return client.get();
    }
}
