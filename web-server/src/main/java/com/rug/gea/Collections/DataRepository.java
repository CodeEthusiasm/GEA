package com.rug.gea.Collections;

import com.rug.gea.DataModels.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import  org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

@Document(collection = "data")
public interface DataRepository extends MongoRepository<Data,String> {
    public List<Data> findByType(String type);
}
