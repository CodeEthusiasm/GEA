package com.rug.gea.Controllers;


import com.rug.gea.Collections.ClientsRepository;
import com.rug.gea.Collections.DataRepository;
import com.rug.gea.DataModels.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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

    @RequestMapping(value = "/client",method = RequestMethod.PUT)
    ResponseEntity<?> editClient() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/client",method = RequestMethod.DELETE)
    ResponseEntity<?> deleteClient() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/client",method = RequestMethod.GET)
    ResponseEntity<?> getClient() {
        return ResponseEntity.ok().build();
    }

}
