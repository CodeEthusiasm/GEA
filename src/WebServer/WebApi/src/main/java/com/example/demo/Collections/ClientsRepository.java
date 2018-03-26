package com.example.demo.Collections;

import com.example.demo.DataModels.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClientsRepository extends MongoRepository<Client, String> {

    public List<Client> findByZip(String zip);

}
